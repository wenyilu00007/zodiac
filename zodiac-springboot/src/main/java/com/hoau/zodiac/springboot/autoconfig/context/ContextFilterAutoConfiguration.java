package com.hoau.zodiac.springboot.autoconfig.context;

import com.hoau.zodiac.core.constant.UrlConstants;
import com.hoau.zodiac.web.filter.ContextFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
* @Title: ContextFilterAutoConfiguration 
* @Package com.hoau.zodiac.springboot.autoconfig.context 
* @Description: contextFilter自动装备实现
* @author 陈宇霖  
* @date 2017/8/7 21:30
* @version V1.0   
*/
@Configuration
public class ContextFilterAutoConfiguration {

    /**
     * 创建框架使用的context拦截器，初始化RequestContext\SessionContext\UserContext
     * @return
     * @author 陈宇霖
     * @date 2017年08月07日21:35:59
     */
    @Bean
    @ConditionalOnMissingBean
    public ContextFilter contextFilter() {
//        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
//        registrationBean.setFilter(new ContextFilter());
//        registrationBean.setUrlPatterns(Arrays.asList(UrlConstants.MATCH_ALL_URL_PATTERN));
        return new ContextFilter();
    }
}
