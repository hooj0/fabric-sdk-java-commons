package io.github.hooj0.fabric.sdk.commons.cache.support;

import java.io.File;

import io.github.hooj0.fabric.sdk.commons.cache.AbstractStoreCache;
import io.github.hooj0.fabric.sdk.commons.cache.CacheKeyPrefix;
import io.github.hooj0.fabric.sdk.commons.store.FabricKeyValueStore;

/**
 * simple store cache support
 * @author hoojo
 * @createDate 2018年7月22日 下午2:49:05
 * @file SimpleStoreCache.java
 * @package io.github.hooj0.fabric.sdk.commons.cache.support
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class SimpleStoreCache extends AbstractStoreCache<String> {

	public SimpleStoreCache(CacheKeyPrefix keyPrefix) {
		super(keyPrefix);
	}
	
	public SimpleStoreCache(CacheKeyPrefix keyPrefix, File keyValueStoreFile) {
		super(keyPrefix, keyValueStoreFile);
	}
	
	public SimpleStoreCache(CacheKeyPrefix keyPrefix, FabricKeyValueStore keyValueStore) {
		super(keyPrefix, keyValueStore);
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
