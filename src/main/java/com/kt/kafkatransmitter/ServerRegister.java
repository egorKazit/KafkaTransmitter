package com.kt.kafkatransmitter;

import com.kt.kafkatransmitter.listener.udp.ServerIterator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.dsl.context.IntegrationFlowContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Log4j2
public class ServerRegister {

    private static final String INTEGRATION_FLOW_PREFIX = "INTEGRATION_FLOW_PREFIX_";

    @Autowired
    private IntegrationFlowContext integrationFlowContext;

    @Autowired
    private ServerIterator serverIterator;

    @PostConstruct
    void registerIntegrations() {
        int i = 0;
        while (serverIterator.hasNext()) {
            i++;
            integrationFlowContext
                    .registration(serverIterator.getNext())
                    .id(INTEGRATION_FLOW_PREFIX + i)
                    .register();
            log.info("Listener {} is started",INTEGRATION_FLOW_PREFIX + i);
        }

    }

}
