package io.github.hooj0.fabric.sdk.commons.core.execution.support;

import java.util.List;

import org.hyperledger.fabric.protos.peer.Query.ChaincodeInfo;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.Peer;

import io.github.hooj0.fabric.sdk.commons.AbstractObject;
import io.github.hooj0.fabric.sdk.commons.core.execution.ChaincodeBasicExecution;
import io.github.hooj0.fabric.sdk.commons.domain.Organization;

/**
 * chaincode execution basic interface implements
 * @author hoojo
 * @createDate 2018年7月24日 下午4:25:13
 * @file ChaincodeBasicExecutionSupport.java
 * @package io.github.hooj0.fabric.sdk.commons.core.execution.support
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public abstract class ChaincodeBasicExecutionSupport extends AbstractObject implements ChaincodeBasicExecution {

	protected Channel channel;
	protected HFClient client;
	
	public ChaincodeBasicExecutionSupport(Class<?> clazz) {
		super(clazz);
	}
	
	/**
	 * 检查Chaincode在peer上是否成功安装
	 * @author hoojo
	 * @createDate 2018年6月25日 上午10:49:01
	 */
	public boolean checkInstallChaincode(Peer peer, ChaincodeID chaincodeId) throws Exception {
		logger.info("安装检查 —— peer: {}，chaincode: {}", peer.getName(), chaincodeId);

		List<ChaincodeInfo> list = client.queryInstalledChaincodes(peer);

		boolean found = false;
		for (ChaincodeInfo chaincodeInfo : list) {
			logger.debug("Peer: {} 已安装chaincode：{}:{}", peer.getName(), chaincodeInfo.getName(), chaincodeInfo.getVersion());

			if (chaincodeId.getPath() != null) {
				found = chaincodeId.getName().equals(chaincodeInfo.getName()) && chaincodeId.getPath().equals(chaincodeInfo.getPath()) && chaincodeId.getVersion().equals(chaincodeInfo.getVersion());
				if (found) {
					break;
				}
			}

			found = chaincodeId.getName().equals(chaincodeInfo.getName()) && chaincodeId.getVersion().equals(chaincodeInfo.getVersion());
			if (found) {
				break;
			}
		}

		return found;
	}
	
	/**
	 * 检查通道上的peer对等节点是否安装chaincode
	 * @author hoojo
	 * @createDate 2018年6月26日 下午5:27:28
	 */
	public boolean checkInstallChaincode(ChaincodeID chaincodeId) throws Exception {
		boolean found = false;
		for (Peer peer : channel.getPeers()) {
			if (checkInstallChaincode(peer, chaincodeId)) {
				found = true;
			}
		}
		
		return found;
	}

	/**
	 * 检查Chaincode在channel上是否实例化
	 * @author hoojo
	 * @createDate 2018年6月25日 上午10:48:29
	 */
	public boolean checkInstantiatedChaincode(Peer peer, ChaincodeID chaincodeId) throws Exception {
		logger.info("实例化检查——channel：{}, peer: {}, chaincode: {}", channel.getName(), peer.getName(), chaincodeId);

		List<ChaincodeInfo> chaincodeList = channel.queryInstantiatedChaincodes(peer);

		boolean found = false;
		for (ChaincodeInfo chaincodeInfo : chaincodeList) {
			logger.debug("Peer: {} 已实例化 chaincode：{}:{}", peer.getName(), chaincodeInfo.getName(), chaincodeInfo.getVersion());

			if (chaincodeId.getPath() != null) {
				found = chaincodeId.getName().equals(chaincodeInfo.getName()) && chaincodeId.getPath().equals(chaincodeInfo.getPath()) && chaincodeId.getVersion().equals(chaincodeInfo.getVersion());
				if (found) {
					break;
				}
			}

			found = chaincodeId.getName().equals(chaincodeInfo.getName()) && chaincodeId.getVersion().equals(chaincodeInfo.getVersion());
			if (found) {
				break;
			}
		}

		return found;
	}

	/**
	 * 检查Chaincode在channel上是否实例化
	 * @author hoojo
	 * @createDate 2018年6月25日 上午10:48:29
	 */
	public boolean checkInstantiatedChaincode(ChaincodeID chaincodeId) throws Exception {
		boolean found = false;
		for (Peer peer : channel.getPeers()) {
			if (checkInstantiatedChaincode(peer, chaincodeId)) {
				found = true;
			}
		}
		
		return found;
	}
	
	/**
	 * 检查Chaincode是否在通道上成功安装和实例化
	 * @author hoojo
	 * @createDate 2018年6月25日 上午10:47:40
	 */
	public boolean checkChaincode(ChaincodeID chaincodeId, Organization org) throws Exception {
		logger.info("安装和实例化检查 —— channel：{}，Chaincode： {}", channel.getName(), chaincodeId.toString());

		// 设置对等节点用户上下文
		client.setUserContext(org.getPeerAdmin());

		boolean found = false;
		for (Peer peer : channel.getPeers()) {
			if (checkInstallChaincode(peer, chaincodeId) && checkInstantiatedChaincode(peer, chaincodeId)) {
				found = true;
			}
		}

		return found;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public void setClient(HFClient client) {
		this.client = client;
	}
}
