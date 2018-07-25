package io.github.hooj0.fabric.sdk.commons.core.execution;

import java.util.Map;

import io.github.hooj0.fabric.sdk.commons.core.execution.option.FuncOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.QueryOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;

/**
 * chaincode transaction query execution interface
 * @author hoojo
 * @createDate 2018年7月24日 上午10:22:55
 * @file ChaincodeQueryExecution.java
 * @package io.github.hooj0.fabric.sdk.commons.core.execution
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface ChaincodeQueryExecution extends ChaincodeExecution<String, QueryOptions, FuncOptions> {

    ResultSet executeFor(QueryOptions options, String func);

    ResultSet executeFor(QueryOptions options, String func, Object... args);

    ResultSet executeFor(QueryOptions options, String func, Map<String, Object> args);

    ResultSet executeFor(QueryOptions options, FuncOptions funcOptions);
}
