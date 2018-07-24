package io.github.hooj0.fabric.sdk.commons.core.execution.support;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.ProposalResponse;

import io.github.hooj0.fabric.sdk.commons.core.execution.option.InvokeOptions;
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
public abstract class AbstractTransactionExecutionSupport<T extends InvokeOptions> extends AbstractChaincodeExecutionSupport {

	public AbstractTransactionExecutionSupport(HFClient client, Channel channel, Class<?> clazz) {
		super(client, channel, clazz);
	}
	
	public abstract ResultSet execute(T options);
	
	public CompletableFuture<TransactionEvent> executeAsync(T options) {
		Collection<ProposalResponse> responses = this.execute(options).getResponses();
		
		CompletableFuture<TransactionEvent> future = null;
		if (options.getOrderers() != null && options.getContextUser() != null) {
			future = channel.sendTransaction(responses, options.getOrderers(), options.getContextUser());
		} else if (options.getOptions() != null) {
			future = channel.sendTransaction(responses, options.getOptions());
		} else if (options.getContextUser() != null) {
			future = channel.sendTransaction(responses, options.getContextUser());
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
	public TransactionEvent executeFor(T options) throws Exception {
		return executeAsync(options).get(options.getTransactionWaitTime(), TimeUnit.SECONDS);
	}
}
