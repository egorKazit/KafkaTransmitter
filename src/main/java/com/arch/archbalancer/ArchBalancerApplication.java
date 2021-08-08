package com.arch.archbalancer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties
@EnableScheduling
public class ArchBalancerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArchBalancerApplication.class, args);
    }

}
