package io.github.hooj0.fabric.sdk.commons.config;

import java.io.File;
import java.util.Collection;
import java.util.Properties;

import io.github.hooj0.fabric.sdk.commons.domain.Organization;
import io.github.hooj0.fabric.sdk.commons.store.FabricKeyValueStore;

/**
 * <b>function:</b>
 * @author hoojo
 * @createDate 2018年7月23日 上午9:22:48
 * @file DefaultFabricConfiguration.java
 * @package io.github.hooj0.fabric.sdk.commons.config
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public enum DefaultFabricConfiguration implements FabricConfiguration {

	INSTANCE;
	
	@Override
	public FabricKeyValueStore getKeyValueStore() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Organization> getOrganizations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Organization getOrganization(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Properties getPeerProperties(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Properties getOrdererProperties(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Properties getEventHubProperties(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Properties getTLSCertProperties(String type, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isRunningFabricTLS() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public File getNetworkConfigFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOrdererOrgsDomain() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTransactionWaitTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDeployWaitTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getProposalWaitTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getFabricConfigGeneratorVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCommonConfigRootPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCryptoTxConfigRootPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getChannelPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getChaincodePath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEndorsementPolicyFilePath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNetworkConfigDirFilePath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFabricConfigTxLaterURL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isRunningAgainstFabric10() {
		// TODO Auto-generated method stub
		return false;
	}
}
