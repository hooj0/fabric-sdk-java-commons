package io.github.hooj0.fabric.sdk.commons.core.manager;

import java.io.File;

import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.junit.Test;

import io.github.hooj0.fabric.sdk.commons.config.DefaultFabricConfiguration;
import io.github.hooj0.fabric.sdk.commons.config.FabricConfiguration;
import io.github.hooj0.fabric.sdk.commons.config.support.FabricClassConfiguration;
import io.github.hooj0.fabric.sdk.commons.config.support.FabricPropertiesConfiguration;
import io.github.hooj0.fabric.sdk.commons.store.FabricKeyValueStore;

/**
 * channel manager test unit
 * @author hoojo
 * @createDate 2018年7月28日 下午9:16:26
 * @file ChannelManagerTest.java
 * @package io.github.hooj0.fabric.sdk.commons.core.manager
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class ChannelManagerTest {

	@Test
	public void testInstance() {
		
		FabricConfiguration config = FabricPropertiesConfiguration.getInstance();
		FabricKeyValueStore store = config.getDefaultKeyValueStore();

		HFClient client = HFClient.createNewInstance();
		try {
			client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		UserManager manager = new UserManager(config, store);
		ChannelManager channelManager = new ChannelManager(config, client, store);
		
		try {
			manager.initialize(config.getCaAdminName(), config.getCaAdminPassword(), config.getUsers());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			//channelManager.initialize("mychannel2", config.getOrganization("peerOrg1"));
			channelManager.initialize("mychannel2", config.getOrganization("peerOrg1"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testInstance2() {
		
		FabricConfiguration config = FabricClassConfiguration.getInstance();
		FabricKeyValueStore store = config.getDefaultKeyValueStore();
		
		HFClient client = HFClient.createNewInstance();
		try {
			client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		UserManager manager = new UserManager(config, store);
		ChannelManager channelManager = new ChannelManager(config, client, store);
		
		try {
			manager.initialize(config.getCaAdminName(), config.getCaAdminPassword(), config.getUsers());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			channelManager.initialize("mychannel2", config.getOrganization("peerOrg1"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testInstance3() {
		
		FabricConfiguration config = DefaultFabricConfiguration.INSTANCE.getDefaultConfiguration();
		FabricKeyValueStore store = config.getDefaultKeyValueStore();
		
		HFClient client = HFClient.createNewInstance();
		try {
			client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		UserManager manager = new UserManager(config, store);
		ChannelManager channelManager = new ChannelManager(config, client, store);
		
		try {
			manager.initialize(config.getCaAdminName(), config.getCaAdminPassword(), config.getUsers());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			channelManager.initialize("mychannel2", config.getOrganization("peerOrg1"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testInstance4() {
		
		FabricConfiguration config = FabricPropertiesConfiguration.getInstance();
		
		HFClient client = HFClient.createNewInstance();
		try {
			client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		File storeFile = new File("src/test/resources/fabric-kv-store.properties");
		
		UserManager manager = new UserManager(config, storeFile);
		ChannelManager channelManager = new ChannelManager(config, client, storeFile);
		
		try {
			manager.initialize(config.getCaAdminName(), config.getCaAdminPassword(), config.getUsers());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			channelManager.initialize("mychannel2", config.getOrganization("peerOrg1"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testInitialize() {
		
		FabricConfiguration config = FabricPropertiesConfiguration.getInstance();
		FabricKeyValueStore store = config.getDefaultKeyValueStore();

		HFClient client = HFClient.createNewInstance();
		try {
			client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		UserManager manager = new UserManager(config, store);
		ChannelManager channelManager = new ChannelManager(config, client, store);
		
		try {
			manager.initialize(config.getCaAdminName(), config.getCaAdminPassword(), config.getUsers());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			channelManager.initialize("mychannel", config.getOrganization("peerOrg1"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testInitialize2() {
		
		FabricConfiguration config = FabricPropertiesConfiguration.getInstance();
		FabricKeyValueStore store = config.getDefaultKeyValueStore();

		HFClient client = HFClient.createNewInstance();
		try {
			client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		UserManager manager = new UserManager(config, store);
		ChannelManager channelManager = new ChannelManager(config, client, store);
		
		try {
			manager.initialize(config.getCaAdminName(), config.getCaAdminPassword(), config.getUsers());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			channelManager.initialize("mychannel", config.getOrganization("peerOrg1"));
			channelManager.initialize("mychannel2", config.getOrganization("peerOrg1"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testInitialize3() {
		
		FabricConfiguration config = FabricPropertiesConfiguration.getInstance();
		FabricKeyValueStore store = config.getDefaultKeyValueStore();

		HFClient client = HFClient.createNewInstance();
		try {
			client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		UserManager manager = new UserManager(config, store);
		ChannelManager channelManager = new ChannelManager(config, client, store);
		
		try {
			manager.initialize(config.getCaAdminName(), config.getCaAdminPassword(), config.getUsers());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			channelManager.initialize("mychannel", config.getOrganization("peerOrg1"));
			channelManager.initialize("mychannel2", config.getOrganization("peerOrg2"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
