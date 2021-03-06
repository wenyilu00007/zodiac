package com.wyl.zodiac.web.context;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
* @Title: RequestContext 
* @Package com.wyl.leo.common.context
* @Description: RequstContext
* @author
* @date 2017/8/2 17:32
* @version V1.0   
*/
public class RequestContext {

    /**
     * request中存储requestId的key
     */
    public static final String REQUEST_ID_ATTRIBUTE_NAME = "APP_REQUEST_ID_ATTRIBUTE_KEY";

    /**
     * request中存储USERID的key
     */
    public static final String REQUEST_USER_ID_ATTRIBUTE_NAME = "APP_REQUEST_USER_ID_ATTRIBUTE_KEY";

    /**
     * 私有构造方法，不允许实例化
     */
    private RequestContext() {}

    /**
     * 全局获取request的方法
     * @return
     * @author
     * @date 2017年08月02日17:34:25
     */
    public static HttpServletRequest getRequest() {
        try {
            return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 设置requestId
     * @param requestId
     * @author
     * @date 2017年08月02日17:46:46
     */
    public static void setRequestId(String requestId) {
        getRequest().setAttribute(RequestContext.REQUEST_ID_ATTRIBUTE_NAME, requestId);
    }

    /**
     * 获取RequestId
     * @return
     * @author
     * @date 2017年08月02日17:48:41
     */
    public static String getRequestId() {
        return (String)getRequest().getAttribute(RequestContext.REQUEST_ID_ATTRIBUTE_NAME);
    }

    /**
     * 设置当前登录的用户id
     * @param userId
     * @author
     * @date 2017年08月07日22:43:59
     */
    public static void setCurrentUserId(String userId) {
        getRequest().setAttribute(RequestContext.REQUEST_USER_ID_ATTRIBUTE_NAME, userId);
    }

    /**
     * 获取当前登录的用户Id
     * @return
     * @author
     * @date 2017年08月02日18:33:11
     */
    public static String getCurrentUserId() {
        return (String)getRequest().getAttribute(RequestContext.REQUEST_USER_ID_ATTRIBUTE_NAME);
    }

}
