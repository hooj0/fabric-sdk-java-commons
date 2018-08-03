package io.github.hooj0.fabric.sdk.commons.cache;

import java.io.File;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;

import io.github.hooj0.fabric.sdk.commons.AbstractObject;
import io.github.hooj0.fabric.sdk.commons.config.FabricConfigurationPropertyKey;
import io.github.hooj0.fabric.sdk.commons.store.FabricKeyValueStore;
import io.github.hooj0.fabric.sdk.commons.store.support.FileSystemKeyValueStore;

/**
 * baisc store cache, implements cache prefix key wrapper and cache object Serialization
 * @author hoojo
 * @createDate 2018年7月22日 上午11:51:13
 * @file AbstractStoreCache.java
 * @package io.github.hooj0.fabric.sdk.commons.cache
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public abstract class AbstractStoreCache<T> extends AbstractObject implements FabricStoreCache<T>, Serialization<T> {

	private volatile Map<String, T> caches = Maps.newConcurrentMap();
	private FabricKeyValueStore keyValueStore;
	private CacheKeyPrefix keyPrefix;
	
	public AbstractStoreCache(CacheKeyPrefix keyPrefix) {
		this(keyPrefix, newDefaultKeyValueStore());
	}

	public AbstractStoreCache(CacheKeyPrefix keyPrefix, File keyValueStoreFile) {
		this(keyPrefix, new FileSystemKeyValueStore(keyValueStoreFile));
	}
	
	public AbstractStoreCache(CacheKeyPrefix keyPrefix, FabricKeyValueStore keyValueStore) {
		super(AbstractStoreCache.class);
		
		this.keyValueStore = keyValueStore;
		this.keyPrefix = keyPrefix;
		
		logger.debug("FabricKeyValueStore support: {}, keyPrefix: {}", keyValueStore.getClass(), keyPrefix);
	}
	
	public static FabricKeyValueStore newDefaultKeyValueStore() {
		FabricKeyValueStore store = new FileSystemKeyValueStore(new File(FabricConfigurationPropertyKey.DEFAULT_KEY_VALUE_STORE_FILE_NAME));
		
		return store;
	}
	
	@Override
	public synchronized void setStore(String[] storeKey, T store) {
		if (store == null) {
			logger.warn("store is null, do not cache '{}'", new Object[] { storeKey });
			return;
		}
		
		String key = keyPrefix.getKeyPrefix(storeKey);
		logger.trace("store cache: '{}'", new Object[] { storeKey });

		caches.put(key, store);
		keyValueStore.set(key, this.serialize(store));
	}
	
	@Override
	public synchronized void setStore(String storeKey, T store) {
		if (store == null) {
			logger.warn("store is null, do not cache '{}'", new Object[] { storeKey });
			return;
		}
		
		String key = keyPrefix.getKeyPrefix(storeKey);
		logger.trace("store cache: '{}'", new Object[] { storeKey });
		
		caches.put(key, store);
		keyValueStore.set(key, this.serialize(store));
	}

	@Override
	public synchronized T getStore(String... storeKey) {
		String key = keyPrefix.getKeyPrefix(storeKey);
		
		if (caches.containsKey(key)) {
			logger.debug("find store cache: '{}'", new Object[] { storeKey });
			return caches.get(key);
		}
		
		String value = keyValueStore.get(key);
		if (StringUtils.isNotBlank(value)) {
			logger.debug("find properties store cache: '{}'", new Object[] { storeKey });
			
			T store = this.deserialize(value);
			caches.put(key, store);
			return store;
		}
		
		return null;
	}

	@Override
	public synchronized boolean hasStore(String... storeKey) {
		return keyValueStore.contains(keyPrefix.getKeyPrefix(storeKey));
	}
	
	@Override
	public synchronized T getCache(String... storeKey) {
		return caches.get(keyPrefix.getKeyPrefix(storeKey));
	}
	
	@Override
	public synchronized boolean hasCache(String... storeKey) {
		return caches.containsKey(keyPrefix.getKeyPrefix(storeKey));
	}
	
	@Override
	public synchronized boolean removeStore(String... storeKey) {
		String key = keyPrefix.getKeyPrefix(storeKey);
		
		if (keyValueStore.remove(key)) {
			caches.remove(key);
			return true;
		}
		
		return false;
	}
}
