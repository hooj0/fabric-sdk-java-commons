package io.github.hooj0.fabric.sdk.commons.core.execution.support;

import java.util.LinkedHashMap;

import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;

import io.github.hooj0.fabric.sdk.commons.core.execution.ChaincodeExecution;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.FuncOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.Options;

/**
 * abstract chaincode execution common method support 
 * @author hoojo
 * @createDate 2018年8月3日 上午9:54:55
 * @file AbstractChaincodeExecutionSupport.java
 * @package io.github.hooj0.fabric.sdk.commons.core.execution.support
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public abstract class AbstractChaincodeExecutionSupport<T, P extends Options> extends ChaincodeBasicExecutionSupport implements ChaincodeExecution<T, P, FuncOptions> {

	public AbstractChaincodeExecutionSupport(HFClient client, Channel channel, Class<?> clazz) {
		super(clazz);
		
		super.setChannel(channel);
		super.setClient(client);
	}

	@Override
	public T execute(P options, String func) {
		return this.execute(options, new FuncOptions(func));
	}

	@Override
	public T execute(P options, String func, Object... args) {
		return this.execute(options, new FuncOptions(func, args));
	}

	@Override
	public T execute(P options, String func, LinkedHashMap<String, Object> args) {
		return this.execute(options, new FuncOptions(func, args));
	}
}
