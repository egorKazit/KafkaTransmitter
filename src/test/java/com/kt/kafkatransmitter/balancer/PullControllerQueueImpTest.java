package com.kt.kafkatransmitter.balancer;

import com.kt.kafkatransmitter.configuration.ConfigurationGetter;
import com.kt.kafkatransmitter.model.AbstractEntity;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.awaitility.Awaitility;
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
class PullControllerQueueImpTest {

    @Test
    public void checkAddingToQueue() throws NoSuchFieldException, IllegalAccessException, InterruptedException {
        Field executorServiceController = PullController.class.getDeclaredField("executorServiceController");
        executorServiceController.setAccessible(true);

        Field modifiers = Field.class.getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        modifiers.set(executorServiceController, executorServiceController.getModifiers() & ~Modifier.FINAL);

        AbstractEntity abstractEntity = Mockito.mock(AbstractEntity.class);
        PullerQueueImp pullerQueueImp = (PullerQueueImp) PullerQueueFactory.getInternalQueue();
        pullerQueueImp.putEntityInQueue(abstractEntity);
        Field entityQueueField = pullerQueueImp.getClass().getDeclaredField("entityQueue");
        entityQueueField.setAccessible(true);
        Queue<AbstractEntity> entityQueue = ((Queue<AbstractEntity>) entityQueueField.get(pullerQueueImp));

        Field processQueueField = PullController.class.getDeclaredField("processQueue");
        processQueueField.setAccessible(true);
        LinkedHashMap<Integer, Queue<PullerWorker>> processQueue = (LinkedHashMap<Integer, Queue<PullerWorker>>) processQueueField.get(PullController.class);

        AtomicBoolean checker = new AtomicBoolean(false);

        int id = ConfigurationGetter.getNextClientConfiguration().getId();
        processQueue.get(id).add(new PullerWorker() {

            @Override
            public void run() {
                checker.set(true);
            }

            @Override
            public int getConfigurationId() {
                return id;
            }
        });

        Awaitility.await().atMost(10, TimeUnit.SECONDS)
                .until(() -> checker.get());

    }

}