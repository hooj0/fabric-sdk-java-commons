package io.github.hooj0.fabric.sdk.commons.config.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import io.github.hooj0.fabric.sdk.commons.config.AbstractConfiguration;
import io.github.hooj0.fabric.sdk.commons.domain.Organization;
import io.github.hooj0.fabric.sdk.commons.store.FabricKeyValueStore;

/**
 * Properties FileSystem configuration
 * @author hoojo
 * @createDate 2018年7月23日 上午9:24:19
 * @file FabricPropertiesConfiguration.java
 * @package io.github.hooj0.fabric.sdk.commons.config.support
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public final class FabricPropertiesConfiguration extends AbstractConfiguration {

	/** 系统变量 SDK配置 */
	private static final String ENV_FABRIC_SDK_CONFIG = "HYPERLEDGER_FABRIC_SDK_COMMONS_CONFIG";
	/** 默认SDK配置 */
	private static final String DEFAULT_SDK_CONFIG = "default-config.properties";

	private FabricPropertiesConfiguration() {
		super(FabricPropertiesConfiguration.class);
		
		InputStream stream = null;
		try {
			// 读取 sdk 配置文件名称，没有就读取默认配置 DEFAULT_CONFIG
			String configPath = StringUtils.defaultIfBlank(System.getenv(ENV_FABRIC_SDK_CONFIG), DEFAULT_SDK_CONFIG);
			File configFile = new File(configPath).getAbsoluteFile();
			logger.info("FileSystem加载SDK配置文件： {}， 配置文件是否存在: {}", configFile.toString(), configFile.exists());
			
			if (!configFile.exists()) {
				stream = FabricPropertiesConfiguration.class.getResourceAsStream("/" + configFile.getName());
				logger.info("ClassPath加载SDK配置文件： {}， 配置文件是否存在: {}", configFile.getName(), stream != null);
			} else {
				stream = new FileInputStream(configFile);
			}
			
			SDK_PROPERTIES.load(stream);
		} catch (Exception e) {
			logger.warn("加载SDK配置文件: {} 失败. 使用SDK默认配置", DEFAULT_SDK_CONFIG);
		} finally {
			
			configurationDefaultValues();
			
			preparConfiguration();

			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
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

	/** configtxlator 配置转换工具URL配置 */
	public String getFabricConfigTxLaterURL() {
		return "http://" + FABRIC_NETWORK_HOST + ":7059";
	}
	
	public String getFabricConfigGeneratorVersion() {
		return getSDKProperty(FABRIC_CONFIG_GENERATOR_VERSION, System.getenv("FAB_CONFIG_GEN_VERS"));
	}
	
	/** 通道、区块、组织等配置根目录 */
	public String getCommonConfigRootPath() {
		return getSDKProperty(COMMON_CONFIG_ROOT_PATH); // src/test/fixture/sdkintegration
	}
	
	/** crypto-config & channel-artifacts 根目录 */
	public String getCryptoTxConfigRootPath() {
		return Paths.get(getCommonConfigRootPath(), getSDKProperty(CRYPTO_TX_CONFIG_ROOT_PATH, "/e2e-2Orgs/"), getFabricConfigGeneratorVersion()).toString();
	}
	
	/** 通道配置路径 */
	public String getChannelPath() {
		return Paths.get(getCryptoTxConfigRootPath(), getSDKProperty(CHANNEL_TRANSACTION_FILE_PATH, "/channel-artifacts")).toString();
	}
	
	/** 通道tx配置目录 */
	public String getChaincodePath() {
		return Paths.get(getCommonConfigRootPath(), getSDKProperty(CHAINCODE_SOURCE_CODE_FILE_PATH)).toString();
	}
	
	/** 背书文件配置路径 */
	public String getEndorsementPolicyFilePath() {
		return Paths.get(getCommonConfigRootPath(), getSDKProperty(ENDORSEMENT_POLICY_FILE_PATH)).toString();
	}

	/** network config 父目录配置路径 */
	public String getNetworkConfigDirFilePath() {
		return Paths.get(getCommonConfigRootPath(), getSDKProperty(NETWORK_CONFIG_DIR_FILE_PATH)).toString();
	}
	
	public String getOrdererOrgsDomain() {
		return getSDKProperty(FABRIC_CONFIG_GENERATOR_VERSION, "example.com");
	}

	@Override
	public FabricKeyValueStore getKeyValueStore() {
		return null;
	}

	@Override
	public String getDefaultOrgName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getChannelName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AdminInfo getAdminInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getUsers() {
		// TODO Auto-generated method stub
		return null;
	}
}
