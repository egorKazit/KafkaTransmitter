package com.kt.kafkatransmitter.client;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import static org.junit.jupiter.api.Assertions.*;

class UdpSocketTest {

    private static final int PORT = 10102;
    private Thread server;
    private UdpSocket udpSocket;

    @BeforeEach
    public void setUp() {
        udpSocket = new UdpSocket();
        server = new Thread(() -> {
            try {
                new LocalServer().start();
            } catch (IOException ignored) {
            }
        });
        server.start();
    }

    @AfterEach
    public void teardown() {
        server.interrupt();
    }

    @Test
    public void checkSocketSendsNonEmptyData() {
        udpSocket.setHost("localhost");
        udpSocket.setPort(PORT);
        String stringIn = "TestStringIn";
        assertTrue(udpSocket.send(stringIn));
        assertFalse(udpSocket.getMessage().isEmpty());
        assertTrue(udpSocket.getMessage().contains(stringIn));
    }

    @Test
    public void checkOnClosedPort() {
        udpSocket.setHost("localhost");
        udpSocket.setPort(0);
        String stringIn = "TestStringIn";
        assertFalse(udpSocket.send(stringIn));
        assertTrue(udpSocket.getMessage().isEmpty());
    }

    private static class LocalServer {
        private final DatagramSocket datagramSocket;

        private LocalServer() throws SocketException {
            datagramSocket = new DatagramSocket(PORT);
        }

        private void start() throws IOException {
            byte[] buf = new byte[256];
            DatagramPacket datagramPacket = new DatagramPacket(buf, 256);
            datagramSocket.receive(datagramPacket);
            InetAddress clientAddress = datagramPacket.getAddress();
            int clientPort = datagramPacket.getPort();
            DatagramPacket response = new DatagramPacket(buf, buf.length, clientAddress, clientPort);
            datagramSocket.send(response);
        }

    }

}