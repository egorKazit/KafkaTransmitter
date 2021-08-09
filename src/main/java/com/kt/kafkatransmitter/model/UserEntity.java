package com.kt.kafkatransmitter.model;

public class UserEntity extends  AbstractEntity{
    @Override
    public AbstractEntity handleString(String values) {
        return this;
    }
}
