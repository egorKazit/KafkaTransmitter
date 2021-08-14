package com.kt.kafkatransmitter.balancer;

import com.kt.kafkatransmitter.model.AbstractEntity;

public interface InternalQueue {
    void putEntityInQueue(AbstractEntity entity);

    void handleQueue();
}
