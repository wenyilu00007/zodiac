package com.wyl.zodiac.springboot.autoconfig.web.restful;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import com.wyl.zodiac.springboot.autoconfig.converter.FastJsonMessageConverterAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.List;

/**
* @Title: RestTemplateConfiguration 
* @Package com.wyl.zodiac.springboot.autoconfig.web.restful
* @Description: restful接口调用客户端自动装配
* @author
* @date 2017/8/17 10:35
* @version V1.0   
*/
@Configuration
@EnableConfigurationProperties(RestfulTemplateProperties.class)
@AutoConfigureAfter({FastJsonMessageConverterAutoConfiguration.class})
public class RestTemplateConfiguration {

    @Autowired
    private RestfulTemplateProperties restfulTemplateProperties;

    @Autowired(required = false)
    private FastJsonHttpMessageConverter4 fastJsonHttpMethodMessageConverter;

    /**
     * 创建访问restful接口的客户端工具
     * @return
     * @author
     * @date 2017年08月17日11:19:52
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RestTemplateClient restTemplateClient() {
        RestTemplateClient restTemplateClient = new RestTemplateClient();
        restTemplateClient.setRestfulTemplateProperties(restfulTemplateProperties);
        restTemplateClient.setFastJsonHttpMessageConverter4(fastJsonHttpMethodMessageConverter);
        return restTemplateClient;
    }
}
