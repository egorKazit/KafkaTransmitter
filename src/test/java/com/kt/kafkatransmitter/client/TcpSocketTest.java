package com.kt.kafkatransmitter.client;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class TcpSocketTest {

    private static final int PORT = 10101;
    private Thread server;
    public TcpSocket tcpSocket;

    @BeforeEach
    public void setUp() {
        tcpSocket = new TcpSocket();
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

//    @Test
//    public void checkSocketSendsEmptyString() {
//        tcpSocket.setHost("localhost");
//        tcpSocket.setPort(PORT);
//        assertFalse(tcpSocket.send(""));
//        assertTrue(tcpSocket.getMessage().isEmpty());
//    }
//
//    @Test
//    public void checkSocketSendsNonEmptyString() {
//        tcpSocket.setHost("localhost");
//        tcpSocket.setPort(PORT);
//        String stringIn = "TestStringIn";
//        assertTrue(tcpSocket.send(stringIn));
//        assertFalse(tcpSocket.getMessage().isEmpty());
//        assertEquals(tcpSocket.getMessage(), stringIn + "\r");
//    }
//
//    @Test
//    public void checkOnClosedPort() {
//        tcpSocket.setHost("localhost");
//        tcpSocket.setPort(0);
//        String stringIn = "TestStringIn";
//        assertFalse(tcpSocket.send(stringIn));
//        assertTrue(tcpSocket.getMessage().isEmpty());
//    }

    private static class LocalServer {
        private final ServerSocket serverSocket;

        private LocalServer() throws IOException {
            serverSocket = new ServerSocket(PORT);
        }

        public void start() throws IOException {
            Socket socketFromClient = serverSocket.accept();
            InputStream inputStream = socketFromClient.getInputStream();
            StringBuilder requestBuilder = new StringBuilder();
            while (true) {
                int i = inputStream.read();
                if (i == -1) break;
                char character = (char) i;
                if (character == '\n') break;
                requestBuilder.append(character);
            }
            socketFromClient.getOutputStream().write(requestBuilder.toString().getBytes());
            socketFromClient.getOutputStream().flush();
            socketFromClient.close();
        }

    }

}