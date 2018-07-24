package io.github.hooj0.fabric.sdk.commons.core.execution;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;

import org.hyperledger.fabric.sdk.ProposalResponse;

import io.github.hooj0.fabric.sdk.commons.core.execution.option.InstallOptions;

/**
 * <b>function:</b>
 * @author hoojo
 * @createDate 2018年7月24日 下午3:30:49
 * @file ChaincodeInstallExecution.java
 * @package io.github.hooj0.fabric.sdk.commons.core.execution
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface ChaincodeInstallExecution {

	Collection<ProposalResponse> execute(InstallOptions options, String chaincodeSourceLocation);

	Collection<ProposalResponse> execute(InstallOptions options, File chaincodeSourceFile);
	
	Collection<ProposalResponse> execute(InstallOptions options, InputStream chaincodeInputStream);
}
