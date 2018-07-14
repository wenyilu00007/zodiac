package com.wyl.zodiac.web.controller;

import com.wyl.zodiac.core.exception.AccessNotAllowException;
import com.wyl.zodiac.core.exception.BusinessException;
import com.wyl.zodiac.core.message.LocaleMessageSource;
import com.wyl.zodiac.web.context.RequestContext;
import com.wyl.zodiac.web.response.PageResponse;
import com.wyl.zodiac.web.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
* @Title: BaseControllerAdvice 
* @Package com.wyl.hbdp.framework.server.web.controller
* @Description: controller增强，用于处理异常、分页等通用信息
* @author
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
     * @author
     * @date 2017年06月28日08:36:50
     */
    public Response returnSuccess() {
        return returnSuccess(null);
    }

    /**
     * 带成功提示的返回
     * @param successMsg
     * @return
     * @author
     * @date 2017年07月04日16:33:48
     */
    public Response returnSuccess(String successMsg) {
        return returnSuccess(null, successMsg);
    }

    /**
     * 请求成功带返回参数
     * @param object
     * @return
     * @author
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
     * @author
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
     * 返回参数校验失败信息
     * @param errorMsg      参数校验失败信息
     * @return
     * @author
     * @date 2017年09月07日21:12:53
     */
    public Response returnValidateError(String errorMsg) {
        Response response = new Response();
        response.setSuccess(false);
        response.setHasBusinessException(true);
        response.setErrorCode(Response.ERROR_CODE_VALIDATE);
        response.setMessage(localeMessageSource.getMessage(errorMsg));
        response.setErrorCode(Response.ERROR_CODE_VALIDATE);
        response.setErrorMsg(errorMsg);
        return response;
    }

    /**
     * 返回参数校验失败
     * @param bindingResult     校验框架校验的结果
     * @return
     * @author
     * @date 2017年09月07日16:59:22
     */
    public Response returnValidateError(BindingResult bindingResult) {
        Response response = new Response();
        response.setSuccess(false);
        response.setHasBusinessException(true);
        StringBuffer errorMsg = new StringBuffer();
        bindingResult.getFieldErrors().forEach(fieldError -> {
            errorMsg.append(localeMessageSource.getMessage(fieldError.getDefaultMessage(), fieldError.getArguments())).append("\n");
        });
        response.setMessage(errorMsg.toString());
        response.setErrorCode(Response.ERROR_CODE_VALIDATE);
        response.setErrorMsg(errorMsg.toString());
        return response;
    }

    /**
     * 统一的请求异常处理，所有异常都转换为json输入到前台，前端根据返回结果进行判断如何展示异常信息
     *
     * @param exception
     * @return
     * @author
     * @date 2017年06月26日16:13:35
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response handlerException(Exception exception) {
        log.error(RequestContext.getRequestId(), exception);
        Response response = new Response();
        response.setSuccess(false);

        if (exception instanceof AccessNotAllowException) { //无权访问
            response = Response.buildNoRightToAccessResponse(localeMessageSource.getMessage(((AccessNotAllowException)exception).getErrorCode()));
        } else if (exception instanceof BusinessException) {    //业务异常
            response.setHasBusinessException(true);
            response.setMessage(localeMessageSource.getMessage(((BusinessException)exception).getErrorCode(), ((BusinessException)exception).getErrorArguments()));
            response.setErrorCode(Response.ERROR_CODE_BUSINESS_EXCEPTION);
            response.setErrorMsg(localeMessageSource.getMessage(((BusinessException)exception).getErrorCode(), ((BusinessException)exception).getErrorArguments()));
        } else if (exception instanceof MethodArgumentNotValidException) { //参数校验失败异常
            response.setHasBusinessException(true);
            BindingResult bindingResult = ((MethodArgumentNotValidException)exception).getBindingResult();
            StringBuffer errorMsg = new StringBuffer();
            bindingResult.getFieldErrors().forEach(fieldError -> {
                errorMsg.append(fieldError.getDefaultMessage()).append("\n");
            });
            response.setMessage(errorMsg.toString());
            response.setErrorCode(Response.ERROR_CODE_VALIDATE);
            response.setErrorMsg(errorMsg.toString());
        } else { //未捕获异常
            response.setRequestId(RequestContext.getRequestId());
            response.setHasBusinessException(false);
            response.setMessage("系统异常");
            response.setErrorCode(Response.ERROR_CODE_UNHANDLED_EXCEPTION);
//            response.setErrorMsg(ExceptionUtils.getStack(exception));
        }
        return response;
    }

}
