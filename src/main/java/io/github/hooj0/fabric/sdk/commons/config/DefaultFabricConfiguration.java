package io.github.hooj0.fabric.sdk.commons.config;

import io.github.hooj0.fabric.sdk.commons.config.support.FabricClassConfiguration;
import io.github.hooj0.fabric.sdk.commons.config.support.FabricPropertiesConfiguration;

/**
 * default fabric configuration support
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
		return this.getClassConfiguration();
		//return this.getPropertiesConfiguration();
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
	
	private void setLocalSettings() {
		FabricClassConfiguration config = this.getClassConfiguration();
		
		config.setCaLocation("aaaaaaa", "127.0.0.1");
		config.setNetworkDomain("hoojo.cnblogs.com");

		config.settingPropertyValue(FabricConfigurationPropertyKey.NETWORK_CA_ADMIN_NAME, "admin");
		config.settingPropertyValue(FabricConfigurationPropertyKey.NETWORK_CA_ADMIN_PASSWD, "adminpw");
		config.settingPropertyValue(FabricConfigurationPropertyKey.NETWORK_ORGS_MEMBER_USERS, "user1");
	}
}
