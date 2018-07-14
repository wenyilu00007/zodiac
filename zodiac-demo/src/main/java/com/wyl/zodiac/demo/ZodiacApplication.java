package com.wyl.zodiac.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
* @Title: ZodiacApplication 
* @Package com.wyl.zodiac.demo
* @Description: 
* @author
* @date 2017/8/9 17:28
* @version V1.0   
*/
@SpringBootApplication
@ComponentScan(basePackages = "com.wyl.*")
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class, SessionAutoConfiguration.class})
@MapperScan("com.wyl.**.dao")
public class ZodiacApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZodiacApplication.class, args);
    }
}
