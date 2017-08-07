package com.hoau.zodiac.springboot.autoconfig.message;

import com.hoau.zodiac.core.constant.CookieConstants;
import com.hoau.zodiac.core.message.LocaleMessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import java.util.Locale;

/**
* @Title: LocaleMessageSourceAutoConfiguration 
* @Package com.hoau.leo.config.message 
* @Description: 消息国际化处理自动装载
* @author 陈宇霖  
* @date 2017/8/3 00:19
* @version V1.0   
*/
@Configuration
@EnableConfigurationProperties(LocaleMessageProperties.class)
public class LocaleMessageAutoConfiguration {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocaleMessageProperties localeMessageProperties;

    /**
     * 实例化自动以的国际化消息获取类
     * @return
     * @author 陈宇霖
     * @date 2017年08月03日00:27:58
     */
    @Bean
    @ConditionalOnProperty(prefix = "wabapp.message", name = "enable", matchIfMissing = true)
    public LocaleMessageSource localeMessageSource() {
        LocaleMessageSource localeMessageSource = new LocaleMessageSource();
        localeMessageSource.setMessageSource(messageSource);
        return localeMessageSource;
    }

    /**
     * 自动构建基于cookie的国际化处理类
     * @return
     * @author 陈宇霖
     * @date 2017年08月03日00:44:27
     */
    @Bean
    @ConditionalOnProperty(prefix = "wabapp.message", name = "enable", matchIfMissing = true)
    public LocaleResolver localeResolver() {
        CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
        cookieLocaleResolver.setCookieName(CookieConstants.COOKIE_LANGUAGE_NAME);
        if (StringUtils.isEmpty(localeMessageProperties.getDefaultCountry())
                || StringUtils.isEmpty(localeMessageProperties.getDefaultLang())) {
            cookieLocaleResolver.setDefaultLocale(Locale.CHINA);
        } else {
            cookieLocaleResolver.setDefaultLocale(new Locale(localeMessageProperties.getDefaultLang(), localeMessageProperties.getDefaultCountry()));
        }
        cookieLocaleResolver.setCookieMaxAge(localeMessageProperties.getCookieMaxAge());
        return cookieLocaleResolver;
    }

}
