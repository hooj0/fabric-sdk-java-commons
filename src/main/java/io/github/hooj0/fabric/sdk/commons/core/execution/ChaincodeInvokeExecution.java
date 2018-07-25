package io.github.hooj0.fabric.sdk.commons.core.execution;

import java.util.concurrent.CompletableFuture;

import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;

import io.github.hooj0.fabric.sdk.commons.core.execution.option.FuncOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.InvokeOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;

/**
 * chaincode transaction invoke operation execution interface
 * @author hoojo
 * @createDate 2018年7月24日 上午10:22:55
 * @file ChaincodeQueryExecution.java
 * @package io.github.hooj0.fabric.sdk.commons.core.execution
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface ChaincodeInvokeExecution extends ChaincodeExecution<ResultSet, InvokeOptions, FuncOptions>, TransactionExecution<CompletableFuture<TransactionEvent>, InvokeOptions, FuncOptions> {
}
