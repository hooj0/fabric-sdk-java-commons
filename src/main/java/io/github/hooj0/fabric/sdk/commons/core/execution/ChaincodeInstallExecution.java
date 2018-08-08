package io.github.hooj0.fabric.sdk.commons.core.execution;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;

import org.hyperledger.fabric.sdk.ProposalResponse;

import io.github.hooj0.fabric.sdk.commons.core.execution.option.InstallOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;

/**
 * chaincode deploy operation install execution interface
 * @author hoojo
 * @createDate 2018年7月24日 下午3:30:49
 * @file ChaincodeInstallExecution.java
 * @package io.github.hooj0.fabric.sdk.commons.core.execution
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface ChaincodeInstallExecution extends ChaincodeBasicExecution {

	Collection<ProposalResponse> execute(InstallOptions options, String chaincodeSourceLocation);

	Collection<ProposalResponse> execute(InstallOptions options, File chaincodeSourceFile);
	
	Collection<ProposalResponse> execute(InstallOptions options, InputStream chaincodeInputStream);
	
	ResultSet executeFor(InstallOptions options, String chaincodeSourceLocation);

	ResultSet executeFor(InstallOptions options, File chaincodeSourceFile);
	
	ResultSet executeFor(InstallOptions options, InputStream chaincodeInputStream);
}
