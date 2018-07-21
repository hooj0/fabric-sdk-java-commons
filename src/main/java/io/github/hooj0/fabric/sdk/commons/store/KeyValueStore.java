package io.github.hooj0.fabric.sdk.commons.store;

/**
 * key-value 数据存储接口，可以是文件、缓存、数据库、内存等持久化实现方式
 * @author hoojo
 * @createDate 2018年7月21日 下午9:03:21
 * @file KeyValueStore.java
 * @package io.github.hooj0.fabric.sdk.commons.store
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface KeyValueStore {

	/**
	 * 获取 键值信息
	 * @author hoojo
	 * @createDate 2018年7月21日 下午9:30:49
	 * @param key store key
	 * @return 返回存储的数据
	 */
	public String get(String key);
	
	/**
	 * 设置键值信息
	 * @author hoojo
	 * @createDate 2018年7月21日 下午9:31:16
	 * @param key store key
	 * @param value store value
	 */
	public void set(String key, String value);
	
	/**
	 * 判断key是否存在 
	 * @author hoojo
	 * @createDate 2018年7月21日 下午9:39:19
	 * @param key store key
	 * @return true | false
	 */
	public boolean contains(String key);
	
	/**
	 * 删除 key 信息
	 * @author hoojo
	 * @createDate 2018年7月21日 下午9:40:48
	 * @param key store key
	 * @return 删除是否成功 
	 */
	public boolean remove(String key);
}
