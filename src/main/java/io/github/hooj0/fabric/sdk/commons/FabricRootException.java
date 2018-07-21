package io.github.hooj0.fabric.sdk.commons;

/**
 * fabric 根异常
 * @author hoojo
 * @createDate 2018年7月21日 下午10:27:53
 * @file FabricRootException.java
 * @package io.github.hooj0.fabric.sdk.commons
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class FabricRootException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FabricRootException() {
		super();
	}

	public FabricRootException(Throwable cause, String message, Object... args) {
		super(String.format(message, args), cause);
	}

	public FabricRootException(String message, Object... args) {
		super(String.format(message, args));
	}

	public FabricRootException(Throwable cause) {
		super(cause);
	}
}
