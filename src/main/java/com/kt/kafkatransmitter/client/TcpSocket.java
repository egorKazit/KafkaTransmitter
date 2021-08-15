package com.kt.kafkatransmitter.client;

import lombok.extern.log4j.Log4j2;

import javax.net.SocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

@Log4j2
class TcpSocket extends AbstractSocket {

    TcpSocket() {
    }

    @Override
    public void send(String message) {
        log.info("Start sending to host {} port {}...", host, port);
        try (Socket socket = SocketFactory
                .getDefault()
                .createSocket(host, port)) {
            socket.setSoTimeout(1000);
            socket.getOutputStream().write((message + "\r\n").getBytes());
            log.debug("Message {} was sent sent", message);
            //BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String responseLine;
            StringBuilder responseBuilder = new StringBuilder();
            log.debug("Waiting for response");
            InputStream inputStream = socket.getInputStream();
            while (true) {
                int i = inputStream.read();
                if (i == -1) break;
                char character = (char) i;
                if (character == '\n') break;
                responseBuilder.append(character);
            }
            System.out.println(responseBuilder);
        } catch (IOException e) {
            log.error("Error during sending: {}; Cause: {}", e.getMessage(), e.getCause());
            log.debug("The error is going to be re-thrown");
        }
    }

}
