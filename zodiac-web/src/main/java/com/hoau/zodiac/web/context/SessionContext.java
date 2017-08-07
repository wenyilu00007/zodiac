package com.hoau.zodiac.web.context;

import javax.servlet.http.HttpSession;
import java.util.Locale;

/**
* @Title: SessionContext 
* @Package com.hoau.leo.common.context 
* @Description: SessionContext，可获取、操作当前请求的session，存储当前登录的用户id
* @author 陈宇霖  
* @date 2017/8/2 18:43
* @version V1.0   
*/
public class SessionContext {

    /**
     * session attribute中存放用户信息的key
     */
    public static final String SESSION_USER_ID_ATTRIBUTE_NAME = "APP_SESSION_USER_ID_ATTRIBUTE_KEY";

    /**
     * session attribute中存放local信息的key
     */
    public static final String SESSION_LOCAL_ATTRIBUTE_NAME = "APP_SESSION_LOCAL_ATTRIBUTE_KEY";

    /**
     * 获取session,没有则创建新的
     * @return
     * @author 陈宇霖
     * @date 2017年08月02日18:52:16
     */
    public static HttpSession getSession() {
        return getSession(true);
    }

    /**
     * 按条件后去session
     * @param create
     * @return
     * @author 陈宇霖
     * @date 2017年08月02日18:52:51
     */
    public static HttpSession getSession(boolean create) {
        return RequestContext.getRequest().getSession(create);
    }

    /**
     * 向session中增加属性
     * @param name
     * @param value
     * @author 陈宇霖
     * @date 2017年08月02日18:55:21
     */
    public static void setAttribute(String name, Object value) {
        getSession().setAttribute(name, value);
    }

    /**
     * 从session中获取属性
     * @param name
     * @return
     * @author 陈宇霖
     * @date 2017年08月02日18:56:40
     */
    public static Object getAttribute(String name) {
        HttpSession session = getSession(false);
        if (session != null) {
            return session.getAttribute(name);
        }
        return null;
    }

    /**
     * 设置当前登录用户id
     *
     * @param userId
     * @author 陈宇霖
     * @date 2017年08月02日19:02:46
     */
    public static void setCurrentUserId(String userId) {
        setAttribute(SESSION_USER_ID_ATTRIBUTE_NAME, userId);
    }

    /**
     * 获取当前登录用户id
     *
     * @return
     * @author 陈宇霖
     * @date 2017年08月02日19:04:03
     */
    public static String getCurrentUserId() {
        return (String)getAttribute(SESSION_USER_ID_ATTRIBUTE_NAME);
    }

    /**
     * 设置方言
     * @param local
     * @author 陈宇霖
     * @date 2017年08月02日19:05:44
     */
    public static void setCurrentLocal(Locale local) {
        setAttribute(SESSION_LOCAL_ATTRIBUTE_NAME, local);
    }

    /**
     * 获取方言
     * @return
     * @author 陈宇霖
     * @date 2017年08月02日19:06:35
     */
    public static Locale getCurrentLocal() {
        return (Locale)getAttribute(SESSION_LOCAL_ATTRIBUTE_NAME);
    }

    /**
     * 销毁session
     * @author 陈宇霖
     * @date 2017年08月02日18:57:37
     */
    public static void invalidateSession() {
        HttpSession session = getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

}
