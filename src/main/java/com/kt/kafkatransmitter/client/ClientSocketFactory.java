package com.kt.kafkatransmitter.client;

import com.kt.kafkatransmitter.configuration.CommunicationConfiguration;
import com.kt.kafkatransmitter.configuration.ConfigurationGetter;
import com.kt.kafkatransmitter.model.AbstractEntity;
import org.springframework.messaging.MessageHeaders;

import javax.naming.CommunicationException;
import java.io.IOException;
import java.util.HashMap;

public class ClientSocketFactory {

    public static void sendEntityToNextDestination(AbstractEntity entity) {
        new Thread(() -> new Socket().sendData(entity.toString(), new MessageHeaders(new HashMap<>()))).start();
    }

    private static class Socket {
        CommunicationConfiguration clientConfiguration;

        private Socket() {
            clientConfiguration = ConfigurationGetter.getNextClientConfiguration();
        }

        public void sendData(String message, MessageHeaders messageHeaders) {
            try {
                switch (clientConfiguration.getType()) {
                    case AbstractSocket.UDP_TYPE:
                        UdpSocket udpSocket = new UdpSocket();
                        udpSocket.setMessageHeaders(messageHeaders);
                        udpSocket.setHost(clientConfiguration.getHost());
                        udpSocket.setPort(clientConfiguration.getPort());
                        udpSocket.send(message);
                        break;
                    case AbstractSocket.TCP_TYPE:
                        TcpSocket tcpSocket = new TcpSocket();
                        tcpSocket.setMessageHeaders(messageHeaders);
                        tcpSocket.setHost(clientConfiguration.getHost());
                        tcpSocket.setPort(clientConfiguration.getPort());
                        tcpSocket.send(message);
                        break;
                    default:
                        throw new CommunicationException();
                }
            } catch (IOException | CommunicationException e) {
            }
        }
    }

}
