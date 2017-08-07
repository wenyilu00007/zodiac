package com.hoau.zodiac.springboot.autoconfig.context;

import com.hoau.zodiac.web.context.ApplicationContextHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
* @Title: ApplicationContextConfiguration 
* @Package com.hoau.leo.config.context 
* @Description: 配置创建ApplicationContextHolder示例
* @author 陈宇霖  
* @date 2017/8/4 09:20
* @version V1.0   
*/
@Configuration
public class ApplicationContextConfiguration {

    /**
     * 实例化ApplicationContextHolder类
     * 由于实现了ApplicationContextWare接口，在实例化的时候会将ApplicationContext注入到Holder中
     * @return
     * @author 陈宇霖
     * @date 2017年08月04日09:27:19
     */
    @Bean
    public ApplicationContextHolder getApplicationContextHolder() {
        return new ApplicationContextHolder();
    }
}
