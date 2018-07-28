package io.github.hooj0.fabric.sdk.commons.config.support;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import io.github.hooj0.fabric.sdk.commons.FabricConfigurationException;
import io.github.hooj0.fabric.sdk.commons.store.FabricKeyValueStore;
import io.github.hooj0.fabric.sdk.commons.store.support.MemoryKeyValueStore;

/**
 * Class System configuration support
 * @changelog Add default fabric key value store const variable, generator properties file method support
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

	private static final FabricKeyValueStore KEY_VALUE_STORE = new MemoryKeyValueStore();
	
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
		return KEY_VALUE_STORE;
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
	
	/**
	 * 将Class配置中的数据，生成 Properties 配置文件 
	 * @author hoojo
	 * @createDate 2018年7月28日 下午6:08:53
	 * @param file
	 */
	public void generatorPropertiesFile(File file) {
		try {
			logger.debug("generator properties config file: {}", file.getAbsolutePath());
			OutputStream out = new FileOutputStream(file);
			
			SDK_COMMONS_PROPERTIES.store(out, "class file fabric configuration support");
			
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new FabricConfigurationException(e, "generator properties config file '%s' exception：", file.getAbsolutePath());
		}
	}
}
