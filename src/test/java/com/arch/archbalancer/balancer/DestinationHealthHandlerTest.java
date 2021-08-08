package com.arch.archbalancer.balancer;

import com.arch.archbalancer.util.ConfigurationReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
class DestinationHealthHandlerTest {

    @Test
    void checkSetDestinationHealth() {
        ConfigurationReader.CommunicationConfiguration communicationConfiguration = DestinationHealthHandler.getFirstReachableDestination();
        Assertions.assertNotNull(communicationConfiguration);
    }

    @Test
    void checkSetDestinationHealthWithChange() {
        ArrayList<ConfigurationReader.CommunicationConfiguration> clientConfigurations = ConfigurationReader.getClientConfigurations();
        for (ConfigurationReader.CommunicationConfiguration communicationConfiguration : clientConfigurations) {
            DestinationHealthHandler.setDestinationHealth(communicationConfiguration.getId(), false);
        }
        ConfigurationReader.CommunicationConfiguration communicationConfiguration = DestinationHealthHandler.getFirstReachableDestination();
        Assertions.assertNull(communicationConfiguration);
    }

}