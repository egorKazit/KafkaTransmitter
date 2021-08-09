package com.kt.kafkatransmitter.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Data
public abstract class AbstractEntity implements Serializable {

    public abstract AbstractEntity handleString(String values);
}
