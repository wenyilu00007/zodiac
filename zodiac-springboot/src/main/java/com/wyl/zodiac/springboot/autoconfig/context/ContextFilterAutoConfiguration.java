package com.wyl.zodiac.springboot.autoconfig.context;

import com.wyl.zodiac.web.filter.ContextFilter;
import com.wyl.zodiac.web.filter.CookieBasedContextFilter;
import com.wyl.zodiac.web.filter.GatewayContextFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
* @Title: ContextFilterAutoConfiguration 
* @Package com.wyl.zodiac.springboot.autoconfig.context
* @Description: contextFilter自动装配实现
* @author
* @date 2017/8/7 21:30
* @version V1.0   
*/
@Configuration
@EnableConfigurationProperties(ContextFilterProperties.class)
public class ContextFilterAutoConfiguration {

    @Autowired
    private ContextFilterProperties contextFilterProperties;

    /**
     * 创建没有通过网关访问的系统使用的context过滤器，初始化RequestContext\SessionContext\UserContext
     * @return
     * @author
     * @date 2017年08月07日21:35:59
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "zodiac.web.context", name = "enable")
    public ContextFilter contextFilter() {
        ContextFilter contextFilter = new ContextFilter();
        contextFilter.setExcludeUrlPatterns(contextFilterProperties.getExcludeUrlPatterns());
        return contextFilter;
    }

    /**
     * 创建使用网关访问系统的系统的context过滤器，初始化RequestContext\SessionContext\UserContext
     * @return
     * @author
     * @date 2017年10月11日18:48:35
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "zodiac.web.context", name = "gatewayEnable")
    public GatewayContextFilter gatewayContextFilter() {
        GatewayContextFilter gatewayContextFilter = new GatewayContextFilter();
        gatewayContextFilter.setExcludeUrlPatterns(contextFilterProperties.getExcludeUrlPatterns());
        gatewayContextFilter.setGatewayLogoutServerUrl(contextFilterProperties.getGatewayLogoutServerUrl());
        gatewayContextFilter.setGatewayLogoutRedirectUrl(contextFilterProperties.getGatewayLogoutRedirectUrl());
        return gatewayContextFilter;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "zodiac.web.context.cookieBasedProperties", name = "enable")
    public CookieBasedContextFilter cookieBasedContextFilter() {
        CookieBasedContextFilter cookieBasedContextFilter = new CookieBasedContextFilter();
        cookieBasedContextFilter.setExcludeUrlPatterns(contextFilterProperties.getExcludeUrlPatterns());
        cookieBasedContextFilter.setCookieName(contextFilterProperties.getCookieBasedProperties().getCookieName());
        cookieBasedContextFilter.setApplicationId(contextFilterProperties.getCookieBasedProperties().getApplicationId());
        cookieBasedContextFilter.setExpireTime(contextFilterProperties.getCookieBasedProperties().getExpireTime());
        return cookieBasedContextFilter;
    }
}
