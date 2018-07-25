package io.github.hooj0.fabric.sdk.commons.core.execution;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;

import io.github.hooj0.fabric.sdk.commons.core.execution.option.Options;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.FuncOptions;

/**
 * chaincode transaction execution base interface
 * @author hoojo
 * @createDate 2018年7月24日 下午4:13:58
 * @file TransactionExecution.java
 * @package io.github.hooj0.fabric.sdk.commons.core.execution
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface TransactionExecution<T extends CompletableFuture<TransactionEvent>, P extends Options, S extends FuncOptions> {

    T executeAsync(P options, String func);

    T executeAsync(P options, String func, Object... args);

    T executeAsync(P options, String func, Map<String, Object> args);

    T executeAsync(P options, S funcOptions);
    
    TransactionEvent executeFor(P options, String func);

    TransactionEvent executeFor(P options, String func, Object... args);

    TransactionEvent executeFor(P options, String func, Map<String, Object> args);

    TransactionEvent executeFor(P options, S funcOptions);
}
