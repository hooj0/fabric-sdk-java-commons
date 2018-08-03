package io.github.hooj0.fabric.sdk.commons;

/**
 * fabric `channel & user` manager custom exception
 * @author hoojo
 * @createDate 2018年7月21日 下午10:27:53
 * @file FabricManagerException.java
 * @package io.github.hooj0.fabric.sdk.commons
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class FabricManagerException extends FabricRootException {

	private static final long serialVersionUID = 1L;

	public FabricManagerException(Throwable cause, String message, Object... args) {
		super(String.format(message, args), cause);
	}

	public FabricManagerException(String message, Object... args) {
		super(String.format(message, args));
	}

	public FabricManagerException(Throwable cause) {
		super(cause);
	}
	
	public FabricManagerException() {
		super();
	}
}
