package com.kt.kafkatransmitter.listener;

import com.kt.kafkatransmitter.kafka.Producer;
import com.kt.kafkatransmitter.model.EntityFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Server MultiTon
 */
@Service
@Log4j2
public class ServerHandlerMultiTon {

    @Autowired
    private static Producer producer;

    private static final HashMap<String, ServerHandler> serverHandlers = new HashMap<>();

    /**
     * Main constructor
     *
     * @param producer kafka producer
     */
    @Autowired
    ServerHandlerMultiTon(Producer producer) {
        ServerHandlerMultiTon.producer = producer;
    }

    /**
     * Method to get server handler
     *
     * @param handlerId handler id
     * @return server handler
     */
    static ServerHandler getServerHandler(String handlerId) {
        return serverHandlers.computeIfAbsent(handlerId, (key) -> new ServerHandlerImp());
    }

    /**
     * Server handler
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Service
    public static class ServerHandlerImp implements ServerHandler {

        @Override
        public void handleMessage(byte[] message, MessageHeaders messageHeaders) {
            String s = new String(message);
            try {
                JSONObject parsedMessage = new JSONObject(s);
                String topicFromInput = parsedMessage.getString("topic");
                String messageFromInput = parsedMessage.getString("message");
                producer.send(topicFromInput, EntityFactory.getEntityByTopicName(topicFromInput).handleString(messageFromInput));
            } catch (JSONException e) {
                log.error("Error during serialization {}", e.getMessage());
            }
        }
    }
}
