package io.github.hooj0.fabric.sdk.commons;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 扩展类，提供基础方法
 * @author hoojo
 * @createDate 2018年6月12日 下午3:12:36
 * @file AbstractFabricObject.java
 * @package com.cnblogs.hoojo.fabric.sdk.common
 * @project fabric-sdk-examples
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public abstract class AbstractObject extends FabricLogging {

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
