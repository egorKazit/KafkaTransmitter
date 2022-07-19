package com.kt.kafkatransmitter.balancer;

import com.kt.kafkatransmitter.model.AbstractEntity;

/**
 * Puller queue.
 * It allows to put entity into pull queue
 */
public interface PullerQueue {

    /**
     * Method to put on queue
     *
     * @param entity abstract entity
     */
    void putEntityInQueue(AbstractEntity entity);

    /**
     * A method for scheduler
     */
    void handleQueue();

}
