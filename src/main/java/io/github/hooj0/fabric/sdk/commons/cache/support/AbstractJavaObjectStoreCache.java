package io.github.hooj0.fabric.sdk.commons.cache.support;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.bouncycastle.util.encoders.Hex;

import io.github.hooj0.fabric.sdk.commons.FabricCacheException;
import io.github.hooj0.fabric.sdk.commons.cache.AbstractStoreCache;
import io.github.hooj0.fabric.sdk.commons.cache.CacheKeyPrefix;
import io.github.hooj0.fabric.sdk.commons.store.FabricKeyValueStore;

/**
 * based serializable java object store cache support
 * @author hoojo
 * @createDate 2018年8月10日 上午9:57:21
 * @file AbstractJavaObjectStoreCache.java
 * @package io.github.hooj0.fabric.sdk.commons.cache.support
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public abstract class AbstractJavaObjectStoreCache<T extends Serializable> extends AbstractStoreCache<T> {

	public AbstractJavaObjectStoreCache(CacheKeyPrefix keyPrefix, Class<?> clazz) {
		super(keyPrefix, clazz);
	}
	
	public AbstractJavaObjectStoreCache(CacheKeyPrefix keyPrefix, File keyValueStoreFile, Class<?> clazz) {
		super(keyPrefix, keyValueStoreFile, clazz);
	}
	
	public AbstractJavaObjectStoreCache(CacheKeyPrefix keyPrefix, FabricKeyValueStore keyValueStore, Class<?> clazz) {
		super(keyPrefix, keyValueStore, clazz);
	}
	
	@Override
	public String serialize(T store) {
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
			throw new FabricCacheException(e, "Serializable Object serialize exception: %s", e.getMessage());
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
	@SuppressWarnings("unchecked")
	public T deserialize(String value) {
		
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		try {
			byte[] serialized = Hex.decode(value);

			// 反序列化，解组对象
			bis = new ByteArrayInputStream(serialized);
			ois = new ObjectInputStream(bis);

			return (T) ois.readObject();
		} catch (Exception e) {
			throw new FabricCacheException(e, "Serializable Object deserialize exception: %s", e.getMessage());
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
