package com.zjut.dropshipping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author zjxjwxk
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class DropshippingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DropshippingApplication.class, args);
    }

}

