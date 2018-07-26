package io.github.hooj0.fabric.sdk.commons.core;

import org.hyperledger.fabric.sdk.Channel;

/**
 * basic chaincode operations init interface
 * @author hoojo
 * @createDate 2018年7月25日 下午6:21:55
 * @file ChaincodeBasicOperations.java
 * @package io.github.hooj0.fabric.sdk.commons.core
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface ChaincodeBasicOperations {

	/**
     * Force the initialization of this instance if it hasn't been
     * initialized yet.
	 * @author hoojo
	 * @createDate 2018年7月23日 下午3:10:28
	 * @return Channel initialization channel
	 */
	Channel init(String channelName, String orgName);
	
    void close();

    boolean isClosed();

    State getState();

    interface State {
       
        ChaincodeTransactionOperations getSession();

        String getConnectedHosts();
    }
}
