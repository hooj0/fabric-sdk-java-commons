package io.github.hooj0.fabric.sdk.commons.core.manager;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.identity.X509Enrollment;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.HFCAIdentity;
import org.hyperledger.fabric_ca.sdk.HFCAInfo;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.hyperledger.fabric_ca.sdk.exception.EnrollmentException;
import org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric_ca.sdk.exception.RegistrationException;

import io.github.hooj0.fabric.sdk.commons.FabricRootException;
import io.github.hooj0.fabric.sdk.commons.config.FabricConfiguration;
import io.github.hooj0.fabric.sdk.commons.core.creator.OrganizationUserCreator;
import io.github.hooj0.fabric.sdk.commons.core.creator.OrganizationUserCreatorImpl;
import io.github.hooj0.fabric.sdk.commons.core.creator.OrganizationUserCreatorImpl.UserEnrollment;
import io.github.hooj0.fabric.sdk.commons.domain.Organization;
import io.github.hooj0.fabric.sdk.commons.domain.OrganizationUser;
import io.github.hooj0.fabric.sdk.commons.store.FabricKeyValueStore;
import io.github.hooj0.fabric.sdk.commons.util.GzipUtils;
import io.github.hooj0.fabric.sdk.commons.util.PrivateKeyConvertUtils;

