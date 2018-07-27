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
 * abstract basic configuration support
 * @author hoojo
 * @createDate 2018年7月23日 上午9:28:04
 * @file AbstractConfiguration.java
 * @package io.github.hooj0.fabric.sdk.commons.config
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public abstract class AbstractConfiguration extends AbstractObject implements FabricConfiguration, FabricConfigurationPropertyKey {

	/** SDK 配置 */
	protected static final Properties SDK_COMMONS_PROPERTIES = new Properties();
	/** ORG 配置 */
	protected static final HashMap<String, Organization> ORGANIZATIONS = new HashMap<>();
	
	/** 区块链网络配置key的前缀 */
	protected static final String NETWORK_KEY_PREFIX = PREFIX + "network.org.";
	
	/** 匹配到 mspid 值*/
	private static final Pattern ORG_MSPID_PATTERN = Pattern.compile("^" + Pattern.quote(NETWORK_KEY_PREFIX) + "([^\\.]+)\\.mspid$");
	
	/** 开启TLS证书，也就是https(grpcs)协议和http(grpc)协议之间的切换 */
	protected boolean enableTLS;
	protected boolean enableFabricCATLS;
	protected boolean enableFabricTLS;
	
	public AbstractConfiguration(Class<?> clazz) {
		super(clazz);
	}
	
	/** 默认的组织配置信息配置设置 */
	protected void defaultValueSettings() {
		// Default network domain
		defaultProperty(NETWORK_DOMAIN, "example.com");
		
		// Default values
		defaultProperty(INVOKE_WAIT_TIME, "120");
		defaultProperty(DEPLOY_WAIT_TIME, "120000");
		defaultProperty(PROPOSAL_WAIT_TIME, "120000");

		// Default network values
		defaultProperty(NETWORK_KEY_PREFIX + "peerOrg1.mspid", "Org1MSP");
		defaultProperty(NETWORK_KEY_PREFIX + "peerOrg1.domname", "org1.example.com");
		defaultProperty(NETWORK_KEY_PREFIX + "peerOrg1.caName", "ca0");
		defaultProperty(NETWORK_KEY_PREFIX + "peerOrg1.ca_location", "http://" + FABRIC_NETWORK_HOST + ":7054");
		defaultProperty(NETWORK_KEY_PREFIX + "peerOrg1.orderer_locations", "orderer.example.com@grpc://" + FABRIC_NETWORK_HOST + ":7050");
		defaultProperty(NETWORK_KEY_PREFIX + "peerOrg1.peer_locations", "peer0.org1.example.com@grpc://" + FABRIC_NETWORK_HOST + ":7051, peer1.org1.example.com@grpc://" + FABRIC_NETWORK_HOST + ":7056");
		defaultProperty(NETWORK_KEY_PREFIX + "peerOrg1.eventhub_locations", "peer0.org1.example.com@grpc://" + FABRIC_NETWORK_HOST + ":7053, peer1.org1.example.com@grpc://" + FABRIC_NETWORK_HOST + ":7058");
		
		defaultProperty(NETWORK_KEY_PREFIX + "peerOrg2.mspid", "Org2MSP");
		defaultProperty(NETWORK_KEY_PREFIX + "peerOrg2.domname", "org2.example.com");
		//defaultProperty(NETWORK_KEY_PREFIX + "peerOrg2.caName", "ca1");
		defaultProperty(NETWORK_KEY_PREFIX + "peerOrg2.ca_location", "http://" + FABRIC_NETWORK_HOST + ":8054");
		defaultProperty(NETWORK_KEY_PREFIX + "peerOrg2.orderer_locations", "orderer.example.com@grpc://" + FABRIC_NETWORK_HOST + ":7050");
		defaultProperty(NETWORK_KEY_PREFIX + "peerOrg2.peer_locations", "peer0.org2.example.com@grpc://" + FABRIC_NETWORK_HOST + ":8051, peer1.org2.example.com@grpc://" + FABRIC_NETWORK_HOST + ":8056");
		defaultProperty(NETWORK_KEY_PREFIX + "peerOrg2.eventhub_locations", "peer0.org2.example.com@grpc://" + FABRIC_NETWORK_HOST + ":8053, peer1.org2.example.com@grpc://" + FABRIC_NETWORK_HOST + ":8058");

		// Default tls values
		defaultProperty(NETWORK_TLS_ENABLED, null);

		printConfig("sdk properties");
	}
	
	/** 创建配置对象信息 */
	protected void instantiateConfiguration() {
		// TLS 
		String enabledTLS = getProperty(NETWORK_TLS_ENABLED, System.getenv(ENV_DEFAULT_TLS_ENABLED));
		logger.debug("tlsEnabled: {}", enabledTLS);
		
		enableTLS = StringUtils.equals(enabledTLS, "true");
		enableFabricCATLS = enableTLS;
		enableFabricTLS = enableTLS;
		
		// 找到组织配置 peerOrg1/peerOrg2
		addOrganizations();

		// 设置组织 orderer、peer、eventhub、domain、cert等配置
		for (Map.Entry<String, Organization> org : ORGANIZATIONS.entrySet()) {
			final Organization organization = org.getValue();
			final String orgName = org.getKey();

			final String domainName = getProperty(NETWORK_KEY_PREFIX + orgName + ".domname");
			organization.setDomainName(domainName);

			addPeerLocation(organization, orgName);
			addOrdererLocation(organization, orgName);
			addEventHubLocation(organization, orgName);

			setCAProperties(organization, orgName);
			
			logger.debug("最终organization配置：{}", organization);
		}

		logger.debug("最终ORGANIZATION_RESOURCES配置：{}", ORGANIZATIONS);
	}
	
	/** 默认配置优先读取 系统级别 配置，如果系统环境配置为空，则读取运行变量中的配置 */
	protected void defaultProperty(String key, String value) {
		String data = System.getProperty(key);
		logger.trace("读取系统环境配置：{} => {}", key, data);
		
		if (data == null) {
			String envKey = key.toLowerCase().replaceAll("\\.", "_");
			data = System.getenv(envKey);
			
			logger.trace("读取变量环境配置：{} => {}", envKey, data);
			if (data == null) {
				if (null == getProperty(key) && value != null) {
					data = value;
					logger.trace("使用默认配置：{} => {}", key, data);
				}
			}
		}

		if (data != null) {
			SDK_COMMONS_PROPERTIES.put(key, data);
			logger.debug("添加SDK配置: {} => {}", key, data);
		}
	}
	
	/** 从sdk配置文件读取配置，并提供默认值 */
	protected String getProperty(String property, String defaultValue) {
		return SDK_COMMONS_PROPERTIES.getProperty(property, defaultValue);
	}
	
	protected String getProperty(String property) {
		return SDK_COMMONS_PROPERTIES.getProperty(property);
	}
	
	/** 添加组织对象信息 */
	private void addOrganizations() {
		for (Map.Entry<Object, Object> item : SDK_COMMONS_PROPERTIES.entrySet()) {
			final String key = item.getKey() + "";
			final String val = item.getValue() + "";

			if (key.startsWith(NETWORK_KEY_PREFIX)) {
				
				Matcher match = ORG_MSPID_PATTERN.matcher(key);
				if (match.matches() && match.groupCount() == 1) {
					String orgName = match.group(1).trim();
					
					Organization org = new Organization(orgName, val.trim());
					ORGANIZATIONS.put(orgName, org);
					
					logger.debug("添加组织: {} => {}", orgName, org);
				}
			}
		}
	}

	/** 添加组织事件总线URL配置 */
	private void addEventHubLocation(Organization organization, String orgName) {
		String eventHubProps = getProperty(NETWORK_KEY_PREFIX + orgName + ".eventhub_locations");
		String[] eventHubs = eventHubProps.split("[ \t]*,[ \t]*");
		for (String eventHub : eventHubs) {
			String[] key_val = eventHub.split("[ \t]*@[ \t]*");
			organization.addEventHubLocation(key_val[0], grpcTLSify(key_val[1]));
			
			logger.debug("addEventHubLocation：{}->{}", key_val[0], grpcTLSify(key_val[1]));
		}
	}
	
	/** 添加Orderer服务URL配置 */
	private void addOrdererLocation(Organization organization, String orgName) {
		String ordererProps = getProperty(NETWORK_KEY_PREFIX + orgName + ".orderer_locations");
		String[] orderers = ordererProps.split("[ \t]*,[ \t]*");
		for (String orderer : orderers) {
			String[] key_val = orderer.split("[ \t]*@[ \t]*");
			organization.addOrdererLocation(key_val[0], grpcTLSify(key_val[1]));
			
			logger.debug("addOrdererLocation：{}->{}", key_val[0], grpcTLSify(key_val[1]));
		}
	}
	
	/** 添加Peer节点URL配置 */
	private void addPeerLocation(Organization organization, String orgName) {
		String peerProps = getProperty(NETWORK_KEY_PREFIX + orgName + ".peer_locations");
		String[] peers = peerProps.split("[ \t]*,[ \t]*");
		for (String peer : peers) {
			String[] key_val = peer.split("[ \t]*@[ \t]*");
			organization.addPeerLocation(key_val[0], grpcTLSify(key_val[1]));
			
			logger.debug("addPeerLocation：{}->{}", key_val[0], grpcTLSify(key_val[1]));
		}
	}
	
	/** 设置 CA 配置 */
	private void setCAProperties(Organization organization, String orgName) {
		organization.setCALocation(httpTLSify(getProperty((NETWORK_KEY_PREFIX + orgName + ".ca_location"))));
		organization.setCAName(getProperty((NETWORK_KEY_PREFIX + orgName + ".caName")));
		
		if (enableFabricCATLS) {
			String cert = getCryptoChannelConfigRootPath() + "/crypto-config/peerOrganizations/DNAME/ca/ca.DNAME-cert.pem";
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

		return enableFabricTLS ? location.replaceFirst("^grpc://", "grpcs://") : location;
	}

	/** HTTP 协议 开启TLS证书 */
	private String httpTLSify(String location) {
		location = location.trim();

		return enableFabricCATLS ? location.replaceFirst("^http://", "https://") : location;
	}
	
	/** 获取全部组织 */
	public Collection<Organization> getOrganizations() {
		return Collections.unmodifiableCollection(ORGANIZATIONS.values());
	}

	/** 获取组织 */
	public Organization getOrganization(String name) {
		return ORGANIZATIONS.get(name);
	}
	
	/** 节点配置 */
	public Properties getPeerProperties(String name) {
		Properties props = getTLSCertKeyProperties("peer", name);
		
		logger.debug("{} properties: {}", name, props);
		printConfig(props, name + " peer");
		return props;
	}
	
	/** orderer 服务配置 */
	public Properties getOrdererProperties(String name) {
		Properties props = getTLSCertKeyProperties("orderer", name);
		
		logger.debug("{} properties: {}", name, props);
		printConfig(props, name + " orderer");
		return props;
	}
	
	/** 事件机制配置 */
	public Properties getEventHubProperties(String name) {
		Properties props = getTLSCertKeyProperties("peer", name); // uses same as named peer
		
		logger.debug("{} properties: {}", name, props);
		printConfig(props, name + " peer");
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

	/** 获取orderer/organization的  TLS 证书 Cert Key 配置信息 */
	private Properties getTLSCertKeyProperties(final String type, final String name) {
		Properties props = new Properties();

		final String domainName = getDomainName(name);
		final String txconfigRootPath = getCryptoChannelConfigRootPath();

		File cert = Paths.get(txconfigRootPath, "crypto-config/ordererOrganizations".replace("orderer", type), domainName, type + "s", name, "tls/server.crt").toFile();
		if (!cert.exists()) {
			throw new FabricConfigurationException("Missing cert file for: %s. Could not find at location: %s", name, cert.getAbsolutePath());
		}

		if (!isFabricConfigtxV10()) {
			File clientCert;
			File clientKey;
			String topDomain = this.getNetworkDomain();
			if ("orderer".equals(type)) {
				clientCert = Paths.get(txconfigRootPath, "crypto-config/ordererOrganizations/", topDomain, "/users/Admin@", topDomain, "/tls/client.crt").toFile();
				clientKey = Paths.get(txconfigRootPath, "crypto-config/ordererOrganizations/", topDomain, "/users/Admin@", topDomain, "/tls/client.key").toFile();
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
		String fileName = enableTLS ? "network-config-tls.yaml" : "network-config.yaml";
		String filePath = getNetworkConfigRootPath(); //"src/test/fixture/sdkintegration/network_configs/";
		
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

				if (!Objects.equals("true", System.getenv(ENV_NETWORK_FILE_KEEP))) {
					transferNetworkConfig.deleteOnExit();
				} else {
					logger.info("network-config.yaml replace Host after path: {}", transferNetworkConfig.getAbsolutePath());
				}
			} catch (Exception e) {
				throw new FabricConfigurationException(e, "transfer network config & replace location exception: %s", e.getMessage());
			}
			
			networkConfig = transferNetworkConfig;
		}

		return networkConfig;
	}
	
	protected void printConfig(String tag) {
		printConfig(null, tag);
	}
	
	protected void printConfig(Properties properties, String tag) {
		Properties props = properties == null ? SDK_COMMONS_PROPERTIES : properties;
		System.out.println("---------------------------" + tag + "------------------------------");
		for (Map.Entry<Object, Object> item : props.entrySet()) {
			final String key = item.getKey() + "";
			final String val = item.getValue() + "";
			
			System.out.println(key + "->" + val);
		}
		System.out.println("-----------------------------" + tag + "----------------------------");
	} 
}
