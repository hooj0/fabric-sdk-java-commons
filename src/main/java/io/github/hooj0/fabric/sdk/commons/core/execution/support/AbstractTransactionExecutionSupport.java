package io.github.hooj0.fabric.sdk.commons.core.execution.support;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.ProposalResponse;

import io.github.hooj0.fabric.sdk.commons.FabricChaincodeTransactionException;
import io.github.hooj0.fabric.sdk.commons.core.execution.TransactionExecution;
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
public abstract class AbstractTransactionExecutionSupport<T extends TransactionsOptions> extends AbstractChaincodeExecutionSupport<ResultSet, T> implements TransactionExecution<CompletableFuture<TransactionEvent>, T, FuncOptions> {

	public AbstractTransactionExecutionSupport(HFClient client, Channel channel, Class<?> clazz) {
		super(client, channel, clazz);
	}
	
	/**
	 * 发起交易，准备交易，还未发送到交易提议到peer节点
	 * @author hoojo
	 * @createDate 2018年8月3日 上午11:24:27
	 * @param options TransactionsOptions
	 * @param funcOptions FuncOptions
	 * @return ResultSet
	 */
	protected abstract ResultSet prepareTransaction(T options, FuncOptions funcOptions);
	
	protected ResultSet executeTransaction(T options, FuncOptions funcOptions) {
		ResultSet resultSet = this.prepareTransaction(options, funcOptions);
		Collection<ProposalResponse> responses = resultSet.getResponses();
		
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
		resultSet.setFuture(future);
		
		return resultSet;
	}
	
	public ResultSet execute(T options, FuncOptions funcOptions) {
		TransactionEvent transactionEvent = null;
		
		ResultSet resultSet = executeTransaction(options, funcOptions);
		try {
			transactionEvent = resultSet.getFuture().get(options.getTransactionWaitTime(), TimeUnit.SECONDS);
			
			resultSet.setTransactionEvent(transactionEvent);
			resultSet.setTransactionId(transactionEvent.getTransactionID());
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
		
		return resultSet;
	}

	@Override
	public CompletableFuture<TransactionEvent> executeAsync(T options, String func) {
		return this.executeAsync(options, new FuncOptions(func));
	}

	@Override
	public CompletableFuture<TransactionEvent> executeAsync(T options, String func, Object... args) {
		return this.executeAsync(options, new FuncOptions(func, args));
	}

	@Override
	public CompletableFuture<TransactionEvent> executeAsync(T options, String func, Map<String, Object> args) {
		return this.executeAsync(options, new FuncOptions(func, args));
	}

	@Override
	public CompletableFuture<TransactionEvent> executeAsync(T options, FuncOptions funcOptions) {
		return this.executeTransaction(options, funcOptions).getFuture();
	}

	@Override
	public TransactionEvent executeFor(T options, String func) {
		return this.executeFor(options, new FuncOptions(func));
	}

	@Override
	public TransactionEvent executeFor(T options, String func, Object... args) {
		return this.executeFor(options, new FuncOptions(func, args));
	}

	@Override
	public TransactionEvent executeFor(T options, String func, Map<String, Object> args) {
		return this.executeFor(options, new FuncOptions(func, args));
	}

	@Override
	public TransactionEvent executeFor(T options, FuncOptions funcOptions) {
		return this.execute(options, funcOptions).getTransactionEvent();
	}
}
