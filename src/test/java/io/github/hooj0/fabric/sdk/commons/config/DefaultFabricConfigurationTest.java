package io.github.hooj0.fabric.sdk.commons.config;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import io.github.hooj0.fabric.sdk.commons.config.support.FabricClassConfiguration;
import io.github.hooj0.fabric.sdk.commons.config.support.FabricPropertiesConfiguration;
import lombok.extern.slf4j.Slf4j;

/**
 * default fabric configuration test unit
 * @author hoojo
 * @createDate 2018年7月28日 下午6:23:48
 * @file DefaultFabricConfigurationTest.java
 * @package io.github.hooj0.fabric.sdk.commons.config
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Slf4j
public class DefaultFabricConfigurationTest {

	@Test
	public void testSettings() {
		
		FabricClassConfiguration config = DefaultFabricConfiguration.INSTANCE.getClassConfiguration();

		String orgName = "peerOrg1";
		
		// config.setNetworkDomain("example.com");
		config.setNetworkDomain("hoojo.cnblogs.com");
		
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
		
		
		config.settingPropertyValue(FabricConfigurationPropertyKey.FABRIC_CONFIGTX_VERSION, "v1.0");
		
		
		config.settingPropertyValue(FabricConfigurationPropertyKey.CONFIG_TXLATER_URL, "http://127.0.0.1:70599");

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

		config.settingPropertyValue(FabricConfigurationPropertyKey.NETWORK_CA_ADMIN_NAME, "admin2");
		config.settingPropertyValue(FabricConfigurationPropertyKey.NETWORK_CA_ADMIN_PASSWD, "adminpw2");
		config.settingPropertyValue(FabricConfigurationPropertyKey.NETWORK_ORGS_MEMBER_USERS, "user1");

		config.printConfig("custom class properties");
	}
	
	@Test
	public void testGeneratorFilePropertiesForClass() {
		this.testSettings();
		
		DefaultFabricConfiguration.INSTANCE.getClassConfiguration().generatorPropertiesFile(new File("myconfig2.properties"));
	}
	
	@Test
	public void testClassProperties() {
		this.testSettings();
		
		assertEquals(DefaultFabricConfiguration.INSTANCE.getClassConfiguration().getCaAdminName(), "admin2");
		assertEquals(DefaultFabricConfiguration.INSTANCE.getClassConfiguration().getCaAdminPassword(), "adminpw2");
		
		FabricConfiguration classConf = DefaultFabricConfiguration.INSTANCE.getClassConfiguration();
		
		assertEquals(classConf.getFabricConfigtxVersion(), "v1.0");
		assertEquals(classConf.isEnabledFabricTLS(), true);
		assertEquals(classConf.isFabricConfigtxV10(), true);

		assertEquals(classConf.getFabricNetworkHost(), "192.168.8.8");
		assertEquals(classConf.getConfigtxlaterURL(), "http://127.0.0.1:70599");

		assertEquals(classConf.getNetworkDomain(), "hoojo.cnblogs.com");

		assertEquals(classConf.getCommonRootPath(), "src/test/fixture/integration");
		assertEquals(classConf.getChaincodeRootPath(), "src\\test\\fixture\\integration\\gocc\\sample11");
		assertEquals(classConf.getCryptoChannelConfigRootPath(), "src\\test\\fixture\\integration\\e2e-2Orgs\\v1.0");
		assertEquals(classConf.getChannelArtifactsPath(), "src\\test\\fixture\\integration\\e2e-2Orgs\\v1.0\\channel-artifacts");
		assertEquals(classConf.getEndorsementPolicyFilePath(), "src\\test\\fixture\\integration\\chaincode-endorsement-policy.yaml");
		assertEquals(classConf.getNetworkConfigRootPath(), "src\\test\\fixture\\integration\\network_configs");
		

		assertEquals(classConf.getDeployWaitTime(), 100);
		assertEquals(classConf.getProposalWaitTime(), 1000);
		assertEquals(classConf.getTransactionWaitTime(), 10);
		
		assertArrayEquals(classConf.getUsers(), new String[] { "user1" });
		
	}
	
	@Test
	public void testFabricClassProperties() {
		this.testSettings();
		
		FabricConfiguration classConf = DefaultFabricConfiguration.INSTANCE.getClassConfiguration();
		
		log.debug("getOrganizations: {}", classConf.getOrganizations());
		log.debug("peerOrg1: {}", classConf.getOrganization("peerOrg1"));
		log.debug("peerOrg2: {}", classConf.getOrganization("peerOrg2"));
		
		log.debug("orderer: {}", classConf.getOrdererProperties("orderer.example.com"));
		log.debug("eventhub: {}", classConf.getEventHubProperties("peer0.org2.example.com"));
		log.debug("peer: {}", classConf.getPeerProperties("peer0.org1.example.com"));
	}
	
	@Test
	public void testGeneratorFileProperties() {
		
		// FabricConfigurationPropertyKey.SDK_CONFIG_NAME = "myconfig.properties";
		DefaultFabricConfiguration.INSTANCE.getPropertiesConfiguration().generatorPropertiesFile();
	}
	
	@Test
	public void testFileProperties() {
		
		assertEquals(DefaultFabricConfiguration.INSTANCE.getPropertiesConfiguration().getCaAdminName(), "admin2");
		assertEquals(DefaultFabricConfiguration.INSTANCE.getPropertiesConfiguration().getCaAdminPassword(), "adminpw2");
		
		FabricPropertiesConfiguration classConf = DefaultFabricConfiguration.INSTANCE.getPropertiesConfiguration();
		
		assertEquals(classConf.getFabricConfigtxVersion(), "v1.0");
		assertEquals(classConf.isEnabledFabricTLS(), false);
		assertEquals(classConf.isFabricConfigtxV10(), true);

		assertEquals(classConf.getFabricNetworkHost(), "192.168.8.8");
		assertEquals(classConf.getConfigtxlaterURL(), "http://127.0.0.1:70599");

		assertEquals(classConf.getNetworkDomain(), "example.com");

		assertEquals(classConf.getCommonRootPath(), "src/test/fixture/integration");
		assertEquals(classConf.getChaincodeRootPath(), "src\\test\\fixture\\integration\\gocc\\sample11");
		assertEquals(classConf.getCryptoChannelConfigRootPath(), "src\\test\\fixture\\integration\\e2e-2Orgs\\v1.0");
		assertEquals(classConf.getChannelArtifactsPath(), "src\\test\\fixture\\integration\\e2e-2Orgs\\v1.0\\channel-artifacts");
		assertEquals(classConf.getEndorsementPolicyFilePath(), "src\\test\\fixture\\integration\\chaincode-endorsement-policy.yaml");
		assertEquals(classConf.getNetworkConfigRootPath(), "src\\test\\fixture\\integration\\network_configs");
		

		assertEquals(classConf.getDeployWaitTime(), 100);
		assertEquals(classConf.getProposalWaitTime(), 1000);
		assertEquals(classConf.getTransactionWaitTime(), 10);
		
		assertArrayEquals(classConf.getUsers(), new String[] { "user1" });
	}
	
	@Test
	public void testFabricPropertiesProperties() {
		FabricConfiguration classConf = DefaultFabricConfiguration.INSTANCE.getPropertiesConfiguration();
		
		log.debug("getOrganizations: {}", classConf.getOrganizations());
		log.debug("peerOrg1: {}", classConf.getOrganization("peerOrg1"));
		log.debug("peerOrg2: {}", classConf.getOrganization("peerOrg2"));
		
		log.debug("orderer: {}", classConf.getOrdererProperties("orderer.example.com"));
		log.debug("eventhub: {}", classConf.getEventHubProperties("peer0.org2.example.com"));
		log.debug("peer: {}", classConf.getPeerProperties("peer0.org1.example.com"));
	}
}
