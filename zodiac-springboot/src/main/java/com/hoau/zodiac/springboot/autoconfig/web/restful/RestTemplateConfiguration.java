package com.hoau.zodiac.springboot.autoconfig.web.restful;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.List;

/**
* @Title: RestTemplateConfiguration 
* @Package com.hoau.zodiac.springboot.autoconfig.web.restful
* @Description: restful接口调用客户端自动装配
* @author 陈宇霖  
* @date 2017/8/17 10:35
* @version V1.0   
*/
@Configuration
@EnableConfigurationProperties(RestfulTemplateProperties.class)
public class RestTemplateConfiguration {

    @Autowired
    private RestfulTemplateProperties restfulTemplateProperties;

    /**
     * 创建访问restful接口的客户端工具
     * @return
     * @author 陈宇霖
     * @date 2017年08月17日11:19:52
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RestTemplateClient restTemplateClient() {
        RestTemplateClient restTemplateClient = new RestTemplateClient();
        restTemplateClient.setRestfulTemplateProperties(restfulTemplateProperties);
        return restTemplateClient;
    }
}
