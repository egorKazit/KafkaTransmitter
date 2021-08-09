package com.kt.kafkatransmitter.listener.udp;

import org.springframework.integration.dsl.IntegrationFlow;

public interface ServerIterator {
    boolean hasNext();
    IntegrationFlow getNext();
}
