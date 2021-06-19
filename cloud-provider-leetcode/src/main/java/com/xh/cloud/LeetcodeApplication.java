package com.xh.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableEurekaClient
public class LeetcodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeetcodeApplication.class, args);
    }

}
