package com.hoau.zodiac.core.exception;

import java.io.Serializable;

/**
 * @author 陈宇霖
 * @ClassName BusinessException
 * @Description 业务异常基类
 * @date 2017年08月03日01:25:45
 */
public class BusinessException extends RuntimeException implements Serializable {

    /**
     * 异常国际化编码
     */
    protected String errCode;

    /**
     * 异常参数
     */
    private Object[] arguments;

    public BusinessException() {
    }

    public BusinessException(String msg) {
        super(msg);
        this.errCode = msg;
    }

    public BusinessException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public BusinessException(String code, String msg) {
        super(msg);
        this.errCode = code;
    }

    public BusinessException(String code, String msg, Throwable cause) {
        super(msg, cause);
        this.errCode = code;
    }

    public BusinessException(String code, Object... args) {
        this.errCode = code;
        this.arguments = args;
    }

    public BusinessException(String code, String msg, Object... args) {
        super(msg);
        this.errCode = code;
        this.arguments = args;
    }

    public BusinessException(Throwable throwable) {
        super(throwable);
    }

    public void setErrorArguments(Object... args) {
        this.arguments = args;
    }

    public Object[] getErrorArguments() {
        return this.arguments;
    }

    public String getErrorCode() {
        return this.errCode;
    }

}
