package io.github.hooj0.fabric.sdk.commons.core.support;

import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.TransactionRequest.Type;

/**
 * Based Template Test
 * @author hoojo
 * @createDate 2018年7月29日 上午11:19:52
 * @file BasedTemplateTest.java
 * @package io.github.hooj0.fabric.sdk.commons.core.support
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class BasedTemplateTest {

	final static String foo = "mychannel";
	final static String bar = "mychannel2";
	final static String org1 = "peerOrg1";
	final static String org2 = "peerOrg2";
	
	static String CHAIN_CODE_NAME = "example_cc_go";
	static String CHAIN_CODE_PATH = "github.com/example_cc";
	static String CHAIN_CODE_VERSION = "1";
	static String CHAIN_CODE_VERSION_11 = "11";
	static Type CHAIN_CODE_LANG = Type.GO_LANG;
	
	ChaincodeID chaincodeID_1 = ChaincodeID.newBuilder().setName(CHAIN_CODE_NAME).setPath(CHAIN_CODE_PATH).setVersion(CHAIN_CODE_VERSION).build();
	ChaincodeID chaincodeID_11 = ChaincodeID.newBuilder().setName(CHAIN_CODE_NAME).setPath(CHAIN_CODE_PATH).setVersion(CHAIN_CODE_VERSION_11).build();
}
