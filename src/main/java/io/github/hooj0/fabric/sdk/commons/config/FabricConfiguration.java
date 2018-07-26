package io.github.hooj0.fabric.sdk.commons.config;

import java.io.File;
import java.util.Collection;
import java.util.Properties;

import io.github.hooj0.fabric.sdk.commons.domain.Organization;
import io.github.hooj0.fabric.sdk.commons.store.FabricKeyValueStore;

/**
 * fabric config interface
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
	
	/** 键值存储系统 */
	public FabricKeyValueStore getKeyValueStore();
	
	/** 系统使用默认的组织名称 */
	public String getDefaultOrgName();
	
	/** 通道名称 */
	public String getChannelName();
	
	/** 区块链网络 管理员用户名和密码  */
	public AdminInfo getAdminInfo();
	
	/** 区块链网络 普通用户 */
	public String[] getUsers();
	
	/** 区块链网络主机host */
	public String getFabricNetworkHost();
	
	/** 区块链网络是否是 1.0的版本 */
	public boolean isRunningAgainstFabric10();
	
	/** 是否启用TLS */
	public boolean isRunningFabricTLS();
	
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
	
	interface AdminInfo {
		String getName();
		String getPassword();
	}
}
