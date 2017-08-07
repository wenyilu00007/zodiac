package com.hoau.zodiac.springboot.autoconfig.web;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import com.hoau.zodiac.core.constant.UrlConstants;
import com.hoau.zodiac.springboot.autoconfig.context.ContextFilterAutoConfiguration;
import com.hoau.zodiac.web.filter.ContextFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private FastJsonHttpMessageConverter4 fastJsonHttpMessageConverter4;


    /**
     * 增加使用fastjson消息转换器
     * @param converters
     * @author 陈宇霖
     * @date 2017年08月02日17:14:38
     */
    @Override
    @ConditionalOnBean(FastJsonHttpMessageConverter4.class)
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        converters.add(fastJsonHttpMessageConverter4);
    }

    /**
     * 增加跨域访问
     * @param registry
     * @author 刘德云
     * @date 2017/8/6
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowedOrigins("*");
        super.addCorsMappings(registry);
    }

    /**
     * 构建context过滤器实例
     * @param contextFilter
     * @return
     * @author 陈宇霖
     * @date 2017年08月08日00:17:11
     */
    @Bean
    public FilterRegistrationBean contextFilterRegistration(ContextFilter contextFilter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(contextFilter);
        registrationBean.setUrlPatterns(Arrays.asList(UrlConstants.MATCH_ALL_URL_PATTERN));
        return registrationBean;
    }


}
