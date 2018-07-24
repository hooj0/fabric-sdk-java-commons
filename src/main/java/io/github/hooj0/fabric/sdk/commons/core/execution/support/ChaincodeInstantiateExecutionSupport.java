package io.github.hooj0.fabric.sdk.commons.core.execution.support;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.InstantiateProposalRequest;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;

import com.google.common.base.Optional;

import io.github.hooj0.fabric.sdk.commons.FabricChaincodeInstantiateException;
import io.github.hooj0.fabric.sdk.commons.core.execution.ChaincodeInstantiateExecution;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.InstantiateOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.Options;
import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;

/**
 * <b>function:</b>
 * @author hoojo
 * @createDate 2018年7月24日 下午4:22:05
 * @file ChaincodeInstantiateExecutionSupport.java
 * @package io.github.hooj0.fabric.sdk.commons.core.execution.support
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class ChaincodeInstantiateExecutionSupport extends AbstractTransactionExecutionSupport<InstantiateOptions> implements ChaincodeInstantiateExecution {

	public ChaincodeInstantiateExecutionSupport(HFClient client, Channel channel) {
		super(client, channel, ChaincodeInstantiateExecutionSupport.class);
	}

	@Override
	public ResultSet execute(Options options, String func) {
		return this.execute(bindOptions(options, new InstantiateOptions(func)));
	}

	@Override
	public ResultSet execute(Options options, String func, Object... args) {
		return this.execute(bindOptions(options, new InstantiateOptions(func, args)));
	}

	@Override
	public ResultSet execute(Options options, String func, LinkedHashMap<String, Object> args) {
		
		return this.execute(bindOptions(options, new InstantiateOptions(func, args)));
	}

	private void checkArgs(InstantiateOptions options) {
		checkNotNull(options.getEndorsementPolicy(), "endorsementPolicy 背书策略文件为必填项");
	}
	
	@Override
	public ResultSet execute(InstantiateOptions options) {
		logger.info("在通道：{} 实例化Chaincode：{}", channel.getName(), options.getChaincodeId());

		checkArgs(options);

		String func = Optional.fromNullable(options.getFunc()).or("init");
		String[] args = Optional.fromNullable(options.getArgs()).or(new String[] {});

		// 注意安装chaincode不需要事务不需要发送给 Orderers
		InstantiateProposalRequest instantiateRequest = client.newInstantiationProposalRequest();
		instantiateRequest.setChaincodeLanguage(options.getChaincodeType());
		instantiateRequest.setChaincodeID(options.getChaincodeId());
		instantiateRequest.setFcn(func);
		instantiateRequest.setArgs(args);
		instantiateRequest.setProposalWaitTime(options.getProposalWaitTime());

		try {
			Map<String, byte[]> transientMap = new HashMap<>();
			transientMap.put("HyperLedgerFabric", "InstantiateProposalRequest:JavaSDK".getBytes(UTF_8));
			transientMap.put("method", "InstantiateProposalRequest".getBytes(UTF_8));
			
			if (options.getTransientData() != null) {
				transientMap.putAll(options.getTransientData());
			}
			instantiateRequest.setTransientMap(transientMap);

			if (options.getContextUser() != null) {
				instantiateRequest.setUserContext(options.getContextUser());
			}
			// 设置背书策略
			instantiateRequest.setChaincodeEndorsementPolicy(options.getEndorsementPolicy());
			

			// 通过指定对等节点和使用通道上的方式发送请求响应
			Collection<ProposalResponse> responses = null;
			if (options.getSend2Peers() != null) {
				responses = channel.sendInstantiationProposal(instantiateRequest, options.getSend2Peers());
			} else if (options.isSpecificPeers()) {
				responses = channel.sendInstantiationProposal(instantiateRequest, channel.getPeers());
				logger.info("向channel.Peers节点——发送实例化Chaincode请求：{}", instantiateRequest);
			} else {
				responses = channel.sendInstantiationProposal(instantiateRequest);
				logger.info("向CHAINCODE_QUERY/ENDORSING_PEER Peer——发送实例化Chaincode请求：{}", instantiateRequest);
			}
			logger.info("发送实例化Chaincode参数：{}", instantiateRequest.getArgs());

			Collection<ProposalResponse> successResponses = new LinkedList<>();
			Collection<ProposalResponse> failedResponses = new LinkedList<>();
			
			for (ProposalResponse response : responses) {
				if (response.isVerified() && response.getStatus() == ProposalResponse.Status.SUCCESS) {
					successResponses.add(response);
					logger.debug("成功实例化 Txid: {} , peer: {}", response.getTransactionID(), response.getPeer().getName());
				} else {
					failedResponses.add(response);
					logger.debug("失败实例化 Txid: {} , peer: {}", response.getTransactionID(), response.getPeer().getName());
				}
			}
			logger.info("接收实例化请求数量： {}， 成功安装并验证通过数量: {}， 失败数量: {}", responses.size(), successResponses.size(), failedResponses.size());

			if (failedResponses.size() > 0) {
				for (ProposalResponse fail : failedResponses) {
					logger.error("没有足够的 endorsers 实例化:" + successResponses.size() + "，endorser failed： " + fail.getMessage() + ", peer：" + fail.getPeer());
				}

				ProposalResponse first = failedResponses.iterator().next();
				throw new FabricChaincodeInstantiateException("没有足够的 endorsers 实例化: %s，endorser failed：%s , verified：%s.", successResponses.size(), first.getMessage(), first.isVerified());
			}

			return new ResultSet(successResponses);
		} catch (InvalidArgumentException | ProposalException e) {
			logger.error("实例化chaincode时发生异常：", e);
            throw new FabricChaincodeInstantiateException(e, "实例化chaincode时发生异常： %s", e.getMessage());
		}
	}

	@Override
	public CompletableFuture<TransactionEvent> executeAsync(Options options, String func) {
		return this.executeAsync(bindOptions(options, new InstantiateOptions(func)));
	}

	@Override
	public CompletableFuture<TransactionEvent> executeAsync(Options options, String func, Object... args) {
		return this.executeAsync(bindOptions(options, new InstantiateOptions(func, args)));
	}

	@Override
	public CompletableFuture<TransactionEvent> executeAsync(Options options, String func, Map<String, Object> args) {
		return this.executeAsync(bindOptions(options, new InstantiateOptions(func, args)));
	}
	
	/**
	 * 设置背书策略配置
	 * @author hoojo
	 * @createDate 2018年6月25日 下午1:02:33
	 */
	/*public ChaincodeEndorsementPolicy getChaincodeEndorsementPolicy(String endorsementPolicyYamlFilePath) throws Exception {
		
		File policyFile = null;
		if (StringUtils.isEmpty(endorsementPolicyYamlFilePath)) {
			policyFile = new File(config.getEndorsementPolicyFilePath());
		} else {
			policyFile = Paths.get(config.getCommonConfigRootPath(), endorsementPolicyYamlFilePath).toFile();
		}
		logger.info("背书策略文件：{}", policyFile.getAbsolutePath());

		ChaincodeEndorsementPolicy endorsementPolicy = new ChaincodeEndorsementPolicy();
		endorsementPolicy.fromYamlFile(policyFile);

		return endorsementPolicy;
	}*/
}
