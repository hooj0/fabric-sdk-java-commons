package io.github.hooj0.fabric.sdk.commons.cache;

/**
 * 缓存对象与字符串之间的序列号和反序列化
 * @author hoojo
 * @createDate 2018年7月22日 下午1:49:19
 * @file Serialization.java
 * @package io.github.hooj0.fabric.sdk.commons.cache
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface Serialization<T> {

	/**
	 * 将对象序列成字符串 
	 * @author hoojo
	 * @createDate 2018年7月22日 下午1:51:14
	 * @param store T
	 * @return serialize string
	 */
	public String serialize(T store);
	
	/**
	 * 将字符串反序列成对象
	 * @author hoojo
	 * @createDate 2018年7月22日 下午1:51:47
	 * @param value
	 * @return deserialize value
	 */
	public T deserialize(String value);
}
