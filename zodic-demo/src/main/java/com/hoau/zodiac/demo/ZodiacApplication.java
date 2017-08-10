package com.hoau.zodiac.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
* @Title: ZodiacApplication 
* @Package com.hoau.zodiac.demo 
* @Description: 
* @author 陈宇霖  
* @date 2017/8/9 17:28
* @version V1.0   
*/
@SpringBootApplication
@ComponentScan(basePackages = "com.hoau.*")
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class, SessionAutoConfiguration.class})
@MapperScan("com.hoau.**.dao")
public class ZodiacApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZodiacApplication.class, args);
    }
}
