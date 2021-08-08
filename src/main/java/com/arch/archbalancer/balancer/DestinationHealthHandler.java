package com.arch.archbalancer.balancer;

import com.arch.archbalancer.util.ConfigurationReader;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class DestinationHealthHandler {
    private final static HashMap<Integer, Boolean> destinationHealth = new HashMap<>();
    private static final ArrayList<ConfigurationReader.CommunicationConfiguration> clientConfigurations;

    static {
        clientConfigurations = ConfigurationReader.getClientConfigurations();
        for (ConfigurationReader.CommunicationConfiguration clientConfiguration : clientConfigurations) {
            destinationHealth.put(clientConfiguration.getId(), true);
        }
    }

    public static ConfigurationReader.CommunicationConfiguration getFirstReachableDestination() {
        for (int destinationId : destinationHealth.keySet()) {
            if (destinationHealth.get(destinationId)) {
                for (ConfigurationReader.CommunicationConfiguration clientConfiguration : clientConfigurations) {
                    if (clientConfiguration.getId() == destinationId)
                        return clientConfiguration;
                }
            }
        }
        return null;
    }

    public static void setDestinationHealth(Integer destinationId, Boolean isReachable) {
        destinationHealth.put(destinationId, isReachable);
    }

}
