package com.kt.kafkatransmitter.kafka;

import com.kt.kafkatransmitter.model.AbstractEntity;
import com.kt.kafkatransmitter.util.JavaSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Kafka producer
 */
@Service
public class ProducerImp extends AbstractKafkaConfig implements Producer {

    private static final KafkaTemplate<Long, AbstractEntity> kafkaTemplate;

    static {
        kafkaTemplate
                = new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(getConfig()));
        kafkaTemplate.setMessageConverter(new StringJsonMessageConverter());
    }

    /**
     * See Producer.send
     *
     * @param topic          kafka topic
     * @param abstractEntity abstract entity
     */
    @Override
    public void send(String topic, AbstractEntity abstractEntity) {
        kafkaTemplate.send(topic, abstractEntity);
    }

    /**
     * Method to get config
     *
     * @return map with kafka configuration
     */
    protected static Map<String, Object> getConfig() {
        Map<String, Object> props = AbstractKafkaConfig.getConfig();
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JavaSerializer.class);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "server.broadcast");
        return props;
    }
}
