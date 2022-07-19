package com.kt.kafkatransmitter.balancer;

import com.kt.kafkatransmitter.client.AbstractSocket;
import com.kt.kafkatransmitter.client.ClientSocketFactory;
import com.kt.kafkatransmitter.model.AbstractEntity;
import lombok.extern.log4j.Log4j2;

import javax.naming.CommunicationException;

/**
 * An implementation of PullerWorker.
 * It consumes abstract entity, gets the next configuration for socket
 */
@Log4j2
public class PullerWorkerImp extends Thread implements PullerWorker {
    private final AbstractSocket genericSocket;
    private final AbstractEntity entity;

    /**
     * Constructor
     *
     * @param entity abstract entity
     * @throws CommunicationException exception if client can not be connected
     */
    PullerWorkerImp(AbstractEntity entity) throws CommunicationException {
        this.entity = entity;
        genericSocket = ClientSocketFactory.getNextDestination();
    }

    /**
     * See PullerWorker.getConfigurationId
     *
     * @return configuration Id
     */
    @Override
    public int getConfigurationId() {
        return genericSocket.getConfigurationId();
    }

    /**
     * Run body
     */
    @Override
    public void run() {
        super.run();
        log.trace(entity);
        genericSocket.send(entity.toString());
    }

}
