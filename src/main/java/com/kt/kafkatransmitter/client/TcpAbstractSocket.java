package com.kt.kafkatransmitter.client;

import lombok.extern.log4j.Log4j2;

import javax.net.SocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

@Log4j2
public class TcpAbstractSocket extends AbstractSocket {

    TcpAbstractSocket() {
    }

    public void send(String message) throws IOException {
        log.info("Start sending to host {} port {}...", host, port);
        try (Socket socket = SocketFactory
                .getDefault()
                .createSocket(host, port)) {
            socket.setSoTimeout(1000);
            socket.getOutputStream().write((message + "\r\n").getBytes());
            log.debug("Message {} was sent sent", message);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String responseLine;
            StringBuilder responseBuilder = new StringBuilder();
            log.debug("Waiting for response");
            while ((responseLine = bufferedReader.readLine()) != null) {
                responseBuilder.append(responseLine);
            }
            if (responseBuilder.length() == 0) {
                log.debug("Empty response is very bad sign");
                throw new IOException();
            }
            // handle specific response
//            if(!responseBuilder.toString().contains(""))
//                throw new IOException();
        } catch (IOException e) {
            log.error("Error during sending: {}; Cause: {}", e.getMessage(), e.getCause());
            log.debug("The error is going to be re-thrown");
            throw e;
        }
    }

}
