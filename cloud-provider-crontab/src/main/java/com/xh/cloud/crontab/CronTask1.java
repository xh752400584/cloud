package com.xh.cloud.crontab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 一、基于注解的定时任务
 */
@Component
@EnableScheduling
public class CronTask1 {
    Logger taskLogger = LoggerFactory.getLogger("crontabTask_1");

    /**
     * @Scheduled除过cron还有三种方式：fixedRate，fixedDelay，initialDelay
     * cron:表达式可以定制化执行任务，但是执行的方式是与fixedDelay相近的，也是会按照上一次方法结束时间开始算起。
     * fixedRate:控制方法执行的间隔时间，是以上一次方法执行完开始算起，如上一次方法执行阻塞住了，那么直到上一次执行完，并间隔给定的时间后，执行下一次。
     * fixedDelay:是按照一定的速率执行，是从上一次方法执行开始的时间算起，如果上一次方法阻塞住了，下一次也是不会执行，但是在阻塞这段时间内累计应该执行的次数，当不再阻塞时，一下子把这些全部执行掉，而后再按照固定速率继续执行。
     * initialDelay：initialDelay = 10000 表示在容器启动后，延迟10秒后再执行一次定时器。
     */
    @Scheduled(cron = "*/6 * * * * ?")
    public void crontabTask() {
//        taskLogger.info("这是基于注解的方式的定时任务" + new Date());
        String info = "基于注解的定时任务，" + "\r\n线程 : " +  Thread.currentThread().getName();
        System.out.println(info);
    }
}
