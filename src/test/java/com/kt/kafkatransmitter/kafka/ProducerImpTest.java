package com.kt.kafkatransmitter.kafka;

import com.kt.kafkatransmitter.model.AbstractEntity;
import com.kt.kafkatransmitter.model.EntityFactory;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.kafka.core.KafkaTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ProducerImpTest {

    @Test
    public void checkSendMethod() throws NoSuchFieldException, IllegalAccessException {
        AtomicBoolean isCalled = new AtomicBoolean(false);
        KafkaTemplate<Long, AbstractEntity> kafkaTemplate = Mockito.mock(KafkaTemplate.class);
        Mockito.when(kafkaTemplate.send(Mockito.anyString(), Mockito.any())).thenAnswer(invocationOnMock -> {
            isCalled.set(true);
            return null;
        });
        ProducerImp producerImp = new ProducerImp();
        Field kafkaTemplateField = producerImp.getClass().getDeclaredField("kafkaTemplate");
        kafkaTemplateField.setAccessible(true);

        Field modifiers = Field.class.getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        modifiers.set(kafkaTemplateField, kafkaTemplateField.getModifiers() & ~Modifier.FINAL);

        kafkaTemplateField.set(producerImp, kafkaTemplate);
        producerImp.send("Generic", EntityFactory.getEntityByTopicName("Generic"));
        assertTrue(isCalled.get());
    }

}