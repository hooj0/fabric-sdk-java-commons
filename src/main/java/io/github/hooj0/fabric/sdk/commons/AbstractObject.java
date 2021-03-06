package io.github.hooj0.fabric.sdk.commons;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import io.github.hooj0.fabric.sdk.commons.log.FabricLogging;

/**
 * 扩展类，提供基础方法
 * @changelog Add empty parameter constructor
 * @author hoojo
 * @createDate 2018年6月12日 下午3:12:36
 * @file AbstractFabricObject.java
 * @package com.cnblogs.hoojo.fabric.sdk.commons
 * @project fabric-sdk-examples
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public abstract class AbstractObject extends FabricLogging {

	public AbstractObject() {
		super(null);
	}
	
	public AbstractObject(Class<?> clazz) {
		super(clazz);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
