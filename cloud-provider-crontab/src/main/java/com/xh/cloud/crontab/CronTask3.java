package com.xh.cloud.crontab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 一、基于注解设定多线程定时任务
 */
@Component
@EnableScheduling
@EnableAsync
public class CronTask3 {
    Logger taskLogger = LoggerFactory.getLogger("crontabTask_3");

    @Async
    @Scheduled(fixedDelay = 10000)  //间隔10秒
    public void first() throws InterruptedException {
        System.out.println("第一个基于注解设定多线程定时任务开始 : " + LocalDateTime.now().toLocalTime() + "\r\n线程 : " + Thread.currentThread().getName());
//        taskLogger.info("第一个基于注解设定多线程定时任务");
        Thread.sleep(1000 * 10);
    }

    @Async
    @Scheduled(fixedDelay = 20000)
    public void second() {
        System.out.println("第二个基于注解设定多线程定时任务开始 : " + LocalDateTime.now().toLocalTime() + "\r\n线程 : " + Thread.currentThread().getName());
//        taskLogger.info("第二个基于注解设定多线程定时任务");

    }
}
