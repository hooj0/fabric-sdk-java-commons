package io.github.hooj0.fabric.sdk.commons.store.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.hooj0.fabric.sdk.commons.AbstractObject;
import io.github.hooj0.fabric.sdk.commons.FabricStoreException;
import io.github.hooj0.fabric.sdk.commons.store.FabricKeyValueStore;

/**
 * 文件系统KeyValue持久化存储
 * @author hoojo
 * @createDate 2018年7月21日 下午9:46:14
 * @file FileSystemKeyValueStore.java
 * @package io.github.hooj0.fabric.sdk.commons.store.support
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class FileSystemKeyValueStore extends AbstractObject implements FabricKeyValueStore {

	private final static Logger logger = LoggerFactory.getLogger(FileSystemKeyValueStore.class);
	private File storeFile;
	
	static {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}

	public FileSystemKeyValueStore(File storeFile) {
		super(FileSystemKeyValueStore.class);
		logger.debug("KeyValue store file location：{}", storeFile.getAbsolutePath());
		
		this.storeFile = storeFile;
	}
	
	@Override
	public String get(String key) {
		Properties properties = loadProperties();
		
		return properties.getProperty(key);
	}

	@Override
	public void set(String key, String value) {
		Properties properties = loadProperties();
		
		try (OutputStream output = new FileOutputStream(storeFile)) {
			properties.setProperty(key, value);
			properties.store(output, "");
			output.close();
		} catch (IOException e) {
			logger.warn("Could not set the keyvalue store, reason: {}", e.getMessage());
			throw new FabricStoreException("Could not set the keyvalue store, reason: %s", e.getMessage());
		}
	}

	@Override
	public boolean contains(String key) {
		Properties properties = loadProperties();
		
		return properties.containsKey(key);
	}

	@Override
	public boolean remove(String key) {
		Properties properties = loadProperties();
		
		if (!contains(key)) {
			return true;
		}
		
		try (OutputStream output = new FileOutputStream(storeFile)) {
			
			properties.remove(key);
			properties.store(output, "");
			output.close();
			
			return true;
		} catch (IOException e) {
			logger.warn("Could not remove the keyvalue store, reason: {}", e.getMessage());
			throw new FabricStoreException("Could not remove the keyvalue store, reason: %s", e.getMessage());
		}
	}

	private Properties loadProperties() {
		Properties properties = new Properties();
		
		try (InputStream input = new FileInputStream(storeFile)) {
			
			properties.load(input);
			input.close();
			
		} catch (FileNotFoundException e) {
			logger.warn("Could not find the file {}", storeFile);
			throw new FabricStoreException("Could not find the file {}", storeFile.getAbsoluteFile());
		} catch (IOException e) {
			logger.warn("Could not load keyvalue store from file {}, reason:{}", storeFile, e.getMessage());
			throw new FabricStoreException("Could not load keyvalue store from file %s, reason: %s", storeFile, e.getMessage());
		}

		return properties;
	}
}
