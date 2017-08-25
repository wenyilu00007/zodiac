package com.hoau.zodiac.springboot.autoconfig.message;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
* @Title: LocaleMessageProperties 
* @Package com.hoau.leo.config.message 
* @Description: 消息国际化处理配置类
* @author 陈宇霖  
* @date 2017/8/3 00:30
* @version V1.0   
*/
@ConfigurationProperties(prefix = "zodiac.message")
public class LocaleMessageProperties {

    /**
     * 是否启用自动配置
     */
    private boolean enable;

    /**
     * 默认地区
     */
    private String defaultCountry;

    /**
     * 默认语言
     */
    private String defaultLang;

    /**
     * cookie保存时间
     */
    private int cookieMaxAge;
    /**
     * Cookie中存储语言的键
     */
    private String cookieLanguageNameKey;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getDefaultCountry() {
        return defaultCountry;
    }

    public void setDefaultCountry(String defaultCountry) {
        this.defaultCountry = defaultCountry;
    }

    public String getDefaultLang() {
        return defaultLang;
    }

    public void setDefaultLang(String defaultLang) {
        this.defaultLang = defaultLang;
    }

    public int getCookieMaxAge() {
        return cookieMaxAge;
    }

    public void setCookieMaxAge(int cookieMaxAge) {
        this.cookieMaxAge = cookieMaxAge;
    }

    public String getCookieLanguageNameKey() {
        return cookieLanguageNameKey;
    }

    public void setCookieLanguageNameKey(String cookieLanguageNameKey) {
        this.cookieLanguageNameKey = cookieLanguageNameKey;
    }
}
