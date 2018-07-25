package io.github.hooj0.fabric.sdk.commons.core.execution;

import java.util.concurrent.CompletableFuture;

import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;

import io.github.hooj0.fabric.sdk.commons.core.execution.option.FuncOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.InstantiateOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;

/**
 * chaincode operation deploy instantiate execution interface
 * @author hoojo
 * @createDate 2018年7月24日 下午4:01:45
 * @file ChaincodeInstantiateExecution.java
 * @package io.github.hooj0.fabric.sdk.commons.core.execution
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface ChaincodeInstantiateExecution extends ChaincodeExecution<ResultSet, InstantiateOptions, FuncOptions>, TransactionExecution<CompletableFuture<TransactionEvent>, InstantiateOptions, FuncOptions> {

}
