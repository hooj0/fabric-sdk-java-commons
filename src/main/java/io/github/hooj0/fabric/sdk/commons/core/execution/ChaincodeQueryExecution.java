package io.github.hooj0.fabric.sdk.commons.core.execution;

import java.util.Map;

import io.github.hooj0.fabric.sdk.commons.core.execution.option.Options;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.SimpleOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;

/**
 * <b>function:</b>
 * @author hoojo
 * @createDate 2018年7月24日 上午10:22:55
 * @file ChaincodeQueryExecution.java
 * @package io.github.hooj0.fabric.sdk.commons.core.execution
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface ChaincodeQueryExecution extends ChaincodeExecution<String, Options, SimpleOptions> {

    ResultSet executeFor(Options options, String func);

    ResultSet executeFor(Options options, String func, Object... args);

    ResultSet executeFor(Options options, String func, Map<String, Object> args);

    ResultSet executeFor(SimpleOptions options);
}
