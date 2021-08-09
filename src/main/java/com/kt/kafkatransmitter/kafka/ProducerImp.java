package com.kt.kafkatransmitter.kafka;

import com.kt.kafkatransmitter.model.AbstractEntity;
import com.kt.kafkatransmitter.util.JavaSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProducerImp implements Producer {

    @Value("${kafka.server}")
    private static String kafkaServer;

    @Value("${kafka.producer.id}")
    private static String kafkaProducerId;

    private static final KafkaTemplate<Long, AbstractEntity> kafkaTemplate;

    static {
        kafkaTemplate
                = new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(producerConfigs()));
        kafkaTemplate.setMessageConverter(new StringJsonMessageConverter());
    }

    @Override
    public void send(String topic, AbstractEntity message) {
        kafkaTemplate.send(topic, message);
    }

    private static Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");//kafkaServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JavaSerializer.class);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "server.broadcast");
        return props;
    }

}
