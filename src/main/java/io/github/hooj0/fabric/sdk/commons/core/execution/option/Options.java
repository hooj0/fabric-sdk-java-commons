package io.github.hooj0.fabric.sdk.commons.core.execution.option;

import java.util.Collection;
import java.util.Map;

import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.TransactionRequest.Type;
import org.hyperledger.fabric.sdk.User;

import com.google.common.collect.ImmutableMap;

/**
 * Chaincode Operation execution Options
 * @changlog updated `proposalWaitTime` type to long
 * @author hoojo
 * @createDate 2018年7月23日 下午3:00:27
 * @file Options.java
 * @package io.github.hooj0.fabric.sdk.commons.core
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class Options {

	/** Chaincode 名称 */
	private String chancodeName;
	/** Chaincode 路径 */
	private String chaincodePath;
	/** Chaincode 版本 */
	private String chanicodeVersion;
	/** Chaincode 类型 */
	private Type chaincodeType;
	
	/** 向特定的Peer节点发送请求 */
	private boolean specificPeers;
	/** 向集合中的peer发送请求 */
	private Collection<Peer> send2Peers;
	
	/** 请求等待时间 */
	private long proposalWaitTime;
	/** 账本瞬时数据 */
	private Map<String, byte[]> transientData;
	
	/** 客户端User上下文 */
	private User clientUserContext;
	
	public Options setChancodeName(String chancodeName) {
		this.chancodeName = chancodeName;
		return this;
	}
	
	public Options setChaincodePath(String chaincodePath) {
		this.chaincodePath = chaincodePath;
		return this;
	}
	
	public Options setChanicodeVersion(String chanicodeVersion) {
		this.chanicodeVersion = chanicodeVersion;
		return this;
	}
	
	public Options setChaincodeType(Type chaincodeType) {
		this.chaincodeType = chaincodeType;
		return this;
	}

	public Options setSpecificPeers(boolean specificPeers) {
		this.specificPeers = specificPeers;
		return this;
	}
	
	public Options setProposalWaitTime(long proposalWaitTime) {
		this.proposalWaitTime = proposalWaitTime;
		return this;
	}
	
	public Options setTransientData(Map<String, byte[]> transientData) {
        this.transientData = transientData == null ? null : ImmutableMap.copyOf(transientData);
        return this;
    }
	
	public Options setSend2Peers(Collection<Peer> send2Peers) {
		this.send2Peers = send2Peers;
		
		return this;
	}

	public long getProposalWaitTime() {
		return proposalWaitTime;
	}

	public Map<String, byte[]> getTransientData() {
		return transientData;
	}

	public String getChancodeName() {
		return chancodeName;
	}

	public String getChaincodePath() {
		return chaincodePath;
	}

	public String getChanicodeVersion() {
		return chanicodeVersion;
	}

	public Type getChaincodeType() {
		return chaincodeType;
	}

	public boolean isSpecificPeers() {
		return specificPeers;
	}
	
	public ChaincodeID getChaincodeId() {
		return ChaincodeID.newBuilder().setName(chancodeName).setPath(chaincodePath).setVersion(chanicodeVersion).build();
	}

	public Collection<Peer> getSend2Peers() {
		return send2Peers;
	}

	public User getClientUserContext() {
		return clientUserContext;
	}

	public Options setClientUserContext(User clientUserContext) {
		this.clientUserContext = clientUserContext;
		return this;
	}
}
