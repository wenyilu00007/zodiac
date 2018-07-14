package com.wyl.zodiac.cache.exception;

/**
 * @author
 * @ClassName KeyIsNotFoundException
 * @Description key不存在
 * @date 2017年08月07日07:59:11
 */
public class KeyIsNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -4928429573985189018L;

    public KeyIsNotFoundException(String message) {
        super(message);
    }

    public KeyIsNotFoundException(Throwable e) {
        super(e);
    }

    public KeyIsNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
