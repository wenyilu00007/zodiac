package com.hoau.zodiac.springboot.autoconfig.security;

import com.hoau.zodiac.core.security.SecurityAccess;
import com.hoau.zodiac.springboot.autoconfig.message.LocaleMessageProperties;
import com.hoau.zodiac.web.interceptor.AccessInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
* @Title: SecurityAccessAutoConfiguration
* @Package com.hoau.leo.config.web 
* @Description: 访问控制自动装配
* @author 陈宇霖  
* @date 2017/8/4 16:45
* @version V1.0   
*/
@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties(LocaleMessageProperties.class)
@ConditionalOnProperty(prefix = "zodiac.web.securityAccess", name = "enable")
public class SecurityAccessAutoConfiguration {

    /**
     * 创建访问控制实体
     * @return
     * @author 陈宇霖
     * @date 2017年08月06日09:59:10
     */
    @Bean
    @ConditionalOnMissingBean
    public SecurityAccess securityAccess () {
        return new SecurityAccess();
    }

    /**
     * 创建访问控制拦截器
     * @return
     * @author 陈宇霖
     * @date 2017年08月06日09:59:27
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "zodiac.web.securityAccess", name = "enable")
    public AccessInterceptor accessInterceptor() {
        return new AccessInterceptor();
    }

}