package com.kt.kafkatransmitter.listener;

import com.kt.kafkatransmitter.kafka.Producer;
import com.kt.kafkatransmitter.model.EntityFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ServerHandlerMultiTon {

    @Autowired
    private static Producer producer;

    @Autowired
    ServerHandlerMultiTon(Producer producer) {
        ServerHandlerMultiTon.producer = producer;
    }

    private static final HashMap<String, ServerHandler> serverHandlers = new HashMap<>();

    static ServerHandler getServerHandler(String handlerId) {
        if (!serverHandlers.containsKey(handlerId)) {
            serverHandlers.put(handlerId, new ServerHandlerImp());
        }
        return serverHandlers.get(handlerId);
    }

    @Service
    public static class ServerHandlerImp implements ServerHandler {

        private ServerHandlerImp() {
        }

        @Override
        public void handleMessage(byte[] message, MessageHeaders messageHeaders) throws JSONException {
            String s = new String(message);
            JSONObject parsedMessage = new JSONObject(s);
            String topicFromInput = parsedMessage.getString("topic");
            String messageFromInput = parsedMessage.getString("message");
            producer.send(topicFromInput, EntityFactory.getEntityByTopicName(topicFromInput).handleString(messageFromInput));
        }
    }
}
