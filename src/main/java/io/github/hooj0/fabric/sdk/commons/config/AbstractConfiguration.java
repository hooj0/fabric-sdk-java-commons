package io.github.hooj0.fabric.sdk.commons.config;


import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.hyperledger.fabric.sdk.helper.Utils;

import io.github.hooj0.fabric.sdk.commons.AbstractObject;
import io.github.hooj0.fabric.sdk.commons.FabricConfigurationException;
import io.github.hooj0.fabric.sdk.commons.domain.Organization;

/**
 * <b>function:</b>
 * @author hoojo
 * @createDate 2018年7月23日 上午9:28:04
 * @file AbstractConfiguration.java
 * @package io.github.hooj0.fabric.sdk.commons.config
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public abstract class AbstractConfiguration extends AbstractObject implements FabricConfiguration {

	/** SDK 配置 */
	protected static final Properties SDK_PROPERTIES = new Properties();
	/** ORG 配置 */
	protected static final HashMap<String, Organization> ORGANIZATION_RESOURCES = new HashMap<>();
	
	/** 配置Key前缀 */
	protected static final String PREFIX = "hyperledger.fabric.sdk.commons.";
	
	/** 区块链网络配置key的前缀 */
	protected static final String FABRIC_NETWORK_KEY_PREFIX = PREFIX + "network.org.";
	
	/** 调用合约等待时间 */
	protected static final String INVOKE_WAIT_TIME = PREFIX + "invoke.wait.time";
	/** 部署合约等待时间 */
	protected static final String DEPLOY_WAIT_TIME = PREFIX + "deploy.wait.time";
	/** 发起proposal等待时间 */
	protected static final String PROPOSAL_WAIT_TIME = PREFIX + "proposal.wait.time";
	/** tls */
	protected static final String TLS_PATH = PREFIX + "network.tls.enable";
	/** 匹配到 mspid 值*/
	protected static final Pattern ORG_MSPID_PATTERN = Pattern.compile("^" + Pattern.quote(FABRIC_NETWORK_KEY_PREFIX) + "([^\\.]+)\\.mspid$");
	
	/** 不同版本通道、证书、交易配置 v1.0 and v1.1 src/test/fixture/sdkintegration/e2e-2Orgs */
	protected static final String FABRIC_CONFIG_GENERATOR_VERSION = PREFIX + "tx.config.version"; //"v1.0" : "v1.1";
	
	protected static final String FABRIC_NETWORK_HOST_KEY = PREFIX + "network.host";
	/** fabric network host 区块链网络的主机IP地址 */
	protected static final String FABRIC_NETWORK_HOST = StringUtils.defaultString(System.getenv(FABRIC_NETWORK_HOST_KEY), "192.168.8.8");
	
	/** chaincode 和 组织 、通道、区块配置的根目录 */
	protected static final String COMMON_CONFIG_ROOT_PATH = PREFIX + "config.root.path";
	/** crypto-config & channel-artifacts 根目录 */
	protected static final String CRYPTO_TX_CONFIG_ROOT_PATH = PREFIX + "crypto.txconfig.root.path";
	/** chaincode 源码文件路径 */
	protected static final String CHAINCODE_SOURCE_CODE_FILE_PATH = PREFIX + "chaincode.source.code.file.path";
	/** 通道配置 路径*/
	protected static final String CHANNEL_TRANSACTION_FILE_PATH = PREFIX + "channel.transaction.file.path";
	/** chaincode背书策略文件路径 */
	protected static final String ENDORSEMENT_POLICY_FILE_PATH = PREFIX + "endorsement.policy.file.path";
	/** fabric network  config 配置文件路径 */
	protected static final String NETWORK_CONFIG_DIR_FILE_PATH = PREFIX + "network.config.root.path";
	
	/** 开启TLS证书，也就是https(grpcs)协议和http(grpc)协议之间的切换 */
	protected boolean runningTLS;
	protected boolean runningFabricCATLS;
	protected boolean runningFabricTLS;
	
	public AbstractConfiguration(Class<?> clazz) {
		super(clazz);
	}
	
	protected void configurationDefaultValues() {
		// Default values
		defaultProperty(INVOKE_WAIT_TIME, "120");
		defaultProperty(DEPLOY_WAIT_TIME, "120000");
		defaultProperty(PROPOSAL_WAIT_TIME, "120000");

		// Default network values
		defaultProperty(FABRIC_NETWORK_KEY_PREFIX + "peerOrg1.mspid", "Org1MSP");
		defaultProperty(FABRIC_NETWORK_KEY_PREFIX + "peerOrg1.domname", "org1.example.com");
		defaultProperty(FABRIC_NETWORK_KEY_PREFIX + "peerOrg1.caName", "ca0");
		defaultProperty(FABRIC_NETWORK_KEY_PREFIX + "peerOrg1.ca_location", "http://" + FABRIC_NETWORK_HOST + ":7054");
		defaultProperty(FABRIC_NETWORK_KEY_PREFIX + "peerOrg1.orderer_locations", "orderer.example.com@grpc://" + FABRIC_NETWORK_HOST + ":7050");
		defaultProperty(FABRIC_NETWORK_KEY_PREFIX + "peerOrg1.peer_locations", "peer0.org1.example.com@grpc://" + FABRIC_NETWORK_HOST + ":7051, peer1.org1.example.com@grpc://" + FABRIC_NETWORK_HOST + ":7056");
		defaultProperty(FABRIC_NETWORK_KEY_PREFIX + "peerOrg1.eventhub_locations", "peer0.org1.example.com@grpc://" + FABRIC_NETWORK_HOST + ":7053, peer1.org1.example.com@grpc://" + FABRIC_NETWORK_HOST + ":7058");
		
		defaultProperty(FABRIC_NETWORK_KEY_PREFIX + "peerOrg2.mspid", "Org2MSP");
		defaultProperty(FABRIC_NETWORK_KEY_PREFIX + "peerOrg2.domname", "org2.example.com");
		//defaultProperty(FABRIC_NETWORK_KEY_PREFIX + "peerOrg2.caName", "ca1");
		defaultProperty(FABRIC_NETWORK_KEY_PREFIX + "peerOrg2.ca_location", "http://" + FABRIC_NETWORK_HOST + ":8054");
		defaultProperty(FABRIC_NETWORK_KEY_PREFIX + "peerOrg2.orderer_locations", "orderer.example.com@grpc://" + FABRIC_NETWORK_HOST + ":7050");
		defaultProperty(FABRIC_NETWORK_KEY_PREFIX + "peerOrg2.peer_locations", "peer0.org2.example.com@grpc://" + FABRIC_NETWORK_HOST + ":8051, peer1.org2.example.com@grpc://" + FABRIC_NETWORK_HOST + ":8056");
		defaultProperty(FABRIC_NETWORK_KEY_PREFIX + "peerOrg2.eventhub_locations", "peer0.org2.example.com@grpc://" + FABRIC_NETWORK_HOST + ":8053, peer1.org2.example.com@grpc://" + FABRIC_NETWORK_HOST + ":8058");

		// Default tls values
		defaultProperty(TLS_PATH, null);
		
		logger.debug("SDK Properties：{}", SDK_PROPERTIES);
	}
	
	/**
	 * 默认配置优先读取 系统级别 配置，如果系统环境配置为空，则读取运行变量中的配置
	 */
	protected void defaultProperty(String key, String value) {
		String data = System.getProperty(key);
		logger.trace("读取系统环境配置：{} => {}", key, data);
		
		if (data == null) {
			String envKey = key.toLowerCase().replaceAll("\\.", "_");
			data = System.getenv(envKey);
			
			logger.trace("读取变量环境配置：{} => {}", envKey, data);
			if (data == null) {
				if (null == getSDKProperty(key) && value != null) {
					data = value;
					logger.trace("使用默认配置：{} => {}", key, data);
				}
			}
		}

		if (data != null) {
			SDK_PROPERTIES.put(key, data);
			logger.debug("添加SDK配置: {} => {}", key, data);
		}
	}
	
	/**
	 * 从 SDK 配置文件中读取配置
	 */
	protected String getProperty(String property) {
		String ret = getSDKProperty(property);
		
		if (null == ret) {
			logger.warn("not find property：{}", property);
		}
		return ret;
	}
	
	protected String getSDKProperty(String property, String defaultValue) {
		return SDK_PROPERTIES.getProperty(property, defaultValue);
	}
	
	protected String getSDKProperty(String property) {
		return SDK_PROPERTIES.getProperty(property);
	}
	
	protected void addOrganizationResources() {
		for (Map.Entry<Object, Object> item : SDK_PROPERTIES.entrySet()) {
			final String key = item.getKey() + "";
			final String val = item.getValue() + "";

			if (key.startsWith(FABRIC_NETWORK_KEY_PREFIX)) {
				
				Matcher match = ORG_MSPID_PATTERN.matcher(key);
				if (match.matches() && match.groupCount() == 1) {
					String orgName = match.group(1).trim();
					
					Organization org = new Organization(orgName, val.trim());
					ORGANIZATION_RESOURCES.put(orgName, org);
					
					logger.debug("添加组织: {} => {}", orgName, org);
				}
			}
		}
	}

	/** 添加组织事件总线URL配置 */
	protected void addEventHubLocation(Organization organization, String orgName) {
		String eventHubProps = getSDKProperty(FABRIC_NETWORK_KEY_PREFIX + orgName + ".eventhub_locations");
		String[] eventHubs = eventHubProps.split("[ \t]*,[ \t]*");
		for (String eventHub : eventHubs) {
			String[] key_val = eventHub.split("[ \t]*@[ \t]*");
			organization.addEventHubLocation(key_val[0], grpcTLSify(key_val[1]));
			
			logger.debug("addEventHubLocation：{}->{}", key_val[0], grpcTLSify(key_val[1]));
		}
	}
	
	/** 添加Orderer服务URL配置 */
	protected void addOrdererLocation(Organization organization, String orgName) {
		String ordererProps = getSDKProperty(FABRIC_NETWORK_KEY_PREFIX + orgName + ".orderer_locations");
		String[] orderers = ordererProps.split("[ \t]*,[ \t]*");
		for (String orderer : orderers) {
			String[] key_val = orderer.split("[ \t]*@[ \t]*");
			organization.addOrdererLocation(key_val[0], grpcTLSify(key_val[1]));
			
			logger.debug("addOrdererLocation：{}->{}", key_val[0], grpcTLSify(key_val[1]));
		}
	}
	
	/** 添加Peer节点URL配置 */
	protected void addPeerLocation(Organization organization, String orgName) {
		String peerProps = getSDKProperty(FABRIC_NETWORK_KEY_PREFIX + orgName + ".peer_locations");
		String[] peers = peerProps.split("[ \t]*,[ \t]*");
		for (String peer : peers) {
			String[] key_val = peer.split("[ \t]*@[ \t]*");
			organization.addPeerLocation(key_val[0], grpcTLSify(key_val[1]));
			
			logger.debug("addPeerLocation：{}->{}", key_val[0], grpcTLSify(key_val[1]));
		}
	}
	
	/** 设置 CA 配置 */
	protected void setCAProperties(Organization organization, String orgName) {
		organization.setCALocation(httpTLSify(getSDKProperty((FABRIC_NETWORK_KEY_PREFIX + orgName + ".ca_location"))));
		organization.setCAName(getSDKProperty((FABRIC_NETWORK_KEY_PREFIX + orgName + ".caName")));
		
		if (runningFabricCATLS) {
			String cert = getCryptoTxConfigRootPath() + "/crypto-config/peerOrganizations/DNAME/ca/ca.DNAME-cert.pem";
			cert = cert.replaceAll("DNAME", organization.getDomainName());
			
			File certFile = new File(cert);
			if (!certFile.exists() || !certFile.isFile()) {
				logger.debug("certFile path: {}", certFile.getAbsolutePath());
				throw new FabricConfigurationException("证书文件不存在： " + certFile.getAbsolutePath());
			}
			
			Properties properties = new Properties();
			properties.setProperty("pemFile", certFile.getAbsolutePath());
			properties.setProperty("allowAllHostNames", "true"); // testing environment only NOT FOR PRODUCTION!

			organization.setCAProperties(properties);
		}
		
		logger.debug("ca properties: {}", organization.getCAProperties());
	}
	
	/** GRPC 协议 开启TLS证书 */
	private String grpcTLSify(String location) {
		location = location.trim();
		Exception e = Utils.checkGrpcUrl(location);
		if (e != null) {
			throw new FabricConfigurationException(String.format("Bad TEST parameters for grpc url %s", location), e);
		}

		return runningFabricTLS ? location.replaceFirst("^grpc://", "grpcs://") : location;
	}

	/** HTTP 协议 开启TLS证书 */
	private String httpTLSify(String location) {
		location = location.trim();

		return runningFabricCATLS ? location.replaceFirst("^http://", "https://") : location;
	}
	
	/** 获取全部组织 */
	public Collection<Organization> getOrganizations() {
		return Collections.unmodifiableCollection(ORGANIZATION_RESOURCES.values());
	}

	/** 获取组织 */
	public Organization getOrganization(String name) {
		return ORGANIZATION_RESOURCES.get(name);
	}
	
	/** 节点配置 */
	public Properties getPeerProperties(String name) {
		Properties props = getTLSCertProperties("peer", name);
		
		logger.debug("{} properties: {}", name, props);
		return props;
	}
	
	/** orderer 服务配置 */
	public Properties getOrdererProperties(String name) {
		Properties props = getTLSCertProperties("orderer", name);
		
		logger.debug("{} properties: {}", name, props);
		return props;
	}
	
	/** 事件机制配置 */
	public Properties getEventHubProperties(String name) {
		Properties props = getTLSCertProperties("peer", name); // uses same as named peer
		
		logger.debug("{} properties: {}", name, props);
		return props;
	}
	
	private String getDomainName(final String name) {
		int dot = name.indexOf(".");
		if (-1 == dot) {
			return null;
		} else {
			return name.substring(dot + 1);
		}
	}

	public Properties getTLSCertProperties(final String type, final String name) {
		Properties props = new Properties();

		final String domainName = getDomainName(name);
		final String txconfigRootPath = getCryptoTxConfigRootPath();

		File cert = Paths.get(txconfigRootPath, "crypto-config/ordererOrganizations".replace("orderer", type), domainName, type + "s", name, "tls/server.crt").toFile();
		if (!cert.exists()) {
			throw new FabricConfigurationException("Missing cert file for: %s. Could not find at location: %s", name, cert.getAbsolutePath());
		}

		if (!isRunningAgainstFabric10()) {
			File clientCert;
			File clientKey;
			if ("orderer".equals(type)) {
				clientCert = Paths.get(txconfigRootPath, "crypto-config/ordererOrganizations/example.com/users/Admin@example.com/tls/client.crt").toFile();
				clientKey = Paths.get(txconfigRootPath, "crypto-config/ordererOrganizations/example.com/users/Admin@example.com/tls/client.key").toFile();
			} else {
				clientCert = Paths.get(txconfigRootPath, "crypto-config/peerOrganizations/", domainName, "users/User1@" + domainName, "tls/client.crt").toFile();
				clientKey = Paths.get(txconfigRootPath, "crypto-config/peerOrganizations/", domainName, "users/User1@" + domainName, "tls/client.key").toFile();
			}

			if (!clientCert.exists()) {
				throw new FabricConfigurationException("Missing  client cert file for: %s. Could not find at location: %s", name, clientCert.getAbsolutePath());
			}
			if (!clientKey.exists()) {
				throw new FabricConfigurationException("Missing  client key file for: %s. Could not find at location: %s", name, clientKey.getAbsolutePath());
			}
			
			props.setProperty("clientCertFile", clientCert.getAbsolutePath());
			props.setProperty("clientKeyFile", clientKey.getAbsolutePath());
		}

		props.setProperty("pemFile", cert.getAbsolutePath());
		props.setProperty("hostnameOverride", name);
		props.setProperty("sslProvider", "openSSL");
		props.setProperty("negotiationType", "TLS");
		
		return props;
	}
	
	/** 
	 * 如果host不是localhost，将替换 network-config.yaml 中的host地址
	 */
	public File getNetworkConfigFile() {
		String fileName = runningTLS ? "network-config-tls.yaml" : "network-config.yaml";
		String filePath = getNetworkConfigDirFilePath(); //"src/test/fixture/sdkintegration/network_configs/";
		
		File networkConfig = new File(filePath, fileName);
		logger.debug("network yaml config file path：{}", networkConfig);
		
		if (!"localhost".equals(FABRIC_NETWORK_HOST)) {
			// change on the fly ...
			File transferNetworkConfig = null;

			try {
				// create a temp file
				transferNetworkConfig = File.createTempFile(fileName, "-FixedUp.yaml");

				if (transferNetworkConfig.exists()) { // For testing start fresh
					transferNetworkConfig.delete();
				}

				// 读取内容
				byte[] data = Files.readAllBytes(Paths.get(networkConfig.getAbsolutePath()));

				String sourceText = new String(data, StandardCharsets.UTF_8);

				// 替换配置
				sourceText = sourceText.replaceAll("https://localhost", "https://" + FABRIC_NETWORK_HOST);
				sourceText = sourceText.replaceAll("http://localhost", "http://" + FABRIC_NETWORK_HOST);
				sourceText = sourceText.replaceAll("grpcs://localhost", "grpcs://" + FABRIC_NETWORK_HOST);
				sourceText = sourceText.replaceAll("grpc://localhost", "grpc://" + FABRIC_NETWORK_HOST);

				// 写入替换后的内容
				Files.write(Paths.get(transferNetworkConfig.getAbsolutePath()), sourceText.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE_NEW, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);

				if (!Objects.equals("true", System.getenv(FABRIC_NETWORK_HOST_KEY + "_KEEP"))) {
					transferNetworkConfig.deleteOnExit();
				} else {
					logger.info("network-config.yaml replace Host after path: {}", transferNetworkConfig.getAbsolutePath());
				}
			} catch (Exception e) {
				throw new FabricConfigurationException(e);
			}
			
			networkConfig = transferNetworkConfig;
		}

		return networkConfig;
	}
	
	public boolean isRunningAgainstFabric10() {
		return getFabricConfigGeneratorVersion().contains("1.0");
	}
	
	public boolean isRunningFabricTLS() {
		return runningFabricTLS;
	}
}
