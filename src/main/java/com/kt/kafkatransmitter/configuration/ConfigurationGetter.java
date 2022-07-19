package com.kt.kafkatransmitter.configuration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Configuration getter
 */
@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfigurationGetter {

    private static LinkedList<CommunicationConfiguration> serverConfigurations;
    private static boolean isServerConfigurationsRead;
    private static LinkedList<CommunicationConfiguration> clientConfigurations;
    private static boolean isClientConfigurationsRead;
    private static ListIterator<CommunicationConfiguration> clientConfigurationListIterator;

    private static final String SERVERS = "Servers";
    private static final String SERVER = "Server";
    private static final String CLIENTS = "Clients";
    private static final String CLIENT = "Client";

    /**
     * Method to get server configurations
     *
     * @return server configurations
     */
    public static List<CommunicationConfiguration> getServerConfigurations() {
        if (!isServerConfigurationsRead) {
            serverConfigurations = CommunicationConfigurationGetter.getCommunicationConfigurations(SERVERS, SERVER);
            isServerConfigurationsRead = true;
        }
        return serverConfigurations;
    }

    /**
     * Method to get client configurations
     *
     * @return client configurations
     */
    public static List<CommunicationConfiguration> getClientConfigurations() {
        if (!isClientConfigurationsRead) {
            clientConfigurations = CommunicationConfigurationGetter.getCommunicationConfigurations(CLIENTS, CLIENT);
            isClientConfigurationsRead = true;
        }
        return clientConfigurations;
    }

    /**
     * Method to get the next configuration
     *
     * @return the next configuration
     */
    public static CommunicationConfiguration getNextClientConfiguration() {
        if (getClientConfigurations().isEmpty()) return null;
        if (clientConfigurationListIterator == null) {
            log.info("Init client config iter");
            clientConfigurationListIterator = getClientConfigurations().listIterator();
        }
        if (!clientConfigurationListIterator.hasNext()) {
            log.info("Reset client config iter");
            clientConfigurationListIterator = getClientConfigurations().listIterator();
        }
        return clientConfigurationListIterator.next();
    }

}
