package com.kt.kafkatransmitter.balancer;

import com.kt.kafkatransmitter.client.AbstractSocket;
import com.kt.kafkatransmitter.model.AbstractEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.naming.CommunicationException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

class PullControllerWorkerImpTest {
    private PullerWorkerImp pullerWorkerImp;
    private AbstractSocket genericSocket;
    private AbstractEntity entity;
    private String stringToCheck;

    @BeforeEach
    public void setUp() throws CommunicationException, NoSuchFieldException, IllegalAccessException {
        entity = Mockito.mock(AbstractEntity.class);
        pullerWorkerImp = new PullerWorkerImp(entity);
        genericSocket = Mockito.mock(AbstractSocket.class);
        Mockito.when(genericSocket.send(Mockito.anyString())).thenAnswer(
                invocationOnMock -> {
                    stringToCheck = invocationOnMock.getArgument(0).toString();
                    pullerWorkerImp.interrupt();
                    return null;
                }
        );
        Mockito.when(genericSocket.getConfigurationId()).thenReturn(2004);

        Field genericSocketField = pullerWorkerImp.getClass().getDeclaredField("genericSocket");
        genericSocketField.setAccessible(true);

        Field modifiers = Field.class.getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        modifiers.set(genericSocketField, genericSocketField.getModifiers() & ~Modifier.FINAL);

        genericSocketField.set(pullerWorkerImp, genericSocket);
    }

    @Test
    public void checkSending() throws InterruptedException {
        pullerWorkerImp.start();
        int counter = 0;
        while (pullerWorkerImp.isAlive() && counter < 10) {
            Thread.sleep(1000);
            counter++;
        }
        assertEquals(stringToCheck, entity.toString());
    }

    @Test
    public void checkConfigurationId() {
        assertEquals(2004, pullerWorkerImp.getConfigurationId());
    }

}