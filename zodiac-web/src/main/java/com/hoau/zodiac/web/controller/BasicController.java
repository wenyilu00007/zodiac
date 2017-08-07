package com.hoau.zodiac.web.controller;

import com.hoau.zodiac.core.exception.AccessNotAllowException;
import com.hoau.zodiac.core.exception.BusinessException;
import com.hoau.zodiac.core.message.LocaleMessageSource;
import com.hoau.zodiac.core.util.ExceptionUtils;
import com.hoau.zodiac.web.context.RequestContext;
import com.hoau.zodiac.web.response.PageResponse;
import com.hoau.zodiac.web.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
* @Title: BaseControllerAdvice 
* @Package com.hoau.hbdp.framework.server.web.controller 
* @Description: controller增强，用于处理异常、分页等通用信息
* @author 陈宇霖  
* @date 2017/6/26 15:21
* @version V1.0   
*/
public class BasicController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 用于国际化消息生成
     */
    @Resource
    protected LocaleMessageSource localeMessageSource;

    /**
     * 请求成功，不带返回参数
     * @return
     * @author 陈宇霖
     * @date 2017年06月28日08:36:50
     */
    public Response returnSuccess() {
        return returnSuccess(null);
    }

    /**
     * 带成功提示的返回
     * @param successMsg
     * @return
     * @author 陈宇霖
     * @date 2017年07月04日16:33:48
     */
    public Response returnSuccess(String successMsg) {
        return returnSuccess(null, successMsg);
    }

    /**
     * 请求成功带返回参数
     * @param object
     * @return
     * @author 陈宇霖
     * @date 2017年06月28日08:37:02
     */
    public Response returnSuccess(Object object) {
        return returnSuccess(object, null);
    }

    /**
     * 带成功提示和返回参数的结果
     * @param object
     * @param successMsg
     * @return
     * @author 陈宇霖
     * @date 2017年07月04日16:28:55
     */
    public Response returnSuccess(Object object, String successMsg) {
        Response response = new Response();
        response.setSuccess(true);
        response.setHasBusinessException(false);
        response.setMessage(localeMessageSource.getMessage(successMsg));
        response.setResult(object);
        return response;
    }

    /**
     * 分页查询请求返回参数
     * @param object
     * @param totalCount
     * @return
     */
    public PageResponse returnSuccess(Object object, long totalCount) {
        PageResponse response = new PageResponse();
        response.setSuccess(true);
        response.setHasBusinessException(false);
        response.setResult(object);
        response.setTotalCount(totalCount);
        return response;
    }

    /**
     * 统一的请求异常处理，所有异常都转换为json输入到前台，前端根据返回结果进行判断如何展示异常信息
     *
     * @param exception
     * @return
     * @author 陈宇霖
     * @date 2017年06月26日16:13:35
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response handlerException(Exception exception) {
        log.error(exception.getMessage(), exception);
        Response response = new Response();
        response.setSuccess(false);

        if (exception instanceof AccessNotAllowException) { //无权访问
            response.setHasBusinessException(true);
            response.setMessage(localeMessageSource.getMessage(Response.NO_RIGHT_TO_ACCESS_MSG_BUNDLE_KEY));
            response.setErrorCode(Response.ERROR_CODE_VALIDATE);
            response.setErrorMsg(localeMessageSource.getMessage(Response.NO_RIGHT_TO_ACCESS_MSG_BUNDLE_KEY));
        } else if (exception instanceof BusinessException) {    //业务异常
            response.setHasBusinessException(true);
            response.setMessage(localeMessageSource.getMessage(((BusinessException)exception).getErrorCode(), ((BusinessException)exception).getErrorArguments()));
            response.setErrorCode(Response.ERROR_CODE_BUSINESS_EXCEPTION);
            response.setErrorMsg(localeMessageSource.getMessage(((BusinessException)exception).getErrorCode(), ((BusinessException)exception).getErrorArguments()));
        } else { //未捕获异常
            response.setRequestId(RequestContext.getRequestId());
            response.setHasBusinessException(false);
            response.setMessage(exception.getMessage());
            response.setErrorCode(Response.ERROR_CODE_UNHANDLED_EXCEPTION);
            response.setErrorMsg(ExceptionUtils.getStack(exception));
        }
        return response;
    }

}
