package io.github.hooj0.fabric.sdk.commons.core.support;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;

import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import com.google.common.base.Strings;

import io.github.hooj0.fabric.sdk.commons.AbstractObject;
import io.github.hooj0.fabric.sdk.commons.FabricChaincodeOperationException;
import io.github.hooj0.fabric.sdk.commons.config.DefaultFabricConfiguration;
import io.github.hooj0.fabric.sdk.commons.config.FabricConfiguration;
import io.github.hooj0.fabric.sdk.commons.core.ChaincodeBasicOperations;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.InstallOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.InstantiateOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.InvokeOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.Options;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.QueryOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.TransactionsOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.UpgradeOptions;
import io.github.hooj0.fabric.sdk.commons.core.manager.ChannelManager;
import io.github.hooj0.fabric.sdk.commons.core.manager.UserManager;
import io.github.hooj0.fabric.sdk.commons.domain.Organization;
import io.github.hooj0.fabric.sdk.commons.store.FabricKeyValueStore;
import io.github.hooj0.fabric.sdk.commons.store.support.FileSystemKeyValueStore;

/**
 * common basic operation support
 * @changelog Add `File & FabricKeyValueStore` constructor method support & add common initialize method
 * @author hoojo
 * @createDate 2018年7月25日 下午6:28:10
 * @file AbstractOperationSupport.java
 * @package io.github.hooj0.fabric.sdk.commons.core.support
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class AbstractOperationSupport extends AbstractObject implements ChaincodeBasicOperations {

	protected HFClient client;
	protected UserManager userManager;
	protected ChannelManager channelManager;
	protected FabricConfiguration config;
	
	protected Channel channel;
	protected String channelName;
	protected String orgName;
	
	public AbstractOperationSupport(String channelName, String orgName, Class<?> clazz) {
		this(channelName, orgName, DefaultFabricConfiguration.INSTANCE.getDefaultConfiguration(), clazz);
	}
	
	public AbstractOperationSupport(String channelName, String orgName, FabricConfiguration config, Class<?> clazz) {
		super(clazz);
		this.initialize(channelName, orgName, config, null);
	}
	
	public AbstractOperationSupport(String channelName, String orgName, FabricConfiguration config, File storeFile, Class<?> clazz) {
		this(channelName, orgName, config, new FileSystemKeyValueStore(storeFile), clazz);
	}
	
	public AbstractOperationSupport(String channelName, String orgName, FabricConfiguration config, FabricKeyValueStore store, Class<?> clazz) {
		super(clazz);
		this.initialize(channelName, orgName, config, store);
	}
	
	private void initialize(String channelName, String orgName, FabricConfiguration config, FabricKeyValueStore store) {
		checkNotNull(config, "FabricConfiguration is not null!");
		
		this.client = HFClient.createNewInstance();
		try {
			client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		this.config = config;
		if (store != null) {
			this.userManager = new UserManager(config, store);
			this.channelManager = new ChannelManager(config, client, store);
		} else {
			this.userManager = new UserManager(config);
			this.channelManager = new ChannelManager(config, client);
		}
		
		this.init(config.getUsers());
		this.channel = init(channelName, orgName);
		
		this.orgName = orgName;
		this.channelName = channelName;
	}
	
	@Override
	public void init(String... users) {
		checkNotNull(users, "FabricConfiguration is not null!");
		checkArgument(users.length > 0, "FabricConfiguration.getUsers() result is not null!");
		
		try {
			userManager.initialize(config.getCaAdminName(), config.getCaAdminPassword(), users);
			logger.debug("Successfully initialize admin: {} & users: {}", config.getCaAdminName(), users);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new FabricChaincodeOperationException("Instantiate administrator, user, authentication, registration failed.");
		}
	}
	
	@Override
	public Channel init(String channelName, String orgName) {
		checkArgument(!Strings.isNullOrEmpty(channelName), "channelName is not null!");
		checkArgument(!Strings.isNullOrEmpty(orgName), "orgName is not null!");
		
		Organization org = config.getOrganization(orgName);
		if (org == null) {
			throw new FabricChaincodeOperationException("Organization that did not find the name '%s'", orgName);
		}
		
		try {
			Channel channel = channelManager.initialize(channelName, org);
			logger.debug("Successfully initialize channel: {}", channel.getName());
			
			return channel;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new FabricChaincodeOperationException("Instantiate preparing the channel '%s' failed.", channelName);
		}
	}
	
	public void afterOptionSet(Options options) {
		checkNotNull(options, "options params not null!");
		
		if (options.getProposalWaitTime() <= 0) {
			options.setProposalWaitTime(config.getProposalWaitTime());
		}
		
		if (options instanceof TransactionsOptions) {
			TransactionsOptions transactionsOptions = (TransactionsOptions) options;
			
			if (transactionsOptions.getTransactionWaitTime() <= 0) {
				transactionsOptions.setTransactionWaitTime(config.getTransactionWaitTime());
			}
		}
		
		if (options instanceof QueryOptions) {
			QueryOptions queryOptions = (QueryOptions) options;
			if (queryOptions.getClientUserContext() == null) {
				queryOptions.setClientUserContext(getOrganization().getUser(config.getUsers()[0]));
			}
		}
		if (options instanceof InvokeOptions) {
			InvokeOptions invokeOptions = (InvokeOptions) options;
			if (invokeOptions.getClientUserContext() == null) {
				invokeOptions.setClientUserContext(getOrganization().getUser(config.getUsers()[0]));
			}
		}
		
		if (options instanceof InstantiateOptions) {
			InstantiateOptions instantiateOptions = (InstantiateOptions) options;
			if (instantiateOptions.getClientUserContext() == null) {
				instantiateOptions.setClientUserContext(getOrganization().getPeerAdmin());
			}
		}
		if (options instanceof InstallOptions) {
			InstallOptions installOptions = (InstallOptions) options;
			if (installOptions.getClientUserContext() == null) {
				installOptions.setClientUserContext(getOrganization().getPeerAdmin());
			}
		}
		if (options instanceof UpgradeOptions) {
			UpgradeOptions upgradeOptions = (UpgradeOptions) options;
			if (upgradeOptions.getClientUserContext() == null) {
				upgradeOptions.setClientUserContext(getOrganization().getPeerAdmin());
			}
		}
	}

	public HFClient getClient() {
		return client;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public ChannelManager getChannelManager() {
		return channelManager;
	}

	public FabricConfiguration getConfig() {
		return config;
	}

	public Channel getChannel() {
		return channel;
	}

	public String getChannelName() {
		return channelName;
	}

	public String getOrgName() {
		return orgName;
	}
	
	/**
	 * 获取当前的组织信息
	 * @author hoojo
	 * @createDate 2018年7月29日 上午11:25:24
	 * @return Organization
	 */
	public Organization getOrganization() {
		return this.config.getOrganization(this.orgName);
	}
}
