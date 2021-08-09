package com.kt.kafkatransmitter.kafka;

import com.kt.kafkatransmitter.model.AbstractEntity;

public interface Producer {
    void send(String topic, AbstractEntity message);

}
