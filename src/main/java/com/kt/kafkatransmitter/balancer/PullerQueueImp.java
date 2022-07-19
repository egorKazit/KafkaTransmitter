package com.kt.kafkatransmitter.balancer;

import com.kt.kafkatransmitter.model.AbstractEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Implementation of PullerQueue
 */
@Log4j2
@Service
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class PullerQueueImp implements PullerQueue {
    private static final Queue<AbstractEntity> entityQueue = new LinkedBlockingDeque<>();

    /**
     * see PullerQueue.putEntityInQueue
     *
     * @param entity abstract entity
     */
    @Override
    public void putEntityInQueue(AbstractEntity entity) {
        log.trace("Adding entity {}", entity.toString());
        entityQueue.add(entity);
    }

    /**
     * See PullerQueue.handleQueue for definition
     */
    @Scheduled(fixedDelay = 100)
    @Override
    public void handleQueue() {
        log.trace("Handle {} entries from internal rows", entityQueue.size());
        while (!entityQueue.isEmpty()) {
            PullController.addToQueue(entityQueue.poll());
        }
    }
}
