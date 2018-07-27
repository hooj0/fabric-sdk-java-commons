package io.github.hooj0.fabric.sdk.commons.config.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;

import io.github.hooj0.fabric.sdk.commons.store.FabricKeyValueStore;
import io.github.hooj0.fabric.sdk.commons.store.support.FileSystemKeyValueStore;

/**
 * Properties FileSystem configuration
 * @changelog Add properties filesystem configuration singleton holder
 * @author hoojo
 * @createDate 2018年7月23日 上午9:24:19
 * @file FabricPropertiesConfiguration.java
 * @package io.github.hooj0.fabric.sdk.commons.config.support
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public final class FabricPropertiesConfiguration extends AbstractConfigurationSupport {

	private final static class SingletonHolder {
		public static final FabricPropertiesConfiguration instance = new FabricPropertiesConfiguration();
	}
	
	public static FabricPropertiesConfiguration getInstance() {
		return SingletonHolder.instance;
	}
	
	private FabricPropertiesConfiguration() {
		super(FabricPropertiesConfiguration.class);
		
		InputStream stream = null;
		try {
			// 读取 sdk 配置文件名称，没有就读取默认配置 DEFAULT_CONFIG
			String configPath = StringUtils.defaultIfBlank(System.getenv(ENV_FABRIC_SDK_CONFIG), DEFAULT_SDK_CONFIG_NAME);
			File configFile = new File(configPath).getAbsoluteFile();
			logger.info("FileSystem加载SDK配置文件： {}， 配置文件存在: {}", configFile.toString(), configFile.exists());
			
			if (!configFile.exists()) {
				stream = FabricPropertiesConfiguration.class.getResourceAsStream("/" + configFile.getName());
				logger.info("ClassPath加载SDK配置文件： {}， 配置文件存在: {}", configFile.getName(), stream != null);
			} else {
				stream = new FileInputStream(configFile);
			}
			
			SDK_COMMONS_PROPERTIES.load(stream);
		} catch (Exception e) {
			logger.warn("加载SDK配置文件: {} 失败. 使用SDK默认配置", DEFAULT_SDK_CONFIG_NAME);
		} finally {

			defaultValueSettings();
			instantiateConfiguration();

			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}
	
	@Override
	public FabricKeyValueStore getDefaultKeyValueStore() {
		return new FileSystemKeyValueStore(new File(this.getProperty(DEFAULT_KEY_VALUE_STORE_FILE, DEFAULT_KEY_VALUE_STORE_FILE_NAME)));
	}
}
