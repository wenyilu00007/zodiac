package com.hoau.zodiac.web.filter;

import com.alibaba.fastjson.JSON;
import com.hoau.zodiac.core.dataprovider.IUserProvider;
import com.hoau.zodiac.core.entity.IUser;
import com.hoau.zodiac.web.context.RequestContext;
import com.hoau.zodiac.web.context.SessionContext;
import com.hoau.zodiac.web.context.UserContext;
import com.hoau.zodiac.web.request.CookieToken;
import com.hoau.zodiac.web.request.TokenMarshal;
import com.hoau.zodiac.web.response.Response;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.entity.ContentType;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
* @Title: ContextFilter 
* @Package com.hoau.leo.common.filter 
* @Description: 基于cookie实现的登陆保持context
* @author 陈宇霖  
* @date 2017/8/2 19:12
* @version V1.0   
*/
public class CookieBasedContextFilter extends OncePerRequestFilter implements ApplicationContextAware {

    Log logger = LogFactory.getLog(CookieBasedContextFilter.class);

    private ApplicationContext applicationContext;

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 不进行过滤的请求pattern
     */
    private List<String> excludeUrlPatterns = new ArrayList<String>(Arrays.asList("/health"));

    /**
     * 存储登陆信息的cookie的名称
     */
    private String cookieName;

    /**
     * 项目名称
     */
    private String applicationId;

    /**
     * cookie超时时间
     */
    private int expireTime = 30 * 60;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String url = request.getServletPath();
        boolean matched = false;
        for (String pattern : excludeUrlPatterns) {
            matched = antPathMatcher.match(pattern, url);
            if (matched) {
                break;
            }
        }
        return matched;
    }

    /**
     * 对 RequestContext、SessionContext、UserContext等进行初始化
     * @author 陈宇霖
     * @date 2017年08月02日19:20:09
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            //设置请求的requestId
            RequestContext.setRequestId(UUID.randomUUID().toString());

            /**----------------------登陆用户信息处理---------------------*/
            //当前操作的用户id
            String token = null;
            //从cookie里面获取用户id
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookieName.equals(cookie.getName())) {
                        token = cookie.getValue();
                    }
                }
            }
            //如果还是没有用户id，那就有问题了吧
            if (StringUtils.isEmpty(token)) {
                SessionContext.invalidateSession();
                doRedirectReturn(request, response);
                return;
            }

            //从token中解析用户信息
            String userId = null;
            CookieToken cookieToken = TokenMarshal.unMarshal(token);
            if (cookieToken != null && !cookieToken.expired()) {
                userId = cookieToken.getUserId();
            }

            RequestContext.setCurrentUserId(userId);
            SessionContext.setCurrentUserId(userId);

            //存缓存中获取用户信息
            IUserProvider userProvider = applicationContext.getBean(IUserProvider.class);
            if (userProvider == null) {
                logger.error("系统异常，未实现用户信息提供接口！");
                SessionContext.invalidateSession();
                doRedirectReturn(request, response);
                return;
            }
            //从提供者读取数据
            IUser user = userProvider.getUserById(userId);
            //如果通过用户id获取到的用户为空，则跳转到登陆界面
            if (user == null) {
                SessionContext.invalidateSession();
                doRedirectReturn(request, response);
                return;
            }
            //将用户信息设置到UserContext中
            UserContext.setCurrentUser(user);
            /**----------------------end of 登陆用户信息处理---------------------*/
        }
        try {
            filterChain.doFilter(request, response);
        } finally {
            //请求处理完成后将UserContext中的用户信息移除掉
            UserContext.remove();
            SessionContext.invalidateSession();
        }
    }

    /**
     * 前端使用ajax
     * @param request
     * @param response
     * @throws IOException
     */
    private void doRedirectReturn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(ContentType.APPLICATION_JSON.toString());
        Response redirectResponse = new Response();
        redirectResponse.setErrorCode(Response.ERROR_REDIRECT);
        //判断应用是否接入了CAS统一登录
        String withoutCasOfflineRedirectUrl = getEnvironment().getProperty("zodiac.web.context.withoutCasOfflineRedirectUrl");
        if (StringUtils.isEmpty(withoutCasOfflineRedirectUrl)) {
            redirectResponse.setResult(request.getServletPath() + request.getContextPath());
        } else {
            redirectResponse.setResult(withoutCasOfflineRedirectUrl);
        }
        PrintWriter writer = response.getWriter();
        writer.write(JSON.toJSONString(redirectResponse));
    }

    @Override
    public void destroy() {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public List<String> getExcludeUrlPatterns() {
        return excludeUrlPatterns;
    }

    public void setExcludeUrlPatterns(List<String> excludeUrlPatterns) {
        this.excludeUrlPatterns = excludeUrlPatterns;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public int getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }
}
