package com.kt.kafkatransmitter.balancer;

import com.kt.kafkatransmitter.configuration.ConfigurationGetter;
import com.kt.kafkatransmitter.model.AbstractEntity;
import java.util.LinkedHashMap;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import javax.naming.CommunicationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Puller service.
 * It creates a pool of threads to control others thread.
 * Each thread retrieves thread from internal queue and start it
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Service
@Log4j2
public class PullController {

    private static boolean isRunning = true;
    private static final LinkedHashMap<Integer, Queue<PullerWorker>> processQueue = new LinkedHashMap<>();
    private static final ExecutorService executorServiceController;

    static {
        // create new executor controller
        executorServiceController =
                Executors.newFixedThreadPool(ConfigurationGetter.getClientConfigurations().size());
        // start a separate thread on each configuration id
        ConfigurationGetter
                .getClientConfigurations()
                .forEach(communicationConfiguration -> {
                    // put puller worker in map
                    Queue<PullerWorker> pullerWorkers
                            = processQueue.compute(communicationConfiguration.getId(), (integer, pullerWorkersIn) -> new LinkedBlockingDeque<>());
                    // submit a thread for the current config
                    executorServiceController.submit(() -> {
                        // read worker and start it
                        while (isRunning) {
                            if (!pullerWorkers.isEmpty()) {
                                // get puller worker
                                PullerWorker pullerWorker = pullerWorkers.poll();
                                if (pullerWorker != null)
                                    Executors.newSingleThreadExecutor().submit(pullerWorker);
                            }
                            sleep();
                        }
                    });
                });
    }

    /**
     * Method to update the queue with new worker per entry
     *
     * @param entity abstract entry
     */
    static void addToQueue(AbstractEntity entity) {
        try {
            // create new puller worker
            PullerWorkerImp pullerWorkerImp = new PullerWorkerImp(entity);
            // put the worker in queue
            processQueue.get(pullerWorkerImp.getConfigurationId()).add(pullerWorkerImp);
        } catch (CommunicationException exception) {
            log.error("Error on send to destination: {}", exception.getMessage());
        }
    }

    /**
     * Method to stop all queues
     */
    static void stop() {
        isRunning = false;
    }

    /**
     * Method to sleep the current thread
     */
    @SneakyThrows
    private static void sleep() {
        Thread.sleep(100);
    }

}
