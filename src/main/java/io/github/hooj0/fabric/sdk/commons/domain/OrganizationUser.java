package io.github.hooj0.fabric.sdk.commons.domain;

import java.io.Serializable;
import java.util.Set;

import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;

import io.github.hooj0.fabric.sdk.commons.AbstractObject;
import io.github.hooj0.fabric.sdk.commons.cache.FabricStoreCache;
import io.netty.util.internal.StringUtil;

/**
 * fabric user organization domain
 * @author hoojo
 * @createDate 2018年8月2日 上午8:51:57
 * @file OrganizationUser.java
 * @package io.github.hooj0.fabric.sdk.commons.domain
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class OrganizationUser extends AbstractObject implements User, Serializable {

	private static final long serialVersionUID = -383568751809394444L;

	/** 获取由用户组织提供的会员服务提供商标识符 */
	private String mspId;
	/** 名称 */
	private String name;
	/** 角色 */
	private Set<String> roles;
	/** 账户  */
	private String account;
	/** 成员 membership  */
	private String affiliation;
	/** 组织 */
	private String organization;
	/** 注册证书认证秘钥 */
	private String enrollmentSecret;
	/** 注册证书信息  */
	private Enrollment enrollment = null; // need access in test env.
	
	/** 键值存储 */
	private transient FabricStoreCache<OrganizationUser> storeCache;

	/**
	 * 构建一个用户，如果存在就从缓存中恢复，不存在就保存到缓存
	 * @param name 用户名
	 * @param org 组织名称
	 * @param fileStore 文件缓存
	 */
	public OrganizationUser(String name, String org, FabricStoreCache<OrganizationUser> storeCache) {
		super(OrganizationUser.class);
		
		this.name = name;
		this.organization = org;
		this.storeCache = storeCache;
		
		if (storeCache.hasStore(org, name)) {
			bind(storeCache.getStore(org, name));
		} else {
			setStoreCache();
		}
	}
	
	private void bind(OrganizationUser user) {
		if (user != null) {
			this.name = user.name;
			this.roles = user.roles;
			this.account = user.account;
			this.affiliation = user.affiliation;
			this.organization = user.organization;
			this.enrollmentSecret = user.enrollmentSecret;
			this.enrollment = user.enrollment;
			this.mspId = user.mspId;
			
			logger.debug("Restore User from store cache: {}", this);
		}
	}
	
	private void setStoreCache() {
		storeCache.setStore(new String[] { this.organization, this.name }, this);
	}
	
	/**
	 * 确定该名称是否已注册
	 */
	public boolean isRegistered() {
		return !StringUtil.isNullOrEmpty(enrollmentSecret);
	}

	/**
	 * 确定该名称是否已认证
	 */
	public boolean isEnrolled() {
		return this.enrollment != null;
	}

	public String getEnrollmentSecret() {
		return enrollmentSecret;
	}

	public void setEnrollmentSecret(String enrollmentSecret) {
		this.enrollmentSecret = enrollmentSecret;
		
		setStoreCache();
	}

	public void setEnrollment(Enrollment enrollment) {
		this.enrollment = enrollment;
		
		setStoreCache();
	}

	@Override
	public String getMspId() {
		return mspId;
	}

	public void setMspId(String mspID) {
		this.mspId = mspID;
		
		setStoreCache();
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Set<String> getRoles() {
		return this.roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
		
		setStoreCache();
	}

	@Override
	public String getAccount() {
		return this.account;
	}

	public void setAccount(String account) {
		this.account = account;
		
		setStoreCache();
	}

	@Override
	public String getAffiliation() {
		return this.affiliation;
	}

	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
		
		setStoreCache();
	}

	@Override
	public Enrollment getEnrollment() {
		return this.enrollment;
	}
}
