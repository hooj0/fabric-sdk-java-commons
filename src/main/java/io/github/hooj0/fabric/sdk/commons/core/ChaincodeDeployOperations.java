package io.github.hooj0.fabric.sdk.commons.core;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.concurrent.CompletableFuture;

import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.ProposalResponse;

import io.github.hooj0.fabric.sdk.commons.core.execution.option.FuncOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.InstallOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.InstantiateOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.UpgradeOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;

/**
 * chaincode deploy operation `install & instantiate & upgrade` interface
 * @author hoojo
 * @createDate 2018年7月24日 下午5:46:02
 * @file ChaincodeDeployOperations.java
 * @package io.github.hooj0.fabric.sdk.commons.core
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface ChaincodeDeployOperations {

	// install
	
	Collection<ProposalResponse> install(InstallOptions options, String chaincodeSourceLocation);

	Collection<ProposalResponse> install(InstallOptions options, File chaincodeSourceFile);
	
	Collection<ProposalResponse> install(InstallOptions options, InputStream chaincodeInputStream);
	
	// instantiate
	
	ResultSet instantiate(InstantiateOptions options, FuncOptions funcOptions);

	ResultSet instantiate(InstantiateOptions options, String func);
	
	ResultSet instantiate(InstantiateOptions options, String func, Object... args);

	ResultSet instantiate(InstantiateOptions options, String func, LinkedHashMap<String, Object> args);
	
	// instantiate async
	
	CompletableFuture<TransactionEvent> instantiateAsync(InstantiateOptions options, String func);
	
	CompletableFuture<TransactionEvent> instantiateAsync(InstantiateOptions options, String func, Object... args);

	CompletableFuture<TransactionEvent> instantiateAsync(InstantiateOptions options, String func, LinkedHashMap<String, Object> args);
	
	// instantiate async return event
	
	TransactionEvent instantiateFor(InstantiateOptions options, String func);
	
	TransactionEvent instantiateFor(InstantiateOptions options, String func, Object... args);

	TransactionEvent instantiateFor(InstantiateOptions options, String func, LinkedHashMap<String, Object> args);
	
	
	
	// upgrade
	
	ResultSet upgrade(UpgradeOptions options, FuncOptions funcOptions);

	ResultSet upgrade(UpgradeOptions options, String func);
	
	ResultSet upgrade(UpgradeOptions options, String func, Object... args);

	ResultSet upgrade(UpgradeOptions options, String func, LinkedHashMap<String, Object> args);
	
	// upgrade async
	
	CompletableFuture<TransactionEvent> upgradeAsync(UpgradeOptions options, String func);
	
	CompletableFuture<TransactionEvent> upgradeAsync(UpgradeOptions options, String func, Object... args);

	CompletableFuture<TransactionEvent> upgradeAsync(UpgradeOptions options, String func, LinkedHashMap<String, Object> args);
	
	// upgrade async return event
	
	TransactionEvent upgradeFor(UpgradeOptions options, String func);
	
	TransactionEvent upgradeFor(UpgradeOptions options, String func, Object... args);

	TransactionEvent upgradeFor(UpgradeOptions options, String func, LinkedHashMap<String, Object> args);
}
