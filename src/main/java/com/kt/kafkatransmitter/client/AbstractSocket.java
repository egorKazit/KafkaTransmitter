package com.kt.kafkatransmitter.client;

import lombok.Setter;
import org.springframework.messaging.MessageHeaders;

public abstract class AbstractSocket {
    public static final String TCP_TYPE = "tcp";
    public static final String UDP_TYPE = "udp";
    @Setter
    protected MessageHeaders messageHeaders;
    @Setter
    protected String host;
    @Setter
    protected int port;
}
