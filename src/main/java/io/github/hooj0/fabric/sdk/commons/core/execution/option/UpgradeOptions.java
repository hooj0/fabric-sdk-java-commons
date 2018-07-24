package io.github.hooj0.fabric.sdk.commons.core.execution.option;

import java.util.Map;

/**
 * <b>function:</b>
 * @author hoojo
 * @createDate 2018年7月24日 下午5:00:43
 * @file UpgradeOptions.java
 * @package io.github.hooj0.fabric.sdk.commons.core
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class UpgradeOptions extends InstantiateOptions {

	public UpgradeOptions(String func) {
		super(func);
	}
	
	public UpgradeOptions(String func, Object... values) {
		super(func, values);
	}

	public UpgradeOptions(String func, Map<String, Object> values) {
		super(func, values);
	}
}
