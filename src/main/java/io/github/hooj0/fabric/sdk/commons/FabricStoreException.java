package io.github.hooj0.fabric.sdk.commons;

/**
 * fabric store 异常
 * @author hoojo
 * @createDate 2018年7月21日 下午10:27:53
 * @file FabricStoreException.java
 * @package io.github.hooj0.fabric.sdk.commons
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class FabricStoreException extends FabricRootException {

	private static final long serialVersionUID = 1L;

	public FabricStoreException() {
		super();
	}

	public FabricStoreException(Throwable cause, String message, Object... args) {
		super(String.format(message, args), cause);
	}

	public FabricStoreException(String message, Object... args) {
		super(String.format(message, args));
	}

	public FabricStoreException(Throwable cause) {
		super(cause);
	}
}
