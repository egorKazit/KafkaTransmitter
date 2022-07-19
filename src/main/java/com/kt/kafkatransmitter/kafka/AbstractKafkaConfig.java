package com.kt.kafkatransmitter.kafka;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka configuration
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractKafkaConfig {

    /**
     * Method to get config
     *
     * @return map with kafka configuration
     */
    protected static Map<String, Object> getConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        return props;
    }

}
