package io.github.hooj0.fabric.sdk.commons.core.support;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static org.junit.Assert.fail;

import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.StringUtils;
import org.hyperledger.fabric.sdk.BlockEvent;
import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.exception.TransactionEventException;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;

import io.github.hooj0.fabric.sdk.commons.config.support.FabricPropertiesConfiguration;
import io.github.hooj0.fabric.sdk.commons.core.ChaincodeDeployOperations;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.InstallOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.InstantiateOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.UpgradeOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;

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
		operations = new ChaincodeDeployTemplate(foo, org1, FabricPropertiesConfiguration.getInstance()); 
		
		//operations = new ChaincodeDeployTemplate(foo, org1, FabricPropertiesConfiguration.getInstance(), new File("my-kv-store.properties")); 
		//operations = new ChaincodeDeployTemplate(foo, org1, FabricPropertiesConfiguration.getInstance(), new MemoryKeyValueStore()); 
	}
	
	@Test
	public void testInstallDeployTemplate() {
		
		InstallOptions options = new InstallOptions();
		options.setChaincodeId(chaincodeID_11).setChaincodeType(CHAIN_CODE_LANG);
		
		String chaincodeSourceFile = operations.getConfig().getChaincodeRootPath();
		operations.install(options, chaincodeSourceFile);
	}
	
	@Test
	public void testInstantiateDeployTemplate() {
		
		InstantiateOptions options = new InstantiateOptions();
		options.setChaincodeId(chaincodeID_11).setChaincodeType(CHAIN_CODE_LANG);
		options.setEndorsementPolicyFile(Paths.get(operations.getConfig().getEndorsementPolicyFilePath()).toFile());
		
		ResultSet rs = operations.instantiate(options, "init", "a", 200, "b", 300);
		System.out.println("-------->>>>>>>" + rs);
	}

	@Test
	public void testInstantiate2DeployTemplate() {
		
		InstantiateOptions options = new InstantiateOptions();
		
		options.setChaincodeId(chaincodeID_11).setChaincodeType(CHAIN_CODE_LANG);
		options.setEndorsementPolicyFile(Paths.get(operations.getConfig().getEndorsementPolicyFilePath()).toFile());
		
		LinkedHashMap<String, Object> args = Maps.newLinkedHashMap();
		args.put("a", 300);
		args.put("b", 500);
		
		ResultSet rs = operations.instantiate(options, "init", args);
		System.out.println("-------->>>>>>>" + rs);
	}
	
	@Test
	public void testInstantiate3DeployTemplate() throws Exception {
		
		InstantiateOptions options = new InstantiateOptions();
		
		options.setChaincodeId(chaincodeID_11).setChaincodeType(CHAIN_CODE_LANG);
		options.setEndorsementPolicyFile(Paths.get(operations.getConfig().getEndorsementPolicyFilePath()).toFile());
		
		LinkedHashMap<String, Object> args = Maps.newLinkedHashMap();
		args.put("a", 300);
		args.put("b", 500);
		
		CompletableFuture<TransactionEvent> future = operations.instantiateAsync(options, "init", args);
		future.thenApply((BlockEvent.TransactionEvent transactionEvent) -> {
			
			// 必须是有效交易事件
			checkArgument(transactionEvent.isValid(), "没有签名的交易事件");
			// 必须有签名
			checkNotNull(transactionEvent.getSignature(), "没有签名的交易事件");
			// 必须有交易区块事件发生
			BlockEvent blockEvent = checkNotNull(transactionEvent.getBlockEvent(), "交易事件的区块事件对象为空");
			
			try {
				System.out.println("成功实例化Chaincode，本次实例化交易ID：" +  transactionEvent.getTransactionID());
				System.out.println(transactionEvent.getChannelId());
				checkArgument(StringUtils.equals(blockEvent.getChannelId(), operations.getChannel().getName()), "事件名称和对应通道名称不一致");

				// 检查
				if (!operations.checkInstantiatedChaincode(options.getChaincodeId())) {
					throw new AssertionError("chaincode 1 没有实例化");
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			return "success";
		}).exceptionally(e -> {
			if (e instanceof CompletionException && e.getCause() != null) {
				e = e.getCause();
			}
			
			if (e instanceof TransactionEventException) {
				BlockEvent.TransactionEvent te = ((TransactionEventException) e).getTransactionEvent();
				if (te != null) {
					e.printStackTrace(System.err);
					fail(format("Transaction with txid %s failed. %s", te.getTransactionID(), e.getMessage()));
				}
			}
			
			e.printStackTrace(System.err);
			fail(format("Test failed with %s exception %s", e.getClass().getName(), e.getMessage()));
			
			return null;
		}).get(operations.getConfig().getTransactionWaitTime(), TimeUnit.SECONDS); 
	}
	
	@Test
	public void testInstantiate4DeployTemplate() {
		
		InstantiateOptions options = new InstantiateOptions();
		
		options.setChaincodeId(chaincodeID_11).setChaincodeType(CHAIN_CODE_LANG);
		options.setEndorsementPolicyFile(Paths.get(operations.getConfig().getEndorsementPolicyFilePath()).toFile());
		
		LinkedHashMap<String, Object> args = Maps.newLinkedHashMap();
		args.put("a", 300);
		args.put("b", 500);
		
		TransactionEvent event = operations.instantiateFor(options, "init", args);
		System.out.println("-------------->>>>>>>" + event);
	}
	
	@Test
	public void testCheckInstantiate3DeployTemplate() throws Exception {
		System.out.println(operations.checkInstantiatedChaincode(chaincodeID_11));
		System.out.println(operations.checkInstantiatedChaincode(chaincodeID_1));
	}
	
	
	@Test
	public void testInstantiateUser() throws Exception {
		operations.instantiate("hoojo3");
	}
	
	@Test
	public void testUpgradeDeployTemplate() throws Exception {
		
		if (!operations.checkChaincode(chaincodeID_11, operations.getOrganization())) {
			fail(chaincodeID_11 + " not install | instantiate.");
		}
		
		if (!operations.checkInstallChaincode(chaincodeID_11_2)) {
			InstallOptions options = new InstallOptions();
			options.setChaincodeId(chaincodeID_11).setChaincodeType(CHAIN_CODE_LANG);
			options.setChaincodeUpgradeVersion("11.2");
			
			operations.install(options, operations.getConfig().getChaincodeRootPath());
		}
		
		if (operations.checkInstallChaincode(chaincodeID_11_2) && !operations.checkInstantiatedChaincode(chaincodeID_11_2)) {
			UpgradeOptions upgradeOptions = new UpgradeOptions();
			
			upgradeOptions.setChaincodeId(chaincodeID_11_2).setChaincodeType(CHAIN_CODE_LANG);
			upgradeOptions.setEndorsementPolicyFile(Paths.get(operations.getConfig().getEndorsementPolicyFilePath()).toFile());
			
			ResultSet rs = operations.upgrade(upgradeOptions, "init");
			System.out.println("-------------->>>>>>>" + rs);
		}
	}
	
	@Test
	public void testUpgrade2DeployTemplate() throws Exception {
		
		UpgradeOptions options = new UpgradeOptions();
		
		options.setChaincodeId(chaincodeID_11).setChaincodeType(CHAIN_CODE_LANG).setChaincodeVersion("11.2");
		options.setEndorsementPolicyFile(Paths.get(operations.getConfig().getEndorsementPolicyFilePath()).toFile());
		
		InstallOptions installOptions = new InstallOptions().setChaincodeUpgradeVersion("11.2");
		installOptions.setChaincodeId(options.getChaincodeId()).setChaincodeType(CHAIN_CODE_LANG);
		
		String chaincodeSourceFile = operations.getConfig().getChaincodeRootPath();
		if (!operations.checkInstallChaincode(installOptions.getChaincodeId())) {
			operations.install(installOptions, chaincodeSourceFile);
		}

		CompletableFuture<TransactionEvent> future = operations.upgradeAsync(options, "init", "a", "100", "b", "100");
		Object result = future.thenApply((TransactionEvent event) -> {
			
			System.out.println(event.isValid());
			System.out.println(event.getBlockEvent());
			System.out.println(event.getChannelId());
			System.out.println(event.getTimestamp());
			System.out.println(event.getSignature());
			System.out.println(event.getValidationCode());
			System.out.println(event.getType());
			
			return event.getTransactionID();
		}).exceptionally(e -> {
			e.printStackTrace();
			return null;
		}).get(10000, TimeUnit.MILLISECONDS);
		
		System.out.println("-------------->>>>>>>" + result);
	}
	
	@Test
	public void testUpgrade3DeployTemplate() throws Exception {
		
		UpgradeOptions options = new UpgradeOptions();
		
		options.setChaincodeId(chaincodeID_11).setChaincodeType(CHAIN_CODE_LANG).setChaincodeVersion("11.2");
		options.setEndorsementPolicyFile(Paths.get(operations.getConfig().getEndorsementPolicyFilePath()).toFile());
		
		InstallOptions installOptions = new InstallOptions().setChaincodeUpgradeVersion("11.2");
		installOptions.setChaincodeId(options.getChaincodeId()).setChaincodeType(CHAIN_CODE_LANG);
		
		String chaincodeSourceFile = operations.getConfig().getChaincodeRootPath();
		if (!operations.checkInstallChaincode(installOptions.getChaincodeId())) {
			operations.install(installOptions, chaincodeSourceFile);
		}

		TransactionEvent event = operations.upgradeFor(options, "init");
		
		System.out.println("-------------->>>>>>>" + event);
	}
}
