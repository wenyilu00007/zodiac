package com.wyl.zodiac.cache.redis.exception;

/**
 * @author
 * @ClassName RedisCacheStorageException
 * @Description Redis缓存操作统一异常
 * @date 2017年08月07日08:44:33
 */
public class RedisCacheStorageException extends RuntimeException {

    private static final long serialVersionUID = -8149325382164622728L;

    public RedisCacheStorageException(String message) {
        super(message);
    }

    public RedisCacheStorageException(Throwable e) {
        super(e);
    }

    public RedisCacheStorageException(String message, Throwable cause) {
        super(message, cause);
    }

}
