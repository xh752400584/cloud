package com.xh.cloud.service.impl;

import com.xh.cloud.constant.RedisConstant;
import com.xh.cloud.service.IDemo;
import com.xh.cloud.util.RedisUtil;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DemoServiceImpl implements IDemo {

    @Resource
    private RedisUtil redisUtil;

    @Override
    public void testDemo() {
        redisUtil.set(RedisConstant.INVENTORY_WAREHOUSE_REDIS_KEY,100,RedisConstant.DEADLINE_ONE_DAY);
    }
}
