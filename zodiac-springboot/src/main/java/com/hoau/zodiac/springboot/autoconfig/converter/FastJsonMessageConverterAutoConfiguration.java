package com.hoau.zodiac.springboot.autoconfig.converter;

import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
* @Title: FastJsonMessageConverterAutoConfiguration 
* @Package com.hoau.leo.config.converter 
* @Description: FastJson自动装载
* @author 陈宇霖  
* @date 2017/8/2 15:49
* @version V1.0   
*/
@Configuration
@EnableConfigurationProperties(FastJsonMessageConverterProperties.class)
@ConditionalOnProperty(prefix = "zodiac.fastjson", name = "enable")
public class FastJsonMessageConverterAutoConfiguration {

    @Autowired
    private FastJsonMessageConverterProperties fastJsonMessageConverterProperties;

    /**
     * 配置fastjson转换类
     * @return
     * @author 陈宇霖
     * @date 2017年08月02日16:09:38
     */
    @Bean
    public FastJsonHttpMessageConverter4 fastJsonHttpMethodMessageConverter() {
        FastJsonHttpMessageConverter4 fastJsonHttpMessageConverter4 = new FastJsonHttpMessageConverter4();
        fastJsonHttpMessageConverter4.setSupportedMediaTypes(supportedMediaTypes());
        fastJsonHttpMessageConverter4.setFastJsonConfig(fastJsonConfig());
        return fastJsonHttpMessageConverter4;
    }

    /**
     * 配置可处理的消息类型
     * @return
     * @author 陈宇霖
     * @date 2017年08月02日16:15:06
     */
    private List<MediaType> supportedMediaTypes() {
        List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
        List<String> supportedMediaTypesConfig = fastJsonMessageConverterProperties.getSupportedMediaTypes();
        if (!CollectionUtils.isEmpty(supportedMediaTypesConfig)) {
            for (String supportedMediaType : supportedMediaTypesConfig) {
                supportedMediaTypes.add(MediaType.valueOf(supportedMediaType));
            }
        }
        return supportedMediaTypes;
    }

    /**
     * 配置转换规则
     * @return
     * @author 陈宇霖
     * @date 2017年08月02日16:14:51
     */
    @Bean
    public FastJsonConfig fastJsonConfig() {
        FastJsonConfig config = new FastJsonConfig();
        List<String> featuresConfig = fastJsonMessageConverterProperties.getFeatures();
        if (!CollectionUtils.isEmpty(featuresConfig)) {
            Feature[] features = new Feature[featuresConfig.size()];
            for (int i = 0; i < featuresConfig.size(); i++) {
                features[i] = Feature.valueOf(featuresConfig.get(i));
            }
            config.setFeatures(features);
        }
        return config;
    }

}
