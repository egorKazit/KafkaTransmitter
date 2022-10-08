package com.kt.kafkatransmitter.client;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class TcpSocketTest {

    private static final int PORT = 10101;
    private static Thread server;
    public static TcpSocket tcpSocket;

    @BeforeAll
    public static void globalSetUp() {
        tcpSocket = new TcpSocket();
        server = new Thread(() -> {
            try {
                new LocalServer().start();
            } catch (IOException ignored) {
            }
        });
        server.start();
    }

    @AfterAll
    public static void teardown() {
        LocalServer.isAlive = false;
    }

    @Test
    @Disabled
    public void checkSocketSendsNonEmptyString() {
        tcpSocket.setHost("localhost");
        tcpSocket.setPort(PORT);
        String stringIn = "TestStringIn";
        assertTrue(tcpSocket.send(stringIn));
        assertFalse(tcpSocket.getMessage().isEmpty());
        assertEquals(tcpSocket.getMessage(), stringIn + "\r");
    }

    @Test
    public void checkOnClosedPort() {
        tcpSocket.setHost("localhost");
        tcpSocket.setPort(0);
        String stringIn = "TestStringIn";
        assertFalse(tcpSocket.send(stringIn));
        assertTrue(tcpSocket.getMessage().isEmpty());
    }

    private static class LocalServer {
        private static boolean isAlive;
        private final ServerSocket serverSocket;

        private LocalServer() throws IOException {
            serverSocket = new ServerSocket(PORT);
            isAlive = true;
        }

        public void start() throws IOException {
            while (isAlive) {
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

}