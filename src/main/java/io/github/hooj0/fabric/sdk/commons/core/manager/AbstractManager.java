package io.github.hooj0.fabric.sdk.commons.core.manager;

import java.io.File;

import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;

import io.github.hooj0.fabric.sdk.commons.AbstractObject;
import io.github.hooj0.fabric.sdk.commons.cache.FabricStoreCache;
import io.github.hooj0.fabric.sdk.commons.cache.support.ChannelStoreCache;
import io.github.hooj0.fabric.sdk.commons.cache.support.OrganizationUserStoreCache;
import io.github.hooj0.fabric.sdk.commons.cache.support.PEMTLSCertStoreCache;
import io.github.hooj0.fabric.sdk.commons.cache.support.PEMTLSKeyStoreCache;
import io.github.hooj0.fabric.sdk.commons.config.FabricConfiguration;
import io.github.hooj0.fabric.sdk.commons.domain.OrganizationUser;
import io.github.hooj0.fabric.sdk.commons.store.FabricKeyValueStore;
import io.github.hooj0.fabric.sdk.commons.store.support.FileSystemKeyValueStore;

/**
 * basic manager support
 * @changelog Add key value store & file constructor support
 * @author hoojo
 * @createDate 2018年7月23日 下午6:29:43
 * @file AbstractManager.java
 * @package io.github.hooj0.fabric.sdk.commons.core.manager
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
	
	protected FabricConfiguration config;
	protected HFClient client;
	
	AbstractManager(FabricConfiguration config, HFClient client, Class<?> clazz) {
		this(config, config.getDefaultKeyValueStore(), client, clazz);
	}
	
	AbstractManager(FabricConfiguration config, File keyValueStoreFile, HFClient client, Class<?> clazz) {
		this(config, new FileSystemKeyValueStore(keyValueStoreFile), client, clazz);
	}
	
	AbstractManager(FabricConfiguration config, FabricKeyValueStore keyValueStore, HFClient client, Class<?> clazz) {
		super(clazz);
		
		logger.info("fabric key value store support is '{}'", keyValueStore.getClass());
		
		this.client = client;
		this.config = config;
		this.channelStoreCache = new ChannelStoreCache(keyValueStore, client);
	}
	
	AbstractManager(FabricConfiguration config, Class<?> clazz) {
		this(config, config.getDefaultKeyValueStore(), clazz);
	}
	
	AbstractManager(FabricConfiguration config, File keyValueStoreFile, Class<?> clazz) {
		this(config, new FileSystemKeyValueStore(keyValueStoreFile), clazz);
	}
	
	AbstractManager(FabricConfiguration config, FabricKeyValueStore keyValueStore, Class<?> clazz) {
		super(clazz);
		logger.info("fabric key value store support is '{}'", keyValueStore.getClass());
		
		this.config = config;
		
		this.userStoreCache = new OrganizationUserStoreCache(keyValueStore);
		this.certStoreCache = new PEMTLSCertStoreCache(keyValueStore);
		this.keyStoreCache = new PEMTLSKeyStoreCache(keyValueStore);
	}
}