/**
 * fabric user manager support
 * @changelog Add peerAdmin & adimn cache store support
 * @author hoojo
 * @createDate 2018年6月22日 下午5:23:50
 * @file UserManager.java
 * @package io.github.hooj0.fabric.sdk.commons.core.manager
 * @project fabric-sdk-examples
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class UserManager extends AbstractManager {

	private Collection<Organization> organizations;
	private OrganizationUserCreator userCreator;

	public UserManager(FabricConfiguration config) {
		super(config, UserManager.class);
		this.init();
	}
	
	public UserManager(FabricConfiguration config, File keyValueStoreFile) {
		super(config, keyValueStoreFile, UserManager.class);
		this.init();
	}
	
	public UserManager(FabricConfiguration config, FabricKeyValueStore keyValueStore) {
		super(config, keyValueStore, UserManager.class);
		this.init();
	}

	private void init() {
		this.organizations = config.getOrganizations();
		this.userCreator = new OrganizationUserCreatorImpl(enrollmentStoreCache, userStoreCache, certStoreCache, keyStoreCache);
	}
	
	public void initialize(String adminName, String adminSecret, String... userNames) throws Exception {
		logger.info("initialize organization user.");

		initializeCaClient();
		enrollOrganizationUsers(adminName, adminSecret, userNames);
	}

	/**
	 * 启动前 设置每个 org资源的 ca 客户端对象
	 * @author hoojo
	 * @createDate 2018年6月12日 下午5:44:48
	 * @throws MalformedURLException
	 * @throws InvalidArgumentException
	 */
	public void initializeCaClient() throws Exception {
		logger.info("initialize all organization peer node Ca Client");

		// 创建 CA client
		for (Organization org : organizations) {
			String caName = org.getCAName(); // Try one of each name and no name.

			if (org.getCAClient() != null) {
				continue;
			}
			if (StringUtils.isNotBlank(caName)) {
				org.setCAClient(HFCAClient.createNewInstance(caName, org.getCALocation(), org.getCAProperties()));
			} else {
				org.setCAClient(HFCAClient.createNewInstance(org.getCALocation(), org.getCAProperties()));
			}
		}
	}

	/**
	 * CA Admin、User、Peer Admin、TLS 注册和认证
	 * @author hoojo
	 * @createDate 2018年6月13日 上午11:01:15
	 * @throws Exception
	 */
	private void enrollOrganizationUsers(String adminName, String adminSecret, String... userNames) throws Exception {
		logger.info("Start Enroll Organization: CA Admin、User、Peer Admin(TLS) ");

		for (Organization org : organizations) {
			logger.debug("iterator Organization orgName: `{}`, mspID: `{}` enroll & register user", org.getName(), org.getMSPID());

			HFCAClient ca = org.getCAClient();
			// PKI密钥创建/签署/验证
			ca.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());

			/** TLS 证书模式：如何从Fabric CA获取客户端TLS证书，为orderer、peer 使用一个客户端TLS证书 */
			if (config.isEnabledFabricTLS()) {
				enrollAdminTLS(org, adminName, adminSecret);
			} else {
				logger.debug("TLS Enabled：{}", config.isEnabledFabricTLS());
			}

			HFCAInfo info = ca.info();
			checkNotNull(info, "HFCAInfo is null");
			
			if (!StringUtils.isBlank(org.getCAName())) {
				checkArgument(StringUtils.equals(info.getCAName(), ca.getCAName()), "HFCAInfo.CAName [%s] 和  CaInfo.CAName [%s] 不等", info.getCAName(), ca.getCAName());
			}

			// admin enroll
			OrganizationUser admin = enrollAdmin(org, adminName, adminSecret);

			// 设置当前组织 admin
			org.setAdmin(admin);

			for (String userName : userNames) {
				// user register/enroll
				OrganizationUser user = registerAndEnrollUser(org, userName);
				
				// 设置当前组织 user
				org.addUser(user);
			}

			// peer admin
			OrganizationUser peerAdmin = createPeerAdmin(org);

			// 设置当前组织 peerAdmin
			org.setPeerAdmin(peerAdmin);
		}
	}

	/**
	 * 从Fabric CA获取客户端TLS证书，使用客户端TLS证书
	 * @author hoojo
	 * @createDate 2018年6月13日 上午10:38:55
	 */
	public Enrollment enrollAdminTLS(Organization org, String adminName, String adminSecret) throws EnrollmentException, InvalidArgumentException, Exception {
		logger.info("the Admin `{}` `TLS` authenticates by account 'adminName' & 'adminSecret'", adminName);

		String[] key = new String[] { org.getName(), adminName, "tls" };
		Enrollment enrollment = hfcaEnrollmentStoreCache.getStore(key);
		if (enrollment == null) {
			HFCAClient ca = org.getCAClient();
			
			// 构建认证请求
			final EnrollmentRequest request = new EnrollmentRequest();
			request.addHost("localhost");
			request.setProfile("tls");
			logger.trace("ca admin request: {}", request);
			
			// 发起请求进行认证
			enrollment = ca.enroll(adminName, adminSecret, request); // 设置 ca 管理员名称和密码，对应 docker-compose.yaml
			final String tlsCertPEM = enrollment.getCert();
			final String tlsKeyPEM = PrivateKeyConvertUtils.getStringFromPrivateKey(enrollment.getKey());
			
			logger.trace("enrollment: {}", enrollment);
			logger.trace("tlsKeyPEM: {}, tlsCertPEM: {}", tlsKeyPEM, tlsCertPEM);
			
			hfcaEnrollmentStoreCache.setStore(key, (X509Enrollment) enrollment);
			// 保存证书 key、cert
			certStoreCache.setStore(key, tlsCertPEM);
			keyStoreCache.setStore(key, tlsKeyPEM);
		} 

		return enrollment;
	}

	/**
	 * ca Admin 角色 - 管理员认证
	 * 
	 * @author hoojo
	 * @createDate 2018年6月13日 上午10:41:54
	 */
	public OrganizationUser enrollAdmin(Organization org, String adminName, String adminSecret) throws EnrollmentException, InvalidArgumentException {
		logger.info("the Admin `{}` authenticates by account 'adminName' & 'adminSecret'", adminName);

		HFCAClient ca = org.getCAClient();

		// 从缓存或store中获取用户
		OrganizationUser admin = userCreator.create(adminName, org.getName());
		if (!admin.isEnrolled()) { // 未认证，只需用用ca client进行认证

			// 认证：获取用户的签名证书和私钥。
			Enrollment enrollment = ca.enroll(admin.getName(), adminSecret);
			logger.trace("用户：{} 进行认证: {}", admin.getName(), enrollment);

			admin.setEnrollmentSecret(adminSecret);
			admin.setEnrollment(enrollment);
			admin.setMspId(org.getMSPID());
		}

		return admin;
	}

	/**
	 * User 角色-普通用户注册和认证
	 * @author hoojo
	 * @createDate 2018年6月13日 上午10:48:39
	 */
	public OrganizationUser registerAndEnrollUser(Organization org, String userName, String affiliation) throws Exception {
		logger.info("the User `{}` authenticates by account 'userName' & 'affiliation'", userName);

		HFCAClient ca = org.getCAClient();

		// 从缓存或store中获取用户
		OrganizationUser user = userCreator.create(userName, org.getName());
		if (!user.isRegistered()) { // 未注册
			// 用户注册
			final RegistrationRequest request = new RegistrationRequest(user.getName(), affiliation);

			// 利用管理员权限进行普通user注册
			String secret = null;
			try {
				secret = ca.register(request, org.getAdmin());
				logger.trace("用户 {} 注册，秘钥：{}", user, secret);
				
				user.setAffiliation(request.getAffiliation());
				user.setEnrollmentSecret(secret);
			} catch (RegistrationException e) {
				HFCAIdentity ident = ca.newHFCAIdentity(userName);
				
				if (ident.read(org.getAdmin()) == 200) {
					user.setAffiliation(ident.getAffiliation());
					user.setEnrollmentSecret(ident.getSecret());
				}
			}
		}

		if (!user.isEnrolled()) { // 未认证
			Enrollment enrollment = null;
			try {
				// 用户认证
				enrollment = ca.enroll(user.getName(), user.getEnrollmentSecret());
				logger.trace("用户：{} 进行认证: {}", user.getName(), enrollment);
				
			} catch (Exception e) {
				// reason = null，无理由撤销用户证书
				ca.revoke(org.getAdmin(), user.getName(), null); 
				enrollment = ca.reenroll(user); // 重新认证
			}

			checkNotNull(enrollment, "OrganizationUser '%s' enrollment secret is not set", user.getName());
			
			user.setEnrollment(enrollment);
			user.setMspId(org.getMSPID());
		}

		return user;
	}
	
	/** {@link #registerAndEnrollUser(Organization, String, String) } */
	public OrganizationUser registerAndEnrollUser(Organization org, String userName) throws Exception {
		return this.registerAndEnrollUser(org, userName, "org1.department1");
	}

	/**
	 * peer节点管理员 注册和认证
	 * 
	 * @author hoojo
	 * @createDate 2018年6月13日 上午10:53:35
	 * @throws IOException
	 */
	public OrganizationUser createPeerAdmin(Organization org) throws Exception {
		logger.info("the PeerAdmin `{}` cert & key by account", org.getName() + "Admin");

		final String orgName = org.getName();
		final String mspid = org.getMSPID();
		final String domain = org.getDomainName();
		final String userName = orgName + "Admin";
		
		File privateKeyFile = null, certificateFile = null;
		UserEnrollment enrollment = enrollmentStoreCache.getStore(new String[] { org.getName(), userName });
		if (enrollment == null) {
			// src/test/fixture/sdkintegration/e2e-2Orgs/channel/crypto-config/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/keystore/
			String peerOrgs = "crypto-config/peerOrganizations/";
			File keydir = Paths.get(config.getCryptoChannelConfigRootPath(), peerOrgs, domain, format("/users/Admin@%s/msp/keystore", domain)).toFile();
			privateKeyFile = GzipUtils.findFileSk(keydir);
			certificateFile = Paths.get(config.getCryptoChannelConfigRootPath(), peerOrgs, domain, format("/users/Admin@%s/msp/signcerts/Admin@%s-cert.pem", domain, domain)).toFile();
			
			logger.trace("privateKeyDir: {}", keydir.getAbsolutePath());
			logger.trace("privateKeyFile: {}", privateKeyFile.getAbsolutePath());
			logger.trace("certificateFile: {}", certificateFile.getAbsolutePath());
			
			if (!privateKeyFile.exists()) {
				throw new FabricRootException("*_sk file not found: %s", privateKeyFile.getAbsolutePath());
			}
		}

		// 从缓存或store中获取用户
		OrganizationUser peerAdmin = userCreator.create(userName, orgName, mspid, privateKeyFile, certificateFile);
		logger.trace("构建Peer Admin用户：{}", peerAdmin);

		return peerAdmin;
	}
}
