package com.kt.kafkatransmitter.client;

import com.kt.kafkatransmitter.configuration.CommunicationConfiguration;
import com.kt.kafkatransmitter.configuration.ConfigurationGetter;

import javax.naming.CommunicationException;
import java.util.Objects;

public class ClientSocketFactory {

    public static AbstractSocket getNextDestination() throws CommunicationException {
        CommunicationConfiguration clientConfiguration = ConfigurationGetter.getNextClientConfiguration();
        AbstractSocket genericSocket;
        switch (Objects.requireNonNull(clientConfiguration).getType()) {
            case AbstractSocket.UDP_TYPE:
                genericSocket = new UdpSocket();
                break;
            case AbstractSocket.TCP_TYPE:
                genericSocket = new TcpSocket();
                break;
            default:
                throw new CommunicationException();
        }
        genericSocket.setHost(clientConfiguration.getHost());
        genericSocket.setPort(clientConfiguration.getPort());
        genericSocket.configurationId = clientConfiguration.getId();
        return genericSocket;
    }

}
