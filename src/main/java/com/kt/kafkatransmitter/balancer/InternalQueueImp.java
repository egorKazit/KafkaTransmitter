package com.kt.kafkatransmitter.balancer;

import com.kt.kafkatransmitter.client.ClientSocketFactory;
import com.kt.kafkatransmitter.configuration.ConfigurationGetter;
import com.kt.kafkatransmitter.model.AbstractEntity;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

@Log4j2
@Service
class InternalQueueImp implements InternalQueue {
    private static final Queue<AbstractEntity> entityQueue = new LinkedBlockingDeque<>();

    @Override
    public void putEntityInQueue(AbstractEntity entity) {
        log.trace("Adding entity {}", entity.toString());
        entityQueue.add(entity);
    }

    @Scheduled(fixedDelay = 10 * 1000)
    @Override
    public void handleQueue() {
        log.trace("Handle {} entries from internal rows", entityQueue.size());
        while (!entityQueue.isEmpty()) {
            ClientSocketFactory.sendEntityToNextDestination(entityQueue.poll());
        }
    }
}
