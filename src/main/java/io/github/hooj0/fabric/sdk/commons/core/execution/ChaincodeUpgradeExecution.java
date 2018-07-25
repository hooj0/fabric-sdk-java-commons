package io.github.hooj0.fabric.sdk.commons.core.execution;

import java.util.concurrent.CompletableFuture;

import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;

import io.github.hooj0.fabric.sdk.commons.core.execution.option.FuncOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.UpgradeOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;

/**
 * chaincode deploy operation upgrade execution interface
 * @author hoojo
 * @createDate 2018年7月24日 下午4:56:58
 * @file ChaincodeUpgradeExecution.java
 * @package io.github.hooj0.fabric.sdk.commons.core.execution
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface ChaincodeUpgradeExecution extends ChaincodeExecution<ResultSet, UpgradeOptions, FuncOptions>, TransactionExecution<CompletableFuture<TransactionEvent>, UpgradeOptions, FuncOptions> {
}
