package com.xh.cloud.crontab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

/**
 * 一、基于接口的定时任务
 */
@Component
@EnableScheduling
public class CronTask2 implements SchedulingConfigurer {
    Logger taskLogger = LoggerFactory.getLogger("crontabTask_2");
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(() -> process(),
                triggerContext -> {
                    //每隔1分钟执行一次
                    String cron = "10,20 * * * * ?";
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                });
    }
    private void process() {
        String info = "基于接口的定时任务，" + "\r\n线程 : " + Thread.currentThread().getName();
        System.out.println(info);
//        taskLogger.info(info);
    }
}
