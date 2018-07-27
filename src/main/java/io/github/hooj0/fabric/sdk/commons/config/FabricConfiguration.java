package io.github.hooj0.fabric.sdk.commons.config;

import java.util.Collection;
import java.util.Properties;

import io.github.hooj0.fabric.sdk.commons.domain.Organization;
import io.github.hooj0.fabric.sdk.commons.store.FabricKeyValueStore;

/**
 * fabric config interface
 * @changelog Redefining the interface method
 * @author hoojo
 * @createDate 2018年7月21日 下午11:17:03
 * @file FabricConfiguration.java
 * @package io.github.hooj0.fabric.sdk.commons.config
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface FabricConfiguration {
	
	/** 是否是使用 fabric configtx 1.0的版本 */
	public boolean isFabricConfigtxV10();
	
	/** 是否启用TLS 安全证书 */ 
	public boolean isEnabledFabricTLS();
	
	/** 区块链网络主机IP地址 */
	public String getFabricNetworkHost();
	
	/** 交易等待时间 */
	public int getTransactionWaitTime();

	/** 部署等待时间 */
	public int getDeployWaitTime();

	/** 交易动作等待时间 */
	public long getProposalWaitTime();

	/** configtxlator 配置转换工具URL配置，如：http://127.0.0.1:7059 */
	public String getConfigtxlaterURL();
	
	/** fabric network 使用 configtx.yaml 中的 Capabilities 下的版本信息，v1.0 & v1.1 */
	public String getFabricConfigtxVersion();
	
	/**
	 * 通道、区块、组织、Chaincode等文件根目录，
	 * 如 ：src/test/fixture/sdkintegration 
	 */
	public String getCommonRootPath();
	
	/** 
	 * crypto-config & channel-artifacts 加密配置和通道文件的 根目录，
	 * 如：/e2e-2Orgs <br/>
	 * 返回完整路径：src/test/fixture/integration/e2e-2Orgs/v1.0/
	 **/
	public String getCryptoChannelConfigRootPath();
	
	/** 
	 * 通道文件的配置目录，如: /channel-artifacts <br/>
	 * 返回完整路径： src/test/fixture/integration/e2e-2Orgs/v1.0/channel-artifacts 
	 **/
	public String getChannelArtifactsPath();
	
	/** 
	 * chaincode 源码文件路径，如：/gocc/sample11 <br/> 
	 * 返回完整路径：src/test/fixture/integration/gocc/sample_11 
	 **/
	public String getChaincodeRootPath();
	
	/** 
	 * 背书文件配置路径，如：chaincodeendorsementpolicy.yaml <br/> 
	 * 返回完整路径：src/test/fixture/integration/chaincodeendorsementpolicy.yaml 
	 **/
	public String getEndorsementPolicyFilePath();

	/** 
	 * network config 配置路径父目录，如：network_configs <br/>
	 * 返回完整路径：src/test/fixture/sdkintegration/network_configs 
	 **/
	public String getNetworkConfigRootPath();
	
	/** 区块链网络使用的域名，对应 crypto-config.yaml 中的 OrdererOrgs.domain */ 
	public String getNetworkDomain();
	
	
	/** fabric网络 Ca 节点 管理员 密码  */
	public String getCaAdminPassword();
	
	/** fabric网络 Ca 节点 管理员 用户名*/
	public String getCaAdminName();
	
	/** organization中的member user */
	public String[] getUsers();
	
	/**  获取全部组织 */
	public Collection<Organization> getOrganizations();
	
	/** 获取 事件机制配置 */
	public Properties getEventHubProperties(String name);
	
	/** 获取节点配置 */
	public Properties getPeerProperties(String name);
	
	/** 获取 orderer 服务配置 */
	public Properties getOrdererProperties(String name);
	
	/** 获取组织 */
	public Organization getOrganization(String name);
	
	/**
	 * 系统默认使用的 {@link #FabricKeyValueStore}，<br/>
	 * <b>在使用properties配置情况下</b>会使用文件配置 {@link #FileSystemKeyValueStore}，<br/>
	 * 文件的 properties配置 default.key.value.store.file <br/>
	 * 如：default.key.value.store.file=/home/fabric-key-val.properties 
	 * <br/><br/>
	 * 
	 * <b>在使用class配置情况下</b>会使用文件配置 {@link #MemoryKeyValueStore }
	 * @author hoojo
	 * @createDate 2018年7月27日 下午5:25:44
	 * @return FabricKeyValueStore
	 */
	public FabricKeyValueStore getDefaultKeyValueStore();
}
