package io.github.hooj0.fabric.sdk.commons.cache;

/**
 * 缓存前缀枚举
 * @author hoojo
 * @createDate 2018年7月22日 上午9:10:47
 * @file CacheKeyPrefix.java
 * @package io.github.hooj0.fabric.sdk.commons.cache
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public enum CacheKeyPrefix {

	CHANNEL_PREFIX("channel"),
	ORGANIZATION_USER_PREFIX("user"),
	ENROLLEMENT_USER_PREFIX("ca.enroll"),
	PEM_TLS_CERT_PREFIX("clientPEMTLSCertificate"),
	PEM_TLS_KEY_PREFIX("clientPEMTLSKey");
	
	private String keyPrefix;
	CacheKeyPrefix(String keyPrefix) {
		this.keyPrefix = keyPrefix;
	}
	
	public String getKeyPrefix(String... centerKeys) {
		StringBuilder builder = new StringBuilder(keyPrefix);
		
		for (String key : centerKeys) {
			builder.append(".").append(key);
		}
		
		return builder.toString();
	}
}
