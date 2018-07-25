package io.github.hooj0.fabric.sdk.commons.core.execution.option;

import org.hyperledger.fabric.sdk.User;

/**
 * chaincode deploy operation install options
 * @author hoojo
 * @createDate 2018年7月24日 下午3:38:32
 * @file InstallOptions.java
 * @package io.github.hooj0.fabric.sdk.commons.core
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class InstallOptions extends Options {

	private String chaincodeUpgradeVersion;
	private User requestUser;

	public String getChaincodeUpgradeVersion() {
		return chaincodeUpgradeVersion;
	}

	public InstallOptions setChaincodeUpgradeVersion(String chaincodeUpgradeVersion) {
		this.chaincodeUpgradeVersion = chaincodeUpgradeVersion;
		
		return this;
	}

	public User getRequestUser() {
		return requestUser;
	}

	public InstallOptions setRequestUser(User installUser) {
		this.requestUser = installUser;
		return this;
	}
}
