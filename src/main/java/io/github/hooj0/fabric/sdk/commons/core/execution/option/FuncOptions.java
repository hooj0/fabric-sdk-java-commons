package io.github.hooj0.fabric.sdk.commons.core.execution.option;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

/**
 * chaincode transaction function (instantiate/invoke/query) options
 * @author hoojo
 * @createDate 2018年7月23日 下午4:32:02
 * @file FuncOptions.java
 * @package io.github.hooj0.fabric.sdk.commons.core.execution.option
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class FuncOptions {

	/** 请求执行函数 */
	private String func;

	/** 请求执行的参数 */
	private String[] args;
	
	public FuncOptions(String func) {
		this(func, new Object[] {});
	}

	public FuncOptions(String func, Object... values) {
		if (values != null && values.length > 50) {
			throw new IllegalArgumentException("Too many values, the maximum allowed is 50");
		}

		this.func = func;
		this.args = buildArgs(values);
	}

	public FuncOptions(String func, Map<String, Object> values) {
		if (values.size() > 50) {
			throw new IllegalArgumentException("Too many values, the maximum allowed is 50");
		}

		this.func = func;
		this.args = buildArgs(values);
	}

	private String[] buildArgs(Map<String, Object> values) {
		if (values == null) {
			return new String[] {};
		}
		
		String[] args = new String[values.size() * 2];
		
		int index = 0;
		Set<Entry<String, Object>> sets = values.entrySet();
		Iterator<Entry<String, Object>> iter = sets.iterator();
		while (iter.hasNext()) {
			Entry<String, Object> entry = iter.next();
			args[index++] = entry.getKey();
			args[index++] = Optional.ofNullable(entry.getValue()).orElse("").toString();
		}
		
		return args;
	}
	
	private String[] buildArgs(Object[] values) {
		if (values == null) {
			return new String[] {};
		}
		
		int index = 0;
		String[] args = new String[values.length];
		for (Object value : values) {
			args[index++] = Optional.ofNullable(value).orElse("").toString();
		}
		
		return args;
	}
	
	public String getFunc() {
		return func;
	}
	
	public String[] getArgs() {
		return this.args;
	}
}
