package com.wyl.zodiac.cache.redis.constant;

import java.io.Serializable;

/**
* @Title: RedisConstants 
* @Package com.wyl.zodiac.cache.redis.constant
* @Description: Redis缓存使用的常量
* @author
* @date 2017/8/7 09:47
* @version V1.0   
*/
public class RedisConstants implements Serializable {

    /**
     * 只读缓存初始化标示的统一前缀
     */
    public static final String STRONG_CACHE_INIT_FLAG_PREFIX = "zodiac.redis.strong.initialization.";

    /**
     * 只读缓存正在初始化时加锁标志的统一前缀
     */
    public static final String STRONG_CACHE_INIT_LOCKING_FLAG_PREFIX = "zodiac.redis.strong.lock.";

    /**
     * 只读缓存正在初始化时加锁的缓存对应的值，无实际意义，只要有此缓存就认为正在初始化
     */
    public static final String STRONG_CACHE_INIT_LOCKING_CACHE_VALUE = "1";

    /**
     * 只读缓存重试初始化的间隔时间，同时只能有一个只读缓存在初始化，后续的缓存每个指定的间隔时间再进行初始化
     */
    public static final int STRONG_RETRY_INIT_INTERVAL = 60 * 1000;

    /**
     * 未指定失效时间的TTL缓存，给定一个默认失效时间10分钟
     */
    public static final int TTL_DEFAULT_EXPIRED_TIME = 10 * 60;
}
