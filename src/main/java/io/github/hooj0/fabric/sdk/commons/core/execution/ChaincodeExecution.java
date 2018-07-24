package io.github.hooj0.fabric.sdk.commons.core.execution;

import java.util.LinkedHashMap;

import io.github.hooj0.fabric.sdk.commons.core.execution.option.Options;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.SimpleOptions;

/**
 * <b>function:</b>
 * @author hoojo
 * @createDate 2018年7月24日 下午3:04:40
 * @file ChaincodeExecution.java
 * @package io.github.hooj0.fabric.sdk.commons.core.execution
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface ChaincodeExecution<T, P extends Options, S extends SimpleOptions> {

	T execute(P options, String func);

    T execute(P options, String func, Object... args);

    T execute(P options, String func, LinkedHashMap<String, Object> args);

    T execute(S options);
}
