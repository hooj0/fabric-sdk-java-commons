package io.github.hooj0.fabric.sdk.commons.core.execution.option;

import java.util.Collection;
import java.util.Map;

import org.hyperledger.fabric.sdk.Channel.TransactionOptions;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.User;

/**
 * <b>function:</b>
 * @author hoojo
 * @createDate 2018年7月24日 上午11:40:50
 * @file InvokeOptions.java
 * @package io.github.hooj0.fabric.sdk.commons.core
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class InvokeOptions extends SimpleOptions {

	private int transactionWaitTime;
	
	private User contextUser;
	private Collection<Orderer> orderers;
	private TransactionOptions options;
	
	public InvokeOptions(String func) {
		super(func);
	}
	
	public InvokeOptions(String func, Object... values) {
		super(func, values);
	}

	public InvokeOptions(String func, Map<String, Object> values) {
		super(func, values);
	}

	public User getContextUser() {
		return contextUser;
	}

	public InvokeOptions setContextUser(User contextUser) {
		this.contextUser = contextUser;
		
		return this;
	}

	public int getTransactionWaitTime() {
		return transactionWaitTime;
	}

	public InvokeOptions setTransactionWaitTime(int transactionWaitTime) {
		this.transactionWaitTime = transactionWaitTime;
		
		return this;
	}

	public Collection<Orderer> getOrderers() {
		return orderers;
	}

	public InvokeOptions setOrderers(Collection<Orderer> orderers) {
		this.orderers = orderers;
		
		return this;
	}

	public TransactionOptions getOptions() {
		return options;
	}

	public InvokeOptions setOptions(TransactionOptions options) {
		this.options = options;
		
		return this;
	}
}
