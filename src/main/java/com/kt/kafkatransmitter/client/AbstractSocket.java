package com.kt.kafkatransmitter.client;

import lombok.Setter;
import org.springframework.messaging.MessageHeaders;

public abstract class AbstractSocket {
    @Setter
    protected MessageHeaders messageHeaders;
    @Setter
    protected String host;
    @Setter
    protected int port;
}
