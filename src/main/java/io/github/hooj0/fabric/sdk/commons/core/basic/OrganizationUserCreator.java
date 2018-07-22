package io.github.hooj0.fabric.sdk.commons.core.basic;

import java.io.File;

import io.github.hooj0.fabric.sdk.commons.domain.OrganizationUser;

/**
 * Organization User Creator
 * @author hoojo
 * @createDate 2018年7月22日 上午11:40:48
 * @file OrganizationUserCreator.java
 * @package io.github.hooj0.fabric.sdk.commons.core.basic
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface OrganizationUserCreator {

	public OrganizationUser create(String name, String org);
	
	public OrganizationUser create(String name, String org, String mspId, File privateKeyFile, File certificateFile);
}
