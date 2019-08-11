package com.jbr.middletier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by jason on 24/12/16.
 */

@SpringBootApplication
@EnableScheduling
public class MiddleTier {
    public static void main(String[] args) {
        SpringApplication.run(PodcastCtrlConfig.class, args);
    }
}