package com.kt.kafkatransmitter.model;

import lombok.Getter;

import java.util.ArrayList;

public enum Topic {
    User(new ArrayList<>()),
    Generic(new ArrayList<>());

    @Getter
    private final ArrayList<String> fields;

    Topic(ArrayList<String> fields) {
        this.fields = fields;
    }

}
