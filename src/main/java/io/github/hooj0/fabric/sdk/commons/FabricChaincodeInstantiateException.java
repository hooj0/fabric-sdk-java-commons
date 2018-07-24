package io.github.hooj0.fabric.sdk.commons;

/**
 * chaincode instantiate execution exception
 * @author hoojo
 * @createDate 2018年7月24日 下午2:21:42
 * @file FabricChaincodeInstantiateException.java
 * @package io.github.hooj0.fabric.sdk.commons
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class FabricChaincodeInstantiateException extends FabricRootException {
	private static final long serialVersionUID = 1L;

	public FabricChaincodeInstantiateException() {
		super();
	}

	public FabricChaincodeInstantiateException(Throwable cause) {
		super(cause);
	}
	
	public FabricChaincodeInstantiateException(String message, Object... args) {
		super(String.format(message, args));
	}
	
	public FabricChaincodeInstantiateException(Throwable cause, String message, Object... args) {
		super(String.format(message, args), cause);
	}
}
