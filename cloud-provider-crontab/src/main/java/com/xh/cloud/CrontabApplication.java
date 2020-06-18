package com.xh.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 三种方式实现定时任务
 */
//@ComponentScan(basePackages = {"com.vipsoft.model.user"})
@SpringBootApplication
@EnableScheduling
public class CrontabApplication {
    public static void main(String[] args) {
        SpringApplication.run(CrontabApplication.class, args);

    }
}
