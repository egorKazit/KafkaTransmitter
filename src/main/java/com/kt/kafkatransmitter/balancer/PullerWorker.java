package com.kt.kafkatransmitter.balancer;

import com.kt.kafkatransmitter.client.AbstractSocket;
import com.kt.kafkatransmitter.client.ClientSocketFactory;
import com.kt.kafkatransmitter.model.AbstractEntity;
import lombok.extern.log4j.Log4j2;

import javax.naming.CommunicationException;

@Log4j2
public class PullerWorker extends Thread implements Runnable {
    private final AbstractSocket genericSocket;
    private final AbstractEntity entity;

    PullerWorker(AbstractEntity entity) throws CommunicationException {
        this.entity = entity;
        genericSocket = ClientSocketFactory.getNextDestination();
    }

    public int getConfigurationId() {
        return genericSocket.getConfigurationId();
    }

    @Override
    public void run() {
        super.run();
        log.trace(entity);
        genericSocket.send(entity.toString());
    }

}
