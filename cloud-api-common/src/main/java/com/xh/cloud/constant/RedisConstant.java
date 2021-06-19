package com.xh.cloud.constant;

/**
 * @description Redis常量池
 * @author xianghui
 * @date 2021/5/20 20:33:22
 */
public class RedisConstant {

    //======================================================= redis key **=======================================================================//

    /**
     * 库存-仓库库存redisKey
     */
    public static final String INVENTORY_WAREHOUSE_REDIS_KEY = "inventory:warehouse:{0}";


    //======================================================= redis key deadline **==============================================================//

    public static final long DEADLINE_ONE_DAY = 86400;

}