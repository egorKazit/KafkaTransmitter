package com.kt.kafkatransmitter.listener;

import com.kt.kafkatransmitter.client.AbstractSocket;
import com.kt.kafkatransmitter.configuration.CommunicationConfiguration;
import com.kt.kafkatransmitter.configuration.ConfigurationGetter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.integration.dsl.IntegrationFlow;

import java.lang.reflect.Field;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class ServerIteratorImpTest {

    private ServerIteratorImp serverIteratorImp;
    LinkedList<CommunicationConfiguration> serverConfigurations;

    @BeforeEach
    public void setUp() {
        serverIteratorImp = new ServerIteratorImp();
        serverConfigurations = new LinkedList<>();
    }

    @AfterAll
    static void globalTeardown() throws NoSuchFieldException, IllegalAccessException {
        Field isServerConfigurationsReadField = ConfigurationGetter.class.getDeclaredField("isServerConfigurationsRead");
        isServerConfigurationsReadField.setAccessible(true);
        isServerConfigurationsReadField.set(ConfigurationGetter.class, false);
        Field serverConfigurationsField = ConfigurationGetter.class.getDeclaredField("serverConfigurations");
        serverConfigurationsField.setAccessible(true);
        serverConfigurationsField.set(ConfigurationGetter.class, null);
    }

    @Test
    public void checkTcpListener() throws NoSuchFieldException, IllegalAccessException {
        addConfiguration(1, AbstractSocket.TCP_TYPE);
        addConfiguration(2, AbstractSocket.UDP_TYPE);
        addConfiguration(3, "Http");
        finishConfiguration();

        assertTrue(serverIteratorImp.hasNext());
        IntegrationFlow integrationFlow = serverIteratorImp.getNext();
        assertNotNull(integrationFlow);

        assertTrue(serverIteratorImp.hasNext());
        integrationFlow = serverIteratorImp.getNext();
        assertNotNull(integrationFlow);

        assertTrue(serverIteratorImp.hasNext());
        integrationFlow = serverIteratorImp.getNext();
        assertNull(integrationFlow);

        assertFalse(serverIteratorImp.hasNext());
        integrationFlow = serverIteratorImp.getNext();
        assertNull(integrationFlow);
    }

    private void addConfiguration(int id, String type) throws NoSuchFieldException, IllegalAccessException {
        CommunicationConfiguration communicationConfiguration = new CommunicationConfiguration();
        Field idField = communicationConfiguration.getClass().getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(communicationConfiguration, id);
        Field typeField = communicationConfiguration.getClass().getDeclaredField("type");
        typeField.setAccessible(true);
        typeField.set(communicationConfiguration, type);
        serverConfigurations.add(communicationConfiguration);
    }

    private void finishConfiguration() throws NoSuchFieldException, IllegalAccessException {
        Field isServerConfigurationsReadField = ConfigurationGetter.class.getDeclaredField("isServerConfigurationsRead");
        isServerConfigurationsReadField.setAccessible(true);
        isServerConfigurationsReadField.set(ConfigurationGetter.class, true);
        Field serverConfigurationsField = ConfigurationGetter.class.getDeclaredField("serverConfigurations");
        serverConfigurationsField.setAccessible(true);
        serverConfigurationsField.set(ConfigurationGetter.class, serverConfigurations);
    }

}