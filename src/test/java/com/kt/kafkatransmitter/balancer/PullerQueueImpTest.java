package com.kt.kafkatransmitter.balancer;

import com.kt.kafkatransmitter.model.AbstractEntity;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class PullerQueueImpTest {

    @Test
    public void checkAddingToQueue() throws NoSuchFieldException, IllegalAccessException, InterruptedException {
        Field pullThreadField = Puller.class.getDeclaredField("pullThread");
        pullThreadField.setAccessible(true);

        Field modifiers = Field.class.getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        modifiers.set(pullThreadField, pullThreadField.getModifiers() & ~Modifier.FINAL);

        ((Thread) pullThreadField.get(Puller.class)).stop();

        AbstractEntity abstractEntity = Mockito.mock(AbstractEntity.class);
        PullerQueueImp pullerQueueImp = (PullerQueueImp) PullerQueueFactory.getInternalQueue();
        pullerQueueImp.putEntityInQueue(abstractEntity);
        Field entityQueueField = pullerQueueImp.getClass().getDeclaredField("entityQueue");
        entityQueueField.setAccessible(true);
        Queue<AbstractEntity> entityQueue = ((Queue<AbstractEntity>) entityQueueField.get(pullerQueueImp));

        Field processQueueField = Puller.class.getDeclaredField("processQueue");
        processQueueField.setAccessible(true);
        LinkedHashMap<Integer, Queue<Thread>> processQueue = (LinkedHashMap<Integer, Queue<Thread>>) processQueueField.get(Puller.class);

        boolean isInPullerQueue = false;
        for (Integer id : processQueue.keySet()) {
            isInPullerQueue = !processQueue.get(id).isEmpty();
            if (isInPullerQueue) break;
        }

        assertFalse(entityQueue.isEmpty() || isInPullerQueue);

        Thread.sleep(15000);
        for (Integer id : processQueue.keySet()) {
            isInPullerQueue = !processQueue.get(id).isEmpty();
            if (isInPullerQueue) break;
        }
        assertTrue(isInPullerQueue);

    }


}