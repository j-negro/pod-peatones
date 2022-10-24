package ar.edu.itba.pod.server;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

import static java.lang.System.getProperty;

public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        logger.info("Configuring Server Node...");

        // Config
        Config config = new Config();

        // Group Config
        GroupConfig groupConfig = new GroupConfig().setName("g8").setPassword("g8-pass");
        config.setGroupConfig(groupConfig);

        // Network Config
        MulticastConfig multicastConfig = new MulticastConfig();

        JoinConfig joinConfig = new JoinConfig().setMulticastConfig(multicastConfig);

        String addressMask = getProperty("addressMask");
        logger.info("Using custom address mask, {}", addressMask);

        if (addressMask == null) {
            addressMask = "127.0.0.*";
        } else {
            logger.info("Using custom address mask, {}", addressMask);
        }

        InterfacesConfig interfacesConfig = new InterfacesConfig().setInterfaces(Collections.singletonList(addressMask)).setEnabled(true);

        NetworkConfig networkConfig = new NetworkConfig().setInterfaces(interfacesConfig).setJoin(joinConfig);

        config.setNetworkConfig(networkConfig);

        logger.info("Node configuration done. Now starting...");

        // Start cluster
        Hazelcast.newHazelcastInstance(config);
    }
}
