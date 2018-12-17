package io.github.hooj0.fabric.sdk.commons.core.manager;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.Channel.PeerOptions;
import org.hyperledger.fabric.sdk.ChannelConfiguration;
import org.hyperledger.fabric.sdk.EventHub;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.Peer.PeerRole;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import io.github.hooj0.fabric.sdk.commons.FabricManagerException;
import io.github.hooj0.fabric.sdk.commons.config.FabricConfiguration;
import io.github.hooj0.fabric.sdk.commons.domain.Organization;
import io.github.hooj0.fabric.sdk.commons.store.FabricKeyValueStore;

/**
 * fabric channel manager service support
 * @changelog Add key value store & file constructor super support
 * @author hoojo
 * @createDate 2018年6月22日 下午5:24:46
 * @file ChannelManager.java
 * @package io.github.hooj0.fabric.sdk.commons.core.manager
 * @project fabric-sdk-examples
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class ChannelManager extends AbstractManager {

	public ChannelManager(FabricConfiguration config, HFClient client) {
		super(config, client, ChannelManager.class);
	}
	
	public ChannelManager(FabricConfiguration config, HFClient client, File keyValueStoreFile) {
		super(config, keyValueStoreFile, client, ChannelManager.class);
	}
	
	public ChannelManager(FabricConfiguration config, HFClient client, FabricKeyValueStore keyValueStore) {
		super(config, keyValueStore, client, ChannelManager.class);
	}

	/**
	 * 初始化通道
	 * @author hoojo
	 * @createDate 2018年6月25日 下午12:58:27
	 */
	public Channel initialize(String channelName, Organization org) throws Exception {
		logger.info("initialize channel -> Organization: {} , Constructing channel: {}", org.getName(), channelName);

		checkArgument(!Strings.isNullOrEmpty(channelName), "channel name is not null!");
		checkNotNull(org, "Organization is not null!");
		checkNotNull(org.getPeerAdmin(), "Organization PeerAdmin is not null!");
		
		/** 设置 peer 管理员User上下文 */
		client.setUserContext(org.getPeerAdmin());

		/** 恢复或创建通道 */
		Channel channel = channelStoreCache.getStore(org.getName(), channelName);
		if (channel == null) {
			try {
				/** 创建 Orderer 共识服务 */
				List<Orderer> orderers = createOrderer(org);

				/** 选择第一个 Orderer 创建通道 */
				Orderer anOrderer = orderers.iterator().next();

				/** 剔除已选择 Orderer */
				logger.info("remove choose orderer service");
				orderers.remove(anOrderer);

				/** 创建通道 */
				channel = createChannel(channelName, anOrderer, org);

				/** 创建 peer，channel加入Peer */
				createPeer(channel, true, org);

				/** 为通道添加其他 Orderer服务 */
				logger.info("Add Orderer to Channel: {}", channelName);
				for (Orderer orderer : orderers) {
					channel.addOrderer(orderer);
					logger.trace("Add Channel Orderer: {} -> {}", orderer.getName(), orderer.getUrl());
				}
			} catch (Exception e) {
				logger.warn("准备通道发生异常，尝试重建从缓存中恢复通道：{}", e.getMessage());
				/** 重建通道 */
				channel = recreateChannel(channelName, org);
			}
			
			/** 添加事件总线 */
			createEventHub(channel, org);
		} else {
			logger.debug("used store cache channel '{}_{}'", org.getName(), channelName);
		}

		/** 初始化 */
		if (!channel.isInitialized()) {
			logger.info("initialize channel: {}.{}", org.getName(), channelName);
			channel.initialize();
		}
		
		if (channel.isShutdown() || !channel.isInitialized()) {
			logger.warn("=============> 通道关闭或为初始化，需要重新开启");
			channel = checkChannelSerialize(channel);
		}

		checkChannel(channelName, channel);
		logger.info("Organization: {} , Finished initialization channel： {}", org.getName(), channelName);
		
		channelStoreCache.setStore(new String[] { org.getName(), channelName }, channel);
		
		return channel;
	}

	/**
	 * 在store恢复失败的时候，重新创建通道
	 * @author hoojo
	 * @createDate 2018年6月26日 下午4:50:46
	 */
	public Channel recreateChannel(String channelName, Organization org) throws Exception {
		logger.info("Restore failed in store, recreate channel '{}.{}'", org.getName(), channelName);
		/** 创建通道 */
		Channel channel = createChannel(channelName);

		createOrderer(channel, org);

		/** 创建Orderer */
		createPeer(channel, false, org);
		
		return channel;
	}
	
	/**
	 * 恢复通道，在之前已有通道情况下，进行恢复通道操作
	 * @author hoojo
	 * @createDate 2018年6月25日 下午12:58:42
	 */
	public Channel restoreChannel(String channelName, Organization org) throws Exception {
		logger.info("Restore channel in store '{}.{}'", org.getName(), channelName);
		
		checkArgument(!Strings.isNullOrEmpty(channelName), "channel name is not null!");
		checkNotNull(org, "Organization is not null!");
		checkNotNull(org.getPeerAdmin(), "Organization PeerAdmin is not null!");
		
		client.setUserContext(org.getPeerAdmin());
		// client.setUserContext(org.getUser(USER_NAME));
		
		/** 恢复或创建通道 */
		Channel channel = channelStoreCache.getStore(org.getName(), channelName);
		if (channel == null) {
			/** 创建通道 */
			channel = createChannel(channelName);

			createOrderer(channel, org);

			/** 创建Orderer */
			createPeer(channel, false, org);

			createEventHub(channel, org);
		}

		/** 初始化 */
		if (!channel.isInitialized()) {
			logger.info("initialize channel: {}.{}", org.getName(), channelName);
			channel.initialize();
		}
		
		if (channel.isShutdown() || !channel.isInitialized()) {
			logger.warn("=============> 通道关闭或为初始化，需要重新开启");
			channel = checkChannelSerialize(channel);
		}

		checkChannel(channelName, channel);
		logger.info("Organization: {} , Finished initialization channel： {}", org.getName(), channelName);
		
		channelStoreCache.setStore(new String[] { org.getName(), channelName }, channel);

		return channel;
	}

	/**
	 * 准备通道：创建 Orderer服务、创建 channel、创建 Peer/Peer加入通道、添加EventHub、初始化通道
	 * @author hoojo
	 * @createDate 2018年6月13日 下午4:05:22
	 * @param channelName 通道名称
	 * @param client HFClient
	 * @param org Organization
	 * @return Channel
	 * @throws Exception
	 */
	public Channel prepareChannel(String channelName, Organization org) throws Exception {
		logger.info("PrepareChannel -> Organization: {} , Constructing channel: {}", org.getName(), channelName);

		/** 设置 peer 管理员User上下文 */
		client.setUserContext(org.getPeerAdmin());

		/** 创建 Orderer 共识服务 */
		List<Orderer> orderers = createOrderer(org);

		/** 选择第一个 Orderer 创建通道 */
		Orderer anOrderer = orderers.iterator().next();

		logger.info("remove choose orderer service");
		/** 剔除已选择 Orderer */
		orderers.remove(anOrderer);

		/** 创建通道 */
		Channel channel = createChannel(channelName, anOrderer, org);

		/** 创建 peer，channel加入Peer */
		createPeer(channel, true, org);

		logger.info("Add Orderer to Channel: {}", channelName);
		/** 为通道添加其他 Orderer服务 */
		for (Orderer orderer : orderers) {
			channel.addOrderer(orderer);
			logger.trace("Add Channel Orderer: {}->{}", orderer.getName(), orderer.getUrl());
		}

		/** 添加事件总线 */
		createEventHub(channel, org);

		logger.info("initialize channel: {}", channelName);
		/** 初始化 */
		channel.initialize();
		logger.info("Organization: {} , Finished initialization channel： {}", org.getName(), channelName);

		return checkChannelSerialize(channel);
	}

	/**
	 * 创建Orderer服务
	 * @author hoojo
	 * @createDate 2018年6月13日 下午4:33:20
	 */
	private Orderer createOrderer(String ordererName, Organization org) throws Exception {
		String grpcURL = org.getOrdererLocation(ordererName);
		logger.debug("create Orderer：{}, URL：{}", ordererName, grpcURL);

		Properties ordererProps = config.getOrdererProperties(ordererName);
		// 5分钟以下需要更改服务器端才能接受更快的Ping速率。
		ordererProps.put("grpc.NettyChannelBuilderOption.keepAliveTime", new Object[] { 5L, TimeUnit.MINUTES });
		// 设置keepAlive以避免不活动http2连接超时的示例。
		ordererProps.put("grpc.NettyChannelBuilderOption.keepAliveTimeout", new Object[] { 8L, TimeUnit.SECONDS });
		ordererProps.put("grpc.NettyChannelBuilderOption.keepAliveWithoutCalls", new Object[] { true });

		Orderer orderer = client.newOrderer(ordererName, grpcURL, ordererProps);

		return orderer;
	}

	/**
	 * 创建Orderer服务
	 * @author hoojo
	 * @createDate 2018年6月13日 下午4:33:20
	 */
	private List<Orderer> createOrderer(Organization org) throws Exception {
		logger.info("create Orderer service");
		List<Orderer> orderers = Lists.newLinkedList();

		for (String ordererName : org.getOrdererNames()) {
			orderers.add(createOrderer(ordererName, org));
		}

		return orderers;
	}

	/**
	 * 创建Orderer服务
	 * @author hoojo
	 * @createDate 2018年6月13日 下午4:33:20
	 */
	private void createOrderer(Channel channel, Organization org) throws Exception {
		logger.info("In the channel: `{}` create the Orderer node", channel.getName());

		for (String ordererName : org.getOrdererNames()) {
			logger.debug("in the Channel add Orderer: {}", ordererName);

			channel.addOrderer(createOrderer(ordererName, org));
		}
	}

	/**
	 * 通过channel.tx配置文件，创建通道
	 * @author hoojo
	 * @createDate 2018年6月13日 下午4:29:53
	 * @throws Exception
	 */
	private Channel createChannel(String channelName, Orderer anOrderer, Organization org) throws Exception {
		logger.info("create channel：{}", channelName);

		// 通道配置文件
		File channelFile = new File(config.getChannelArtifactsPath(), channelName + ".tx");
		if (!channelFile.exists()) {
			throw new FabricManagerException("channel tx config file not found: %s", channelFile.getPath());
		}
		
		logger.debug("channel tx config file location：{}", channelFile.getAbsolutePath());
		ChannelConfiguration channelConfiguration = new ChannelConfiguration(channelFile);

		byte[] channelConfigurationSignatures = client.getChannelConfigurationSignature(channelConfiguration, org.getPeerAdmin());
		// 创建只有一个管理员(orgs peer admin)签名的通道。 如果通道创建策略需要更多签名，则需要添加更多管理员签名。
		Channel channel = client.newChannel(channelName, anOrderer, channelConfiguration, channelConfigurationSignatures);
		logger.info("create channel '{}' success", channel.getName());

		return channel;
	}

	private Channel createChannel(String channelName) throws Exception {
		logger.info("create new channel：{}", channelName);
		Channel channel = client.newChannel(channelName);

		return channel;
	}

	/**
	 * 创建peer节点，加入该Peer节点
	 * @author hoojo
	 * @createDate 2018年6月13日 下午4:21:51
	 */
	private void createPeer(Channel channel, boolean joining, Organization org) throws Exception {
		logger.info("created peer add/join channel: {}", channel.getName(), joining);

		for (String peerName : org.getPeerNames()) {
			String grpcURL = org.getPeerLocation(peerName);
			logger.debug("create peer node:{}, URL：{}", peerName, grpcURL);

			Properties peerProps = config.getPeerProperties(peerName);
			peerProps.put("grpc.NettyChannelBuilderOption.maxInboundMessageSize", 9000000);

			// 创建节点
			Peer peer = client.newPeer(peerName, grpcURL, peerProps);

			PeerOptions options = PeerOptions.createPeerOptions();
			if (config.isFabricVersionAtOrAfter("1.3")) {
				// 默认 所有角色
				options.setPeerRoles(EnumSet.of(PeerRole.ENDORSING_PEER, PeerRole.LEDGER_QUERY, PeerRole.CHAINCODE_QUERY, PeerRole.EVENT_SOURCE));
				// 默认 所有角色
				//options.registerEventsForBlocks();
			} else {
				// 除事件源外的所有角色
				options.setPeerRoles(PeerRole.NO_EVENT_SOURCE);
			}

			if (joining) {
				// 加入通道
				channel.joinPeer(peer, options);
			} else {
				channel.addPeer(peer, options);
			}

			logger.debug("peer node '{}' role '{}' join channel：{}", peerName, options.getPeerRoles(), channel.getName());
		}
	}

	/**
	 * 添加事件总线
	 * @author hoojo
	 * @createDate 2018年6月13日 下午4:15:45
	 */
	private void createEventHub(Channel channel, Organization org) throws Exception {
		logger.info("in the channel '{}', create event hub", channel.getName());

		for (String eventHubName : org.getEventHubNames()) {
			String grpcURL = org.getEventHubLocation(eventHubName);
			logger.info("add event hub：{}, URL：{}", eventHubName, grpcURL);

			Properties eventHubProps = config.getEventHubProperties(eventHubName);

			eventHubProps.put("grpc.NettyChannelBuilderOption.keepAliveTime", new Object[] { 5L, TimeUnit.MINUTES });
			eventHubProps.put("grpc.NettyChannelBuilderOption.keepAliveTimeout", new Object[] { 8L, TimeUnit.SECONDS });

			EventHub eventHub = client.newEventHub(eventHubName, grpcURL, eventHubProps);
			logger.trace("create EventHub name: {}, url: {}, conntime: {}", eventHub.getName(), eventHub.getUrl(), eventHub.getConnectedTime());

			channel.addEventHub(eventHub);
		}
	}

	/**
	 * 检查“序列化/反序列化”通道。 可以进行持久化存储，方便下次直接从缓存中恢复通道
	 * @author hoojo
	 * @throws IOException
	 * @throws org.hyperledger.fabric.sdk.exception.InvalidArgumentException
	 * @createDate 2018年6月13日 下午4:26:08
	 */
	private Channel checkChannelSerialize(Channel channel) throws Exception {
		logger.info("check channel '{}' serialize", channel.getName());

		if (!channel.isInitialized()) {
			logger.warn("通道还未初始化操作");
		}
		if (channel.isShutdown()) {
			logger.warn("通道已经关闭");
		}

		// 检查通道是否可以序列化，可以进行持久化存储，方便下次直接从缓存中恢复通道
		byte[] serializedChannelBytes = channel.serializeChannel();
		// 关闭所有释放资源的频道
		channel.shutdown(true);
		// 从通道序列化数据中恢复通道
		channel = client.deSerializeChannel(serializedChannelBytes).initialize();

		checkState(channel.isInitialized(), "通道未初始化");
		checkState(!channel.isShutdown(), "通道被关闭");

		return channel;
	}

	/**
	 * 检查通道在Peer节点上是否安装
	 * @author hoojo
	 * @createDate 2018年6月25日 下午1:01:43
	 */
	private void checkChannel(String channelName, Channel channel) throws Exception {
		logger.info("Check channel '{}' is installed on the Peer node.", channelName);

		// 查找指定通道是否在Peer节点中存在
		for (Peer peer : channel.getPeers()) {
			Set<String> channels = client.queryChannels(peer);
			logger.debug("In the peer node '{}' find channels：{}", peer.getName(), channels);

			if (!channels.contains(channelName)) {
				throw new FabricManagerException("peer node '%s' not found channel '%s'", peer.getName(), channelName);
			}
		}
	}
}
