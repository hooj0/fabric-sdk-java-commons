package io.github.hooj0.fabric.sdk.commons.core;

import java.util.LinkedHashMap;
import java.util.concurrent.CompletableFuture;

import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;

import io.github.hooj0.fabric.sdk.commons.core.execution.ChaincodeBasicExecution;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.FuncOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.InvokeOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.QueryOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;

/**
 * fabric chaincode transaction `invoke & query` operations
 * @changelog Add extends `ChaincodeBasicOperations` interface
 * @author hoojo
 * @createDate 2018年7月23日 下午2:49:13
 * @file Session.java
 * @package io.github.hooj0.fabric.sdk.commons.core
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface ChaincodeTransactionOperations extends ChaincodeBasicOperations, ChaincodeBasicExecution {

	// invoke
	
	ResultSet invoke(InvokeOptions options, FuncOptions funcOptions);

	ResultSet invoke(InvokeOptions options, String func);
	
	ResultSet invoke(InvokeOptions options, String func, Object... args);

	ResultSet invoke(InvokeOptions options, String func, LinkedHashMap<String, Object> args);
	
	// invoke async
	
	CompletableFuture<TransactionEvent> invokeAsync(InvokeOptions options, String func);
	
	CompletableFuture<TransactionEvent> invokeAsync(InvokeOptions options, String func, Object... args);

	CompletableFuture<TransactionEvent> invokeAsync(InvokeOptions options, String func, LinkedHashMap<String, Object> args);
	
	// invoke async return event
	
	TransactionEvent invokeFor(InvokeOptions options, String func);
	
	TransactionEvent invokeFor(InvokeOptions options, String func, Object... args);

	TransactionEvent invokeFor(InvokeOptions options, String func, LinkedHashMap<String, Object> args);
	
	
	
	// query
	
	String query(QueryOptions options, FuncOptions funcOptions);

	String query(QueryOptions options, String func);
	
	String query(QueryOptions options, String func, Object... args);

	String query(QueryOptions options, String func, LinkedHashMap<String, Object> args);
	
	// query async return event
	
	ResultSet queryFor(QueryOptions options, String func);
	
	ResultSet queryFor(QueryOptions options, String func, Object... args);

	ResultSet queryFor(QueryOptions options, String func, LinkedHashMap<String, Object> args);
		
}
