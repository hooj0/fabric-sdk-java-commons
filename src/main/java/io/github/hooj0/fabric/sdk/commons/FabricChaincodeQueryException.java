package io.github.hooj0.fabric.sdk.commons;

/**
 * chaincode query execution exception
 * @author hoojo
 * @createDate 2018年7月24日 下午2:21:42
 * @file FabricChaincodeQueryException.java
 * @package io.github.hooj0.fabric.sdk.commons
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class FabricChaincodeQueryException extends FabricRootException {
	private static final long serialVersionUID = 1L;

	public FabricChaincodeQueryException() {
		super();
	}

	public FabricChaincodeQueryException(Throwable cause) {
		super(cause);
	}
	
	public FabricChaincodeQueryException(String message, Object... args) {
		super(String.format(message, args));
	}
	
	public FabricChaincodeQueryException(Throwable cause, String message, Object... args) {
		super(String.format(message, args), cause);
	}
}
