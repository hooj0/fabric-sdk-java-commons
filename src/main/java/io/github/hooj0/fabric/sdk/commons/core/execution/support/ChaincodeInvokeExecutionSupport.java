package io.github.hooj0.fabric.sdk.commons.core.execution.support;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.StringUtils;
import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.ChaincodeResponse.Status;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.SDKUtils;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;
import org.hyperledger.fabric.sdk.TxReadWriteSetInfo;

import com.google.common.base.Strings;

import io.github.hooj0.fabric.sdk.commons.FabricChaincodeInvokeException;
import io.github.hooj0.fabric.sdk.commons.core.execution.ChaincodeInvokeExecution;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.FuncOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.InvokeOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;

/**
 * chaincode transaction invoke execution interface support
 * @author hoojo
 * @createDate 2018年7月24日 上午11:32:39
 * @file ChaincodeInvokeExecutionSupport.java
 * @package io.github.hooj0.fabric.sdk.commons.core.execution.support
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class ChaincodeInvokeExecutionSupport extends AbstractTransactionExecutionSupport<InvokeOptions, FuncOptions> implements ChaincodeInvokeExecution {

	public ChaincodeInvokeExecutionSupport(HFClient client, Channel channel) {
		super(client, channel, ChaincodeInvokeExecutionSupport.class);
	}

	@Override
	public ResultSet execute(InvokeOptions options, String func) {
		return this.execute(options, new FuncOptions(func));
	}

	@Override
	public ResultSet execute(InvokeOptions options, String func, Object... args) {
		return this.execute(options, new FuncOptions(func, args));
	}

	@Override
	public ResultSet execute(InvokeOptions options, String func, LinkedHashMap<String, Object> args) {
		return this.execute(options, new FuncOptions(func, args));
	}

	private void checkArgs(InvokeOptions options, FuncOptions funcOptions) {
		checkNotNull(options.getClientUserContext(), "client user 参数不可忽略设置");

		checkArgument(!Strings.isNullOrEmpty(funcOptions.getFunc()), "func 参数为必填项");
		checkArgument(!Objects.isNull(funcOptions.getArgs()), "args 参数为必填项");		
	}
	
	@Override
	public ResultSet execute(InvokeOptions options, FuncOptions funcOptions) {
		logger.info("在通道：{}，发起调用Chaincode 交易业务: {}", channel.getName(), options.getChaincodeId());

		checkArgs(options, funcOptions);
		
		try {
			client.setUserContext(options.getClientUserContext());
			
            // 构建——交易提议请求，向所有对等节点发送
            TransactionProposalRequest request = client.newTransactionProposalRequest();
            request.setProposalWaitTime(options.getProposalWaitTime());
            request.setChaincodeLanguage(options.getChaincodeType());
            request.setChaincodeID(options.getChaincodeId());
            request.setFcn(funcOptions.getFunc());
            request.setArgs(funcOptions.getArgs());
            
            // 添加——到分类账的提案中的瞬时数据
            Map<String, byte[]> transientMap = new HashMap<>();
            transientMap.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8)); //Just some extra junk in transient map
            transientMap.put("method", "TransactionProposalRequest".getBytes(UTF_8)); // ditto
            transientMap.put("result", ":)".getBytes(UTF_8));  // This should be returned see chaincode why.

            if (options.getTransientData() != null) {
				transientMap.putAll(options.getTransientData());
			}
            request.setTransientMap(transientMap);
            if (options.getRequestUser() != null) { // 使用特定用户
				request.setUserContext(options.getRequestUser());
			}
            
            // 发送——交易请求
            Collection<ProposalResponse> responses = null;
            if (options.getSend2Peers() != null) { // 向指定的Peer节点发送请求
            	responses = channel.sendTransactionProposal(request, options.getSend2Peers());
            } else if (options.isSpecificPeers()) {
            	responses = channel.sendTransactionProposal(request, channel.getPeers()); // default
            } else {
            	responses = channel.sendTransactionProposal(request);
            }
            logger.info("向 channel.Peers节点——发起交易“提议”请求，参数: {}", request);
            
            Collection<ProposalResponse> successResponses = new LinkedList<>();
            Collection<ProposalResponse> failedResponses = new LinkedList<>();
            
			for (ProposalResponse response : responses) {
				if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
					logger.debug("交易成功 Txid: {} from peer {}", response.getTransactionID(), response.getPeer().getName());
					successResponses.add(response);
				} else {
					logger.debug("交易失败 Txid: {} from peer {}", response.getTransactionID(), response.getPeer().getName());
					failedResponses.add(response);
				}
			}
			
			// 检查请求——响应结果有效且不为空
			Collection<Set<ProposalResponse>> proposalConsistencySets = SDKUtils.getProposalConsistencySets(responses);
            if (proposalConsistencySets.size() != 1) {
                throw new FabricChaincodeInvokeException("成功响应请求结果的数量等于1，实际响应数量： %d", proposalConsistencySets.size());
            }
            logger.info("接收交易请求响应： {} ，Successful+verified: {}， Failed: {}", responses.size(), successResponses.size(), failedResponses.size());
            
            if (failedResponses.size() > 0) {
                ProposalResponse firstResponse = failedResponses.iterator().next();
                throw new FabricChaincodeInvokeException("没有足够的背书节点调用: %s， endorser error: %s. Was verified: %s", failedResponses.size(), firstResponse.getMessage(), firstResponse.isVerified());
            }
            
            ProposalResponse response = successResponses.iterator().next();
            // 对应上面构建的 transientMap->result
            byte[] chaincodeBytes = response.getChaincodeActionResponsePayload(); // 链码返回的数据
            String resultAsString = null;
            if (chaincodeBytes != null) {
                resultAsString = new String(chaincodeBytes, UTF_8);
            }
            
            String result = new String(transientMap.get("result"), UTF_8);
            checkArgument(StringUtils.equals(result, resultAsString), "%s :和定义的账本数据 '%s'不一致", resultAsString, result);
            checkState(response.getChaincodeActionResponseStatus() == Status.SUCCESS.getStatus(), "%s：非正常的响应状态码", response.getChaincodeActionResponseStatus());
            
            TxReadWriteSetInfo readWriteSetInfo = response.getChaincodeActionResponseReadWriteSetInfo();
            checkNotNull(readWriteSetInfo, "提议请求响应的读写集为空");
            checkArgument(readWriteSetInfo.getNsRwsetCount() > 0, "提议请求读写集数量为空");
            
            ChaincodeID chaincodeId = response.getChaincodeID();
            checkNotNull(chaincodeId, "提议请求响应ChaincodeID为空");
            checkArgument(StringUtils.equals(options.getChaincodeId().getName(), chaincodeId.getName()), "chaincode 名称不一致");
            checkArgument(StringUtils.equals(options.getChaincodeId().getVersion(), chaincodeId.getVersion()), "chaincode 版本不一致");
            
            final String path = chaincodeId.getPath();
            if (options.getChaincodeId().getPath() == null) {
                checkArgument(StringUtils.isBlank(path), "chaincode Path不为空");
            } else {
            	checkArgument(StringUtils.equals(options.getChaincodeId().getPath(), path), "chaincode Path不一致");
            }

            result = response.getProposalResponse().getResponse().getPayload().toStringUtf8();
            
            return new ResultSet(successResponses).setTransactionId(response.getTransactionID()).setResult(result);
		} catch (Exception e) {
            logger.error("调用chaincode时发生异常：", e);
            throw new FabricChaincodeInvokeException(e, "调用chaincode时发生异常： %s", e.getMessage());
		}
	}

	@Override
	public CompletableFuture<TransactionEvent> executeAsync(InvokeOptions options, String func) {
		return this.executeAsync(options, new FuncOptions(func));
	}

	@Override
	public CompletableFuture<TransactionEvent> executeAsync(InvokeOptions options, String func, Object... args) {
		return this.executeAsync(options, new FuncOptions(func, args));
	}

	@Override
	public CompletableFuture<TransactionEvent> executeAsync(InvokeOptions options, String func, Map<String, Object> args) {
		return this.executeAsync(options, new FuncOptions(func, args));
	}

	@Override
	public TransactionEvent executeFor(InvokeOptions options, String func) {
		return super.executeFor(options, new FuncOptions(func));
	}

	@Override
	public TransactionEvent executeFor(InvokeOptions options, String func, Object... args) {
		return super.executeFor(options, new FuncOptions(func, args));
	}

	@Override
	public TransactionEvent executeFor(InvokeOptions options, String func, Map<String, Object> args) {
		return super.executeFor(options, new FuncOptions(func, args));
	}
}
