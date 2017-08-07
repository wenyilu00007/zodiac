package com.hoau.zodiac.web.filter;

import com.hoau.zodiac.core.dataprovider.IUserProvider;
import com.hoau.zodiac.core.entity.IUser;
import com.hoau.zodiac.web.context.RequestContext;
import com.hoau.zodiac.web.context.SessionContext;
import com.hoau.zodiac.web.context.UserContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
* @Title: ContextFilter 
* @Package com.hoau.leo.common.filter 
* @Description: 主要进行各个Context的初始化等操作
* @author 陈宇霖  
* @date 2017/8/2 19:12
* @version V1.0   
*/
public class ContextFilter extends OncePerRequestFilter implements ApplicationContextAware{

    Log logger = LogFactory.getLog(ContextFilter.class);

    private ApplicationContext applicationContext;

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 不进行过滤的请求pattern
     * TODO 增加配置
     */
    private Set<String> excludeUrlPattern = new HashSet<String>(Arrays.asList("/health"));

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String url = request.getServletPath();
        boolean matched = false;
        for (String pattern : excludeUrlPattern) {
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
            //设置登陆的用户相关信息
            //先从session中获取用户id,如果session为空，不创建
            String userId = SessionContext.getCurrentUserId();
            //如果session中没有，则从request中获取
            if (StringUtils.isEmpty(userId)) {
                Principal principal = httpRequest.getUserPrincipal();
                if (principal != null) {
                    userId = httpRequest.getUserPrincipal().getName();
                }
            }

            //如果还是没有用户，那就有问题了吧
            if (StringUtils.isEmpty(userId)) {
                SessionContext.invalidateSession();
                httpResponse.sendRedirect("/");
                return;
            }
            RequestContext.setCurrentUserId(userId);
            SessionContext.setCurrentUserId(userId);

            //存缓存中获取用户信息
            IUserProvider userProvider = applicationContext.getBean(IUserProvider.class);
            if (userProvider == null) {
                logger.error("系统异常，未实现用户信息提供接口！");
                SessionContext.invalidateSession();
                httpResponse.sendRedirect("/");
                return;
            }
            //从提供者读取数据
            IUser user = userProvider.getUserById(userId);
            //如果通过用户id获取到的用户为空，则跳转到登陆界面
            if (user == null) {
                SessionContext.invalidateSession();
                httpResponse.sendRedirect("/");
                return;
            }
            //将用户信息设置到UserContext中
            UserContext.setCurrentUser(user);
            /**----------------------end of 登陆用户信息处理---------------------*/

            httpRequest.getCookies();
        }
        try {
            filterChain.doFilter(request, response);
        } finally {
            //请求处理完成后将UserContext中的用户信息移除掉
            UserContext.remove();
        }
    }

    public void destroy() {

    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public Set<String> getExcludeUrlPattern() {
        return excludeUrlPattern;
    }

    public void setExcludeUrlPattern(Set<String> excludeUrlPattern) {
        this.excludeUrlPattern = excludeUrlPattern;
    }
}
