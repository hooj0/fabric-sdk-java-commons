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
 * chaincode deploy operation `install & instantiate & upgrade` interface support
 * @author hoojo
 * @createDate 2018年7月25日 下午6:26:01
 * @file ChaincodeDeployTemplate.java
 * @package io.github.hooj0.fabric.sdk.commons.core
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class ChaincodeDeployTemplate implements ChaincodeDeployOperations {

	@Override
	public Collection<ProposalResponse> install(InstallOptions options, String chaincodeSourceLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<ProposalResponse> install(InstallOptions options, File chaincodeSourceFile) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<ProposalResponse> install(InstallOptions options, InputStream chaincodeInputStream) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet instantiate(InstantiateOptions options, FuncOptions funcOptions) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet instantiate(InstantiateOptions options, String func) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet instantiate(InstantiateOptions options, String func, Object... args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet instantiate(InstantiateOptions options, String func, LinkedHashMap<String, Object> args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CompletableFuture<TransactionEvent> instantiateAsync(InstantiateOptions options, String func) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CompletableFuture<TransactionEvent> instantiateAsync(InstantiateOptions options, String func,
			Object... args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CompletableFuture<TransactionEvent> instantiateAsync(InstantiateOptions options, String func,
			LinkedHashMap<String, Object> args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransactionEvent instantiateFor(InstantiateOptions options, String func) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransactionEvent instantiateFor(InstantiateOptions options, String func, Object... args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransactionEvent instantiateFor(InstantiateOptions options, String func,
			LinkedHashMap<String, Object> args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet upgrade(UpgradeOptions options, FuncOptions funcOptions) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet upgrade(UpgradeOptions options, String func) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet upgrade(UpgradeOptions options, String func, Object... args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet upgrade(UpgradeOptions options, String func, LinkedHashMap<String, Object> args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CompletableFuture<TransactionEvent> upgradeAsync(UpgradeOptions options, String func) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CompletableFuture<TransactionEvent> upgradeAsync(UpgradeOptions options, String func, Object... args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CompletableFuture<TransactionEvent> upgradeAsync(UpgradeOptions options, String func,
			LinkedHashMap<String, Object> args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransactionEvent upgradeFor(UpgradeOptions options, String func) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransactionEvent upgradeFor(UpgradeOptions options, String func, Object... args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransactionEvent upgradeFor(UpgradeOptions options, String func, LinkedHashMap<String, Object> args) {
		// TODO Auto-generated method stub
		return null;
	}

}
