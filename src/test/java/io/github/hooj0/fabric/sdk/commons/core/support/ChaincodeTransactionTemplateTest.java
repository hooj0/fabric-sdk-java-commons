package io.github.hooj0.fabric.sdk.commons.core.support;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static org.junit.Assert.fail;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.StringUtils;
import org.hyperledger.fabric.protos.common.Common.Envelope;
import org.hyperledger.fabric.sdk.BlockEvent;
import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.TransactionInfo;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionEventException;
import org.junit.Before;
import org.junit.Test;

import io.github.hooj0.fabric.sdk.commons.config.support.FabricPropertiesConfiguration;
import io.github.hooj0.fabric.sdk.commons.core.ChaincodeTransactionOperations;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.InvokeOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.QueryOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;

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
		operations = new ChaincodeTransactionTemplate(foo, org1, FabricPropertiesConfiguration.getInstance()); 
		
		//operations = new ChaincodeTransactionTemplate(foo, org1, FabricPropertiesConfiguration.getInstance(), new File("my-kv-store.properties")); 
		
		//operations = new ChaincodeTransactionTemplate(foo, org1, FabricPropertiesConfiguration.getInstance(), new MemoryKeyValueStore()); 
	}
	
	@Test
	public void testInvokeTransactionTemplate() {
		
		InvokeOptions options = new InvokeOptions();
		
		options.setChaincodeId(chaincodeID_11).setChaincodeType(CHAIN_CODE_LANG);
		
		options.setClientUserContext(operations.getOrganization().getUser("user1"));
		
		ResultSet rs = operations.invoke(options, "move", "a", "b", 20);
		System.out.println(rs);
		
		try {
			TransactionEvent event = operations.getChannel().sendTransaction(rs.getResponses()).get();
			System.out.println(event.isValid());
			System.out.println(event.getBlockEvent());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testInvokeTransactionTemplate2() throws InterruptedException, ExecutionException, TimeoutException {
		
		InvokeOptions options = new InvokeOptions();
		options.setChaincodeId(chaincodeID_11).setChaincodeType(CHAIN_CODE_LANG);
		
		options.setClientUserContext(operations.getOrganization().getUser("user1"));
		
		CompletableFuture<TransactionEvent> future = operations.invokeAsync(options, "move", "b", "a", 3);
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
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			QueryOptions options2 = new QueryOptions();
			options2.setChaincodeId(chaincodeID_11).setChaincodeType(CHAIN_CODE_LANG);
			
			options2.setProposalWaitTime(100000);
			options2.setSpecificPeers(false);
			
			//operations.instantiate("hoojo3");
			options2.setClientUserContext(operations.getOrganization().getUser("hoojo5"));
			
			String a = operations.query(options2, "query", "a");
			String b = operations.query(options2, "query", "b");
			
			System.out.println("a mount ---->" + a);
			System.out.println("b mount ---->" + b);
			
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
	public void testInvokeTransactionTemplate3() {
		
		InvokeOptions options = new InvokeOptions();
		options.setChaincodeId(chaincodeID_11).setChaincodeType(CHAIN_CODE_LANG);
		
		options.setClientUserContext(operations.getOrganization().getUser("user1"));
		
		TransactionEvent event = operations.invokeFor(options, "move", "a", "b", 5);
		System.out.println(event.getTransactionID());
		System.out.println(event.getCreator().getId());
	}
	
	@Test
	public void testQueryTransactionTemplate() {
		
		QueryOptions options = new QueryOptions();
		options.setChaincodeId(chaincodeID_11).setChaincodeType(CHAIN_CODE_LANG);
		
		options.setProposalWaitTime(100000);
		options.setSpecificPeers(false);
		
		operations.instantiate("hoojo3");
		
		options.setClientUserContext(operations.getOrganization().getUser("hoojo3"));
		// options.setRequestUser(requestUser);
		
		String rs = operations.query(options, "query", "a");
		System.out.println(rs);
		rs = operations.query(options, "query", "b");
		System.out.println(rs);
	}
	
	@Test
	public void testQueryTransactionTemplate2() {
		
		QueryOptions options = new QueryOptions();
		options.setChaincodeId(chaincodeID_11).setChaincodeType(CHAIN_CODE_LANG);
		
		operations.instantiate("hoojo3");
		options.setClientUserContext(operations.getOrganization().getUser("user1"));
		options.setRequestUser(operations.getOrganization().getUser("hoojo3"));
		
		ResultSet rs = operations.queryFor(options, "query", "a");
		System.out.println(rs);
	}
	
	@Test
	public void testQueryTtransaction() throws ProposalException, InvalidArgumentException {
		TransactionInfo info = operations.getChannel().queryTransactionByID("6957f7f249d95f4393f59481dce88e4d97cd6255cb771880169df2a1beba4a91");
		System.out.println(info.getValidationCode().getNumber());
		
		System.out.println(info.getEnvelope().getAllFields());
		System.out.println(info.getProcessedTransaction().getAllFields());
		
		Envelope envelope = info.getEnvelope();
		
		if (envelope.getPayload().isValidUtf8()) {
			System.out.println(envelope.getPayload().toStringUtf8());
		} else {
			System.out.println(envelope.getPayload().toString());
		}
		
		if (envelope.getSignature().isValidUtf8()) {
			System.out.println(envelope.getSignature().isValidUtf8());
		} else {
			System.out.println(envelope.getSignature().toString());
		}
	}
	
}
