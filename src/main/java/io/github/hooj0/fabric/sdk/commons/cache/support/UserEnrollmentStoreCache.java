package io.github.hooj0.fabric.sdk.commons.cache.support;

import java.io.File;

import io.github.hooj0.fabric.sdk.commons.cache.CacheKeyPrefix;
import io.github.hooj0.fabric.sdk.commons.core.creator.OrganizationUserCreatorImpl.UserEnrollment;
import io.github.hooj0.fabric.sdk.commons.store.FabricKeyValueStore;

/**
 * user ca enroll ment store cache support
 * @author hoojo
 * @createDate 2018年8月10日 上午10:11:29
 * @file UserEnrollmentStoreCache.java
 * @package io.github.hooj0.fabric.sdk.commons.cache.support
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class UserEnrollmentStoreCache extends AbstractJavaObjectStoreCache<UserEnrollment> {

	public UserEnrollmentStoreCache() {
		super(CacheKeyPrefix.ENROLLEMENT_USER_PREFIX, OrganizationUserStoreCache.class);
	}
	
	public UserEnrollmentStoreCache(File keyValueStoreFile) {
		super(CacheKeyPrefix.ENROLLEMENT_USER_PREFIX, keyValueStoreFile, OrganizationUserStoreCache.class);
	}
	
	public UserEnrollmentStoreCache(FabricKeyValueStore keyValueStore) {
		super(CacheKeyPrefix.ENROLLEMENT_USER_PREFIX, keyValueStore, OrganizationUserStoreCache.class);
	}
}
