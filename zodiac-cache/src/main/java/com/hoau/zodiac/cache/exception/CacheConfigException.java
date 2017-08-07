package com.hoau.zodiac.cache.exception;

/**
 * @author 陈宇霖
 * @ClassName CacheConfigException
 * @Description 缓存配置异常
 * @date 2017年08月07日07:55:36
 */
public class CacheConfigException extends RuntimeException {

    private static final long serialVersionUID = 4717145799805874827L;

    public CacheConfigException(String msg) {
        super(msg);
    }

}
