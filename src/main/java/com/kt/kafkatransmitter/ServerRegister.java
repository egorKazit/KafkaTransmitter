package com.kt.kafkatransmitter;

import com.kt.kafkatransmitter.listener.ServerIterator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.dsl.context.IntegrationFlowContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Server register.
 * It goes thought server configurations and creates communication flow
 */
@Component
@Log4j2
public class ServerRegister {

    private static final String INTEGRATION_FLOW_PREFIX = "INTEGRATION_FLOW_PREFIX_";

    @Autowired
    private IntegrationFlowContext integrationFlowContext;

    @Autowired
    private ServerIterator serverIterator;

    /**
     * Post-construct to create bean per each configuration
     */
    @PostConstruct
    void registerIntegrations() {
        int index = 0;
        while (serverIterator.hasNext()) {
            index++;
            // perform registration
            integrationFlowContext.registration(serverIterator.getNext()).id(INTEGRATION_FLOW_PREFIX + index).register();
            log.info("Listener {} is started", INTEGRATION_FLOW_PREFIX + index);
        }

    }

}
