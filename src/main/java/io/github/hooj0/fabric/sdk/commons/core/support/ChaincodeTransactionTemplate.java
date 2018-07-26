package io.github.hooj0.fabric.sdk.commons.core.support;

import java.util.LinkedHashMap;
import java.util.concurrent.CompletableFuture;

import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;

import io.github.hooj0.fabric.sdk.commons.config.FabricConfiguration;
import io.github.hooj0.fabric.sdk.commons.core.ChaincodeTransactionOperations;
import io.github.hooj0.fabric.sdk.commons.core.execution.ChaincodeInvokeExecution;
import io.github.hooj0.fabric.sdk.commons.core.execution.ChaincodeQueryExecution;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.FuncOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.InvokeOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.QueryOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;
import io.github.hooj0.fabric.sdk.commons.core.execution.support.ChaincodeInvokeExecutionSupport;
import io.github.hooj0.fabric.sdk.commons.core.execution.support.ChaincodeQueryExecutionSupport;

/**
 * fabric chaincode transaction `invoke & query` operations interface support
 * @author hoojo
 * @createDate 2018年7月26日 下午4:19:55
 * @file ChaincodeTransactionTemplate.java
 * @package io.github.hooj0.fabric.sdk.commons.core.support
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class ChaincodeTransactionTemplate extends AbstractOperationSupport implements ChaincodeTransactionOperations {

	private ChaincodeInvokeExecution invokeExecution;
	private ChaincodeQueryExecution queryExecution;
	
	public ChaincodeTransactionTemplate(FabricConfiguration config) {
		super(config.getChannelName(), config.getDefaultOrgName(), config, ChaincodeDeployTemplate.class);
		this.init();
	}
	
	public ChaincodeTransactionTemplate(String channelName, String orgName) {
		super(channelName, orgName, ChaincodeDeployTemplate.class);
		this.init();
	}

	public ChaincodeTransactionTemplate(String channelName, String orgName, FabricConfiguration config) {
		super(channelName, orgName, config, ChaincodeDeployTemplate.class);
		this.init();
	}
	
	private void init() {
		invokeExecution = new ChaincodeInvokeExecutionSupport(client, channel);
		queryExecution = new ChaincodeQueryExecutionSupport(client, channel);
	}
	
	@Override
	public ResultSet invoke(InvokeOptions options, FuncOptions funcOptions) {
		afterOptionSet(options);
		
		return invokeExecution.execute(options, funcOptions);
	}

	@Override
	public ResultSet invoke(InvokeOptions options, String func) {
		afterOptionSet(options);
		
		return invokeExecution.execute(options, func);
	}

	@Override
	public ResultSet invoke(InvokeOptions options, String func, Object... args) {
		afterOptionSet(options);
		
		return invokeExecution.execute(options, func, args);
	}

	@Override
	public ResultSet invoke(InvokeOptions options, String func, LinkedHashMap<String, Object> args) {
		afterOptionSet(options);
		
		return invokeExecution.execute(options, func, args);
	}

	@Override
	public CompletableFuture<TransactionEvent> invokeAsync(InvokeOptions options, String func) {
		afterOptionSet(options);
		
		return invokeExecution.executeAsync(options, func);
	}

	@Override
	public CompletableFuture<TransactionEvent> invokeAsync(InvokeOptions options, String func, Object... args) {
		afterOptionSet(options);
		
		return invokeExecution.executeAsync(options, func, args);
	}

	@Override
	public CompletableFuture<TransactionEvent> invokeAsync(InvokeOptions options, String func, LinkedHashMap<String, Object> args) {
		afterOptionSet(options);
		
		return invokeExecution.executeAsync(options, func, args);
	}

	@Override
	public TransactionEvent invokeFor(InvokeOptions options, String func) {
		afterOptionSet(options);
		
		return invokeExecution.executeFor(options, func);
	}

	@Override
	public TransactionEvent invokeFor(InvokeOptions options, String func, Object... args) {
		afterOptionSet(options);
		
		return invokeExecution.executeFor(options, func, args);
	}

	@Override
	public TransactionEvent invokeFor(InvokeOptions options, String func, LinkedHashMap<String, Object> args) {
		afterOptionSet(options);
		
		return invokeExecution.executeFor(options, func, args);
	}

	@Override
	public String query(QueryOptions options, FuncOptions funcOptions) {
		afterOptionSet(options);
		
		return queryExecution.execute(options, funcOptions);
	}

	@Override
	public String query(QueryOptions options, String func) {
		afterOptionSet(options);
		
		return queryExecution.execute(options, func);
	}

	@Override
	public String query(QueryOptions options, String func, Object... args) {
		afterOptionSet(options);
		
		return queryExecution.execute(options, func, args);
	}

	@Override
	public String query(QueryOptions options, String func, LinkedHashMap<String, Object> args) {
		afterOptionSet(options);
		
		return queryExecution.execute(options, func, args);
	}

	@Override
	public ResultSet queryFor(QueryOptions options, String func) {
		afterOptionSet(options);
		
		return queryExecution.executeFor(options, func);
	}

	@Override
	public ResultSet queryFor(QueryOptions options, String func, Object... args) {
		afterOptionSet(options);
		
		return queryExecution.executeFor(options, func, args);
	}

	@Override
	public ResultSet queryFor(QueryOptions options, String func, LinkedHashMap<String, Object> args) {
		afterOptionSet(options);
		
		return queryExecution.executeFor(options, func, args);
	}
}
