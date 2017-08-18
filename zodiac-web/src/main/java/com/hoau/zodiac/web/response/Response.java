package com.hoau.zodiac.web.response;

import java.io.Serializable;

/**
* @Title: Response
* @Package com.hoau.hbdp.framework.server.web.response 
* @Description: springMvc统一返回参数
* @author 陈宇霖  
* @date 2017/6/27 08:12
* @version V1.0   
*/
public class Response<T> implements Serializable {

    /**
     * 要求前端进行重定向的编码
     */
    public static final String ERROR_REDIRECT = "302";

    /**
     * 校验类异常编码
     */
    public static final String ERROR_CODE_VALIDATE = "10000";

    /**
     * 业务异常类异常编码
     */
    public static final String ERROR_CODE_BUSINESS_EXCEPTION = "20000";

    /**
     * 未捕获类异常编码
     */
    public static final String ERROR_CODE_UNHANDLED_EXCEPTION = "90000";

    /**
     * 无权访问国际化key
     */
    public static final String NO_RIGHT_TO_ACCESS_MSG_BUNDLE_KEY = "error.user.no.right.to.access";

    /**
     * 请求id，系统异常时需要将此参数传递到前台去
     */
    private String requestId;

    /**
     * 请求是否处理成功
     */
    private boolean success;

    /**
     * 返回结果有业务异常
     */
    private boolean hasBusinessException;

    /**
     * 业务异常错误代码
     */
    private String errorCode;

    /**
     * 业务异常错误信息
     */
    private String errorMsg;

    /**
     * 提示消息，需要进行国际化
     */
    private String message;

    /**
     * 正常返回参数
     */
    private T result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isHasBusinessException() {
        return hasBusinessException;
    }

    public void setHasBusinessException(boolean hasBusinessException) {
        this.hasBusinessException = hasBusinessException;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
