package com.kt.kafkatransmitter.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
class GenericEntity extends AbstractEntity {
    private String genericValue;

    @Override
    public AbstractEntity handleString(String values) {
        this.genericValue = values;
        return this;
    }
}
