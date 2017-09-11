package com.hoau.zodiac.springboot.autoconfig.web;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import com.hoau.zodiac.core.constant.UrlConstants;
import com.hoau.zodiac.springboot.autoconfig.context.ContextFilterAutoConfiguration;
import com.hoau.zodiac.web.filter.ContextFilter;
import com.hoau.zodiac.web.filter.CorsFilter;
import com.hoau.zodiac.web.filter.IpWhiteListFilter;
import com.hoau.zodiac.web.filter.ParamsVerifyFilter;
import com.hoau.zodiac.web.interceptor.AccessInterceptor;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.beanvalidation.MessageSourceResourceBundleLocator;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;
import javax.validation.Validation;
import javax.validation.Validator;
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

    Logger logger = LoggerFactory.getLogger(WebMvcConfiguration.class);

    @Autowired
    private MessageSource messageSource;

    private ApplicationContext applicationContext;

    private AccessInterceptor accessInterceptor;

    public WebMvcConfiguration(ObjectProvider<AccessInterceptor> accessInterceptorObjectProvider) {
        accessInterceptor = accessInterceptorObjectProvider.getIfAvailable();
    }

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
     * 增加拦截器配置
     * @param registry
     * @author 陈宇霖
     * @date 2017年08月22日15:14:12
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (accessInterceptor != null) {
            registry.addInterceptor(accessInterceptor);
        }
        super.addInterceptors(registry);
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
     * @return
     * @author 陈宇霖
     * @date 2017年08月08日00:17:11
     */
    @Bean
    @ConditionalOnProperty(prefix = "zodiac.web.cors", name = "enable")
    public FilterRegistrationBean corsFilterRegistration() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new CorsFilter());
        registrationBean.setUrlPatterns(Arrays.asList(UrlConstants.MATCH_ALL_URL_PATTERN));
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
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

    /**
     * 参数签名校验过滤器实例
     * @param paramsVerifyFilter
     * @return
     * @author 陈宇霖
     * @date 2017年08月18日14:14:59
     */
    @Bean
    @ConditionalOnBean(ParamsVerifyFilter.class)
    public FilterRegistrationBean aramsVerifyFilterRegistration(ParamsVerifyFilter paramsVerifyFilter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(paramsVerifyFilter);
        registrationBean.setUrlPatterns(Arrays.asList(UrlConstants.MATCH_ALL_URL_PATTERN));
        registrationBean.setOrder(Ordered.LOWEST_PRECEDENCE - 10000);
        return registrationBean;
    }

    /**
     * 创建参数校验validator
     * @return
     * @author 陈宇霖
     * @date 2017年09月08日08:50:46
     */
    @Bean
    public Validator validator () {
        Validator validator = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(
                        new ResourceBundleMessageInterpolator(
                                new MessageSourceResourceBundleLocator(messageSource)
                        )
                )
                .buildValidatorFactory()
                .getValidator();
        return validator;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
