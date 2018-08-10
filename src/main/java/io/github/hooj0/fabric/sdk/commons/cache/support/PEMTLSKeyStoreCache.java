package io.github.hooj0.fabric.sdk.commons.cache.support;

import java.io.File;

import io.github.hooj0.fabric.sdk.commons.cache.CacheKeyPrefix;
import io.github.hooj0.fabric.sdk.commons.store.FabricKeyValueStore;

/**
 * PEM TLS key store cache support, 保存客户端 证书 PEM tls key
 * @author hoojo
 * @createDate 2018年7月22日 下午2:15:13
 * @file PEMTLSKeyStoreCache.java
 * @package io.github.hooj0.fabric.sdk.commons.cache.support
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class PEMTLSKeyStoreCache extends AbstractStringStoreCache {

	public PEMTLSKeyStoreCache() {
		super(CacheKeyPrefix.PEM_TLS_KEY_PREFIX, PEMTLSKeyStoreCache.class);
	}
	
	public PEMTLSKeyStoreCache(File keyValueStoreFile) {
		super(CacheKeyPrefix.PEM_TLS_KEY_PREFIX, keyValueStoreFile, PEMTLSKeyStoreCache.class);
	}
	
	public PEMTLSKeyStoreCache(FabricKeyValueStore keyValueStore) {
		super(CacheKeyPrefix.PEM_TLS_KEY_PREFIX, keyValueStore, PEMTLSKeyStoreCache.class);
	}
}
