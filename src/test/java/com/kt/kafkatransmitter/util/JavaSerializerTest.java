package com.kt.kafkatransmitter.util;

import com.kt.kafkatransmitter.model.AbstractEntity;
import com.kt.kafkatransmitter.model.EntityFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class JavaSerializerTest {

    private JavaSerializer javaSerializer;

    @BeforeEach
    public void setUp() {
        javaSerializer = new JavaSerializer();
    }

    @Test
    public void checkSerialization() {
        AbstractEntity entity = EntityFactory.getEntityByTopicName("Generic");
        byte[] serializedData = javaSerializer.serialize("Generic", entity);
        assertNotNull(serializedData);
    }

    @Test
    public void checkSerializationWithError() {
        AbstractEntity entity = new AbstractEntity() {
            @Override
            public AbstractEntity handleString(String values) {
                return this;
            }
        };
        try {
            javaSerializer.serialize("Generic", entity);
        } catch (IllegalStateException e) {
            return;
        }
        fail();
    }

    @Test
    public void checkEmptyMethods() {
        javaSerializer.configure(new HashMap<>(), true);
        javaSerializer.close();
    }

}