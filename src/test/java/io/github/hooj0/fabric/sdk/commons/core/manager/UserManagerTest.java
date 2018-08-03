package io.github.hooj0.fabric.sdk.commons.core.manager;

import java.io.File;

import org.junit.Test;

import io.github.hooj0.fabric.sdk.commons.config.DefaultFabricConfiguration;
import io.github.hooj0.fabric.sdk.commons.config.FabricConfiguration;
import io.github.hooj0.fabric.sdk.commons.config.support.FabricPropertiesConfiguration;
import io.github.hooj0.fabric.sdk.commons.store.FabricKeyValueStore;

/**
 * user manager support test unit
 * @author hoojo
 * @createDate 2018年7月28日 下午8:29:27
 * @file UserManagerTest.java
 * @package io.github.hooj0.fabric.sdk.commons.core.manager
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class UserManagerTest {

	@Test
	public void testInstance() {
		
		FabricConfiguration config = DefaultFabricConfiguration.INSTANCE.getDefaultConfiguration();
		FabricKeyValueStore store = config.getDefaultKeyValueStore();

		UserManager manager = new UserManager(config, store);
		
		try {
			manager.initialize(config.getCaAdminName(), config.getCaAdminPassword(), config.getUsers());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testInstance2() {
		
		FabricConfiguration config = FabricPropertiesConfiguration.getInstance();
		FabricKeyValueStore store = config.getDefaultKeyValueStore();

		UserManager manager = new UserManager(config, store);
		
		try {
			manager.initialize(config.getCaAdminName(), config.getCaAdminPassword(), config.getUsers());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testInstance3() {
		
		FabricConfiguration config = FabricPropertiesConfiguration.getInstance();

		UserManager manager = new UserManager(config, new File("fabric-kv-store.properties"));
		
		try {
			manager.initialize(config.getCaAdminName(), config.getCaAdminPassword(), config.getUsers());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testRegisterUser() {
		
		FabricConfiguration config = FabricPropertiesConfiguration.getInstance();

		UserManager manager = new UserManager(config, new File("fabric-kv-store.properties"));
		
		try {
			manager.initialize(config.getCaAdminName(), config.getCaAdminPassword(), config.getUsers());

			//HFCAClient client = config.getOrganization("peerOrg1").getCAClient();
			
			System.out.println(manager.registerAndEnrollUser(config.getOrganization("peerOrg1"), "hoojo1"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
