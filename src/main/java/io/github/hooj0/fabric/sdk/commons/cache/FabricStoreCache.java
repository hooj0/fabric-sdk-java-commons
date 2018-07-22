package io.github.hooj0.fabric.sdk.commons.cache;

/**
 * fabric store KeyValue 持久化存储接口
 * @author hoojo
 * @createDate 2018年7月21日 下午9:38:03
 * @file FabricStoreCache.java
 * @package io.github.hooj0.fabric.sdk.commons.cache
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface FabricStoreCache<T> {

	/**
	 * 缓存store对象 
	 * @author hoojo
	 * @createDate 2018年7月22日 上午11:54:27
	 * @param key store Keys
	 * @param store <T>
	 */
	public void setStore(String[] storeKey, T store);

	/**
	 * 缓存store对象 
	 * @author hoojo
	 * @createDate 2018年7月22日 上午11:54:27
	 * @param key store Key
	 * @param store <T>
	 */
	public void setStore(String storeKey, T store);
	
	/**
	 * 获取缓存的store对象
	 * @author hoojo
	 * @createDate 2018年7月22日 上午11:56:14
	 * @param key store Key
	 * @return
	 */
	public T getStore(String... storeKey);
	
	/**
	 * 判断缓存对象是否存在
	 * @author hoojo
	 * @createDate 2018年7月22日 上午11:56:56
	 * @param storeKey store Key
	 * @return true | false
	 */
	public boolean hasStore(String... storeKey);
	
	/**
	 * 删除缓存数据对象
	 * @author hoojo
	 * @createDate 2018年7月22日 下午2:19:08
	 * @param storeKey store Key
	 * @return true | false
	 */
	public boolean removeStore(String... storeKey);
}
