package io.github.hooj0.fabric.sdk.commons.cache.support;

import java.io.File;

import io.github.hooj0.fabric.sdk.commons.cache.AbstractStoreCache;
import io.github.hooj0.fabric.sdk.commons.cache.CacheKeyPrefix;
import io.github.hooj0.fabric.sdk.commons.store.FabricKeyValueStore;

/**
 * based string store cache support
 * @author hoojo
 * @createDate 2018年7月22日 下午2:49:05
 * @file AbstractStringStoreCache.java
 * @package io.github.hooj0.fabric.sdk.commons.cache.support
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public abstract class AbstractStringStoreCache extends AbstractStoreCache<String> {

	public AbstractStringStoreCache(CacheKeyPrefix keyPrefix, Class<?> clazz) {
		super(keyPrefix, clazz);
	}
	
	public AbstractStringStoreCache(CacheKeyPrefix keyPrefix, File keyValueStoreFile, Class<?> clazz) {
		super(keyPrefix, keyValueStoreFile, clazz);
	}
	
	public AbstractStringStoreCache(CacheKeyPrefix keyPrefix, FabricKeyValueStore keyValueStore, Class<?> clazz) {
		super(keyPrefix, keyValueStore, clazz);
	}

	@Override
	public String serialize(String store) {
		return store;
	}

	@Override
	public String deserialize(String value) {
		return value;
	}
}
