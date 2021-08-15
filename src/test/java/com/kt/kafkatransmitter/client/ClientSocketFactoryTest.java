package com.kt.kafkatransmitter.client;

import com.kt.kafkatransmitter.configuration.CommunicationConfiguration;
import com.kt.kafkatransmitter.configuration.ConfigurationGetter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.naming.CommunicationException;
import java.lang.reflect.Field;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class ClientSocketFactoryTest {
    static LinkedList<CommunicationConfiguration> serverConfigurations;

    @BeforeAll
    static void globalSetUp() throws NoSuchFieldException, IllegalAccessException {
        Field clientConfigurationListIteratorField = ConfigurationGetter.class.getDeclaredField("clientConfigurationListIterator");
        clientConfigurationListIteratorField.setAccessible(true);
        clientConfigurationListIteratorField.set(ConfigurationGetter.class, null);
        serverConfigurations = new LinkedList<>();
        addConfiguration(1, AbstractSocket.TCP_TYPE);
        addConfiguration(2, AbstractSocket.UDP_TYPE);
        addConfiguration(3, "Http");
        addConfiguration(4, AbstractSocket.TCP_TYPE);
        finishConfiguration();
    }

    @AfterAll
    static void globalTeardown() throws NoSuchFieldException, IllegalAccessException {
        Field isServerConfigurationsReadField = ConfigurationGetter.class.getDeclaredField("isClientConfigurationsRead");
        isServerConfigurationsReadField.setAccessible(true);
        isServerConfigurationsReadField.set(ConfigurationGetter.class, false);
        Field serverConfigurationsField = ConfigurationGetter.class.getDeclaredField("clientConfigurations");
        serverConfigurationsField.setAccessible(true);
        serverConfigurationsField.set(ConfigurationGetter.class, null);
    }

    @Test
    public void checkTcpClientOnTheFirstPlace() throws CommunicationException {
        AbstractSocket genericSocket = ClientSocketFactory.getNextDestination();
        assertTrue(genericSocket instanceof TcpSocket);
        assertEquals(1, genericSocket.getConfigurationId());

        genericSocket = ClientSocketFactory.getNextDestination();
        assertTrue(genericSocket instanceof UdpSocket);
        assertEquals(2, genericSocket.getConfigurationId());

        try {
            ClientSocketFactory.getNextDestination();
            fail("incorrect beh");
        } catch (CommunicationException ignored) {

        }

        // check new cycle
        genericSocket = ClientSocketFactory.getNextDestination();
        assertTrue(genericSocket instanceof TcpSocket);
        assertEquals(4, genericSocket.getConfigurationId());

    }

    private static void addConfiguration(int id, String type) throws NoSuchFieldException, IllegalAccessException {
        CommunicationConfiguration communicationConfiguration = new CommunicationConfiguration();
        Field idField = communicationConfiguration.getClass().getDeclaredField("Id");
        idField.setAccessible(true);
        idField.set(communicationConfiguration, id);
        Field typeField = communicationConfiguration.getClass().getDeclaredField("type");
        typeField.setAccessible(true);
        typeField.set(communicationConfiguration, type);
        serverConfigurations.add(communicationConfiguration);
    }

    private static void finishConfiguration() throws NoSuchFieldException, IllegalAccessException {
        Field isServerConfigurationsReadField = ConfigurationGetter.class.getDeclaredField("isClientConfigurationsRead");
        isServerConfigurationsReadField.setAccessible(true);
        isServerConfigurationsReadField.set(ConfigurationGetter.class, true);
        Field serverConfigurationsField = ConfigurationGetter.class.getDeclaredField("clientConfigurations");
        serverConfigurationsField.setAccessible(true);
        serverConfigurationsField.set(ConfigurationGetter.class, serverConfigurations);
    }
}