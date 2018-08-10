package io.github.hooj0.fabric.sdk.commons.cache.support;

import java.io.File;

import io.github.hooj0.fabric.sdk.commons.cache.CacheKeyPrefix;
import io.github.hooj0.fabric.sdk.commons.domain.OrganizationUser;
import io.github.hooj0.fabric.sdk.commons.store.FabricKeyValueStore;

/**
 * Organization User store cache support
 * @author hoojo
 * @createDate 2018年7月22日 下午2:34:20
 * @file OrganizationUserStoreCache.java
 * @package io.github.hooj0.fabric.sdk.commons.cache.support
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class OrganizationUserStoreCache extends AbstractJavaObjectStoreCache<OrganizationUser> {
	
	public OrganizationUserStoreCache() {
		super(CacheKeyPrefix.ORGANIZATION_USER_PREFIX, OrganizationUserStoreCache.class);
	}
	
	public OrganizationUserStoreCache(File keyValueStoreFile) {
		super(CacheKeyPrefix.ORGANIZATION_USER_PREFIX, keyValueStoreFile, OrganizationUserStoreCache.class);
	}
	
	public OrganizationUserStoreCache(FabricKeyValueStore keyValueStore) {
		super(CacheKeyPrefix.ORGANIZATION_USER_PREFIX, keyValueStore, OrganizationUserStoreCache.class);
	}
}
