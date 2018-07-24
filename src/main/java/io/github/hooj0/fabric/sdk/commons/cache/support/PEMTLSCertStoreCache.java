package io.github.hooj0.fabric.sdk.commons.cache.support;

import java.io.File;

import io.github.hooj0.fabric.sdk.commons.cache.CacheKeyPrefix;
import io.github.hooj0.fabric.sdk.commons.store.FabricKeyValueStore;

/**
 * PEM TLS Cert sotre cache support, 保存客户端证书   PEM tls cert 
 * @author hoojo
 * @createDate 2018年7月22日 下午2:08:03
 * @file PemTLSCertStoreCache.java
 * @package io.github.hooj0.fabric.sdk.commons.cache.support
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class PEMTLSCertStoreCache extends SimpleStoreCache {

	public PEMTLSCertStoreCache() {
		super(CacheKeyPrefix.PEM_TLS_CERT_PREFIX);
	}
	
	public PEMTLSCertStoreCache(File keyValueStoreFile) {
		super(CacheKeyPrefix.PEM_TLS_CERT_PREFIX, keyValueStoreFile);
	}
	
	public PEMTLSCertStoreCache(FabricKeyValueStore keyValueStore) {
		super(CacheKeyPrefix.PEM_TLS_CERT_PREFIX, keyValueStore);
	}
}
