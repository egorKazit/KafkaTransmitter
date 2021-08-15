package com.kt.kafkatransmitter.listener;

import com.kt.kafkatransmitter.kafka.Producer;
import org.apache.tomcat.util.json.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.MessageHeaders;

import javax.naming.CommunicationException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ServerHandlerMultiTonTest {

    private static final String TEST_TOPIC = "Generic";

    private static final Producer producer = (topic, message) -> {
        assertEquals(TEST_TOPIC, topic);
    };

    @Test
    public void checkInstantiation() {
        ServerHandler serverHandlerFirstInstance = ServerHandlerMultiTon.getServerHandler("1");
        ServerHandler serverHandlerSecondInstance = ServerHandlerMultiTon.getServerHandler("1");
        assertEquals(serverHandlerFirstInstance, serverHandlerSecondInstance);
    }

    @Test
    public void checkHandling() throws JSONException, CommunicationException, IOException, ParseException, NoSuchFieldException, IllegalAccessException {
        ServerHandler serverHandler = ServerHandlerMultiTon.getServerHandler("1");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("topic", TEST_TOPIC);
        jsonObject.put("message", "message");
        Field producerField = ServerHandlerMultiTon.class.getDeclaredField("producer");
        producerField.setAccessible(true);
        producerField.set(serverHandler, producer);
        serverHandler.handleMessage(jsonObject.toString().getBytes(), new MessageHeaders(new HashMap<>()));
    }

    @Test
    public void checkHandlingWithError() throws NoSuchFieldException, IllegalAccessException {
        ServerHandler serverHandler = ServerHandlerMultiTon.getServerHandler("1");
        Field producerField = ServerHandlerMultiTon.class.getDeclaredField("producer");
        producerField.setAccessible(true);
        producerField.set(serverHandler, producer);
        try {
            serverHandler.handleMessage("".getBytes(), new MessageHeaders(new HashMap<>()));
        } catch (IOException | CommunicationException | ParseException | JSONException e) {
            assertTrue(e instanceof JSONException);
        }
    }

}