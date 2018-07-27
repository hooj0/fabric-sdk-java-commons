package io.github.hooj0.fabric.sdk.commons.config.support;

import java.nio.file.Paths;

import org.apache.commons.lang3.StringUtils;

import io.github.hooj0.fabric.sdk.commons.config.AbstractConfiguration;
import io.github.hooj0.fabric.sdk.commons.config.FabricConfiguration;

/**
 * basic abstract config support
 * @changelog Add config basic property getter method support
 * @author hoojo
 * @createDate 2018年7月27日 上午11:27:21
 * @file AbstractConfigurationSupport.java
 * @package io.github.hooj0.fabric.sdk.commons.config.support
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public abstract class AbstractConfigurationSupport extends AbstractConfiguration implements FabricConfiguration {

	public AbstractConfigurationSupport(Class<?> clazz) {
		super(clazz);
	}

	/** 是否是使用 fabric configtx 1.0的版本 */
	public boolean isFabricConfigtxV10() {
		return getFabricConfigtxVersion().contains("1.0") || getFabricConfigtxVersion().contains("10");
	}
	
	/** 是否启用TLS 安全证书 */ 
	public boolean isEnabledFabricTLS() {
		return enableFabricTLS;
	}
	
	/** 区块链网络主机IP地址 */
	@Override
	public String getFabricNetworkHost() {
		return FABRIC_NETWORK_HOST;
	}
	
	/** 交易等待时间 */
	public int getTransactionWaitTime() {
		return Integer.parseInt(getProperty(INVOKE_WAIT_TIME));
	}

	/** 部署等待时间 */
	public int getDeployWaitTime() {
		return Integer.parseInt(getProperty(DEPLOY_WAIT_TIME));
	}

	/** 交易动作等待时间 */
	public long getProposalWaitTime() {
		return Integer.parseInt(getProperty(PROPOSAL_WAIT_TIME));
	}

	/** configtxlator 配置转换工具URL配置，如：http://127.0.0.1:7059 */
	public String getConfigtxlaterURL() {
		return getProperty(CONFIG_TXLATER_URL, "http://" + FABRIC_NETWORK_HOST + ":7059");
	}
	
	/** fabric network 使用 configtx.yaml 中的 Capabilities 下的版本信息，v1.0 & v1.1 */
	public String getFabricConfigtxVersion() {
		return getProperty(FABRIC_CONFIGTX_VERSION, System.getenv(ENV_FABRIC_CONFIGTX_VERSION));
	}
	
	/**
	 * 通道、区块、组织、Chaincode等文件根目录，
	 * 如 ：src/test/fixture/sdkintegration 
	 */
	public String getCommonRootPath() {
		return getProperty(COMMON_CONFIG_ROOT_PATH);
	}
	
	/** 
	 * crypto-config & channel-artifacts 加密配置和通道文件的 根目录，
	 * 如：/e2e-2Orgs <br/>
	 * 返回完整路径：src/test/fixture/integration/e2e-2Orgs/v1.0/
	 **/
	public String getCryptoChannelConfigRootPath() {
		return Paths.get(getCommonRootPath(), getProperty(CRYPTO_CHANNEL_CONFIG_ROOT_PATH, "/e2e-2Orgs/"), getFabricConfigtxVersion()).toString();
	}
	
	/** 
	 * 通道文件的配置目录，如: /channel-artifacts <br/>
	 * 返回完整路径： src/test/fixture/integration/e2e-2Orgs/v1.0/channel-artifacts 
	 **/
	public String getChannelArtifactsPath() {
		return Paths.get(getCryptoChannelConfigRootPath(), getProperty(CHANNEL_ARTIFACTS_ROOT_PATH, "/channel-artifacts")).toString();
	}
	
	/** 
	 * chaincode 源码文件路径，如：/gocc/sample11 <br/> 
	 * 返回完整路径：src/test/fixture/integration/gocc/sample_11 
	 **/
	public String getChaincodeRootPath() {
		return Paths.get(getCommonRootPath(), getProperty(CHAINCODE_SOURCE_ROOT_PATH)).toString();
	}
	
	/** 
	 * 背书文件配置路径，如：chaincodeendorsementpolicy.yaml <br/> 
	 * 返回完整路径：src/test/fixture/integration/chaincodeendorsementpolicy.yaml 
	 **/
	public String getEndorsementPolicyFilePath() {
		return Paths.get(getCommonRootPath(), getProperty(ENDORSEMENT_POLICY_FILE_PATH)).toString();
	}

	/** 
	 * network config 配置路径父目录，如：network_configs <br/>
	 * 返回完整路径：src/test/fixture/sdkintegration/network_configs 
	 **/
	public String getNetworkConfigRootPath() {
		return Paths.get(getCommonRootPath(), getProperty(NETWORK_CONFIG_ROOT_PATH)).toString();
	}
	
	/** 区块链网络使用的域名，对应 crypto-config.yaml 中的 OrdererOrgs.domain */ 
	public String getNetworkDomain() {
		return this.getProperty(NETWORK_DOMAIN);
	}
	
	
	/** fabric网络 Ca 节点 管理员 密码  */
	public String getCaAdminPassword() {
		return getProperty(NETWORK_CA_ADMIN_NAME);
	}
	
	/** fabric网络 Ca 节点 管理员 用户名*/
	public String getCaAdminName() {
		return getProperty(NETWORK_CA_ADMIN_PASSWD);
	}
	
	/** organization中的member user */
	public String[] getUsers() {
		return StringUtils.split(StringUtils.strip(getProperty(NETWORK_ORGS_MEMBER_USERS)), ",");
	}
}
