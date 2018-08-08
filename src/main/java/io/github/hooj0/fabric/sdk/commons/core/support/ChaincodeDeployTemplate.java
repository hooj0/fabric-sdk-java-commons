package io.github.hooj0.fabric.sdk.commons.core.support;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.concurrent.CompletableFuture;

import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.ProposalResponse;

import io.github.hooj0.fabric.sdk.commons.config.FabricConfiguration;
import io.github.hooj0.fabric.sdk.commons.core.ChaincodeDeployOperations;
import io.github.hooj0.fabric.sdk.commons.core.execution.ChaincodeInstallExecution;
import io.github.hooj0.fabric.sdk.commons.core.execution.ChaincodeInstantiateExecution;
import io.github.hooj0.fabric.sdk.commons.core.execution.ChaincodeUpgradeExecution;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.FuncOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.InstallOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.InstantiateOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.UpgradeOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;
import io.github.hooj0.fabric.sdk.commons.core.execution.support.ChaincodeInstallExecutionSupport;
import io.github.hooj0.fabric.sdk.commons.core.execution.support.ChaincodeInstantiateExecutionSupport;
import io.github.hooj0.fabric.sdk.commons.core.execution.support.ChaincodeUpgradeExecutionSupport;
import io.github.hooj0.fabric.sdk.commons.store.FabricKeyValueStore;

