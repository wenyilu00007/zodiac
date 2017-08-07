package com.hoau.zodiac.web.interceptor;

import com.hoau.zodiac.core.exception.AccessNotAllowException;
import com.hoau.zodiac.core.security.SecurityAccess;
import com.hoau.zodiac.web.context.UserContext;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* @Title: AccessInterceptor 
* @Package com.hoau.leo.common.security 
* @Description: 访问控制拦截器
* @author 陈宇霖  
* @date 2017/8/4 17:13
* @version V1.0   
*/
public class AccessInterceptor extends HandlerInterceptorAdapter {

    /**
     * 是否忽略未配置的链接
     */
    private boolean ignoreNoneConfigUri = true;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean canAccess = SecurityAccess.checkCanAccess(UserContext.getCurrentUser(), request.getRequestURI(), ignoreNoneConfigUri);
        if (!canAccess) {
            throw new AccessNotAllowException();
        }
        return super.preHandle(request, response, handler);
    }

    public boolean isIgnoreNoneConfigUri() {
        return ignoreNoneConfigUri;
    }

    public void setIgnoreNoneConfigUri(boolean ignoreNoneConfigUri) {
        this.ignoreNoneConfigUri = ignoreNoneConfigUri;
    }
}
