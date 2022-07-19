package com.kt.kafkatransmitter.balancer;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Puller queue factory to update a puller queue
 */
@Component
public class PullerQueueFactory {

    @Autowired
    @Getter
    private static PullerQueue internalQueue;

    /**
     * Constructor to autowire the puller queue
     *
     * @param internalQueue the puller queue
     */
    @Autowired
    PullerQueueFactory(PullerQueue internalQueue) {
        PullerQueueFactory.internalQueue = internalQueue;
    }

}
