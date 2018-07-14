package com.wyl.zodiac.cache.exception;

/**
 * @author
 * @ClassName ValueIsBlankException
 * @Description key存在，value为空
 * @date 2017年08月07日07:58:57
 */
public class ValueIsBlankException extends RuntimeException {

    private static final long serialVersionUID = -7890860681143337363L;

    public ValueIsBlankException(String message) {
        super(message);
    }

    public ValueIsBlankException(Throwable e) {
        super(e);
    }

    public ValueIsBlankException(String message, Throwable cause) {
        super(message, cause);
    }

}
