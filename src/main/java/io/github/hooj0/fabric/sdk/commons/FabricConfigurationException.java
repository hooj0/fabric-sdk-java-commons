package io.github.hooj0.fabric.sdk.commons;

/**
 * fabric configuration exception
 * @author hoojo
 * @createDate 2018年7月21日 下午10:27:53
 * @file FabricConfigurationException.java
 * @package io.github.hooj0.fabric.sdk.commons
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class FabricConfigurationException extends FabricRootException {

	private static final long serialVersionUID = 1L;

	public FabricConfigurationException() {
		super();
	}

	public FabricConfigurationException(Throwable cause) {
		super(cause);
	}
	
	public FabricConfigurationException(String message, Object... args) {
		super(String.format(message, args));
	}
	
	public FabricConfigurationException(Throwable cause, String message, Object... args) {
		super(String.format(message, args), cause);
	}
}
