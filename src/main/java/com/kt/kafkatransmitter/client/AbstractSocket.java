package com.kt.kafkatransmitter.client;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.InputStream;

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

    public String getMessage() {
        return messageBuilder.toString();
    }

    abstract public boolean send(String message);

    protected void fillOutMessage(InputStream inputStream) throws IOException {
        while (true) {
            int i = inputStream.read();
            if (i == -1) break;
            char character = (char) i;
            if (character == '\n') break;
            messageBuilder.append(character);
        }
    }

}