/**
 * chaincode deploy operation `install & instantiate & upgrade` interface support
 * @changelog Add `File & FabricKeyValueStore` constructor method support
 * @author hoojo
 * @createDate 2018年7月25日 下午6:26:01
 * @file ChaincodeDeployTemplate.java
 * @package io.github.hooj0.fabric.sdk.commons.core.support
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class ChaincodeDeployTemplate extends AbstractOperationSupport implements ChaincodeDeployOperations {

	private ChaincodeInstallExecution installExecution;
	private ChaincodeInstantiateExecution instantiateExecution;
	private ChaincodeUpgradeExecution upgradeExecution;
	
	public ChaincodeDeployTemplate(String channelName, String orgName) {
		super(channelName, orgName, ChaincodeDeployTemplate.class);
		this.init();
	}

	public ChaincodeDeployTemplate(String channelName, String orgName, FabricConfiguration config) {
		super(channelName, orgName, config, ChaincodeDeployTemplate.class);
		this.init();
	}

	public ChaincodeDeployTemplate(String channelName, String orgName, FabricConfiguration config, File storeFile) {
		super(channelName, orgName, config, storeFile, ChaincodeDeployTemplate.class);
		this.init();
	}

	public ChaincodeDeployTemplate(String channelName, String orgName, FabricConfiguration config, FabricKeyValueStore store) {
		super(channelName, orgName, config, store, ChaincodeDeployTemplate.class);
		this.init();
	}
	
	private void init() {
		installExecution = new ChaincodeInstallExecutionSupport(client, channel);
		instantiateExecution = new ChaincodeInstantiateExecutionSupport(client, channel);
		upgradeExecution = new ChaincodeUpgradeExecutionSupport(client, channel);
	}
	
	@Override
	public Collection<ProposalResponse> install(InstallOptions options, String chaincodeSourceLocation) {
		super.afterOptionSet(options);
		
		return installExecution.execute(options, chaincodeSourceLocation);
	}

	@Override
	public Collection<ProposalResponse> install(InstallOptions options, File chaincodeSourceFile) {
		super.afterOptionSet(options);
		
		return installExecution.execute(options, chaincodeSourceFile);
	}

	@Override
	public Collection<ProposalResponse> install(InstallOptions options, InputStream chaincodeInputStream) {
		super.afterOptionSet(options);
		
		return installExecution.execute(options, chaincodeInputStream);
	}
	
	@Override
	public ResultSet installFor(InstallOptions options, String chaincodeSourceLocation) {
		super.afterOptionSet(options);
		
		return installExecution.executeFor(options, chaincodeSourceLocation);
	}

	@Override
	public ResultSet installFor(InstallOptions options, File chaincodeSourceFile) {
		super.afterOptionSet(options);
		
		return installExecution.executeFor(options, chaincodeSourceFile);
	}

	@Override
	public ResultSet installFor(InstallOptions options, InputStream chaincodeInputStream) {
		super.afterOptionSet(options);
		
		return installExecution.executeFor(options, chaincodeInputStream);
	}

	@Override
	public ResultSet instantiate(InstantiateOptions options, FuncOptions funcOptions) {
		super.afterOptionSet(options);
		
		return instantiateExecution.execute(options, funcOptions);
	}

	@Override
	public ResultSet instantiate(InstantiateOptions options, String func) {
		super.afterOptionSet(options);
		
		return instantiateExecution.execute(options, func);
	}

	@Override
	public ResultSet instantiate(InstantiateOptions options, String func, Object... args) {
		super.afterOptionSet(options);
		
		return instantiateExecution.execute(options, func, args);
	}

	@Override
	public ResultSet instantiate(InstantiateOptions options, String func, LinkedHashMap<String, Object> args) {
		super.afterOptionSet(options);
		
		return instantiateExecution.execute(options, func, args);
	}

	@Override
	public CompletableFuture<TransactionEvent> instantiateAsync(InstantiateOptions options, String func) {
		super.afterOptionSet(options);
		
		return instantiateExecution.executeAsync(options, func);
	}

	@Override
	public CompletableFuture<TransactionEvent> instantiateAsync(InstantiateOptions options, String func, Object... args) {
		super.afterOptionSet(options);
		
		return instantiateExecution.executeAsync(options, func, args);
	}

	@Override
	public CompletableFuture<TransactionEvent> instantiateAsync(InstantiateOptions options, String func, LinkedHashMap<String, Object> args) {
		super.afterOptionSet(options);
		
		return instantiateExecution.executeAsync(options, func, args);
	}

	@Override
	public TransactionEvent instantiateFor(InstantiateOptions options, String func) {
		super.afterOptionSet(options);
		
		return instantiateExecution.executeFor(options, func);
	}

	@Override
	public TransactionEvent instantiateFor(InstantiateOptions options, String func, Object... args) {
		super.afterOptionSet(options);
		
		return instantiateExecution.executeFor(options, func, args);
	}

	@Override
	public TransactionEvent instantiateFor(InstantiateOptions options, String func, LinkedHashMap<String, Object> args) {
		super.afterOptionSet(options);
		
		return instantiateExecution.executeFor(options, func, args);
	}

	@Override
	public ResultSet upgrade(UpgradeOptions options, FuncOptions funcOptions) {
		super.afterOptionSet(options);
		
		return upgradeExecution.execute(options, funcOptions);
	}

	@Override
	public ResultSet upgrade(UpgradeOptions options, String func) {
		super.afterOptionSet(options);
		
		return upgradeExecution.execute(options, func);
	}

	@Override
	public ResultSet upgrade(UpgradeOptions options, String func, Object... args) {
		super.afterOptionSet(options);
		
		return upgradeExecution.execute(options, func, args);
	}

	@Override
	public ResultSet upgrade(UpgradeOptions options, String func, LinkedHashMap<String, Object> args) {
		super.afterOptionSet(options);
		
		return upgradeExecution.execute(options, func, args);
	}

	@Override
	public CompletableFuture<TransactionEvent> upgradeAsync(UpgradeOptions options, String func) {
		super.afterOptionSet(options);
		
		return upgradeExecution.executeAsync(options, func);
	}

	@Override
	public CompletableFuture<TransactionEvent> upgradeAsync(UpgradeOptions options, String func, Object... args) {
		super.afterOptionSet(options);
		
		return upgradeExecution.executeAsync(options, func, args);
	}

	@Override
	public CompletableFuture<TransactionEvent> upgradeAsync(UpgradeOptions options, String func, LinkedHashMap<String, Object> args) {
		super.afterOptionSet(options);
		
		return upgradeExecution.executeAsync(options, func, args);
	}

	@Override
	public TransactionEvent upgradeFor(UpgradeOptions options, String func) {
		super.afterOptionSet(options);
		
		return upgradeExecution.executeFor(options, func);
	}

	@Override
	public TransactionEvent upgradeFor(UpgradeOptions options, String func, Object... args) {
		super.afterOptionSet(options);
		
		return upgradeExecution.executeFor(options, func, args);
	}

	@Override
	public TransactionEvent upgradeFor(UpgradeOptions options, String func, LinkedHashMap<String, Object> args) {
		super.afterOptionSet(options);
		
		return upgradeExecution.executeFor(options, func, args);
	}
}
