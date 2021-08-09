package com.kt.kafkatransmitter.client;

import com.kt.kafkatransmitter.balancer.InternalQueueProducer;
import com.kt.kafkatransmitter.util.ConfigurationReader;
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
            //communicationConfiguration = ConfigurationReader.g
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
            } catch (IOException e) {
                InternalQueueProducer.putEntityInQueue(communicationConfiguration.getId(), message);
            }
        }
    }

}
