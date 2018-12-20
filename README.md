# fabric sdk java commons framework

:twisted_rightwards_arrows: Hyperledger fabric java sdk assembly of public, core functional methods, providing further calls to other frameworks.

## 发布
+ [v1.3 - 2018-12-20](https://github.com/hooj0/fabric-sdk-java-commons/tree/v1.3)
+ [v1.1 - 2018-08-06](https://github.com/hooj0/fabric-sdk-java-commons/tree/v1.1)

## 准备
`fabric sdk java commons framework` 使用 `fabric-sdk-java` 的 `1.3` 版本。

## 1、基本配置

### 1.1、添加 `Chaincode` 必要的配置资源
在项目文件夹中添加目录 `/fabric-sdk-commons/src/test/fixture/integration`，目录中增加资源配置。必须了解 `end-to-end sample` 实例的运行方式，这里不增加额外的赘述。

### 1.2、添加 `fabric`区块链网络链接配置	
根据自己的需要可以适当的修改配置内容，这里的配置依赖上面配置资源`/fabric-sdk-commons/src/test/fixture/integration`。配置文件位置 `fabric-sdk-commons/src/test/resources/fabric-chaincode.properties`。
 内容如下：
```properties
#@changelog properties file fabric configuration
#Sat Jul 28 20:10:31 CST 2018
# 区块链网络版本
fabric.network.configtx.version=v1.0

# 用户 & 密码
hyperledger.fabric.sdk.commons.network.ca.admin.name=admin
hyperledger.fabric.sdk.commons.network.ca.admin.passwd=adminpw
hyperledger.fabric.sdk.commons.network.orgs.member.users=user1

hyperledger.fabric.sdk.commons.network.tls.enable=false

# 公共的根目录
hyperledger.fabric.sdk.commons.config.root.path=src/test/fixture/integration
# peer & order 证书配置
hyperledger.fabric.sdk.commons.crypto.channel.config.root.path=/e2e-2Orgs
# channel & block 配置
hyperledger.fabric.sdk.commons.channel.artifacts.root.path=/channel-artifacts
# Chaincode source code 目录
#hyperledger.fabric.sdk.commons.chaincode.source.code.root.path=/gocc/sample_11
hyperledger.fabric.sdk.commons.chaincode.source.code.root.path=/gocc/sample1
# 背书配置
hyperledger.fabric.sdk.commons.endorsement.policy.file.path=chaincode-endorsement-policy.yaml
# 网络配置文件所在目录
hyperledger.fabric.sdk.commons.network.config.root.path=network_configs
```

### 1.3、添加 `Key Value` 持久化存储配置
持久化缓存配置文件 `/fabric-sdk-commons/fabric-kv-store.properties`，存储通道或用户的缓存信息，方便下次直接使用而不用重复创建用户和通道。


## 2、扩展配置
`fabric sdk java commons`框架支持自定义 `FabricConfiguration` 和 `FabricKeyValueStore` 的配置。如果需要自定义扩展配置，需要实现这两个特定的接口。然后在使用的时候传入自定义实现的实例即可。
```java
public ChaincodeDeployTemplate(String channelName, String orgName, FabricConfiguration config, FabricKeyValueStore store) {
	super(channelName, orgName, config, store, ChaincodeDeployTemplate.class);
	this.init();
}
```

### 2.1、自定义 `config` 配置
`FabricConfiguration`支持两种网络链接配置方式，一种是 `properties` 配置文件的实现方式，参考代码：`FabricPropertiesConfiguration.java`，另一种则是 `class` 内存的配置方式，参考代码：`FabricClassConfiguration.java`。
可以在创建 `ChaincodeDeployTemplate` 或 `ChaincodeTransactionTemplate` 模板实例的时候，传入自己想要的配置模式。

+ `properties config` 配置的模板

  以下是 `properties config` 配置的模板，可以在模板的基础上修改配置定制

```properties
#@changelog properties file fabric configuration
#Sat Jul 28 20:10:31 CST 2018
fabric.network.configtx.version=v1.1

hyperledger.fabric.sdk.commons.network.ca.admin.name=admin
hyperledger.fabric.sdk.commons.network.ca.admin.passwd=adminpw
hyperledger.fabric.sdk.commons.network.orgs.member.users=user1

hyperledger.fabric.sdk.commons.invoke.wait.time=10
hyperledger.fabric.sdk.commons.deploy.wait.time=100
hyperledger.fabric.sdk.commons.proposal.wait.time=1000

hyperledger.fabric.sdk.commons.network.tls.enable=false
hyperledger.fabric.sdk.commons.network.domain=example.com
hyperledger.fabric.sdk.commons.configtxlater.url=http\://192.168.8.8\:7059

hyperledger.fabric.sdk.commons.config.root.path=src/test/fixture/integration
hyperledger.fabric.sdk.commons.crypto.channel.config.root.path=/e2e-2Orgs
hyperledger.fabric.sdk.commons.channel.artifacts.root.path=/channel-artifacts
hyperledger.fabric.sdk.commons.chaincode.source.code.root.path=/gocc/sample11
hyperledger.fabric.sdk.commons.endorsement.policy.file.path=chaincode-endorsement-policy.yaml
hyperledger.fabric.sdk.commons.network.config.root.path=network_configs

hyperledger.fabric.sdk.commons.network.org.peerOrg1.mspid=Org1MSP
hyperledger.fabric.sdk.commons.network.org.peerOrg1.caName=ca0
hyperledger.fabric.sdk.commons.network.org.peerOrg1.domname=org1.example.com
hyperledger.fabric.sdk.commons.network.org.peerOrg1.ca_location=http\://192.168.8.8\:7054
hyperledger.fabric.sdk.commons.network.org.peerOrg1.orderer_locations=orderer.example.com@grpc\://192.168.8.8\:7050
hyperledger.fabric.sdk.commons.network.org.peerOrg1.peer_locations=peer0.org1.example.com@grpc\://192.168.8.8\:7051, peer1.org1.example.com@grpc\://192.168.8.8\:7056
hyperledger.fabric.sdk.commons.network.org.peerOrg1.eventhub_locations=peer0.org1.example.com@grpc\://192.168.8.8\:7053, peer1.org1.example.com@grpc\://192.168.8.8\:7058


hyperledger.fabric.sdk.commons.network.org.peerOrg2.mspid=Org2MSP
hyperledger.fabric.sdk.commons.network.org.peerOrg2.domname=org2.example.com
hyperledger.fabric.sdk.commons.network.org.peerOrg2.ca_location=http\://192.168.8.8\:8054
hyperledger.fabric.sdk.commons.network.org.peerOrg2.orderer_locations=orderer.example.com@grpc\://192.168.8.8\:7050
hyperledger.fabric.sdk.commons.network.org.peerOrg2.peer_locations=peer0.org2.example.com@grpc\://192.168.8.8\:8051, peer1.org2.example.com@grpc\://192.168.8.8\:8056
hyperledger.fabric.sdk.commons.network.org.peerOrg2.eventhub_locations=peer0.org2.example.com@grpc\://192.168.8.8\:8053, peer1.org2.example.com@grpc\://192.168.8.8\:8058
```

+ `class config` 配置的模板

  以下是 `class config` 配置的模板，可以调用`FabricClassConfiguration.getInstance()`修改配置定制

```java
FabricClassConfiguration config = FabricClassConfiguration.getInstance();

String orgName = "peerOrg1";

config.setNetworkDomain("example.com");

config.setMspId(orgName, "Org1MSP");
config.setOrgDomain(orgName, "org1." + config.getNetworkDomain());
config.setCaName(orgName, "ca0");
config.setCaLocation(orgName, "http://" + config.getFabricNetworkHost() + ":7054");
config.setOrdererLocation(orgName, "orderer.example.com@grpc://" + config.getFabricNetworkHost() + ":7050");
config.setPeerLocation(orgName, "peer0.org1.example.com@grpc://" + config.getFabricNetworkHost() + ":7051, peer1.org1.example.com@grpc://" + config.getFabricNetworkHost() + ":7056");
config.setEventHubLocation(orgName, "peer0.org1.example.com@grpc://" + config.getFabricNetworkHost() + ":7053, peer1.org1.example.com@grpc://" + config.getFabricNetworkHost() + ":7058");

orgName = "peerOrg2";
config.setMspId(orgName, "Org2MSP");
config.setOrgDomain(orgName, "org2." + config.getNetworkDomain());
// config.setCaName(orgName, "ca1");
config.setCaLocation(orgName, "http://" + config.getFabricNetworkHost() + ":8054");
config.setOrdererLocation(orgName, "orderer.example.com@grpc://" + config.getFabricNetworkHost() + ":7050");
config.setPeerLocation(orgName, "peer0.org2.example.com@grpc://" + config.getFabricNetworkHost() + ":8051, peer1.org2.example.com@grpc://" + config.getFabricNetworkHost() + ":8056");
config.setEventHubLocation(orgName, "peer0.org2.example.com@grpc://" + config.getFabricNetworkHost() + ":8053, peer1.org2.example.com@grpc://" + config.getFabricNetworkHost() + ":8058");


config.settingPropertyValue(FabricConfigurationPropertyKey.FABRIC_CONFIGTX_VERSION, "v1.1");

config.settingPropertyValue(FabricConfigurationPropertyKey.CONFIG_TXLATER_URL, "http://" + config.getFabricNetworkHost() + ":7059");

config.settingPropertyValue(FabricConfigurationPropertyKey.INVOKE_WAIT_TIME, "10");
config.settingPropertyValue(FabricConfigurationPropertyKey.DEPLOY_WAIT_TIME, "100");
config.settingPropertyValue(FabricConfigurationPropertyKey.PROPOSAL_WAIT_TIME, "1000");

config.settingPropertyValue(FabricConfigurationPropertyKey.NETWORK_TLS_ENABLED, "true");

config.settingPropertyValue(FabricConfigurationPropertyKey.COMMON_CONFIG_ROOT_PATH, "src/test/fixture/integration");
config.settingPropertyValue(FabricConfigurationPropertyKey.CRYPTO_CHANNEL_CONFIG_ROOT_PATH, "/e2e-2Orgs");
config.settingPropertyValue(FabricConfigurationPropertyKey.CHAINCODE_SOURCE_ROOT_PATH, "/gocc/sample11");
config.settingPropertyValue(FabricConfigurationPropertyKey.CHANNEL_ARTIFACTS_ROOT_PATH, "/channel-artifacts");
config.settingPropertyValue(FabricConfigurationPropertyKey.ENDORSEMENT_POLICY_FILE_PATH, "chaincode-endorsement-policy.yaml");
config.settingPropertyValue(FabricConfigurationPropertyKey.NETWORK_CONFIG_ROOT_PATH, "network_configs");

config.settingPropertyValue(FabricConfigurationPropertyKey.NETWORK_CA_ADMIN_NAME, "admin");
config.settingPropertyValue(FabricConfigurationPropertyKey.NETWORK_CA_ADMIN_PASSWD, "adminpw");
config.settingPropertyValue(FabricConfigurationPropertyKey.NETWORK_ORGS_MEMBER_USERS, "user1");
```

### 2.2、自定义 `store` 配置
`FabricKeyValueStore`是KV持久化缓存配置，KV 也支持两种配置方式，一种是 `FileSystemKeyValueStore` 文件系统的配置方式，另一种是 `MemoryKeyValueStore` 内存系统的配置方式。如果有必要可以实现自己的持久化缓存存储，具体可以实现接口 `io.github.hooj0.fabric.sdk.commons.store.FabricKeyValueStore`，同样可以在创建 `ChaincodeDeployTemplate` 或 `ChaincodeTransactionTemplate` 模板实例的时候，传入自己想要的配置模式。


## 3、部署 `Chaincode`
公共的代码常量，通道和对等节点以及 `ChaincodeId`
```java
public class BasedTemplateTest {

	final static String foo = "mychannel";
	final static String bar = "mychannel2";
	final static String org1 = "peerOrg1";
	final static String org2 = "peerOrg2";
	
	static String CHAIN_CODE_NAME = "example_cc_go";
	static String CHAIN_CODE_PATH = "github.com/example_cc";
	static String CHAIN_CODE_VERSION = "1";
	static String CHAIN_CODE_VERSION_11 = "11";
	static Type CHAIN_CODE_LANG = Type.GO_LANG;
	
	ChaincodeID chaincodeID_1 = ChaincodeID.newBuilder().setName(CHAIN_CODE_NAME).setPath(CHAIN_CODE_PATH).setVersion(CHAIN_CODE_VERSION).build();
	ChaincodeID chaincodeID_11 = ChaincodeID.newBuilder().setName(CHAIN_CODE_NAME).setPath(CHAIN_CODE_PATH).setVersion(CHAIN_CODE_VERSION_11).build();
	ChaincodeID chaincodeID_11_2 = ChaincodeID.newBuilder().setName(CHAIN_CODE_NAME).setPath(CHAIN_CODE_PATH).setVersion("11.2").build();
}
```

### 3.1、创建 `Chaincode` 部署模板类实例
```java
private ChaincodeDeployOperations operations;

@Before
public void setup() {
	operations = new ChaincodeDeployTemplate(foo, org1, FabricPropertiesConfiguration.getInstance()); 
	
	// 自定义 key value store 文件位置
	//operations = new ChaincodeDeployTemplate(foo, org1, FabricClassConfiguration.getInstance(), new File("my-kv-store.properties")); 
	// 使用 内存 key value store 文件位置
	//operations = new ChaincodeDeployTemplate(foo, org1, FabricPropertiesConfiguration.getInstance(), new MemoryKeyValueStore()); 
}
```

### 3.2、安装 `Chaincode`
传入必要的参数 `chaincodeID` 和 `Chaincode` 的文件路径，以及 `chaincode` 的类型
```java
@Test
public void testInstallDeployTemplate() {
	
	InstallOptions options = new InstallOptions();
	options.setChaincodeId(chaincodeID_11).setChaincodeType(CHAIN_CODE_LANG);
	
	String chaincodeSourceFile = operations.getConfig().getChaincodeRootPath();
	operations.install(options, chaincodeSourceFile);
}
```

### 3.3、实例化 `Chaincode`
实例化支持多种结果返回形式，以及多种参数签名发送。

```java
@Test
public void testInstantiateDeployTemplate() {
	
	InstantiateOptions options = new InstantiateOptions();
	options.setChaincodeId(chaincodeID_11).setChaincodeType(CHAIN_CODE_LANG);
	options.setEndorsementPolicyFile(Paths.get(operations.getConfig().getEndorsementPolicyFilePath()).toFile());
	
	ResultSet rs = operations.instantiate(options, "init", "a", 200, "b", 300);
	System.out.println("-------->>>>>>>" + rs);
}

@Test
public void testInstantiate2DeployTemplate() {
	
	InstantiateOptions options = new InstantiateOptions();
	
	options.setChaincodeId(chaincodeID_11).setChaincodeType(CHAIN_CODE_LANG);
	options.setEndorsementPolicyFile(Paths.get(operations.getConfig().getEndorsementPolicyFilePath()).toFile());
	
	LinkedHashMap<String, Object> args = Maps.newLinkedHashMap();
	args.put("a", 300);
	args.put("b", 500);
	
	ResultSet rs = operations.instantiate(options, "init", args);
	System.out.println("-------->>>>>>>" + rs);
}

@Test
public void testInstantiate3DeployTemplate() throws Exception {
	
	InstantiateOptions options = new InstantiateOptions();
	
	options.setChaincodeId(chaincodeID_11).setChaincodeType(CHAIN_CODE_LANG);
	options.setEndorsementPolicyFile(Paths.get(operations.getConfig().getEndorsementPolicyFilePath()).toFile());
	
	LinkedHashMap<String, Object> args = Maps.newLinkedHashMap();
	args.put("a", 300);
	args.put("b", 500);
	
	CompletableFuture<TransactionEvent> future = operations.instantiateAsync(options, "init", args);
	future.thenApply((BlockEvent.TransactionEvent transactionEvent) -> {
		
		// 必须是有效交易事件
		checkArgument(transactionEvent.isValid(), "没有签名的交易事件");
		// 必须有签名
		checkNotNull(transactionEvent.getSignature(), "没有签名的交易事件");
		// 必须有交易区块事件发生
		BlockEvent blockEvent = checkNotNull(transactionEvent.getBlockEvent(), "交易事件的区块事件对象为空");
		
		try {
			System.out.println("成功实例化Chaincode，本次实例化交易ID：" +  transactionEvent.getTransactionID());
			System.out.println(transactionEvent.getChannelId());
			checkArgument(StringUtils.equals(blockEvent.getChannelId(), operations.getChannel().getName()), "事件名称和对应通道名称不一致");

			// 检查
			if (!operations.checkInstantiatedChaincode(options.getChaincodeId())) {
				throw new AssertionError("chaincode 1 没有实例化");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return "success";
	}).exceptionally(e -> {
		if (e instanceof CompletionException && e.getCause() != null) {
			e = e.getCause();
		}
		
		if (e instanceof TransactionEventException) {
			BlockEvent.TransactionEvent te = ((TransactionEventException) e).getTransactionEvent();
			if (te != null) {
				e.printStackTrace(System.err);
				fail(format("Transaction with txid %s failed. %s", te.getTransactionID(), e.getMessage()));
			}
		}
		
		e.printStackTrace(System.err);
		fail(format("Test failed with %s exception %s", e.getClass().getName(), e.getMessage()));
		
		return null;
	}).get(operations.getConfig().getTransactionWaitTime(), TimeUnit.SECONDS); 
}

@Test
public void testInstantiate4DeployTemplate() {
	
	InstantiateOptions options = new InstantiateOptions();
	
	options.setChaincodeId(chaincodeID_11).setChaincodeType(CHAIN_CODE_LANG);
	options.setEndorsementPolicyFile(Paths.get(operations.getConfig().getEndorsementPolicyFilePath()).toFile());
	
	LinkedHashMap<String, Object> args = Maps.newLinkedHashMap();
	args.put("a", 300);
	args.put("b", 500);
	
	TransactionEvent event = operations.instantiateFor(options, "init", args);
	System.out.println("-------------->>>>>>>" + event);
}
```

### 3.4、升级 `Chaincode`
升级后，每笔交易会发生在两个链码上。
```java
@Test
public void testUpgradeDeployTemplate() throws Exception {
	
	if (!operations.checkChaincode(chaincodeID_11, operations.getOrganization())) {
		fail(chaincodeID_11 + " not install | instantiate.");
	}
	
	if (!operations.checkInstallChaincode(chaincodeID_11_2)) {
		InstallOptions options = new InstallOptions();
		options.setChaincodeId(chaincodeID_11).setChaincodeType(CHAIN_CODE_LANG);
		options.setChaincodeUpgradeVersion("11.2");
		
		operations.install(options, operations.getConfig().getChaincodeRootPath());
	}
	
	if (operations.checkInstallChaincode(chaincodeID_11_2) && !operations.checkInstantiatedChaincode(chaincodeID_11_2)) {
		UpgradeOptions upgradeOptions = new UpgradeOptions();
		
		upgradeOptions.setChaincodeId(chaincodeID_11_2).setChaincodeType(CHAIN_CODE_LANG);
		upgradeOptions.setEndorsementPolicyFile(Paths.get(operations.getConfig().getEndorsementPolicyFilePath()).toFile());
		
		ResultSet rs = operations.upgrade(upgradeOptions, "init");
		System.out.println("-------------->>>>>>>" + rs);
	}
}

@Test
public void testUpgrade2DeployTemplate() throws Exception {
	
	UpgradeOptions options = new UpgradeOptions();
	
	options.setChaincodeId(chaincodeID_11).setChaincodeType(CHAIN_CODE_LANG).setChaincodeVersion("11.2");
	options.setEndorsementPolicyFile(Paths.get(operations.getConfig().getEndorsementPolicyFilePath()).toFile());
	
	InstallOptions installOptions = new InstallOptions().setChaincodeUpgradeVersion("11.2");
	installOptions.setChaincodeId(options.getChaincodeId()).setChaincodeType(CHAIN_CODE_LANG);
	
	String chaincodeSourceFile = operations.getConfig().getChaincodeRootPath();
	if (!operations.checkInstallChaincode(installOptions.getChaincodeId())) {
		operations.install(installOptions, chaincodeSourceFile);
	}

	CompletableFuture<TransactionEvent> future = operations.upgradeAsync(options, "init", "a", "100", "b", "100");
	Object result = future.thenApply((TransactionEvent event) -> {
		
		System.out.println(event.isValid());
		System.out.println(event.getBlockEvent());
		System.out.println(event.getChannelId());
		System.out.println(event.getTimestamp());
		System.out.println(event.getSignature());
		System.out.println(event.getValidationCode());
		System.out.println(event.getType());
		
		return event.getTransactionID();
	}).exceptionally(e -> {
		e.printStackTrace();
		return null;
	}).get(10000, TimeUnit.MILLISECONDS);
	
	System.out.println("-------------->>>>>>>" + result);
}

@Test
public void testUpgrade3DeployTemplate() throws Exception {
	
	UpgradeOptions options = new UpgradeOptions();
	
	options.setChaincodeId(chaincodeID_11).setChaincodeType(CHAIN_CODE_LANG).setChaincodeVersion("11.2");
	options.setEndorsementPolicyFile(Paths.get(operations.getConfig().getEndorsementPolicyFilePath()).toFile());
	
	InstallOptions installOptions = new InstallOptions().setChaincodeUpgradeVersion("11.2");
	installOptions.setChaincodeId(options.getChaincodeId()).setChaincodeType(CHAIN_CODE_LANG);
	
	String chaincodeSourceFile = operations.getConfig().getChaincodeRootPath();
	if (!operations.checkInstallChaincode(installOptions.getChaincodeId())) {
		operations.install(installOptions, chaincodeSourceFile);
	}

	TransactionEvent event = operations.upgradeFor(options, "init");
	
	System.out.println("-------------->>>>>>>" + event);
}
```

## 4、交易 `Chaincode`
交易是最常用的业务接口调用方式，一般交易分为查询交易和修改交易请求两种。

### 4.1、创建交易模板类 `ChaincodeTransactionOperations`
同样可以支持自定义传入 `config` 和 `store` 的实现
```java
private ChaincodeTransactionOperations operations;
	
@Before
public void setup() {
	operations = new ChaincodeTransactionTemplate(foo, org1, FabricPropertiesConfiguration.getInstance()); 
	//operations = new ChaincodeTransactionTemplate(foo, org1, FabricPropertiesConfiguration.getInstance(), new File("my-kv-store.properties")); 
	//operations = new ChaincodeTransactionTemplate(foo, org1, FabricPropertiesConfiguration.getInstance(), new MemoryKeyValueStore()); 
}
```


### 4.2、`Chaincode invoke` 
执行交易执行3种不同方式的结果返回形式，普通的结果封装 `ResultSet` 和 异步线程模型结果 `CompletableFuture<TransactionEvent>` 以及 `TransactionEvent` 结果返回，适用于不同的业务场景需求。
```java
@Test
public void testInvokeTransactionTemplate() {
	
	InvokeOptions options = new InvokeOptions();
	
	options.setChaincodeId(chaincodeID_11).setChaincodeType(CHAIN_CODE_LANG);
	
	options.setClientUserContext(operations.getOrganization().getUser("user1"));
	
	ResultSet rs = operations.invoke(options, "move", "a", "b", 20);
	System.out.println(rs);
	
	try {
		TransactionEvent event = operations.getChannel().sendTransaction(rs.getResponses()).get();
		System.out.println(event.isValid());
		System.out.println(event.getBlockEvent());
	} catch (InterruptedException | ExecutionException e) {
		e.printStackTrace();
	}
}

@Test
public void testInvokeTransactionTemplate2() throws InterruptedException, ExecutionException, TimeoutException {
	
	InvokeOptions options = new InvokeOptions();
	options.setChaincodeId(chaincodeID_11).setChaincodeType(CHAIN_CODE_LANG);
	
	options.setClientUserContext(operations.getOrganization().getUser("user1"));
	
	CompletableFuture<TransactionEvent> future = operations.invokeAsync(options, "move", "b", "a", 3);
	future.thenApply((BlockEvent.TransactionEvent transactionEvent) -> {
		
		// 必须是有效交易事件
		checkArgument(transactionEvent.isValid(), "没有签名的交易事件");
		// 必须有签名
		checkNotNull(transactionEvent.getSignature(), "没有签名的交易事件");
		// 必须有交易区块事件发生
		BlockEvent blockEvent = checkNotNull(transactionEvent.getBlockEvent(), "交易事件的区块事件对象为空");
		
		try {
			System.out.println("成功实例化Chaincode，本次实例化交易ID：" +  transactionEvent.getTransactionID());
			System.out.println(transactionEvent.getChannelId());
			checkArgument(StringUtils.equals(blockEvent.getChannelId(), operations.getChannel().getName()), "事件名称和对应通道名称不一致");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		QueryOptions options2 = new QueryOptions();
		options2.setChaincodeId(chaincodeID_11).setChaincodeType(CHAIN_CODE_LANG);
		
		options2.setProposalWaitTime(100000);
		options2.setSpecificPeers(false);
		
		//operations.instantiate("hoojo3");
		options2.setClientUserContext(operations.getOrganization().getUser("hoojo5"));
		
		String a = operations.query(options2, "query", "a");
		String b = operations.query(options2, "query", "b");
		
		System.out.println("a mount ---->" + a);
		System.out.println("b mount ---->" + b);
		
		return "success";
	}).exceptionally(e -> {
		if (e instanceof CompletionException && e.getCause() != null) {
			e = e.getCause();
		}
		
		if (e instanceof TransactionEventException) {
			BlockEvent.TransactionEvent te = ((TransactionEventException) e).getTransactionEvent();
			if (te != null) {
				e.printStackTrace(System.err);
				fail(format("Transaction with txid %s failed. %s", te.getTransactionID(), e.getMessage()));
			}
		}
		
		e.printStackTrace(System.err);
		fail(format("Test failed with %s exception %s", e.getClass().getName(), e.getMessage()));
		
		return null;
	}).get(operations.getConfig().getTransactionWaitTime(), TimeUnit.SECONDS); 
}

@Test
public void testInvokeTransactionTemplate3() {
	
	InvokeOptions options = new InvokeOptions();
	options.setChaincodeId(chaincodeID_11).setChaincodeType(CHAIN_CODE_LANG);
	
	options.setClientUserContext(operations.getOrganization().getUser("user1"));
	
	TransactionEvent event = operations.invokeFor(options, "move", "a", "b", 5);
	System.out.println(event.getTransactionID());
	System.out.println(event.getCreator().getId());
}
```


### 4.3、`Chaincode query` 
查询支持两种结果返回的方式，返回字符串和封装的结果集 `ResultSet`。
```java
@Test
public void testQueryTransactionTemplate() {
	
	QueryOptions options = new QueryOptions();
	options.setChaincodeId(chaincodeID_11).setChaincodeType(CHAIN_CODE_LANG);
	
	options.setProposalWaitTime(100000);
	options.setSpecificPeers(false);
	
	operations.instantiate("hoojo3");
	
	options.setClientUserContext(operations.getOrganization().getUser("hoojo3"));
	// options.setRequestUser(requestUser);
	
	String rs = operations.query(options, "query", "a");
	System.out.println(rs);
	rs = operations.query(options, "query", "b");
	System.out.println(rs);
}

@Test
public void testQueryTransactionTemplate2() {
	
	QueryOptions options = new QueryOptions();
	options.setChaincodeId(chaincodeID_11).setChaincodeType(CHAIN_CODE_LANG);
	
	operations.instantiate("hoojo3");
	options.setClientUserContext(operations.getOrganization().getUser("user1"));
	options.setRequestUser(operations.getOrganization().getUser("hoojo3"));
	
	ResultSet rs = operations.queryFor(options, "query", "a");
	System.out.println(rs);
}
```

## 5、问题反馈
如果有任何疑问或交流的想法，欢迎在 [issues](https://github.com/hooj0/fabric-sdk-java-commons/issues) 发布话题，我会及时跟进。
