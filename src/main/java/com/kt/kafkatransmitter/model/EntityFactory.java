package com.kt.kafkatransmitter.model;

public final class EntityFactory {

    public static final String GENERIC_ENTITY = "GENERIC_ENTITY";
    public static final String USER_ENTITY = "USER_ENTITY";

    private EntityFactory() {

    }

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
