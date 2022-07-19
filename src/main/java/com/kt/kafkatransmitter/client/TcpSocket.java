package com.kt.kafkatransmitter.client;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.net.SocketFactory;
import java.io.IOException;
import java.net.Socket;

/**
 * TCP socket
 */
@NoArgsConstructor(access = AccessLevel.MODULE)
@Log4j2
class TcpSocket extends AbstractSocket {

    @Override
    public boolean send(String message) {
        log.info("Start sending to host {} port {}...", host, port);
        messageBuilder.setLength(0);
        try (Socket socket = SocketFactory
                .getDefault()
                .createSocket(host, port)) {
            socket.setSoTimeout(10000);
            socket.getOutputStream().write((message + "\r\n").getBytes());
            log.debug("Message {} was sent sent", message);
            log.debug("Waiting for response");
            readDataFromStream(socket.getInputStream());
        } catch (IOException e) {
            log.error("Error during sending: {}; Cause: {}", e.getMessage(), e.getCause());
            log.debug("The error is going to be re-thrown");
            return false;
        }
        return true;
    }

}
