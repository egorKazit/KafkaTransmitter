package com.kt.kafkatransmitter.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Entity factory.
 * It allows to produce abstract entity
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EntityFactory {

    /**
     * Method to get entity by topic
     *
     * @param topicName topic name
     * @return abstract entity
     */
    public static AbstractEntity getEntityByTopicName(String topicName) {
        switch (Topic.valueOf(topicName)) {
            case Generic:
                return new GenericEntity();
            case User:
                return new UserEntity();
            default:
                return new GenericEntity().handleString("");
        }
    }
}
