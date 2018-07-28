package io.github.hooj0.fabric.sdk.commons.core.manager;

import java.io.File;

import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.junit.Test;

import io.github.hooj0.fabric.sdk.commons.config.DefaultFabricConfiguration;
import io.github.hooj0.fabric.sdk.commons.config.FabricConfiguration;
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
		
		FabricConfiguration config = DefaultFabricConfiguration.INSTANCE.getClassConfiguration();
		
		HFClient client = HFClient.createNewInstance();
		try {
			client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ChannelManager manager = new ChannelManager(config, client);
		
		try {
			manager.initialize("mychannel", config.getOrganization("peerOrg1"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testInstance2() {
		
		FabricConfiguration config = DefaultFabricConfiguration.INSTANCE.getClassConfiguration();
		FabricKeyValueStore store = config.getDefaultKeyValueStore();
		
		HFClient client = HFClient.createNewInstance();
		try {
			client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ChannelManager manager = new ChannelManager(config, client, store);
		
		try {
			manager.initialize("mychannel", config.getOrganization("peerOrg1"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testInstance3() {
		
		FabricConfiguration config = DefaultFabricConfiguration.INSTANCE.getPropertiesConfiguration();
		FabricKeyValueStore store = config.getDefaultKeyValueStore();
		
		HFClient client = HFClient.createNewInstance();
		try {
			client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ChannelManager manager = new ChannelManager(config, client, store);
		
		try {
			manager.initialize("mychannel", config.getOrganization("peerOrg1"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testInstance4() {
		
		FabricConfiguration config = DefaultFabricConfiguration.INSTANCE.getPropertiesConfiguration();
		
		HFClient client = HFClient.createNewInstance();
		try {
			client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ChannelManager manager = new ChannelManager(config, client, new File("src/test/resources/fabric-kv-store.properties"));
		
		try {
			Channel channel = manager.initialize("mychannel", config.getOrganization("peerOrg1"));
			
			Peer peer = channel.getPeers().iterator().next();
			channel.queryInstantiatedChaincodes(peer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
