package io.github.hooj0.fabric.sdk.commons.cache.support;

import java.io.File;

import org.hyperledger.fabric.sdk.identity.X509Enrollment;

import io.github.hooj0.fabric.sdk.commons.cache.CacheKeyPrefix;
import io.github.hooj0.fabric.sdk.commons.store.FabricKeyValueStore;

/**
 * hf ca enroll ment store cache support
 * @author hoojo
 * @createDate 2018年8月10日 上午10:11:29
 * @file HFCAEnrollmentStoreCache.java
 * @package io.github.hooj0.fabric.sdk.commons.cache.support
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class HFCAEnrollmentStoreCache extends AbstractJavaObjectStoreCache<X509Enrollment> {

	public HFCAEnrollmentStoreCache() {
		super(CacheKeyPrefix.ENROLLEMENT_USER_PREFIX, HFCAEnrollmentStoreCache.class);
	}
	
	public HFCAEnrollmentStoreCache(File keyValueStoreFile) {
		super(CacheKeyPrefix.ENROLLEMENT_USER_PREFIX, keyValueStoreFile, HFCAEnrollmentStoreCache.class);
	}
	
	public HFCAEnrollmentStoreCache(FabricKeyValueStore keyValueStore) {
		super(CacheKeyPrefix.ENROLLEMENT_USER_PREFIX, keyValueStore, HFCAEnrollmentStoreCache.class);
	}
}
