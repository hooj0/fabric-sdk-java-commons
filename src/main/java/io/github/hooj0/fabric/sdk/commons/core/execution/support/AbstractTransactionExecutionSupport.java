package io.github.hooj0.fabric.sdk.commons.core.execution.support;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.ProposalResponse;

import io.github.hooj0.fabric.sdk.commons.FabricChaincodeTransactionException;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.FuncOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.TransactionsOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;

/**
 * abstract base transaction execution support
 * @author hoojo
 * @createDate 2018年7月24日 下午5:30:41
 * @file AbstractTransactionExecutionSupport.java
 * @package io.github.hooj0.fabric.sdk.commons.core.execution.support
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public abstract class AbstractTransactionExecutionSupport<T extends TransactionsOptions, S extends FuncOptions> extends AbstractChaincodeExecutionSupport {

	public AbstractTransactionExecutionSupport(HFClient client, Channel channel, Class<?> clazz) {
		super(client, channel, clazz);
	}
	
	public abstract ResultSet execute(T options, S funcOptions);
	
	public CompletableFuture<TransactionEvent> executeAsync(T options, S funcOptions) {
		Collection<ProposalResponse> responses = this.execute(options, funcOptions).getResponses();
		
		CompletableFuture<TransactionEvent> future = null;
		if (options.getOrderers() != null && options.getTransactionsUser() != null) {
			future = channel.sendTransaction(responses, options.getOrderers(), options.getTransactionsUser());
		} else if (options.getOptions() != null) {
			future = channel.sendTransaction(responses, options.getOptions());
		} else if (options.getTransactionsUser() != null) {
			future = channel.sendTransaction(responses, options.getTransactionsUser());
		} else if (options.getOrderers() != null) {
			future = channel.sendTransaction(responses, options.getOrderers());
		} else {
			future = channel.sendTransaction(responses);
		}
		
		return future;
	}
	
	/**
	 * 执行交易初始化动作，返回交易事件
	 * @author hoojo
	 * @createDate 2018年6月15日 上午11:54:46
	 */
	public TransactionEvent executeFor(T options, S funcOptions) {
		TransactionEvent transactionEvent = null;
		
		try {
			transactionEvent = executeAsync(options, funcOptions).get(options.getTransactionWaitTime(), TimeUnit.SECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			logger.error(e.getMessage(), e);
			throw new FabricChaincodeTransactionException(e, "chaincode transaction fail exception");
		}
		
		// 必须是有效交易事件
		checkArgument(transactionEvent.isValid(), "没有签名的交易事件");
		// 必须有签名
		checkNotNull(transactionEvent.getSignature(), "没有签名的交易事件");
		// 必须有交易区块事件发生
		checkNotNull(transactionEvent.getBlockEvent(), "交易事件的区块事件对象为空");
		
		return transactionEvent;
	}
}
