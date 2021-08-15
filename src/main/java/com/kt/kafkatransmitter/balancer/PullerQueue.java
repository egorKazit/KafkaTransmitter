package com.kt.kafkatransmitter.balancer;

import com.kt.kafkatransmitter.model.AbstractEntity;

public interface PullerQueue {
    void putEntityInQueue(AbstractEntity entity);

    void handleQueue();
}
