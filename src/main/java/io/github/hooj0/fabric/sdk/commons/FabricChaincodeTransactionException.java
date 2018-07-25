package io.github.hooj0.fabric.sdk.commons;

/**
 * chaincode transaction execution exception
 * @author hoojo
 * @createDate 2018年7月24日 下午2:21:42
 * @file FabricChaincodeQueryException.java
 * @package io.github.hooj0.fabric.sdk.commons
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class FabricChaincodeTransactionException extends FabricRootException {
	private static final long serialVersionUID = 1L;

	public FabricChaincodeTransactionException() {
		super();
	}

	public FabricChaincodeTransactionException(Throwable cause) {
		super(cause);
	}
	
	public FabricChaincodeTransactionException(String message, Object... args) {
		super(String.format(message, args));
	}
	
	public FabricChaincodeTransactionException(Throwable cause, String message, Object... args) {
		super(String.format(message, args), cause);
	}
}
