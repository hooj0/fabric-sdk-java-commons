package io.github.hooj0.fabric.sdk.commons;

/**
 * fabric store 异常
 * @author hoojo
 * @createDate 2018年7月21日 下午10:27:53
 * @file FabricCreatorException.java
 * @package io.github.hooj0.fabric.sdk.commons
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class FabricCreatorException extends FabricRootException {

	private static final long serialVersionUID = 1L;

	public FabricCreatorException(Throwable cause, String message, Object... args) {
		super(String.format(message, args), cause);
	}

	public FabricCreatorException(String message, Object... args) {
		super(String.format(message, args));
	}

	public FabricCreatorException(Throwable cause) {
		super(cause);
	}
	
	public FabricCreatorException() {
		super();
	}
}
