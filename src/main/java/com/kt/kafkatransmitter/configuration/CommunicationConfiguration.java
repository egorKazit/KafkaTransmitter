package com.kt.kafkatransmitter.configuration;

import lombok.Getter;

public class CommunicationConfiguration {
    @Getter
    int Id;
    @Getter
    String host;
    @Getter
    int port;
    @Getter
    String type;
}
