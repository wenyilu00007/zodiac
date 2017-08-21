package com.hoau.zodiac.springboot.autoconfig.web;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import com.hoau.zodiac.core.constant.UrlConstants;
import com.hoau.zodiac.springboot.autoconfig.context.ContextFilterAutoConfiguration;
import com.hoau.zodiac.web.filter.ContextFilter;
import com.hoau.zodiac.web.filter.CorsFilter;
import com.hoau.zodiac.web.filter.IpWhiteListFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Arrays;
import java.util.List;

/**
* @Title: WebMvcConfiguration 
* @Package com.hoau.leo.config.web 
* @Description: mvc相关配置
* @author 陈宇霖  
* @date 2017/8/2 15:37
* @version V1.0   
*/
@Configuration
@ConditionalOnWebApplication
@AutoConfigureAfter(ContextFilterAutoConfiguration.class)
public class WebMvcConfiguration extends WebMvcConfigurerAdapter implements ApplicationContextAware{

    private ApplicationContext applicationContext;

    Logger logger = LoggerFactory.getLogger(WebMvcConfiguration.class);

    /**
     * 增加使用fastjson消息转换器
     * @param converters
     * @author 陈宇霖
     * @date 2017年08月02日17:14:38
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        FastJsonHttpMessageConverter4 converter = null;
        try {
            converter = applicationContext.getBean("fastJsonHttpMethodMessageConverter", FastJsonHttpMessageConverter4.class);
        } catch (NoSuchBeanDefinitionException e) {
            logger.warn("fastJsonHttpMethodMessageConverter bean not created, will use default messageConverter");
        }
        if (converter != null) {
            converters.add(converter);
        }
    }

    /**
     * 构建context过滤器实例
     * @param contextFilter
     * @return
     * @author 陈宇霖
     * @date 2017年08月08日00:17:11
     */
    @Bean
    @ConditionalOnBean(ContextFilter.class)
    public FilterRegistrationBean contextFilterRegistration(ContextFilter contextFilter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(contextFilter);
        registrationBean.setUrlPatterns(Arrays.asList(UrlConstants.MATCH_ALL_URL_PATTERN));
        return registrationBean;
    }

    /**
     * 跨域过滤器实例
     * @param corsFilter
     * @return
     * @author 陈宇霖
     * @date 2017年08月08日00:17:11
     */
    @Bean
    @ConditionalOnBean(CorsFilter.class)
    public FilterRegistrationBean corsFilterRegistration(CorsFilter corsFilter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(corsFilter);
        registrationBean.setUrlPatterns(Arrays.asList(UrlConstants.MATCH_ALL_URL_PATTERN));
        registrationBean.setOrder(Ordered.LOWEST_PRECEDENCE - 200);
        return registrationBean;
    }

    /**
     * ip白名单过滤器实例
     * @param ipWhiteListFilter
     * @return
     * @author 陈宇霖
     * @date 2017年08月18日14:14:59
     */
    @Bean
    @ConditionalOnBean(IpWhiteListFilter.class)
    public FilterRegistrationBean ipWhiteListFilterRegistration(IpWhiteListFilter ipWhiteListFilter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(ipWhiteListFilter);
        registrationBean.setUrlPatterns(Arrays.asList(UrlConstants.MATCH_ALL_URL_PATTERN));
        registrationBean.setOrder(Ordered.LOWEST_PRECEDENCE - 20000);
        return registrationBean;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
