package io.github.hooj0.fabric.sdk.commons.core.support;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.hyperledger.fabric.sdk.ChaincodeCollectionConfiguration;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.CollectionConfigPackage;
import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.TransactionRequest.Type;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;

import io.github.hooj0.fabric.sdk.commons.config.support.FabricPropertiesConfiguration;
import io.github.hooj0.fabric.sdk.commons.core.ChaincodeDeployOperations;
import io.github.hooj0.fabric.sdk.commons.core.ChaincodeTransactionOperations;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.InstallOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.InstantiateOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.InvokeOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.QueryOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;

/**
 * chaincode private data test
 * @author hoojo
 * @createDate 2018年12月18日 上午10:24:19
 * @file ChaincodePrivateDataTest.java
 * @package io.github.hooj0.fabric.sdk.commons.core.support
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class ChaincodePrivateDataTest extends BasedTemplateTest {

	private static final String CHAIN_CODE_LOCATION = "gocc/samplePrivateData";
	
	private ChaincodeDeployOperations operations;
	private ChaincodeTransactionOperations transactionOperations;
	
	{
		CHAIN_CODE_LANG = Type.GO_LANG;
	    CHAIN_CODE_NAME = "private_data_cc1_go";
	    CHAIN_CODE_PATH = "github.com/private_data_cc";
	    
	    chaincodeID_11 = ChaincodeID.newBuilder().setName(CHAIN_CODE_NAME).setPath(CHAIN_CODE_PATH).setVersion(CHAIN_CODE_VERSION).build();
	}
	
	@Before
	public void setup() {
		operations = new ChaincodeDeployTemplate(foo, org1, FabricPropertiesConfiguration.getInstance()); 
		transactionOperations = new ChaincodeTransactionTemplate(foo, org1, FabricPropertiesConfiguration.getInstance()); 
	}
	
	@Test
	public void testInstall() throws Exception {
		
		InstallOptions options = new InstallOptions();
		options.setChaincodeId(chaincodeID_11).setChaincodeType(CHAIN_CODE_LANG);
		
		operations.install(options, Paths.get(operations.getConfig().getCommonRootPath(), CHAIN_CODE_LOCATION).toFile());
	}
	
	@Test
	public void testInstantiate() throws Exception {
		
		InstantiateOptions options = new InstantiateOptions();
		options.setChaincodeId(chaincodeID_11).setChaincodeType(CHAIN_CODE_LANG);
		options.setEndorsementPolicyFile(Paths.get(operations.getConfig().getEndorsementPolicyFilePath()).toFile());
		
		File file = Paths.get(operations.getConfig().getCommonRootPath(), "collection-configs/PrivateData.yaml").toFile();
		ChaincodeCollectionConfiguration collectionConfiguration = ChaincodeCollectionConfiguration.fromYamlFile(file);
		options.setCollectionConfiguration(collectionConfiguration);
		
		ResultSet rs = operations.instantiate(options, "init");
		System.out.println("-------->>>>>>>" + rs);
	}
	
	@Test
	public void testInvokeSet() {
		
		InvokeOptions options = new InvokeOptions();
		options.setChaincodeId(chaincodeID_11).setChaincodeType(CHAIN_CODE_LANG);
		options.setClientUserContext(operations.getOrganization().getUser("user1"));
		
		Map<String, byte[]> transientMap = Maps.newHashMap();
		transientMap.put("A", "a".getBytes(UTF_8));   // test using bytes as args. End2end uses Strings.
        transientMap.put("AVal", "500".getBytes(UTF_8));
        transientMap.put("B", "b".getBytes(UTF_8));
        transientMap.put("BVal", String.valueOf(200 + "50").getBytes(UTF_8));
		options.setTransientData(transientMap);
		
		ResultSet rs = transactionOperations.invoke(options, "set");
		System.out.println(rs);
		
		try {
			TransactionEvent event = transactionOperations.getChannel().sendTransaction(rs.getResponses()).get();
			System.out.println(event.isValid());
			System.out.println(event.getBlockEvent());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testQuery() {
		
		QueryOptions options = new QueryOptions();
		options.setChaincodeId(chaincodeID_11).setChaincodeType(CHAIN_CODE_LANG);
		
		Map<String, byte[]> transientData = new HashMap<>();
		transientData.put("B", "b".getBytes(UTF_8)); // test using bytes as args. End2end uses Strings.
        options.setTransientData(transientData);
		
		ResultSet rs = transactionOperations.queryFor(options, "query");
		System.out.println(rs);
	}
	
	@Test
	public void testInvokeMove() {
		
		InvokeOptions options = new InvokeOptions();
		options.setChaincodeId(chaincodeID_11).setChaincodeType(CHAIN_CODE_LANG);
		options.setClientUserContext(operations.getOrganization().getUser("user1"));
		
		Map<String, byte[]> transientMap = Maps.newHashMap();
		transientMap.put("A", "a".getBytes(UTF_8)); //test using bytes .. end2end uses Strings.
        transientMap.put("B", "b".getBytes(UTF_8));
		transientMap.put("moveAmount", "5".getBytes(UTF_8));
		options.setTransientData(transientMap);
		
		ResultSet rs = transactionOperations.invoke(options, "move");
		System.out.println(rs);
		
		try {
			TransactionEvent event = transactionOperations.getChannel().sendTransaction(rs.getResponses()).get();
			System.out.println(event.isValid());
			System.out.println(event.getBlockEvent());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCollectionData() throws Exception {
		if (operations.getConfig().isFabricVersionAtOrAfter("1.3")) {
			Channel channel = operations.getChannel();

			Set<String> expect = new HashSet<>(Arrays.asList("COLLECTION_FOR_A", "COLLECTION_FOR_B"));
			Set<String> got = new HashSet<>();

			CollectionConfigPackage packages = channel.queryCollectionsConfig(CHAIN_CODE_NAME, channel.getPeers().iterator().next(), operations.getConfig().getOrganization(org1).getPeerAdmin());
			for (CollectionConfigPackage.CollectionConfig collectionConfig : packages.getCollectionConfigs()) {
				System.out.println(collectionConfig.getName());
				got.add(collectionConfig.getName());

			}
			assertEquals(expect, got);
		}
	}
}
