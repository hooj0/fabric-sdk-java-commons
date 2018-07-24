package io.github.hooj0.fabric.sdk.commons.cache;

import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;

import io.github.hooj0.fabric.sdk.commons.cache.support.ChannelStoreCache;
import io.github.hooj0.fabric.sdk.commons.cache.support.OrganizationUserStoreCache;
import io.github.hooj0.fabric.sdk.commons.cache.support.PEMTLSCertStoreCache;
import io.github.hooj0.fabric.sdk.commons.cache.support.PEMTLSKeyStoreCache;
import io.github.hooj0.fabric.sdk.commons.config.DefaultFabricConfiguration;
import io.github.hooj0.fabric.sdk.commons.domain.OrganizationUser;

/**
 * <b>function:</b>
 * @author hoojo
 * @createDate 2018年7月23日 下午6:08:04
 * @file FabricStoreCacheFactory.java
 * @package io.github.hooj0.fabric.sdk.commons.cache
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public abstract class FabricStoreCacheFactory {

	public static FabricStoreCache<Channel> createChannelStoreCache(HFClient client) {
		return new ChannelStoreCache(DefaultFabricConfiguration.INSTANCE.getKeyValueStore(), client);
	}
	
	public static FabricStoreCache<OrganizationUser> createOrganizationUserStoreCache() {
		return new OrganizationUserStoreCache(DefaultFabricConfiguration.INSTANCE.getKeyValueStore());
	}
	
	public static FabricStoreCache<String> createPEMTLSCertStoreCache() {
		return new PEMTLSCertStoreCache(DefaultFabricConfiguration.INSTANCE.getKeyValueStore());
	}
	
	public static FabricStoreCache<String> createPEMTLSKeyStoreCache() {
		return new PEMTLSKeyStoreCache(DefaultFabricConfiguration.INSTANCE.getKeyValueStore());
	}
}
