package io.github.hooj0.fabric.sdk.commons;

/**
 * chaincode invoke execution exception
 * @author hoojo
 * @createDate 2018年7月24日 下午2:21:42
 * @file FabricChaincodeInvokeException.java
 * @package io.github.hooj0.fabric.sdk.commons
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class FabricChaincodeInstallException extends FabricRootException {
	private static final long serialVersionUID = 1L;

	public FabricChaincodeInstallException() {
		super();
	}

	public FabricChaincodeInstallException(Throwable cause) {
		super(cause);
	}
	
	public FabricChaincodeInstallException(String message, Object... args) {
		super(String.format(message, args));
	}
	
	public FabricChaincodeInstallException(Throwable cause, String message, Object... args) {
		super(String.format(message, args), cause);
	}
}
