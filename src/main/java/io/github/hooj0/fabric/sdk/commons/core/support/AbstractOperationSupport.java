package io.github.hooj0.fabric.sdk.commons.core.support;

import static com.google.common.base.Preconditions.checkNotNull;

import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import io.github.hooj0.fabric.sdk.commons.AbstractObject;
import io.github.hooj0.fabric.sdk.commons.FabricChaincodeOperationException;
import io.github.hooj0.fabric.sdk.commons.config.DefaultFabricConfiguration;
import io.github.hooj0.fabric.sdk.commons.config.FabricConfiguration;
import io.github.hooj0.fabric.sdk.commons.core.ChaincodeBasicOperations;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.Options;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.TransactionsOptions;
import io.github.hooj0.fabric.sdk.commons.core.manager.ChannelManager;
import io.github.hooj0.fabric.sdk.commons.core.manager.UserManager;
import io.github.hooj0.fabric.sdk.commons.domain.Organization;

/**
 * common basic operation support
 * @changelog Add empty `constructor & init method` support & afterOptionSet method support
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

	protected final HFClient client;
	protected final UserManager userManager;
	protected final ChannelManager channelManager;
	protected final FabricConfiguration config;
	
	protected final Channel channel;
	
	public AbstractOperationSupport(String channelName, String orgName, Class<?> clazz) {
		this(channelName, orgName, DefaultFabricConfiguration.INSTANCE, clazz);
	}
	
	public AbstractOperationSupport(String channelName, String orgName, FabricConfiguration config, Class<?> clazz) {
		super(clazz);
		
		this.client = HFClient.createNewInstance();
		try {
			client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		this.config = config;
		this.userManager = new UserManager(config);
		this.channelManager = new ChannelManager(config, client);
		
		this.init();
		
		this.channel = init(channelName, orgName);
	}
	
	private void init() {
		try {
			userManager.initialize(config.getAdminInfo().getName(), config.getAdminInfo().getPassword(), config.getUsers());
			logger.debug("Successfully initialize admin: {} & users: {}", config.getAdminInfo().getName(), config.getUsers());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new FabricChaincodeOperationException("Instantiate administrator, user, authentication, registration failed.");
		}
	}
	
	@Override
	public Channel init(String channelName, String orgName) {
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
			throw new FabricChaincodeOperationException("Instantiate preparing the channel '%s' failed.", config.getChannelName());
		}
	}
	
	public void afterOptionSet(Options options) {
		checkNotNull(options, "options params not null!");
		
		options.setClientUserContext(DefaultFabricConfiguration.INSTANCE.getOrganization("").getPeerAdmin());
		
		if (options.getProposalWaitTime() <= 0) {
			options.setProposalWaitTime(config.getProposalWaitTime());
		}
		
		if (options instanceof TransactionsOptions) {
			
			TransactionsOptions transactionsOptions = (TransactionsOptions) options;
			if (transactionsOptions.getTransactionWaitTime() <= 0) {
				transactionsOptions.setTransactionWaitTime(config.getTransactionWaitTime());
			}
		}
	}

	@Override
	public void close() {

	}

	@Override
	public boolean isClosed() {
		return false;
	}

	@Override
	public State getState() {
		return null;
	}
}
