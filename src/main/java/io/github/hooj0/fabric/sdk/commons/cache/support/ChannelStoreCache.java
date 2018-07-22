package io.github.hooj0.fabric.sdk.commons.cache.support;

import java.io.IOException;

import org.bouncycastle.util.encoders.Hex;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;

import io.github.hooj0.fabric.sdk.commons.FabricCacheException;
import io.github.hooj0.fabric.sdk.commons.cache.AbstractStoreCache;
import io.github.hooj0.fabric.sdk.commons.cache.CacheKeyPrefix;
import io.github.hooj0.fabric.sdk.commons.store.FabricKeyValueStore;

/**
 * channel store cache support
 * @author hoojo
 * @createDate 2018年7月22日 下午2:54:31
 * @file ChannelStoreCache.java
 * @package io.github.hooj0.fabric.sdk.commons.cache.support
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class ChannelStoreCache extends AbstractStoreCache<Channel> {

	private HFClient client;
	
	public ChannelStoreCache(FabricKeyValueStore keyValueStore, HFClient client) {
		super(CacheKeyPrefix.CHANNEL_PREFIX, keyValueStore);
		
		this.client = client;
	}

	@Override
	public String serialize(Channel store) {
		try {
			return Hex.toHexString(store.serializeChannel());
		} catch (InvalidArgumentException | IOException e) {
			throw new FabricCacheException(e, "channel serialize exception");
		}
	}

	@Override
	public Channel deserialize(String value) {
		Channel channel;
		try {
			channel = client.deSerializeChannel(Hex.decode(value));
		} catch (Exception e) {
			throw new FabricCacheException(e, "Serialize Channel exception");
		}
		
		return channel;
	}
}
