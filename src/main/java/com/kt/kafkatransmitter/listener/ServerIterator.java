package com.kt.kafkatransmitter.listener;

import org.springframework.integration.dsl.IntegrationFlow;

public interface ServerIterator {
    boolean hasNext();
    IntegrationFlow getNext();
}
