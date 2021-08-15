package com.kt.kafkatransmitter.balancer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PullerQueueFactory {
    @Autowired
    private static PullerQueue pullerQueue;

    @Autowired
    PullerQueueFactory(PullerQueue pullerQueue) {
        PullerQueueFactory.pullerQueue = pullerQueue;
    }

    public static PullerQueue getInternalQueue() {
        return pullerQueue;
    }

}
