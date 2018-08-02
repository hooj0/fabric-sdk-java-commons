package io.github.hooj0.fabric.sdk.commons.config;

import java.io.File;

import io.github.hooj0.fabric.sdk.commons.config.support.FabricClassConfiguration;
import io.github.hooj0.fabric.sdk.commons.config.support.FabricPropertiesConfiguration;

/**
 * default fabric configuration support
 * @changelog Add local settings, set class properties
 * @author hoojo
 * @createDate 2018年7月23日 上午9:22:48
 * @file DefaultFabricConfiguration.java
 * @package io.github.hooj0.fabric.sdk.commons.config
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public enum DefaultFabricConfiguration {
	INSTANCE;

	public FabricConfiguration getDefaultConfiguration() {
		return FabricClassConfiguration.getInstance();
		//return FabricPropertiesConfiguration.getInstance();
	}
	
	public FabricPropertiesConfiguration getPropertiesConfiguration() {
		return FabricPropertiesConfiguration.getInstance();
	}
	
	public FabricClassConfiguration getClassConfiguration() {
		return FabricClassConfiguration.getInstance();
	}

	private DefaultFabricConfiguration() {
		this.setLocalSettings();
	}
	
	@SuppressWarnings("unused")
	private void setLocalSettings() {
		FabricClassConfiguration config = this.getClassConfiguration();

		String orgName = "peerOrg1";
		
		config.setNetworkDomain("example.com");
		
		config.setMspId(orgName, "Org1MSP");
		config.setOrgDomain(orgName, "org1." + config.getNetworkDomain());
		config.setCaName(orgName, "ca0");
		config.setCaLocation(orgName, "http://" + config.getFabricNetworkHost() + ":8054");
		config.setOrdererLocation(orgName, "orderer.example.com@grpc://" + config.getFabricNetworkHost() + ":7050");
		config.setPeerLocation(orgName, "peer0.org1.example.com@grpc://" + config.getFabricNetworkHost() + ":7051, peer1.org1.example.com@grpc://" + config.getFabricNetworkHost() + ":7056");
		config.setEventHubLocation(orgName, "peer0.org1.example.com@grpc://" + config.getFabricNetworkHost() + ":7053, peer1.org1.example.com@grpc://" + config.getFabricNetworkHost() + ":7058");

		orgName = "peerOrg2";
		config.setMspId(orgName, "Org2MSP");
		config.setOrgDomain(orgName, "org2." + config.getNetworkDomain());
		// config.setCaName(orgName, "ca1");
		config.setCaLocation(orgName, "http://" + config.getFabricNetworkHost() + ":7054");
		config.setOrdererLocation(orgName, "orderer.example.com@grpc://" + config.getFabricNetworkHost() + ":7050");
		config.setPeerLocation(orgName, "peer0.org2.example.com@grpc://" + config.getFabricNetworkHost() + ":8051, peer1.org2.example.com@grpc://" + config.getFabricNetworkHost() + ":8056");
		config.setEventHubLocation(orgName, "peer0.org2.example.com@grpc://" + config.getFabricNetworkHost() + ":8053, peer1.org2.example.com@grpc://" + config.getFabricNetworkHost() + ":8058");
		
		
		config.settingPropertyValue(FabricConfigurationPropertyKey.FABRIC_CONFIGTX_VERSION, "v1.1");
		
		config.settingPropertyValue(FabricConfigurationPropertyKey.CONFIG_TXLATER_URL, "http://" + config.getFabricNetworkHost() + ":7059");

		config.settingPropertyValue(FabricConfigurationPropertyKey.INVOKE_WAIT_TIME, "10");
		config.settingPropertyValue(FabricConfigurationPropertyKey.DEPLOY_WAIT_TIME, "100");
		config.settingPropertyValue(FabricConfigurationPropertyKey.PROPOSAL_WAIT_TIME, "1000");

		config.settingPropertyValue(FabricConfigurationPropertyKey.NETWORK_TLS_ENABLED, "true");

		config.settingPropertyValue(FabricConfigurationPropertyKey.COMMON_CONFIG_ROOT_PATH, "src/test/fixture/integration");
		config.settingPropertyValue(FabricConfigurationPropertyKey.CRYPTO_CHANNEL_CONFIG_ROOT_PATH, "/e2e-2Orgs");
		config.settingPropertyValue(FabricConfigurationPropertyKey.CHAINCODE_SOURCE_ROOT_PATH, "/gocc/sample11");
		config.settingPropertyValue(FabricConfigurationPropertyKey.CHANNEL_ARTIFACTS_ROOT_PATH, "/channel-artifacts");
		config.settingPropertyValue(FabricConfigurationPropertyKey.ENDORSEMENT_POLICY_FILE_PATH, "chaincode-endorsement-policy.yaml");
		config.settingPropertyValue(FabricConfigurationPropertyKey.NETWORK_CONFIG_ROOT_PATH, "network_configs");

		config.settingPropertyValue(FabricConfigurationPropertyKey.NETWORK_CA_ADMIN_NAME, "admin");
		config.settingPropertyValue(FabricConfigurationPropertyKey.NETWORK_CA_ADMIN_PASSWD, "adminpw");
		config.settingPropertyValue(FabricConfigurationPropertyKey.NETWORK_ORGS_MEMBER_USERS, "user1");

		config.printConfig("custom class properties");
	}
	
	public static void main(String[] args) {
		DefaultFabricConfiguration.INSTANCE.getClassConfiguration().generatorPropertiesFile(new File("my-store.properties"));
	}
}
