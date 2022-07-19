package com.kt.kafkatransmitter.model;

/**
 * User entry
 */
public class UserEntity implements AbstractEntity {
    @Override
    public AbstractEntity handleString(String value) {
        return this;
    }
}
