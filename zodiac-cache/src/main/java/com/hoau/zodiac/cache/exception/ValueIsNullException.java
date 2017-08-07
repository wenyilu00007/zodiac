package com.hoau.zodiac.cache.exception;

/**
 * @author 陈宇霖
 * @ClassName ValueIsNullException
 * @Description key存在，value为null
 * @date 2017年08月07日07:58:41
 */
public class ValueIsNullException extends RuntimeException {

    private static final long serialVersionUID = 5589666472517709851L;

    public ValueIsNullException(String message) {
        super(message);
    }

    public ValueIsNullException(Throwable e) {
        super(e);
    }

    public ValueIsNullException(String message, Throwable cause) {
        super(message, cause);
    }


}
