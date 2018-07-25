package io.github.hooj0.fabric.sdk.commons.core.execution.option;

import java.util.Collection;

import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.Channel.TransactionOptions;

/**
 * chaincode transaction (instantiate/invoke/query) options
 * 
 * @author hoojo
 * @createDate 2018年7月25日 上午9:48:36
 * @file TransactionsOptions.java
 * @package io.github.hooj0.fabric.sdk.commons.core.execution.option
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class TransactionsOptions extends Options {
	/** 交易等待时间 */
	private int transactionWaitTime;
	/** 发起交易的用户 */
	private User transactionsUser;
	/** 向指定 Orderer发送交易进行排序共识 */
	private Collection<Orderer> orderers;
	/** 交易选项 */
	private TransactionOptions options;

	public User getTransactionsUser() {
		return transactionsUser;
	}

	public TransactionsOptions setTransactionsUser(User transactionsUser) {
		this.transactionsUser = transactionsUser;
		return this;
	}
	
	public int getTransactionWaitTime() {
		return transactionWaitTime;
	}

	public TransactionsOptions setTransactionWaitTime(int transactionWaitTime) {
		this.transactionWaitTime = transactionWaitTime;

		return this;
	}

	public Collection<Orderer> getOrderers() {
		return orderers;
	}

	public TransactionsOptions setOrderers(Collection<Orderer> orderers) {
		this.orderers = orderers;

		return this;
	}

	public TransactionOptions getOptions() {
		return options;
	}

	public TransactionsOptions setOptions(TransactionOptions options) {
		this.options = options;

		return this;
	}
}
