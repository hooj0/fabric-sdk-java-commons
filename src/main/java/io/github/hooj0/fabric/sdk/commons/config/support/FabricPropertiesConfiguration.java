package io.github.hooj0.fabric.sdk.commons.config.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.lang3.StringUtils;

import io.github.hooj0.fabric.sdk.commons.FabricConfigurationException;
import io.github.hooj0.fabric.sdk.commons.store.FabricKeyValueStore;
import io.github.hooj0.fabric.sdk.commons.store.support.FileSystemKeyValueStore;

/**
 * Properties FileSystem configuration
 * @changelog Add properties filesystem configuration generator file
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
	
	private static final File defaultStoreFile = new File(DEFAULT_KEY_VALUE_STORE_FILE_NAME);
	
	private FabricPropertiesConfiguration() {
		super(FabricPropertiesConfiguration.class);
		
		InputStream stream = null;
		try {
			// 读取 sdk 配置文件名称，没有就读取默认配置 DEFAULT_CONFIG
			File config = new File(getConfigPath()).getAbsoluteFile();
			logger.info("FileSystem加载SDK配置文件： {}， 配置文件存在: {}", config.getAbsolutePath(), config.exists());
			
			if (!config.exists()) {
				stream = FabricPropertiesConfiguration.class.getResourceAsStream("/" + config.getName());
				logger.info("ClassPath加载SDK配置文件： {}， 配置文件存在: {}", config.getName(), stream != null);
			} else {
				stream = new FileInputStream(config);
			}
			
			SDK_COMMONS_PROPERTIES.load(stream);
		} catch (Exception e) {
			logger.warn("使用SDK默认配置失败，没有找到SDK配置文件: {} ", DEFAULT_SDK_CONFIG_NAME);
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
		return new FileSystemKeyValueStore(defaultStoreFile);
	}
	
	private String getConfigPath() {
		return StringUtils.defaultIfBlank(System.getenv(ENV_FABRIC_SDK_CONFIG), DEFAULT_SDK_CONFIG_NAME);
	}
	
	/**
	 * 将Class配置中的数据，生成 Properties 配置文件 
	 * @author hoojo
	 * @createDate 2018年7月28日 下午6:08:53
	 * @param file
	 */
	public void generatorPropertiesFile() {
		String path = getConfigPath();
		try {
			logger.debug("generator properties config file: {}", path);
			
			OutputStream out = new FileOutputStream(new File(path));
			
			SDK_COMMONS_PROPERTIES.store(out, "properties file fabric configuration support");
			
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new FabricConfigurationException(e, "generator properties config file '%s' exception：", path);
		}
	}
}
