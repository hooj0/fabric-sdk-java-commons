package io.github.hooj0.fabric.sdk.commons.cache;

import java.io.File;
import java.util.Map;

import com.google.common.collect.Maps;

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
public abstract class AbstractStoreCache<T> implements FabricStoreCache<T>, Serialization<T> {

	private Map<String, T> caches = Maps.newConcurrentMap();
	private FabricKeyValueStore keyValueStore;
	private CacheKeyPrefix keyPrefix;
	
	public AbstractStoreCache(CacheKeyPrefix keyPrefix) {
		this(keyPrefix, newDefaultKeyValueStore());
	}

	public AbstractStoreCache(CacheKeyPrefix keyPrefix, File keyValueStoreFile) {
		this(keyPrefix, new FileSystemKeyValueStore(keyValueStoreFile));
	}
	
	public AbstractStoreCache(CacheKeyPrefix keyPrefix, FabricKeyValueStore keyValueStore) {
		this.keyValueStore = keyValueStore;
		this.keyPrefix = keyPrefix;
	}
	
	public static FabricKeyValueStore newDefaultKeyValueStore() {
		FabricKeyValueStore store = new FileSystemKeyValueStore(new File(FabricConfigurationPropertyKey.DEFAULT_KEY_VALUE_STORE_FILE_NAME));
		
		return store;
	}
	
	@Override
	public void setStore(String[] storeKey, T store) {
		String key = keyPrefix.getKeyPrefix(storeKey);
		caches.put(key, store);

		keyValueStore.set(key, this.serialize(store));
	}
	
	@Override
	public void setStore(String storeKey, T store) {
		String key = keyPrefix.getKeyPrefix(storeKey);
		caches.put(key, store);

		keyValueStore.set(key, this.serialize(store));
	}

	@Override
	public T getStore(String... storeKey) {
		String key = keyPrefix.getKeyPrefix(storeKey);
		
		if (caches.containsKey(key)) {
			return caches.get(key);
		}
		
		String value = keyValueStore.get(key);
		return this.deserialize(value);
	}

	@Override
	public boolean hasStore(String... storeKey) {
		return keyValueStore.contains(keyPrefix.getKeyPrefix(storeKey));
	}
	
	@Override
	public boolean removeStore(String... storeKey) {
		String key = keyPrefix.getKeyPrefix(storeKey);
		
		if (keyValueStore.remove(key)) {
			caches.remove(key);
			return true;
		}
		
		return false;
	}
}
