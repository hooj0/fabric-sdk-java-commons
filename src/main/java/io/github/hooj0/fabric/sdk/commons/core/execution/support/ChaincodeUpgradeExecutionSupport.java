package io.github.hooj0.fabric.sdk.commons.core.execution.support;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.ChaincodeResponse.Status;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.UpgradeProposalRequest;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;

import com.google.common.base.Optional;

import io.github.hooj0.fabric.sdk.commons.FabricChaincodeUpgradeException;
import io.github.hooj0.fabric.sdk.commons.core.execution.ChaincodeUpgradeExecution;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.Options;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.UpgradeOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;

/**
 * <b>function:</b>
 * @author hoojo
 * @createDate 2018年7月24日 下午5:05:46
 * @file ChaincodeUpgradeExecutionSupport.java
 * @package io.github.hooj0.fabric.sdk.commons.core.execution.support
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class ChaincodeUpgradeExecutionSupport extends AbstractTransactionExecutionSupport<UpgradeOptions> implements ChaincodeUpgradeExecution {

	public ChaincodeUpgradeExecutionSupport(HFClient client, Channel channel) {
		super(client, channel, ChaincodeUpgradeExecutionSupport.class);
	}
	
	@Override
	public ResultSet execute(Options options, String func) {
		return this.execute(bindOptions(options, new UpgradeOptions(func)));
	}

	@Override
	public ResultSet execute(Options options, String func, Object... args) {
		return this.execute(bindOptions(options, new UpgradeOptions(func, args)));
	}

	@Override
	public ResultSet execute(Options options, String func, LinkedHashMap<String, Object> args) {
		return this.execute(bindOptions(options, new UpgradeOptions(func, args)));
	}

	@Override
	public ResultSet execute(UpgradeOptions options) {
		logger.info("通道：{} 升级安装 chaincode: {}", channel.getName(), options.getChaincodeId());

		checkNotNull(options.getEndorsementPolicy(), "endorsementPolicy 背书策略文件为必填项");

		String func = Optional.fromNullable(options.getFunc()).or("init");
		String[] args = Optional.fromNullable(options.getArgs()).or(new String[] {});

		try {
			UpgradeProposalRequest upgradeProposalRequest = client.newUpgradeProposalRequest();
			upgradeProposalRequest.setProposalWaitTime(options.getProposalWaitTime());
			upgradeProposalRequest.setChaincodeLanguage(options.getChaincodeType());
			upgradeProposalRequest.setChaincodeID(options.getChaincodeId());
			upgradeProposalRequest.setFcn(func);
			upgradeProposalRequest.setArgs(args); // no arguments don't change the ledger see chaincode.

			upgradeProposalRequest.setTransientMap(options.getTransientData());
			// 设置背书策略
			upgradeProposalRequest.setChaincodeEndorsementPolicy(options.getEndorsementPolicy());
			if (options.getContextUser() != null) {
				upgradeProposalRequest.setUserContext(options.getContextUser());
			}

			// 发送安装升级chaincode请求
			Collection<ProposalResponse> responses = null;
			if (options.getSend2Peers() != null) {
				responses = channel.sendUpgradeProposal(upgradeProposalRequest, options.getSend2Peers());
			} else if (options.isSpecificPeers()) {
				responses = channel.sendUpgradeProposal(upgradeProposalRequest, channel.getPeers());
			} else {
				responses = channel.sendUpgradeProposal(upgradeProposalRequest); // default
			}
			logger.debug("向channel节点——发送安装升级chaincode请求：{}", upgradeProposalRequest);

			final Collection<ProposalResponse> successResponses = new LinkedList<>();
			final Collection<ProposalResponse> failedResponses = new LinkedList<>();
			
			String transactionID = null;
			for (ProposalResponse response : responses) {
				if (response.getStatus() == Status.SUCCESS) {
					successResponses.add(response);
					logger.debug("成功升级 Txid: {} , peer: {}", response.getTransactionID(), response.getPeer().getName());
					if (transactionID == null) {
						transactionID = response.getTransactionID();
					}
				} else {
					failedResponses.add(response);
					logger.debug("失败升级 Txid: {} , peer: {}", response.getTransactionID(), response.getPeer().getName());
				}
			}

			logger.debug("接收安装请求数量： {}， 成功安装并验证通过数量: {} . 失败数量: {}", channel.getPeers().size(), successResponses.size(), failedResponses.size());
			if (failedResponses.size() > 0) {
				ProposalResponse first = failedResponses.iterator().next();
				throw new FabricChaincodeUpgradeException("没有足够的 endorsers 安装 : %s, Message: ", successResponses.size(), first.getMessage());
			}

			return new ResultSet(successResponses).setTransactionId(transactionID);
		} catch (InvalidArgumentException | ProposalException e) {
			logger.error("升级chaincode时发生异常：", e);
            throw new FabricChaincodeUpgradeException(e, "升级chaincode时发生异常： %s", e.getMessage());
		}
	}

	@Override
	public CompletableFuture<TransactionEvent> executeAsync(Options options, String func) {
		return this.executeAsync(bindOptions(options, new UpgradeOptions(func)));
	}

	@Override
	public CompletableFuture<TransactionEvent> executeAsync(Options options, String func, Object... args) {
		return this.executeAsync(bindOptions(options, new UpgradeOptions(func, args)));
	}

	@Override
	public CompletableFuture<TransactionEvent> executeAsync(Options options, String func, Map<String, Object> args) {
		return this.executeAsync(bindOptions(options, new UpgradeOptions(func, args)));
	}
}
