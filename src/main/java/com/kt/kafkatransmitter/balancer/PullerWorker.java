package com.kt.kafkatransmitter.balancer;

public interface PullerWorker extends Runnable {
    int getConfigurationId();

    void start();
}
