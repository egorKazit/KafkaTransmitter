package com.arch.archbalancer.client;

import com.arch.archbalancer.balancer.DestinationHealthHandler;
import com.arch.archbalancer.balancer.InternalQueueProducer;
import com.arch.archbalancer.util.ConfigurationReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;

import javax.naming.CommunicationException;
import java.io.IOException;

public class ClientSocketFactory {

    @Autowired
    static InternalQueueProducer internalQueueProducer;

    public static Socket createSocketForDestination() {
        return new Socket();
    }

    public static class Socket {
        ConfigurationReader.CommunicationConfiguration communicationConfiguration;

        private Socket() {
            communicationConfiguration = DestinationHealthHandler.getFirstReachableDestination();
        }

        public void sendData(String message, MessageHeaders messageHeaders) throws CommunicationException {
            try {
                switch (communicationConfiguration.getType()) {
                    case "udp":
                        UdpSocket udpSocket = new UdpSocket();
                        udpSocket.setMessageHeaders(messageHeaders);
                        udpSocket.setHost(communicationConfiguration.getHost());
                        udpSocket.setPort(communicationConfiguration.getPort());
                        udpSocket.send(message);
                        break;
                    case "tcp":
                        TcpAbstractSocket tcpSocket = new TcpAbstractSocket();
                        tcpSocket.setMessageHeaders(messageHeaders);
                        tcpSocket.setHost(communicationConfiguration.getHost());
                        tcpSocket.setPort(communicationConfiguration.getPort());
                        tcpSocket.send(message);
                        break;
                    default:
                        throw new CommunicationException();
                }
                DestinationHealthHandler.setDestinationHealth(communicationConfiguration.getId(), true);
            } catch (IOException e) {
                DestinationHealthHandler.setDestinationHealth(communicationConfiguration.getId(), false);
                InternalQueueProducer.putEntityInQueue(communicationConfiguration.getId(), message);
            }
        }
    }

}
