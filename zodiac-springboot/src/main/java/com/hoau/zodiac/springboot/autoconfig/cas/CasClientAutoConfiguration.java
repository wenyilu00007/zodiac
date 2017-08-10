package com.hoau.zodiac.springboot.autoconfig.cas;

import com.hoau.zodiac.springboot.autoconfig.session.RedisSessionConfiguration;
import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas30ProxyReceivingTicketValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
* @Title: CasClientAutoConfiguration 
* @Package com.hoau.leo.config.cas 
* @Description: CAS 客户端对应filter自动装载配置
* @author 陈宇霖  
* @date 2017/8/1 22:35
* @version V1.0   
*/
@Configuration
@EnableConfigurationProperties(CasClientProperties.class)
@AutoConfigureAfter({RedisSessionConfiguration.class})
@ConditionalOnProperty(name = "zodiac.cas.client.enable", havingValue = "true")
public class CasClientAutoConfiguration {

    @Autowired
    private CasClientProperties casClientProperties;

    /**
     * 登陆校验过滤器
     * @return
     * @auhtor 陈宇霖
     * @date 2017年08月01日23:38:43
     */
    @Bean
    public FilterRegistrationBean casAuthenticationFilter() {
        FilterRegistrationBean authenticationFilter = new FilterRegistrationBean();
        authenticationFilter.setFilter(new AuthenticationFilter());
        authenticationFilter.setInitParameters(generateAuthenticationFilterInitParameter());
        authenticationFilter.setUrlPatterns(casClientProperties.getAuthenticationUrlPatterns());
        authenticationFilter.setOrder(Ordered.LOWEST_PRECEDENCE - 100);
        return authenticationFilter;
    }

    /**
     * 构建登陆校验过滤器初始化参数
     * @return
     * @author 陈宇霖
     * @date 2017年08月02日00:15:52
     */
    public Map<String, String> generateAuthenticationFilterInitParameter() {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("casServerLoginUrl", casClientProperties.getCasServerLoginUrl());
        params.put("serverName", casClientProperties.getAppServerName());
        if (!StringUtils.isEmpty(casClientProperties.getAuthenticationRedirectStrategyClass())) {
            params.put("authenticationRedirectStrategyClass", casClientProperties.getAuthenticationRedirectStrategyClass());
        }
        //增加不进行拦截的配置
        String exclusions = casClientProperties.getAuthenticationExclusions();
        if (!StringUtils.isEmpty(exclusions)) {
            params.put("ignorePattern", exclusions);
        }
        return params;
    }

    /**
     * 票据校验过滤器
     * @return
     * @auhtor 陈宇霖
     * @date 2017年08月01日23:38:43
     */
    @Bean
    public FilterRegistrationBean casValidationFilter() {
        FilterRegistrationBean validationFilter = new FilterRegistrationBean();
        validationFilter.setFilter(new Cas30ProxyReceivingTicketValidationFilter());
        validationFilter.setInitParameters(generateValidationFilterInitParameter());
        validationFilter.setUrlPatterns(casClientProperties.getValidationUrlPatterns());
        //增加不进行拦截的配置
        String exclusions = casClientProperties.getValidationExclusions();
        if (!StringUtils.isEmpty(exclusions)) {
            validationFilter.addInitParameter("ignorePattern", exclusions);
        }
        validationFilter.setOrder(Ordered.LOWEST_PRECEDENCE - 90);
        return validationFilter;
    }

    /**
     * 构建票据校验过滤器初始化参数
     * @return
     * @author 陈宇霖
     * @date 2017年08月02日00:15:52
     */
    public Map<String, String> generateValidationFilterInitParameter() {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("casServerUrlPrefix", casClientProperties.getCasServerUrlPrefix());
        params.put("serverName", casClientProperties.getAppServerName());
        //增加不进行拦截的配置
        String exclusions = casClientProperties.getValidationExclusions();
        if (!StringUtils.isEmpty(exclusions)) {
            params.put("ignorePattern", exclusions);
        }
        return params;
    }

    /**
     * 票据校验过滤器
     * @return
     * @auhtor 陈宇霖
     * @date 2017年08月01日23:38:43
     */
    @Bean
    public FilterRegistrationBean casRequestWrapperFilter() {
        FilterRegistrationBean requestWrapperFilter = new FilterRegistrationBean();
        requestWrapperFilter.setFilter(new HttpServletRequestWrapperFilter());
        requestWrapperFilter.setUrlPatterns(casClientProperties.getAuthenticationUrlPatterns());
        requestWrapperFilter.setOrder(Ordered.LOWEST_PRECEDENCE - 80);
        return requestWrapperFilter;
    }

}
