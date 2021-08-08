package com.arch.archbalancer.balancer;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class InternalQueueProducer{
    public static void putEntityInQueue(int destinationId, String message) {
        System.out.println("was put");
    }

    @Scheduled(fixedDelay = 60 * 1000)
    public void handleQueue() {
        System.out.println("Schedule is running");
    }
}
