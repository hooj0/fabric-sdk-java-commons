package io.github.hooj0.fabric.sdk.commons.core.execution.result;

import java.util.Collection;

import org.hyperledger.fabric.sdk.ProposalResponse;

/**
 * <b>function:</b>
 * @author hoojo
 * @createDate 2018年7月24日 上午11:09:01
 * @file ResultSet.java
 * @package io.github.hooj0.fabric.sdk.commons.core.execution.result
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class ResultSet {

	private String result;
	
	private String transactionId;
	
	private Collection<ProposalResponse> responses;
	
	public ResultSet(String result) {
		this.result = result;
	}

	public ResultSet(Collection<ProposalResponse> responses) {
		this.responses = responses;
	}

	public String getResult() {
		return result;
	}

	public ResultSet setResult(String result) {
		this.result = result;
		
		return this;
	}

	public Collection<ProposalResponse> getResponses() {
		return responses;
	}

	public ResultSet setResponses(Collection<ProposalResponse> responses) {
		this.responses = responses;
		
		return this;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public ResultSet setTransactionId(String transactionId) {
		this.transactionId = transactionId;
		return this;
	}
}
