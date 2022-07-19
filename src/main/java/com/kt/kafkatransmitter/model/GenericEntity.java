package com.kt.kafkatransmitter.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Generic entry
 */
@Data
@EqualsAndHashCode(callSuper = false)
class GenericEntity implements AbstractEntity {
    private String genericValue;

    @Override
    public AbstractEntity handleString(String value) {
        this.genericValue = value;
        return this;
    }
}
