package io.github.hooj0.fabric.sdk.commons.core.support;

import java.io.File;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.concurrent.CompletableFuture;

import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.TransactionRequest.Type;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;

import io.github.hooj0.fabric.sdk.commons.config.support.FabricPropertiesConfiguration;
import io.github.hooj0.fabric.sdk.commons.core.ChaincodeDeployOperations;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.InstallOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.InstantiateOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.UpgradeOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;
import io.github.hooj0.fabric.sdk.commons.store.support.MemoryKeyValueStore;

/**
 * chaincode deploy template 'Install & Instantiate & Upgrade' test units
 * @author hoojo
 * @createDate 2018年7月28日 下午9:58:03
 * @file ChaincodeDeployTemplateTest.java
 * @package io.github.hooj0.fabric.sdk.commons.core.support
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@SuppressWarnings("unused")
public class ChaincodeDeployTemplateTest extends BasedTemplateTest {

	private ChaincodeDeployOperations operations;
	
	@Before
	public void setup() {
		//operations = new ChaincodeDeployTemplate(foo, org1, FabricPropertiesConfiguration.getInstance()); 
		
		//operations = new ChaincodeDeployTemplate(foo, org1, FabricPropertiesConfiguration.getInstance(), new File("my-kv-store.properties")); 
		
		operations = new ChaincodeDeployTemplate(foo, org1, FabricPropertiesConfiguration.getInstance(), new MemoryKeyValueStore()); 
	}
	
	@Test
	public void testInstallDeployTemplate() {
		
		InstallOptions options = new InstallOptions();
		
		options.setChaincodePath(CHAIN_CODE_PATH).setChaincodeType(CHAIN_CODE_LANG).setChaincodeVersion(CHAIN_CODE_VERSION_11).setChaincodeName(CHAIN_CODE_NAME);
		
		String chaincodeSourceFile = operations.getConfig().getChaincodeRootPath();
		Collection<ProposalResponse> responses = operations.install(options, chaincodeSourceFile);
	}
	
	@Test
	public void testInstantiateDeployTemplate() {
		
		InstantiateOptions options = new InstantiateOptions();
		
		options.setChaincodePath(CHAIN_CODE_PATH).setChaincodeType(CHAIN_CODE_LANG).setChaincodeVersion(CHAIN_CODE_VERSION_11).setChaincodeName(CHAIN_CODE_NAME);
		options.setEndorsementPolicyFile(Paths.get(operations.getConfig().getEndorsementPolicyFilePath()).toFile());
		
		ResultSet rs = operations.instantiate(options, "init", "a", 200, "b", 300);
	}

	@Test
	public void testInstantiate2DeployTemplate() {
		
		InstantiateOptions options = new InstantiateOptions();
		
		options.setChaincodePath(CHAIN_CODE_PATH).setChaincodeType(CHAIN_CODE_LANG).setChaincodeVersion(CHAIN_CODE_VERSION_11).setChaincodeName(CHAIN_CODE_NAME);
		options.setEndorsementPolicyFile(Paths.get(operations.getConfig().getEndorsementPolicyFilePath()).toFile());
		
		LinkedHashMap<String, Object> args = Maps.newLinkedHashMap();
		args.put("a", 300);
		args.put("b", 500);
		
		ResultSet rs = operations.instantiate(options, "init", args);
	}
	
	@Test
	public void testInstantiate3DeployTemplate() {
		
		InstantiateOptions options = new InstantiateOptions();
		
		options.setChaincodePath(CHAIN_CODE_PATH).setChaincodeType(CHAIN_CODE_LANG).setChaincodeVersion(CHAIN_CODE_VERSION_11).setChaincodeName(CHAIN_CODE_NAME);
		options.setEndorsementPolicyFile(Paths.get(operations.getConfig().getEndorsementPolicyFilePath()).toFile());
		
		LinkedHashMap<String, Object> args = Maps.newLinkedHashMap();
		args.put("a", 300);
		args.put("b", 500);
		
		CompletableFuture<TransactionEvent> future = operations.instantiateAsync(options, "init", args);
	}
	
	@Test
	public void testInstantiate4DeployTemplate() {
		
		InstantiateOptions options = new InstantiateOptions();
		
		options.setChaincodePath(CHAIN_CODE_PATH).setChaincodeType(CHAIN_CODE_LANG).setChaincodeVersion(CHAIN_CODE_VERSION_11).setChaincodeName(CHAIN_CODE_NAME);
		options.setEndorsementPolicyFile(Paths.get(operations.getConfig().getEndorsementPolicyFilePath()).toFile());
		
		LinkedHashMap<String, Object> args = Maps.newLinkedHashMap();
		args.put("a", 300);
		args.put("b", 500);
		
		TransactionEvent event = operations.instantiateFor(options, "init", args);
	}
	
	@Test
	public void testUpgradeDeployTemplate() {
		
		UpgradeOptions options = new UpgradeOptions();
		
		options.setChaincodePath(CHAIN_CODE_PATH).setChaincodeType(CHAIN_CODE_LANG).setChaincodeVersion(CHAIN_CODE_VERSION_11).setChaincodeName(CHAIN_CODE_NAME);
		options.setEndorsementPolicyFile(Paths.get(operations.getConfig().getEndorsementPolicyFilePath()).toFile());
		
		InstallOptions installOptions = new InstallOptions();
		installOptions.setChaincodeId(options.getChaincodeId());
		
		String chaincodeSourceFile = operations.getConfig().getChaincodeRootPath();
		operations.install(installOptions, chaincodeSourceFile);

		ResultSet rs = operations.upgrade(options, "init");
	}
	
	@Test
	public void testUpgrade2DeployTemplate() {
		
		UpgradeOptions options = new UpgradeOptions();
		
		options.setChaincodePath(CHAIN_CODE_PATH).setChaincodeType(CHAIN_CODE_LANG).setChaincodeVersion(CHAIN_CODE_VERSION_11).setChaincodeName(CHAIN_CODE_NAME);
		options.setEndorsementPolicyFile(Paths.get(operations.getConfig().getEndorsementPolicyFilePath()).toFile());
		
		InstallOptions installOptions = new InstallOptions();
		installOptions.setChaincodeId(options.getChaincodeId());
		
		String chaincodeSourceFile = operations.getConfig().getChaincodeRootPath();
		operations.install(installOptions, chaincodeSourceFile);

		CompletableFuture<TransactionEvent> future = operations.upgradeAsync(options, "init");
	}
	
	@Test
	public void testUpgrade3DeployTemplate() {
		
		UpgradeOptions options = new UpgradeOptions();
		
		options.setChaincodePath(CHAIN_CODE_PATH).setChaincodeType(CHAIN_CODE_LANG).setChaincodeVersion(CHAIN_CODE_VERSION_11).setChaincodeName(CHAIN_CODE_NAME);
		options.setEndorsementPolicyFile(Paths.get(operations.getConfig().getEndorsementPolicyFilePath()).toFile());
		
		InstallOptions installOptions = new InstallOptions();
		installOptions.setChaincodeId(options.getChaincodeId());
		
		String chaincodeSourceFile = operations.getConfig().getChaincodeRootPath();
		operations.install(installOptions, chaincodeSourceFile);

		TransactionEvent event = operations.upgradeFor(options, "init");
	}
}
