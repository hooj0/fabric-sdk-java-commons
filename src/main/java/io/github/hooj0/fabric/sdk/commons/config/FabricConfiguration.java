package io.github.hooj0.fabric.sdk.commons.config;

import java.io.File;
import java.util.Collection;
import java.util.Properties;

import io.github.hooj0.fabric.sdk.commons.domain.Organization;
import io.github.hooj0.fabric.sdk.commons.store.FabricKeyValueStore;

/**
 * fabric config
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
	
	public FabricKeyValueStore getKeyValueStore();
	
	/**  获取全部组织 */
	public Collection<Organization> getOrganizations();
	
	/** 获取组织 */
	public Organization getOrganization(String name);
	
	/** 获取节点配置 */
	public Properties getPeerProperties(String name);
	
	/** 获取 orderer 服务配置 */
	public Properties getOrdererProperties(String name);
	
	/** 获取 事件机制配置 */
	public Properties getEventHubProperties(String name);
	
	public Properties getTLSCertProperties(final String type, final String name);
	
	//public String getFabricNetworkHost();
	
	public boolean isRunningAgainstFabric10();
	public boolean isRunningFabricTLS();
	
	/** network-config.yaml 配置文件 */
	public File getNetworkConfigFile();
	
	/** 区块链网络使用的域名，对应 crypto-config.yaml 中的 OrdererOrgs.domain */
	public String getOrdererOrgsDomain();

	/** 交易等待时间 */
	public int getTransactionWaitTime();
	
	/** 部署等待时间 */
	public int getDeployWaitTime();

	/** 交易动作等待时间 */
	public long getProposalWaitTime();
	
	public String getFabricConfigGeneratorVersion();
	
	/** 通道、区块、组织等配置根目录 */
	public String getCommonConfigRootPath();
	
	/** crypto-config & channel-artifacts 根目录 */
	public String getCryptoTxConfigRootPath();
	
	/** 通道配置路径 */
	public String getChannelPath();
	
	/** 通道tx配置目录 */
	public String getChaincodePath();
	
	/** 背书文件配置路径 */
	public String getEndorsementPolicyFilePath();

	/** network config 父目录配置路径 */
	public String getNetworkConfigDirFilePath();
	
	/** configtxlator 配置转换工具URL配置 */
	public String getFabricConfigTxLaterURL();
}
