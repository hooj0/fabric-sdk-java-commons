package io.github.hooj0.fabric.sdk.commons.core.creator;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.security.PrivateKey;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.hyperledger.fabric.sdk.Enrollment;

import io.github.hooj0.fabric.sdk.commons.AbstractObject;
import io.github.hooj0.fabric.sdk.commons.FabricCreatorException;
import io.github.hooj0.fabric.sdk.commons.cache.FabricStoreCache;
import io.github.hooj0.fabric.sdk.commons.domain.OrganizationUser;
import io.github.hooj0.fabric.sdk.commons.util.PrivateKeyConvertUtils;

/**
 * Organization User Creator
 * @changelog add peer adimn cache store support
 * @author hoojo
 * @createDate 2018年7月22日 上午11:40:33
 * @file OrganizationUserCreatorImpl.java
 * @package io.github.hooj0.fabric.sdk.commons.core.creator
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class OrganizationUserCreatorImpl extends AbstractObject implements OrganizationUserCreator {
	
	private FabricStoreCache<UserEnrollment> enrollmentStoreCache;
	private FabricStoreCache<OrganizationUser> storeCache;
	private FabricStoreCache<String> certStoreCache;
	private FabricStoreCache<String> keyStoreCache;
	
	public OrganizationUserCreatorImpl(FabricStoreCache<UserEnrollment> enrollmentStoreCache, FabricStoreCache<OrganizationUser> storeCache, FabricStoreCache<String> certStoreCache, FabricStoreCache<String> keyStoreCache) {
		this.storeCache = storeCache;
		this.certStoreCache = certStoreCache;
		this.keyStoreCache = keyStoreCache;
		this.enrollmentStoreCache = enrollmentStoreCache;
	}
	
	@Override
	public OrganizationUser create(String name, String org, String mspId, File privateKeyFile, File certificateFile) {
		logger.debug("create organization User '{}' by Org '{}'", name, org);
		
		try {
			OrganizationUser user = storeCache.getCache(org, name);
			if (user != null) {
				return user;
			}
			user = new OrganizationUser(name, org, storeCache);

			String[] key = new String[] { org, name };
			
			UserEnrollment enrollment = enrollmentStoreCache.getStore(key);
			if (enrollment == null) {
				String certificate = new String(IOUtils.toByteArray(new FileInputStream(certificateFile)), "UTF-8");
				byte[] keyBytes = IOUtils.toByteArray(new FileInputStream(privateKeyFile));
				
				PrivateKey privateKey = PrivateKeyConvertUtils.getPrivateKeyFromBytes(keyBytes);
				enrollment = new UserEnrollment(privateKey, certificate);
				
				// 缓存认证合同
				enrollmentStoreCache.setStore(key, enrollment);
				// 保存证书 key、cert
				certStoreCache.setStore(key, certificate);
				keyStoreCache.setStore(key, new String(keyBytes, "UTF-8"));
			} 

			user.setMspId(mspId);
			// 登记认证
			user.setEnrollment(enrollment);
			
			return user;
		} catch (Exception e) {
			throw new FabricCreatorException(e, "Get '%s.%s' OrganizationUser Member from cache exception: %s", org, name, e.getMessage());
		}	
	}
	
	public OrganizationUser createForStore(String name, String org, String mspId, File privateKeyFile, File certificateFile) {
		logger.debug("create organization User '{}' by Org '{}'", name, org);
		
		try {
			OrganizationUser user = storeCache.getCache(org, name);
			if (user != null) {
				return user;
			}

			user = new OrganizationUser(name, org, storeCache);

			String[] key = new String[] { org, name };
			String storeCert = certStoreCache.getStore(key), storeKey = keyStoreCache.getStore(key);
			
			Enrollment enrollment = null;
			if (StringUtils.isBlank(storeCert) || StringUtils.isBlank(storeKey)) {
				String certificate = new String(IOUtils.toByteArray(new FileInputStream(certificateFile)), "UTF-8");
				byte[] keyBytes = IOUtils.toByteArray(new FileInputStream(privateKeyFile));
				
				PrivateKey privateKey = PrivateKeyConvertUtils.getPrivateKeyFromBytes(keyBytes);
				enrollment = new UserEnrollment(privateKey, certificate);
				
				// 保存证书 key、cert
				certStoreCache.setStore(key, certificate);
				keyStoreCache.setStore(key, new String(keyBytes, "UTF-8"));
			} else {
				enrollment = new UserEnrollment(PrivateKeyConvertUtils.getPrivateKeyFromBytes(storeKey.getBytes("UTF-8")), storeCert);
			}

			user.setMspId(mspId);
			// 登记认证
			user.setEnrollment(enrollment);
			
			return user;
		} catch (Exception e) {
			throw new FabricCreatorException(e, "Get '%s.%s' OrganizationUser Member from cache exception: %s", org, name, e.getMessage());
		}	
	}
	
	public OrganizationUser create(String name, String org) {
		logger.debug("create organization User '{}' by Org '{}'", name, org);
		
		OrganizationUser user = storeCache.getCache(org, name);
		if (null != user) {
			return user;
		}
		
		user = new OrganizationUser(name, org, storeCache);
		
		return user;
	}
	
	public static final class UserEnrollment implements Enrollment, Serializable {

		private static final long serialVersionUID = -2784835212445309006L;
		private final PrivateKey privateKey;
		private final String certificate;

		public UserEnrollment(PrivateKey privateKey, String certificate) {
			this.certificate = certificate;
			this.privateKey = privateKey;
		}

		@Override
		public PrivateKey getKey() {
			return privateKey;
		}

		@Override
		public String getCert() {
			return certificate;
		}
	}
}
