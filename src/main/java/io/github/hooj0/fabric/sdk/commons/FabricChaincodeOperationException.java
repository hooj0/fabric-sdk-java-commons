package io.github.hooj0.fabric.sdk.commons;

/**
 * chaincode operation exception
 * @author hoojo
 * @createDate 2018年7月24日 下午2:21:42
 * @file FabricChaincodeOperationException.java
 * @package io.github.hooj0.fabric.sdk.commons
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class FabricChaincodeOperationException extends FabricRootException {
	private static final long serialVersionUID = 1L;

	public FabricChaincodeOperationException() {
		super();
	}

	public FabricChaincodeOperationException(Throwable cause) {
		super(cause);
	}
	
	public FabricChaincodeOperationException(String message, Object... args) {
		super(String.format(message, args));
	}
	
	public FabricChaincodeOperationException(Throwable cause, String message, Object... args) {
		super(String.format(message, args), cause);
	}
}
