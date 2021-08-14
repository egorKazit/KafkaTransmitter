package com.kt.kafkatransmitter.listener;

import org.apache.tomcat.util.json.ParseException;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.messaging.MessageHeaders;

import javax.naming.CommunicationException;
import java.io.IOException;

public interface ServerHandler {
    void handleMessage(byte[] message, MessageHeaders messageHeaders) throws IOException, CommunicationException, ParseException, JSONException;
}
