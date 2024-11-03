package com.sion.bankeureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class BankEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankEurekaApplication.class, args);
    }

}
