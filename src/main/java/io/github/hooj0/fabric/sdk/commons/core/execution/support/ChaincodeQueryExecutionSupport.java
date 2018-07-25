package io.github.hooj0.fabric.sdk.commons.core.execution.support;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.hyperledger.fabric.sdk.ChaincodeResponse.Status;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.QueryByChaincodeRequest;

import com.google.common.base.Strings;

import io.github.hooj0.fabric.sdk.commons.FabricChaincodeQueryException;
import io.github.hooj0.fabric.sdk.commons.core.execution.ChaincodeQueryExecution;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.FuncOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.QueryOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;

/**
 * chaincode transaction query execution interface support 
 * @author hoojo
 * @createDate 2018年7月24日 上午10:25:31
 * @file ChaincodeQueryExecutionSupport.java
 * @package io.github.hooj0.fabric.sdk.commons.core.execution.support
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class ChaincodeQueryExecutionSupport extends AbstractChaincodeExecutionSupport implements ChaincodeQueryExecution {

	public ChaincodeQueryExecutionSupport(HFClient client, Channel channel) {
		super(client, channel, ChaincodeQueryExecutionSupport.class);
	}
	
	@Override
	public String execute(QueryOptions options, String func) {
		return this.execute(options, new FuncOptions(func));
	}

	@Override
	public String execute(QueryOptions options, String func, Object... args) {
		return this.execute(options, new FuncOptions(func, args));
	}

	@Override
	public String execute(QueryOptions options, String func, LinkedHashMap<String, Object> args) {
		return this.execute(options, new FuncOptions(func, args));
	}

	@Override
	public String execute(QueryOptions options, FuncOptions funcOptions) {
		return this.executeFor(options, funcOptions).getResult();
	}

	@Override
	public ResultSet executeFor(QueryOptions options, String func) {
		return this.executeFor(options, new FuncOptions(func));
	}

	@Override
	public ResultSet executeFor(QueryOptions options, String func, Object... args) {
		
		return this.executeFor(options, new FuncOptions(func, args));
	}

	@Override
	public ResultSet executeFor(QueryOptions options, String func, Map<String, Object> args) {
		return this.executeFor(options, new FuncOptions(func, args));
	}

	private void checkArgs(QueryOptions options, FuncOptions funcOptions) {
		checkArgument(!Strings.isNullOrEmpty(funcOptions.getFunc()), "func 参数为必填项");
		checkArgument(!Objects.isNull(funcOptions.getArgs()), "args 参数为必填项");
		checkNotNull(options.getClientUserContext(), "client user 参数不可忽略设置");
	}
	
	@Override
	public ResultSet executeFor(QueryOptions options, FuncOptions funcOptions) {
		logger.info("在通道：{} 发起chaincode 查询：{}", channel.getName(), options.getChaincodeId());
		
		checkArgs(options, funcOptions);
		
		Collection<ProposalResponse> responses = null;
		String payload = null;
		try {
			client.setUserContext(options.getClientUserContext());
			
			// 构建查询请求
			QueryByChaincodeRequest request = client.newQueryProposalRequest();
			request.setProposalWaitTime(options.getProposalWaitTime());
			request.setChaincodeLanguage(options.getChaincodeType());
			request.setChaincodeID(options.getChaincodeId());
			request.setFcn(funcOptions.getFunc());
			request.setArgs(funcOptions.getArgs());
			
			Map<String, byte[]> transientMap = new HashMap<>();
			transientMap.put("HyperLedgerFabric", "QueryByChaincodeRequest:JavaSDK".getBytes(UTF_8));
			transientMap.put("method", "QueryByChaincodeRequest".getBytes(UTF_8));
			
			if (options.getTransientData() != null) {
				transientMap.putAll(options.getTransientData());
			}
            request.setTransientMap(transientMap);
            if (options.getRequestUser() != null) {
            	request.setUserContext(options.getRequestUser());
            }
            
            if (options.getSend2Peers() != null) { // 向指定的Peer节点发送请求
            	responses = channel.queryByChaincode(request, options.getSend2Peers());
            } else if (options.isSpecificPeers()) { // 向所有Peer节点发送查询请求
            	responses = channel.queryByChaincode(request, channel.getPeers());
            } else {
            	responses = channel.queryByChaincode(request);
            }
            logger.info("向 channel.Peers——发起Chaincode查询请求：{}", request);
            
            for (ProposalResponse response : responses) {
                if (!response.isVerified() || response.getStatus() != Status.SUCCESS) {
                    throw new FabricChaincodeQueryException("查询失败， peer %s, status: %s, Messages: %s, Was verified: %s", response.getPeer().getName(), response.getStatus(), response.getMessage(), response.isVerified());
                } else {
                    payload = response.getProposalResponse().getResponse().getPayload().toStringUtf8();
                    logger.debug("查询来自对等点：{} ，返回结果：{}", response.getPeer().getName(), payload);
                }
            }
		} catch (Exception e) {
			logger.error("调用chaincode时发生异常：", e);
            throw new FabricChaincodeQueryException(e, "调用Chaincode Query Exection查询时发生异常: %s", e.getMessage());
		}
		
		return new ResultSet(payload).setResponses(responses);
	}
}
