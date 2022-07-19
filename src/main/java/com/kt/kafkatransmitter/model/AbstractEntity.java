package com.kt.kafkatransmitter.model;

import java.io.Serializable;

/**
 * Abstract entity.
 * It is general entity that should be a parent on any entity
 */
public interface AbstractEntity extends Serializable {

    /**
     * Method to handle string.
     * For example, assign value to some attribute
     *
     * @param value string value
     * @return abstract entity
     */
    AbstractEntity handleString(String value);

}
