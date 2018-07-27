package io.github.hooj0.fabric.sdk.commons.config;

import org.apache.commons.lang3.StringUtils;

/**
 * fabric configuration properties key
 * @author hoojo
 * @createDate 2018年7月27日 下午6:24:14
 * @file ConfigurationPropertiesKey.java
 * @package io.github.hooj0.fabric.sdk.commons.config
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface FabricConfigurationPropertyKey {

	/** 配置Key前缀 */
	public static final String PREFIX = "hyperledger.fabric.sdk.commons.";
	
	
	
	/**
	 * 当前使用 fabric 网络 configtx 版本。<br/>
	 * 不同版本通道、证书、交易配置 v1.0 and v1.1 src/test/fixture/sdkintegration/e2e-2Orgs
	 */
	public static final String FABRIC_CONFIGTX_VERSION = "fabric.network.configtx.version";
	
	
	
	/** 当前使用 fabric 网络 configtx 版本 */
	public static final String ENV_FABRIC_CONFIGTX_VERSION = "FABRIC_NETWORK_CONFIGTX_VERSION";
	/** 环境变量配置，是否启用tls */
	public static final String ENV_DEFAULT_TLS_ENABLED = "HYPERLEDGER_FABRIC_SDK_COMMONS_TLS_ENABLED";
	/** 环境变量中fabric network主机ip地址 */
	public static final String ENV_NETWORK_HOST_KEY = "HYPERLEDGER_FABRIC_SDK_COMMONS_NETWORK_HOST";
	/** 跳过 network-config */
	public static final String ENV_NETWORK_FILE_KEEP = ENV_NETWORK_HOST_KEY + "_KEEP";
	
	
	
	
	/** fabric network host 区块链网络的主机IP地址 */
	public static final String FABRIC_NETWORK_HOST = StringUtils.defaultString(System.getenv(ENV_NETWORK_HOST_KEY), "192.168.8.8");
	/** configtxlator 配置转换工具URL配置 */
	public static final String CONFIG_TXLATER_URL = PREFIX + ".configtxlater.url";
	
	
	
	
	/** 调用合约等待时间 */
	public static final String INVOKE_WAIT_TIME = PREFIX + "invoke.wait.time";
	/** 部署合约等待时间 */
	public static final String DEPLOY_WAIT_TIME = PREFIX + "deploy.wait.time";
	/** 发起proposal等待时间 */
	public static final String PROPOSAL_WAIT_TIME = PREFIX + "proposal.wait.time";
	
	
	
	
	/** fabric network 域名 */
	public static final String NETWORK_DOMAIN = PREFIX + "network.domain";
	/** 是否启用 tls 安全模式 */
	public static final String NETWORK_TLS_ENABLED = PREFIX + "network.tls.enable";

	
	
	
	/** chaincode 和 组织 、通道、区块配置的根目录 */
	public static final String COMMON_CONFIG_ROOT_PATH = PREFIX + "config.root.path";
	/** crypto-config & channel-artifacts 根目录 */
	public static final String CRYPTO_CHANNEL_CONFIG_ROOT_PATH = PREFIX + "crypto.channel.config.root.path";
	/** chaincode 源码文件路径 */
	public static final String CHAINCODE_SOURCE_ROOT_PATH = PREFIX + "chaincode.source.code.root.path";
	/** 通道配置 路径*/
	public static final String CHANNEL_ARTIFACTS_ROOT_PATH = PREFIX + "channel.artifacts.root.path";
	/** chaincode背书策略文件路径 */
	public static final String ENDORSEMENT_POLICY_FILE_PATH = PREFIX + "endorsement.policy.file.path";
	/** fabric network  config 配置文件路径 */
	public static final String NETWORK_CONFIG_ROOT_PATH = PREFIX + "network.config.root.path";
	/** fabric network Ca 节点管理员用户名 */
	public static final String NETWORK_CA_ADMIN_NAME = PREFIX + "network.ca.admin.name";
	/** fabric network Ca 节点管理员密码 */
	public static final String NETWORK_CA_ADMIN_PASSWD = PREFIX + "network.ca.admin.passwd";
	/** fabric network 普通用户 */
	public static final String NETWORK_ORGS_MEMBER_USERS = PREFIX + "network.orgs.member.users";
	
	
	
	
	/** 系统变量 SDK配置 */
	public static final String ENV_FABRIC_SDK_CONFIG = "HYPERLEDGER_FABRIC_SDK_COMMONS_CONFIG";
	/** 默认SDK配置 */
	public static final String DEFAULT_SDK_CONFIG_NAME = "default-config.properties";
	/** 默认的keyvalue store 配置文件 */
	public static final String DEFAULT_KEY_VALUE_STORE_FILE = "default.key.value.store.file";
	/** 默认的keyvalue store 配置文件名称 */
	public static final String DEFAULT_KEY_VALUE_STORE_FILE_NAME = "HFC-KV.properties";
	
}
