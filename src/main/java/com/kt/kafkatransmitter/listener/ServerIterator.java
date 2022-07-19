package com.kt.kafkatransmitter.listener;

import org.springframework.integration.dsl.IntegrationFlow;

/**
 * Server iterator
 */
public interface ServerIterator {

    /**
     * "Has next" method
     *
     * @return true if the next exists
     */
    boolean hasNext();

    /**
     * "Get next" method
     *
     * @return integration flow
     */
    IntegrationFlow getNext();

}
