package com.xh.cloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@MapperScan(value = "com.xh.cloud.mapper")
public class UserStart {
    public static void main(String[] args) {
        SpringApplication.run(UserStart.class, args);
    }
}
