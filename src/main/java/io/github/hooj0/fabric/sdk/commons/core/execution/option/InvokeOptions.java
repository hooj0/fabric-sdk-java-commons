package io.github.hooj0.fabric.sdk.commons.core.execution.option;

import org.hyperledger.fabric.sdk.User;

/**
 * chaincode transaction invoke execution options
 * @author hoojo
 * @createDate 2018年7月24日 上午11:40:50
 * @file InvokeOptions.java
 * @package io.github.hooj0.fabric.sdk.commons.core
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class InvokeOptions extends TransactionsOptions {

	/** 发起查询用户 */
	private User requestUser;

	public User getRequestUser() {
		return requestUser;
	}

	public InvokeOptions setRequestUser(User requestUser) {
		this.requestUser = requestUser;
		return this;
	}
}
