package com.kt.kafkatransmitter.client;

import com.kt.kafkatransmitter.configuration.CommunicationConfiguration;
import com.kt.kafkatransmitter.configuration.ConfigurationGetter;

import javax.naming.CommunicationException;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Socket factory.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientSocketFactory {

    /**
     * Method to get a socket from next destination configuration
     *
     * @return abstract socket
     * @throws CommunicationException exception on connection or if type can not be recognized
     */
    public static AbstractSocket getNextDestination() throws CommunicationException {
        // get configuration
        CommunicationConfiguration clientConfiguration = ConfigurationGetter.getNextClientConfiguration();
        AbstractSocket genericSocket;
        // create new socket based on type
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
        // set destination host, port and configuration id
        genericSocket.setHost(clientConfiguration.getHost());
        genericSocket.setPort(clientConfiguration.getPort());
        genericSocket.configurationId = clientConfiguration.getId();
        return genericSocket;
    }

}
