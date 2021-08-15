package com.kt.kafkatransmitter.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.messaging.MessageHeaders;

import java.io.IOException;

public abstract class AbstractSocket {
    public static final String TCP_TYPE = "tcp";
    public static final String UDP_TYPE = "udp";
    @Setter
    protected MessageHeaders messageHeaders;
    @Setter
    protected String host;
    @Setter
    protected int port;
    @Getter
    int configurationId;

    abstract public void send(String message);

}
