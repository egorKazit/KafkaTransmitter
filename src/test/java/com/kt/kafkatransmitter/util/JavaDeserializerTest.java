package com.kt.kafkatransmitter.util;

import com.kt.kafkatransmitter.model.AbstractEntity;
import com.kt.kafkatransmitter.model.EntityFactory;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertTrue;

class JavaDeserializerTest {

    private JavaDeserializer javaDeserializer;

    @BeforeEach
    public void setUp() {
        javaDeserializer = new JavaDeserializer();
    }

    @Test
    public void checkDeserialization() {
        AbstractEntity entity = EntityFactory.getEntityByTopicName("Generic");
        byte[] serialize = new JavaSerializer().serialize("Generic", entity);
        Object deserialize = javaDeserializer.deserialize("Generic", serialize);
        assertTrue(deserialize instanceof AbstractEntity);
    }

    @Test
    public void checkDeserializationWithHeaders() {
        AbstractEntity entity = EntityFactory.getEntityByTopicName("Generic");
        byte[] serialize = new JavaSerializer().serialize("Generic", entity);
        Object deserialize = javaDeserializer.deserialize("Generic", new HeadersForTest() {
        }, serialize);
        assertTrue(deserialize instanceof AbstractEntity);
    }

    @Test
    public void checkEmptyMethods() {
        javaDeserializer.close();
    }

    private static class HeadersForTest implements Headers {

        @Override
        public Headers add(Header header) throws IllegalStateException {
            return null;
        }

        @Override
        public Headers add(String s, byte[] bytes) throws IllegalStateException {
            return null;
        }

        @Override
        public Headers remove(String s) throws IllegalStateException {
            return null;
        }

        @Override
        public Header lastHeader(String s) {
            return null;
        }

        @Override
        public Iterable<Header> headers(String s) {
            return null;
        }

        @Override
        public Header[] toArray() {
            return new Header[0];
        }

        @NotNull
        @Override
        public Iterator<Header> iterator() {
            return null;
        }
    }

}