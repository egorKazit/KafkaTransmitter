package com.kt.kafkatransmitter.kafka;

import com.kt.kafkatransmitter.balancer.PullerQueueFactory;
import com.kt.kafkatransmitter.model.AbstractEntity;
import com.kt.kafkatransmitter.util.JavaDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ConsumerImp extends AbstractKafkaConfig implements Consumer {

    private static final KafkaMessageListenerContainer<Long, String> kafkaMessageContainer;
    private static final ContainerProperties containerProperties;

    static {
        containerProperties = new ContainerProperties("Generic");
        containerProperties.setMessageListener((MessageListener<Object, Object>) objectObjectConsumerRecord ->
                PullerQueueFactory.getInternalQueue().putEntityInQueue((AbstractEntity) objectObjectConsumerRecord.value()));
        kafkaMessageContainer = new KafkaMessageListenerContainer<>(new DefaultKafkaConsumerFactory<>(getConfig()), containerProperties);
        kafkaMessageContainer.start();
    }

    protected static Map<String, Object> getConfig() {
        Map<String, Object> props = AbstractKafkaConfig.getConfig();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "server.broadcast");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JavaDeserializer.class);
        return props;
    }

}
