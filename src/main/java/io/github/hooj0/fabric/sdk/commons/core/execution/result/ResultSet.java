package io.github.hooj0.fabric.sdk.commons.core.execution.result;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;

import lombok.ToString;

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
@ToString
public class ResultSet {

	private String result;
	
	private String transactionId;
	
	private Collection<ProposalResponse> responses;
	
	private CompletableFuture<TransactionEvent> future;
	
	private TransactionEvent transactionEvent;
	
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

	public CompletableFuture<TransactionEvent> getFuture() {
		return future;
	}

	public ResultSet setFuture(CompletableFuture<TransactionEvent> future) {
		this.future = future;
		return this;
	}

	public TransactionEvent getTransactionEvent() {
		return transactionEvent;
	}

	public ResultSet setTransactionEvent(TransactionEvent transactionEvent) {
		this.transactionEvent = transactionEvent;
		return this;
	}
}
