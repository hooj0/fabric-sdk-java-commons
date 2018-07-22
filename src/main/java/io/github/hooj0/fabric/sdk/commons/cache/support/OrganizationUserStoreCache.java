package io.github.hooj0.fabric.sdk.commons.cache.support;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.hooj0.fabric.sdk.commons.FabricCacheException;
import io.github.hooj0.fabric.sdk.commons.cache.AbstractStoreCache;
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
public class OrganizationUserStoreCache extends AbstractStoreCache<OrganizationUser> {
	
	private final static Logger logger = LoggerFactory.getLogger(OrganizationUserStoreCache.class);
	
	public OrganizationUserStoreCache(FabricKeyValueStore keyValueStore) {
		super(CacheKeyPrefix.ORGANIZATION_USER_PREFIX, keyValueStore);
	}

	@Override
	public String serialize(OrganizationUser store) {
		ByteArrayOutputStream bos = null;
		ObjectOutputStream oos = null;
		
		String serializeStrings = null;
		try {
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			
			oos.writeObject(store);
			oos.flush();
			
			serializeStrings = Hex.toHexString(bos.toByteArray());
		} catch (Exception e) {
			throw new FabricCacheException(e, "Organization User serialize exception");
		} finally {
			try {
				if (oos != null) oos.close();
				if (bos != null) bos.close();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		
		return serializeStrings;
	}

	@Override
	public OrganizationUser deserialize(String value) {
		
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		try {
			// 用户被发现在关键值存储中，因此恢复状态
			byte[] serialized = Hex.decode(value);

			// 反序列化，解组对象
			bis = new ByteArrayInputStream(serialized);
			ois = new ObjectInputStream(bis);

			return (OrganizationUser) ois.readObject();
		} catch (Exception e) {
			throw new FabricCacheException(e, "Organization User deserialize exception");
		} finally {
			try {
				if (bis != null) bis.close();
				if (bis != null) bis.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
}
