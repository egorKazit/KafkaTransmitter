package com.kt.kafkatransmitter.client;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.InputStream;

/**
 * Abstract socket to any destination.
 * Currently, it supports tcp and udp
 */
public abstract class AbstractSocket {
    public static final String TCP_TYPE = "tcp";
    public static final String UDP_TYPE = "udp";
    protected final StringBuilder messageBuilder = new StringBuilder();
    @Setter
    protected String host;
    @Setter
    protected int port;
    @Getter
    int configurationId;

    /**
     * Method to read received message
     *
     * @return message
     */
    public String getMessage() {
        return messageBuilder.toString();
    }

    /**
     * Own implementation of sending data that depends on connection
     *
     * @param message message to send
     * @return true if sent
     */
    public abstract boolean send(String message);

    /**
     * Method to read data from stream
     *
     * @param inputStream input stream
     * @throws IOException exception on read
     */
    protected void readDataFromStream(InputStream inputStream) throws IOException {
        while (true) {
            int i = inputStream.read();
            if (i == -1 || (char) i == '\n') break;
            messageBuilder.append((char) i);
        }
    }

}
