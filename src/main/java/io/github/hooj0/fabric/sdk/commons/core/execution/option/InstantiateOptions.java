package io.github.hooj0.fabric.sdk.commons.core.execution.option;

import java.io.File;
import java.io.InputStream;

import org.hyperledger.fabric.sdk.ChaincodeCollectionConfiguration;
import org.hyperledger.fabric.sdk.ChaincodeEndorsementPolicy;

/**
 * chaincode deploy operation instantiate options
 * @author hoojo
 * @createDate 2018年7月24日 下午4:04:09
 * @file InstantiateOptions.java
 * @package io.github.hooj0.fabric.sdk.commons.core
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class InstantiateOptions extends TransactionsOptions {

	/** 背书策略文件：yaml/yml/json/其他 */
	private File endorsementPolicyFile;
	/** 背书策略文件流 */
	private InputStream endorsementPolicyInputStream;
	/** 背书策略 */
	private ChaincodeEndorsementPolicy endorsementPolicy;
	/** 链码集合配置 */
	private ChaincodeCollectionConfiguration collectionConfiguration;
	
	public File getEndorsementPolicyFile() {
		return endorsementPolicyFile;
	}

	public InstantiateOptions setEndorsementPolicyFile(File endorsementPolicyFile) {
		this.endorsementPolicyFile = endorsementPolicyFile;
		
		return this;
	}

	public InputStream getEndorsementPolicyInputStream() {
		return endorsementPolicyInputStream;
	}

	public InstantiateOptions setEndorsementPolicyInputStream(InputStream endorsementPolicyInputStream) {
		this.endorsementPolicyInputStream = endorsementPolicyInputStream;
		
		return this;
	}

	public ChaincodeEndorsementPolicy getEndorsementPolicy() {
		return endorsementPolicy;
	}

	public InstantiateOptions setEndorsementPolicy(ChaincodeEndorsementPolicy endorsementPolicy) {
		this.endorsementPolicy = endorsementPolicy;
		
		return this;
	}

	public ChaincodeCollectionConfiguration getCollectionConfiguration() {
		return collectionConfiguration;
	}

	public InstantiateOptions setCollectionConfiguration(ChaincodeCollectionConfiguration collectionConfiguration) {
		this.collectionConfiguration = collectionConfiguration;
		
		return this;
	}
}
