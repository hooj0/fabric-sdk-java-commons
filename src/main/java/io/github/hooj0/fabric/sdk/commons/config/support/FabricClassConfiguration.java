package io.github.hooj0.fabric.sdk.commons.config.support;

import io.github.hooj0.fabric.sdk.commons.store.FabricKeyValueStore;
import io.github.hooj0.fabric.sdk.commons.store.support.MemoryKeyValueStore;

/**
 * Class System configuration support
 * @changelog Add singleton instance support, provide default key value store and property setter method
 * @author hoojo
 * @createDate 2018年7月23日 上午9:23:25
 * @file ClassFabricConfiguration.java
 * @package io.github.hooj0.fabric.sdk.commons.config.support
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public final class FabricClassConfiguration extends AbstractConfigurationSupport {

	private final static class SingletonHolder {
		public static final FabricClassConfiguration instance = new FabricClassConfiguration();
	}
	
	public static FabricClassConfiguration getInstance() {
		return SingletonHolder.instance;
	}
	
	private FabricClassConfiguration() {
		super(FabricClassConfiguration.class);
		
		defaultValueSettings();
		instantiateConfiguration();
	}

	@Override
	public FabricKeyValueStore getDefaultKeyValueStore() {
		return new MemoryKeyValueStore();
	}
	
	public FabricClassConfiguration settingPropertyValue(String key, String value) {
		SDK_COMMONS_PROPERTIES.setProperty(key, value);
		return this;
	}
	
	public FabricClassConfiguration setNetworkDomain(String domain) {
		settingPropertyValue(NETWORK_DOMAIN, domain);
		
		return this;
	}
	
	public FabricClassConfiguration setMspId(String orgName, String mspId) {
		settingPropertyValue(NETWORK_KEY_PREFIX + orgName +  ".mspid", mspId); // "Org2MSP"
		
		return this;
	}
	
	public FabricClassConfiguration setCaName(String orgName, String caName) {
		settingPropertyValue(NETWORK_KEY_PREFIX + orgName +  ".caName", caName); // "ca1"
		
		return this;
	}
	
	public FabricClassConfiguration setCaLocation(String orgName, String location) {
		// "http://" + FABRIC_NETWORK_HOST + ":8054"
		settingPropertyValue(NETWORK_KEY_PREFIX + orgName +  ".ca_location", location);
		
		return this;
	}
	
	public FabricClassConfiguration setOrdererLocation(String orgName, String location) {
		// "orderer.example.com@grpc://" + FABRIC_NETWORK_HOST + ":7050"
		settingPropertyValue(NETWORK_KEY_PREFIX + orgName +  ".orderer_locations", location);
		
		return this;
	}
	
	public FabricClassConfiguration setPeerLocation(String orgName, String location) {
		// "peer0.org1.example.com@grpc://" + FABRIC_NETWORK_HOST + ":7051, peer1.org1.example.com@grpc://" + FABRIC_NETWORK_HOST + ":7056"
		settingPropertyValue(NETWORK_KEY_PREFIX + orgName +  ".peer_locations", location);
		
		return this;
	}

	public FabricClassConfiguration setEventHubLocation(String orgName, String location) {
		// "peer0.org1.example.com@grpc://" + FABRIC_NETWORK_HOST + ":7053, peer1.org1.example.com@grpc://" + FABRIC_NETWORK_HOST + ":7058"
		settingPropertyValue(NETWORK_KEY_PREFIX + orgName +  ".eventhub_locations", location);
		
		return this;
	}
	
	public FabricClassConfiguration setOrgDomain(String orgName, String domain) {
		settingPropertyValue(NETWORK_KEY_PREFIX + orgName +  ".domname", domain); // "org2.example.com"
		
		return this;
	}
}
