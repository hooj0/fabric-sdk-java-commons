package io.github.hooj0.fabric.sdk.commons.core.support;

import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;

import io.github.hooj0.fabric.sdk.commons.AbstractObject;
import io.github.hooj0.fabric.sdk.commons.cache.FabricStoreCache;
import io.github.hooj0.fabric.sdk.commons.cache.FabricStoreCacheFactory;
import io.github.hooj0.fabric.sdk.commons.domain.OrganizationUser;

/**
 * <b>function:</b>
 * @author hoojo
 * @createDate 2018年7月23日 下午6:29:43
 * @file AbstractManager.java
 * @package io.github.hooj0.fabric.sdk.commons.core.support
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public abstract class AbstractManager extends AbstractObject {

	protected FabricStoreCache<OrganizationUser> userStoreCache;
	protected FabricStoreCache<Channel> channelStoreCache;
	protected FabricStoreCache<String> certStoreCache;
	protected FabricStoreCache<String> keyStoreCache;
	
	AbstractManager(HFClient client, Class<?> clazz) {
		super(clazz);
		
		this.channelStoreCache = FabricStoreCacheFactory.createChannelStoreCache(client);
		this.userStoreCache = FabricStoreCacheFactory.createOrganizationUserStoreCache();
		this.certStoreCache = FabricStoreCacheFactory.createPEMTLSCertStoreCache();
		this.keyStoreCache = FabricStoreCacheFactory.createPEMTLSKeyStoreCache();
	}
	
	AbstractManager(Class<?> clazz) {
		super(clazz);
		
		this.userStoreCache = FabricStoreCacheFactory.createOrganizationUserStoreCache();
		this.certStoreCache = FabricStoreCacheFactory.createPEMTLSCertStoreCache();
		this.keyStoreCache = FabricStoreCacheFactory.createPEMTLSKeyStoreCache();
	}
}
