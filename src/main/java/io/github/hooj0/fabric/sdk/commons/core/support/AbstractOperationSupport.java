package io.github.hooj0.fabric.sdk.commons.core;

import java.util.HashMap;
import java.util.Map;

import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import io.github.hooj0.fabric.sdk.commons.AbstractObject;
import io.github.hooj0.fabric.sdk.commons.config.FabricConfiguration;
import io.github.hooj0.fabric.sdk.commons.core.support.ChannelManager;
import io.github.hooj0.fabric.sdk.commons.core.support.UserManager;
import io.github.hooj0.fabric.sdk.commons.domain.Organization;

/**
 * common basic operation support
 * @author hoojo
 * @createDate 2018年7月25日 下午6:28:10
 * @file AbstractOperationSupport.java
 * @package io.github.hooj0.fabric.sdk.commons.core
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
	
	private static final String ADMIN_NAME = "admin";
	private static final String ADMIN_SECRET = "adminpw";
	private static final String USER_NAME = "user1";
	
	private static final String channelName = "mychannel"; // foo bar
	private static final String orgName = "peerOrg1";
	
	public AbstractOperationSupport(FabricConfiguration config) {
		super(AbstractOperationSupport.class);
		
		this.config = config;
		this.client = HFClient.createNewInstance();
		
		try {
			client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		this.userManager = new UserManager(config);
		this.channelManager = new ChannelManager(config, client);
		
		this.init();
	}
	
	@Override
	public ChaincodeTransactionOperations init() {
		
		try {
			userManager.initialize(ADMIN_NAME, ADMIN_SECRET, USER_NAME);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		Organization org = config.getOrganization(orgName);
		
		try {
			channel = channelManager.initialize(channelName, org);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		return null;
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
