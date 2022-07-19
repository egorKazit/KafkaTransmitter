package com.kt.kafkatransmitter.kafka;

import com.kt.kafkatransmitter.model.AbstractEntity;

/**
 * Kafka producer interface
 */
public interface Producer {
    /**
     * Method to send abstract entity to topic
     *
     * @param topic          kafka topic
     * @param abstractEntity abstract entity
     */
    void send(String topic, AbstractEntity abstractEntity);

}
