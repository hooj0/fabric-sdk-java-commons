package io.github.hooj0.fabric.sdk.commons.core.support;

import java.util.concurrent.CompletableFuture;

import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.junit.Before;
import org.junit.Test;

import io.github.hooj0.fabric.sdk.commons.config.support.FabricPropertiesConfiguration;
import io.github.hooj0.fabric.sdk.commons.core.ChaincodeTransactionOperations;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.InvokeOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.QueryOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;
import io.github.hooj0.fabric.sdk.commons.store.support.MemoryKeyValueStore;

/**
 * chaincode transaction template 'query & invoke' test unit
 * @author hoojo
 * @createDate 2018年7月29日 上午11:16:27
 * @file ChaincodeTransactionTemplateTest.java
 * @package io.github.hooj0.fabric.sdk.commons.core.support
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class ChaincodeTransactionTemplateTest extends BasedTemplateTest {

	private ChaincodeTransactionOperations operations;
	
	@Before
	public void setup() {
		//operations = new ChaincodeTransactionTemplate(foo, org1, FabricPropertiesConfiguration.getInstance()); 
		
		//operations = new ChaincodeTransactionTemplate(foo, org1, FabricPropertiesConfiguration.getInstance(), new File("my-kv-store.properties")); 
		
		operations = new ChaincodeTransactionTemplate(foo, org1, FabricPropertiesConfiguration.getInstance(), new MemoryKeyValueStore()); 
	}
	
	@Test
	public void testInvokeTransactionTemplate() {
		
		InvokeOptions options = new InvokeOptions();
		options.setChaincodePath(CHAIN_CODE_PATH).setChaincodeType(CHAIN_CODE_LANG).setChaincodeVersion(CHAIN_CODE_VERSION_11).setChaincodeName(CHAIN_CODE_NAME);
		
		options.setClientUserContext(operations.getOrganization().getUser("user1"));
		
		ResultSet rs = operations.invoke(options, "move", "a", "b", 30);
	}
	
	@Test
	public void testInvokeTransactionTemplate2() {
		
		InvokeOptions options = new InvokeOptions();
		options.setChaincodePath(CHAIN_CODE_PATH).setChaincodeType(CHAIN_CODE_LANG).setChaincodeVersion(CHAIN_CODE_VERSION_11).setChaincodeName(CHAIN_CODE_NAME);
		
		options.setClientUserContext(operations.getOrganization().getUser("user1"));
		
		CompletableFuture<TransactionEvent> futrue = operations.invokeAsync(options, "move", "a", "b", 30);
	}
	
	@Test
	public void testInvokeTransactionTemplate3() {
		
		InvokeOptions options = new InvokeOptions();
		options.setChaincodePath(CHAIN_CODE_PATH).setChaincodeType(CHAIN_CODE_LANG).setChaincodeVersion(CHAIN_CODE_VERSION_11).setChaincodeName(CHAIN_CODE_NAME);
		
		options.setClientUserContext(operations.getOrganization().getUser("user1"));
		
		TransactionEvent event = operations.invokeFor(options, "move", "a", "b", 30);
	}
	
	@Test
	public void testQueryTransactionTemplate() {
		
		QueryOptions options = new QueryOptions();
		options.setChaincodePath(CHAIN_CODE_PATH).setChaincodeType(CHAIN_CODE_LANG).setChaincodeVersion(CHAIN_CODE_VERSION_11).setChaincodeName(CHAIN_CODE_NAME);
		
		options.setClientUserContext(operations.getOrganization().getUser("user1"));
		// options.setRequestUser(requestUser);
		
		String rs = operations.query(options, "a");
	}
	
	@Test
	public void testQueryTransactionTemplate2() {
		
		QueryOptions options = new QueryOptions();
		options.setChaincodePath(CHAIN_CODE_PATH).setChaincodeType(CHAIN_CODE_LANG).setChaincodeVersion(CHAIN_CODE_VERSION_11).setChaincodeName(CHAIN_CODE_NAME);
		
		options.setClientUserContext(operations.getOrganization().getUser("user1"));
		// options.setRequestUser(requestUser);
		
		ResultSet rs = operations.queryFor(options, "a");
	}
}
