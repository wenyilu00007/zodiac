package com.hoau.zodiac.springboot.autoconfig.request;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;

/**
* @Title: RequestHolderAutoConfiguration 
* @Package com.hoau.leo.config.request 
* @Description: RequestContextListener自动装配
* @author 陈宇霖  
* @date 2017/8/2 17:10
* @version V1.0   
*/
@Configuration
@ConditionalOnProperty(prefix = "zodiac.request.holder", name = "enable")
public class RequestHolderAutoConfiguration {

    /**
     * 增加RequestContextListener配置，可以全局获取request对象
     * @return
     * @author 陈宇霖
     * @date 2017年08月02日17:18:17
     */
    @Bean
    public ServletListenerRegistrationBean<RequestContextListener> requestContextListenerServletListenerRegistrationBean() {
        return new ServletListenerRegistrationBean(new RequestContextListener());
    }
}
