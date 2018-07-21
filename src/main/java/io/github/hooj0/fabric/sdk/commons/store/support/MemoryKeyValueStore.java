package io.github.hooj0.fabric.sdk.commons.store.support;

import java.util.Map;

import com.google.common.collect.Maps;

import io.github.hooj0.fabric.sdk.commons.AbstractObject;
import io.github.hooj0.fabric.sdk.commons.store.KeyValueStore;

/**
 * 内存系统KeyValue持久化存储
 * @author hoojo
 * @createDate 2018年7月21日 下午9:46:14
 * @file MemoryKeyValueStore.java
 * @package io.github.hooj0.fabric.sdk.commons.store.support
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class MemoryKeyValueStore extends AbstractObject implements KeyValueStore {

	private Map<String, String> stores = Maps.newConcurrentMap();
	
	@Override
	public String get(String key) {
		return stores.get(key);
	}

	@Override
	public void set(String key, String value) {
		stores.put(key, value);
	}

	@Override
	public boolean contains(String key) {
		return stores.containsKey(key);
	}

	@Override
	public boolean remove(String key) {
		stores.remove(key);
		
		return this.contains(key);
	}
}
