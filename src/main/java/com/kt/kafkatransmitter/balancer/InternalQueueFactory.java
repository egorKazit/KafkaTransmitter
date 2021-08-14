package com.kt.kafkatransmitter.balancer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InternalQueueFactory {
    @Autowired
    private static InternalQueue internalQueue;

    @Autowired
    InternalQueueFactory(InternalQueue internalQueue) {
        InternalQueueFactory.internalQueue = internalQueue;
    }

    public static InternalQueue getInternalQueue() {
        return internalQueue;
    }

}
