package com.arch.archbalancer.listener.udp;

import org.springframework.messaging.MessageHeaders;

import javax.naming.CommunicationException;
import java.io.IOException;

public interface ServerHandler {
    void handleMessage(byte[] message, MessageHeaders messageHeaders) throws IOException, CommunicationException;
}
