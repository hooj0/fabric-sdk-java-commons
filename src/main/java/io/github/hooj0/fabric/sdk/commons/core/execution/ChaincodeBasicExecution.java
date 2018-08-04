package io.github.hooj0.fabric.sdk.commons.core.execution;

import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Peer;

import io.github.hooj0.fabric.sdk.commons.domain.Organization;

/**
 * chaincode basic execution `install check & instantiate check` interface defined
 * @author hoojo
 * @createDate 2018年8月3日 上午9:56:53
 * @file ChaincodeBasicExecution.java
 * @package io.github.hooj0.fabric.sdk.commons.core.execution
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface ChaincodeBasicExecution {

	/**
	 * 检查Chaincode在peer上是否成功安装
	 * @author hoojo
	 * @createDate 2018年6月25日 上午10:49:01
	 */
	public boolean checkInstallChaincode(Peer peer, ChaincodeID chaincodeId) throws Exception;
	
	/**
	 * 检查通道上的peer对等节点是否安装chaincode
	 * @author hoojo
	 * @createDate 2018年6月26日 下午5:27:28
	 */
	public boolean checkInstallChaincode(ChaincodeID chaincodeId) throws Exception;

	/**
	 * 检查Chaincode在channel上是否实例化
	 * @author hoojo
	 * @createDate 2018年6月25日 上午10:48:29
	 */
	public boolean checkInstantiatedChaincode(Peer peer, ChaincodeID chaincodeId) throws Exception;

	/**
	 * 检查Chaincode在channel上是否实例化
	 * @author hoojo
	 * @createDate 2018年6月25日 上午10:48:29
	 */
	public boolean checkInstantiatedChaincode(ChaincodeID chaincodeId) throws Exception;
	
	/**
	 * 检查Chaincode是否在通道上成功安装和实例化
	 * @author hoojo
	 * @createDate 2018年6月25日 上午10:47:40
	 */
	public boolean checkChaincode(ChaincodeID chaincodeId, Organization org) throws Exception;
}
