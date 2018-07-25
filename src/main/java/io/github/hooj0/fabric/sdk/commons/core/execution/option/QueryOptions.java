package io.github.hooj0.fabric.sdk.commons.core.execution.option;

import org.hyperledger.fabric.sdk.User;

/**
 * chaincode query execution interface option
 * @author hoojo
 * @createDate 2018年7月25日 上午11:15:25
 * @file QueryOptions.java
 * @package io.github.hooj0.fabric.sdk.commons.core.execution.option
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class QueryOptions extends Options {

	/** 发起查询用户 */
	private User requestUser;

	public User getRequestUser() {
		return requestUser;
	}

	public QueryOptions setRequestUser(User requestUser) {
		this.requestUser = requestUser;
		return this;
	}
}
