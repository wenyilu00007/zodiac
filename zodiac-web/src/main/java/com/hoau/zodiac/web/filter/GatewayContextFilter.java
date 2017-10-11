package com.hoau.zodiac.web.filter;

import com.alibaba.fastjson.JSON;
import com.hoau.zodiac.core.dataprovider.IUserProvider;
import com.hoau.zodiac.core.entity.IUser;
import com.hoau.zodiac.web.context.RequestContext;
import com.hoau.zodiac.web.context.SessionContext;
import com.hoau.zodiac.web.context.UserContext;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
* @Title: ContextFilter 
* @Package com.hoau.leo.common.filter 
* @Description: 走网关的应用各个Context的初始化等操作,主要的区别是用户信息从header中获取
* @author 陈宇霖  
* @date 2017/8/2 19:12
* @version V1.0   
*/
public class GatewayContextFilter extends OncePerRequestFilter implements ApplicationContextAware {

    Log logger = LogFactory.getLog(GatewayContextFilter.class);

    private ApplicationContext applicationContext;

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 不进行过滤的请求pattern
     */
    private List<String> excludeUrlPatterns = new ArrayList<String>(Arrays.asList("/health"));

    /**
     * 启用网关情况下，服务器登陆地址
     */
    private String gatewayLogoutServerUrl;

    /**
     * 启用网关情况下，登出后重定向地址
     */
    private String gatewayLogoutRedirectUrl;

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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            //设置请求的requestId
            RequestContext.setRequestId(UUID.randomUUID().toString());

            /**----------------------登陆用户信息处理---------------------*/
            //获取session，如果没有则创建一个，因为使用了CAS，所以走到这个地方就信任用户已经登陆了。
            SessionContext.getSession(true);
            //当前操作的用户id
            String userId = httpRequest.getHeader("userCode");
            //如果还是没有用户id，那就有问题了吧
            if (!StringUtils.isEmpty(userId)) {
                RequestContext.setCurrentUserId(userId);
                SessionContext.setCurrentUserId(userId);

                //存缓存中获取用户信息
                IUserProvider userProvider = applicationContext.getBean(IUserProvider.class);
                if (userProvider == null) {
                    logger.error("系统异常，未实现用户信息提供接口！");
                    SessionContext.invalidateSession();
                    doRedirectReturn(httpRequest, httpResponse);
                    return;
                }
                //从提供者读取数据
                IUser user = userProvider.getUserById(userId);
                //如果通过用户id获取到的用户为空，则跳转到登陆界面
                if (user == null) {
                    SessionContext.invalidateSession();
                    doRedirectReturn(httpRequest, httpResponse);
                    return;
                }
                //将用户信息设置到UserContext中
                UserContext.setCurrentUser(user);
            }
            /**----------------------end of 登陆用户信息处理---------------------*/
        }
        try {
            filterChain.doFilter(request, response);
        } finally {
            //请求处理完成后将UserContext中的用户信息移除掉
            UserContext.remove();
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
        //重定向到登出
        redirectResponse.setResult(gatewayLogoutServerUrl + "?service=" + gatewayLogoutRedirectUrl);
        PrintWriter writer = response.getWriter();
        writer.write(JSON.toJSONString(redirectResponse));
    }

    public void destroy() {

    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public List<String> getExcludeUrlPatterns() {
        return excludeUrlPatterns;
    }

    public void setExcludeUrlPatterns(List<String> excludeUrlPatterns) {
        this.excludeUrlPatterns = excludeUrlPatterns;
    }

    public String getGatewayLogoutServerUrl() {
        return gatewayLogoutServerUrl;
    }

    public void setGatewayLogoutServerUrl(String gatewayLogoutServerUrl) {
        this.gatewayLogoutServerUrl = gatewayLogoutServerUrl;
    }

    public String getGatewayLogoutRedirectUrl() {
        return gatewayLogoutRedirectUrl;
    }

    public void setGatewayLogoutRedirectUrl(String gatewayLogoutRedirectUrl) {
        this.gatewayLogoutRedirectUrl = gatewayLogoutRedirectUrl;
    }
}
