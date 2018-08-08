package io.github.hooj0.fabric.sdk.commons.core.execution.support;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.commons.lang3.StringUtils;
import org.hyperledger.fabric.sdk.ChaincodeResponse.Status;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.InstallProposalRequest;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;

import io.github.hooj0.fabric.sdk.commons.FabricChaincodeInstallException;
import io.github.hooj0.fabric.sdk.commons.core.execution.ChaincodeInstallExecution;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.InstallOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;

/**
 * chaincode deploy operation install execution interface support
 * @author hoojo
 * @createDate 2018年7月24日 下午3:46:12
 * @file ChaincodeInstallExecutionSupport.java
 * @package io.github.hooj0.fabric.sdk.commons.core.execution.support
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class ChaincodeInstallExecutionSupport extends ChaincodeBasicExecutionSupport implements ChaincodeInstallExecution {

	public ChaincodeInstallExecutionSupport(HFClient client, Channel channel) {
		super(ChaincodeInstallExecutionSupport.class);
		
		super.setClient(client);
		super.setChannel(channel);
	}

	@Override
	public Collection<ProposalResponse> execute(InstallOptions options, String chaincodeSourceLocation) {
		checkNotNull(chaincodeSourceLocation, "Chaincode sourcecode file is null");
		
		return execute(options, new File(chaincodeSourceLocation));
	}

	@Override
	public Collection<ProposalResponse> execute(InstallOptions options, File chaincodeSourceFile) {
		checkNotNull(chaincodeSourceFile, "Chaincode sourcecode file is null");
		
		return execute(options, null, chaincodeSourceFile).getResponses();
	}

	@Override
	public Collection<ProposalResponse> execute(InstallOptions options, InputStream chaincodeInputStream) {
		checkNotNull(chaincodeInputStream, "Chaincode InputStream is null");
		
		return execute(options, chaincodeInputStream, null).getResponses();
	}
	
	private void checkArgs(InstallOptions options) {
		checkNotNull(options.getClientUserContext(), "client user 参数不可忽略设置");
		
		checkNotNull(options.getChaincodeId(), "ChaincodeId 是必填参数");
		checkNotNull(options.getChaincodeType(), "ChaincodeType 是必填参数");
		
		logger.debug("options: {}", options);
	}
	
	private ResultSet execute(InstallOptions options, InputStream chaincodeInputStream, File chaincodeSourceFile) {
		logger.info("通道：{} 安装chaincode: {}", channel.getName(), options.getChaincodeId());

		checkArgs(options);

		Collection<ProposalResponse> successful = new LinkedList<>();
		Collection<ProposalResponse> failed = new LinkedList<>();
		
		try {
			client.setUserContext(options.getClientUserContext());
			
			// 构建安装chaincode请求
			InstallProposalRequest installRequest = client.newInstallProposalRequest();
			installRequest.setProposalWaitTime(options.getProposalWaitTime());
			installRequest.setChaincodeLanguage(options.getChaincodeType());
			installRequest.setChaincodeID(options.getChaincodeId());
			
			if (!StringUtils.isBlank(options.getChaincodeUpgradeVersion())) {
				installRequest.setChaincodeVersion(options.getChaincodeUpgradeVersion());
			}
			if (options.getRequestUser() != null) {
				installRequest.setUserContext(options.getRequestUser());
			}
			if (chaincodeSourceFile != null) { 
				logger.debug("Chaincode source file path: {}", chaincodeSourceFile.getAbsolutePath());
				installRequest.setChaincodeSourceLocation(chaincodeSourceFile);
			} else {
				installRequest.setChaincodeInputStream(chaincodeInputStream);
			}

			// 发送安装请求
			// 只有来自同一组织的客户端才能发出安装请求
			Collection<ProposalResponse> responses = null;
			Collection<Peer> peers = channel.getPeers();
			if (options.getSend2Peers() != null) {
				responses = client.sendInstallProposal(installRequest, options.getSend2Peers());
			} else {
				responses = client.sendInstallProposal(installRequest, peers);
			}
			logger.info("向channel.Peers节点——发送安装chaincode请求：{}", installRequest);

			String txId = null, payload = null;
			for (ProposalResponse response : responses) {
				if (response.getStatus() == Status.SUCCESS) {
					successful.add(response);
					txId = response.getTransactionID();
					payload = response.getProposalResponse().getResponse().getPayload().toStringUtf8();
					logger.debug("成功安装 Txid: {} , peer: {}", response.getTransactionID(), response.getPeer().getName());
				} else {
					failed.add(response);
					logger.debug("失败安装 Txid: {} , peer: {}", response.getTransactionID(), response.getPeer().getName());
				}
			}

			logger.info("接收安装请求数量： {}， 成功安装并验证通过数量: {} . 失败数量: {}", peers.size(), successful.size(), failed.size());
			if (failed.size() > 0) {
				ProposalResponse first = failed.iterator().next();
				throw new FabricChaincodeInstallException("没有足够的 endorsers 安装 : %s， %s", successful.size(), first.getMessage());
			}
			
			return new ResultSet(responses).setTransactionId(txId).setResult(payload);
		} catch (InvalidArgumentException | ProposalException e) {
			logger.error("安装Chaincode失败", e);
			throw new FabricChaincodeInstallException(e, "安装Chaincode失败 : %s", e.getMessage());
		}
	}

	@Override
	public ResultSet executeFor(InstallOptions options, String chaincodeSourceLocation) {
		checkNotNull(chaincodeSourceLocation, "Chaincode sourcecode file is null");
		
		return executeFor(options, new File(chaincodeSourceLocation));
	}

	@Override
	public ResultSet executeFor(InstallOptions options, File chaincodeSourceFile) {
		checkNotNull(chaincodeSourceFile, "Chaincode sourcecode file is null");
		
		return execute(options, null, chaincodeSourceFile);
	}

	@Override
	public ResultSet executeFor(InstallOptions options, InputStream chaincodeInputStream) {
		checkNotNull(chaincodeInputStream, "Chaincode InputStream is null");
		
		return execute(options, chaincodeInputStream, null);
	}
}
