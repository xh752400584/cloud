package com.xh.cloud;

import com.xh.cloud.constant.RedisConstant;
import com.xh.cloud.service.IDemo;
import com.xh.cloud.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author gg5
 * @description some desc
 * @date 2021/5/20 22:09
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class demoTest {

    @Resource
    private IDemo iDemo;

    @Resource
    private RedisUtil redisUtil;

    @Test
    public void demo1Test() {
        // 设置库存
        redisUtil.set(RedisConstant.INVENTORY_WAREHOUSE_REDIS_KEY,90,RedisConstant.DEADLINE_ONE_DAY);

    }

}
