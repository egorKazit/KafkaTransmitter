package com.kt.kafkatransmitter.balancer;

import com.kt.kafkatransmitter.configuration.ConfigurationGetter;
import com.kt.kafkatransmitter.model.AbstractEntity;
import org.springframework.stereotype.Service;

import javax.naming.CommunicationException;
import java.util.LinkedHashMap;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

@Service
public class Puller {
    private static final LinkedHashMap<Integer, Queue<PullerWorker>> processQueue = new LinkedHashMap<>();
    private static final Thread pullThread;

    static {
        ConfigurationGetter
                .getClientConfigurations()
                .forEach(communicationConfiguration ->
                        processQueue.put(communicationConfiguration.getId(),
                                new LinkedBlockingDeque<>()));
        pullThread = new Thread(() -> {
            while (true) {
                processQueue.forEach((id, pullerWorkers) -> {
                    if (!pullerWorkers.isEmpty()) {
                        pullerWorkers.poll().start();
                    }
                });
            }
        });
        pullThread.start();
    }

    public static void addToQueue(AbstractEntity entity) {
        try {
            PullerWorkerImp pullerWorkerImp = new PullerWorkerImp(entity);
            processQueue.get(pullerWorkerImp.getConfigurationId()).add(pullerWorkerImp);
        } catch (CommunicationException e) {
            e.printStackTrace();
        }
    }

}
