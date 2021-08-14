package com.kt.kafkatransmitter.configuration;

import lombok.extern.log4j.Log4j2;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
public class ConfigurationGetter {

    private static final AtomicReference<LinkedList<CommunicationConfiguration>> serverConfigurations = new AtomicReference<>();
    private static boolean isServerConfigurationsRead;
    private static final AtomicReference<LinkedList<CommunicationConfiguration>> clientConfigurations = new AtomicReference<>();
    private static boolean isClientConfigurationsRead;
    private static ListIterator<CommunicationConfiguration> clientConfigurationListIterator;

    private static final String SERVERS = "Servers";
    private static final String SERVER = "Server";
    private static final String CLIENTS = "Clients";
    private static final String CLIENT = "Client";

    public static List<CommunicationConfiguration> getServerConfigurations() {
        if (!isServerConfigurationsRead) {
            serverConfigurations.set(ConfigurationHandler.getCommunicationConfigurations(SERVERS, SERVER));
            isServerConfigurationsRead = true;
        }
        return serverConfigurations.get();
    }

    public static List<CommunicationConfiguration> getClientConfigurations() {
        if (!isClientConfigurationsRead) {
            clientConfigurations.set(ConfigurationHandler.getCommunicationConfigurations(CLIENTS, CLIENT));
            isClientConfigurationsRead = true;
        }
        return clientConfigurations.get();
    }

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
        log.info("Get Next");
        return clientConfigurationListIterator.next();
    }

}
