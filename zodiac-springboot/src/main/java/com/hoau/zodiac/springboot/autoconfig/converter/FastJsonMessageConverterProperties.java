package com.hoau.zodiac.springboot.autoconfig.converter;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
* @Title: FastJsonMessageConverterProperties 
* @Package com.hoau.leo.config.converter 
* @Description: FastJson转换参数配置
* @author 陈宇霖  
* @date 2017/8/2 15:43
* @version V1.0   
*/
@ConfigurationProperties(prefix = "zodiac.fastjson")
public class FastJsonMessageConverterProperties {

    /**
     * 支持的参数类型
     */
    private List<String> supportedMediaTypes;

    /**
     * 转换的配置
     */
    private List<String> features;

    public List<String> getSupportedMediaTypes() {
        return supportedMediaTypes;
    }

    public void setSupportedMediaTypes(List<String> supportedMediaTypes) {
        this.supportedMediaTypes = supportedMediaTypes;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }
}
