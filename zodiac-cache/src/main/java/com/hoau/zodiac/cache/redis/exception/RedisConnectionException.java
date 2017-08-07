package com.hoau.zodiac.cache.redis.exception;


/**
 * @author 陈宇霖
 * @ClassName RedisConnectionException
 * @Description Redis连接异常
 * @date 2017年08月07日08:43:46
 */
public class RedisConnectionException extends RedisCacheStorageException {

    private static final long serialVersionUID = -4125135648861157964L;

    public RedisConnectionException(String message) {
        super(message);
    }

    public RedisConnectionException(Throwable e) {
        super(e);
    }

    public RedisConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
