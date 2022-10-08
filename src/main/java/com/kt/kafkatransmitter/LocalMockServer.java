package com.kt.kafkatransmitter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class LocalMockServer {
    private static final ExecutorService executorService = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {
        Stream.of(10101, 10100)
                .forEach(port -> {
                    log.info("Start listener on port {}", port);
                    executorService.submit(() -> {
                        log.info("Start accepting of messages on port {}", port);
                        try (ServerSocket serverSocket = new ServerSocket(port)) {
                            while (true) {
                                Socket socket = serverSocket.accept();
                                log.info("Message is accepted on port {}", port);
                                new Thread(() -> {
                                    try {
                                        var writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                                        log.info(new BufferedReader(new InputStreamReader(socket.getInputStream()))
                                                .readLine());
                                        log.info("Message is printed");
                                        writer.write("test\n");
                                        writer.flush();
                                        writer.close();
                                        socket.close();
                                    } catch (IOException e) {
                                        throw new RuntimeSocketServerException(e);
                                    }
                                }).start();
                            }
                        } catch (IOException e) {
                            throw new RuntimeSocketServerException(e);
                        }
                    });
                });
    }

    private static final class RuntimeSocketServerException extends RuntimeException {
        private RuntimeSocketServerException(Throwable throwable) {
            super(throwable);
        }
    }

}
