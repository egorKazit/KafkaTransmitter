package com.kt.kafkatransmitter.listener;

import org.apache.tomcat.util.json.ParseException;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.messaging.MessageHeaders;

import javax.naming.CommunicationException;
import java.io.IOException;

/**
 * Server handler interface
 */
public interface ServerHandler {
    /**
     * Method to handle message
     *
     * @param message        message
     * @param messageHeaders message header
     * @throws IOException            exception on read
     * @throws CommunicationException exception on communication
     * @throws ParseException         exception on parsing
     * @throws JSONException          exception on JSON parsing
     */
    void handleMessage(byte[] message, MessageHeaders messageHeaders)
            throws IOException, CommunicationException, ParseException, JSONException;
}
