package io.github.hooj0.fabric.sdk.commons.core.creator;

import java.io.File;
import java.io.FileInputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.security.PrivateKey;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.hyperledger.fabric.sdk.Enrollment;

import io.github.hooj0.fabric.sdk.commons.AbstractObject;
import io.github.hooj0.fabric.sdk.commons.FabricCreatorException;
import io.github.hooj0.fabric.sdk.commons.cache.FabricStoreCache;
import io.github.hooj0.fabric.sdk.commons.domain.OrganizationUser;

/**
 * Organization User Creator
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
	
	private FabricStoreCache<OrganizationUser> storeCache;
	
	public OrganizationUserCreatorImpl(FabricStoreCache<OrganizationUser> storeCache) {
		this.storeCache = storeCache;
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

			String certificate = new String(IOUtils.toByteArray(new FileInputStream(certificateFile)), "UTF-8");
			PrivateKey privateKey = getPrivateKeyFromBytes(IOUtils.toByteArray(new FileInputStream(privateKeyFile)));

			user.setMspId(mspId);
			// 登记认证
			user.setEnrollment(new StoreEnrollement(privateKey, certificate));
			
			return user;
		} catch (Exception e) {
			throw new FabricCreatorException(e, "Get '%s.%s' OrganizationUser Member from cache exception:", org, name);
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
	
	/**
	 * 将byte字节私钥转换成 PrivateKey 对象
	 * @author hoojo
	 * @createDate 2018年6月13日 上午11:28:54
	 * @param data key
	 * @return PrivateKey
	 */
	private static PrivateKey getPrivateKeyFromBytes(byte[] data) throws Exception {
		final Reader pemReader = new StringReader(new String(data));

		final PrivateKeyInfo pemPair;
		try (PEMParser pemParser = new PEMParser(pemReader)) {
			pemPair = (PrivateKeyInfo) pemParser.readObject();
		}

		PrivateKey privateKey = new JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME).getPrivateKey(pemPair);

		return privateKey;
	}
	
	private static final class StoreEnrollement implements Enrollment, Serializable {

		private static final long serialVersionUID = -2784835212445309006L;
		private final PrivateKey privateKey;
		private final String certificate;

		public StoreEnrollement(PrivateKey privateKey, String certificate) {
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
