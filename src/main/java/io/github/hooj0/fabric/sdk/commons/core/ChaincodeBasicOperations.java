package io.github.hooj0.fabric.sdk.commons.core;

import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;

import io.github.hooj0.fabric.sdk.commons.config.FabricConfiguration;
import io.github.hooj0.fabric.sdk.commons.core.manager.ChannelManager;
import io.github.hooj0.fabric.sdk.commons.core.manager.UserManager;
import io.github.hooj0.fabric.sdk.commons.domain.Organization;

/**
 * basic chaincode operations init interface
 * @changelog Add getter `Organization` method defined
 * @author hoojo
 * @createDate 2018年7月25日 下午6:21:55
 * @file ChaincodeBasicOperations.java
 * @package io.github.hooj0.fabric.sdk.commons.core
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface ChaincodeBasicOperations {

	/**
     * 在指定的orgName节点上初始化通道 channelName
	 * @author hoojo
	 * @createDate 2018年7月23日 下午3:10:28
	 * @return Channel initialization channel
	 */
	public Channel init(String channelName, String orgName);
	
	/**
	 * 初始化系统会员用户
	 * @author hoojo
	 * @createDate 2018年7月27日 下午4:40:43
	 * @param users
	 */
	public void init(String... users);
	
	public HFClient getClient();

	public FabricConfiguration getConfig();

	public Channel getChannel();

	public String getChannelName();

	public String getOrgName();
	
	public UserManager getUserManager();

	public ChannelManager getChannelManager();
	
	/**
	 * 获取当前的组织信息
	 * @author hoojo
	 * @createDate 2018年7月29日 上午11:25:24
	 * @return Organization
	 */
	public Organization getOrganization();
}
