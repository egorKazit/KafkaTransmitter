package com.kt.kafkatransmitter.configuration;

import lombok.Getter;

/**
 * Communication configuration
 */
@Getter
public class CommunicationConfiguration {

    int id;
    String host;
    int port;
    String type;
}
