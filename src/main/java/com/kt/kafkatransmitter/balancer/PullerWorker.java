package com.kt.kafkatransmitter.balancer;

/**
 * Puller worker. It extends Runnable interface to make a worker executable
 */
public interface PullerWorker extends Runnable {

    /**
     * Method to get configuration Id
     *
     * @return configuration Id
     */
    int getConfigurationId();
}
