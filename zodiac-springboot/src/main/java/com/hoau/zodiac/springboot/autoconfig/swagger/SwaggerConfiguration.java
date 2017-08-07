package com.hoau.zodiac.springboot.autoconfig.swagger;

import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author 刘德云
 * @version V1.0
 * @Title: SwaggerConfiguration
 * @Package com.hoau.leo.config.web
 * @date 2017/8/3
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Value("${spring.application.name}")
    String applicationName;
    @Value("${server.context-path}")
    String contextPath;

    @Bean
    public Docket demoApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(applicationName)
                .genericModelSubstitutes(DeferredResult.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(false)
                .pathMapping("/")
                .select().paths(Predicates.or(PathSelectors.regex("/hello/.*")))//控制哪些接口暴露给Swagger
                .build()
                .apiInfo(demoApiInfo());//用来创建该Api的基本信息(这些基本信息会展现在文档页面中)
    }

    private ApiInfo demoApiInfo() {
        ApiInfo apiInfo = new ApiInfo(
                "SSO 系统接口目录",//标题
                " SSO 对外提供的系统接口的统一说明",//副标题
                "1.0",//版本
                "仅限于华宇内部调用，外部调用会被起诉", //条款
                "供应链研发中心",//作者
                "华宇供应链 SSO 统一登录系统",//链接显示文字
                ""+contextPath//网站链接
        );
        return apiInfo;
    }

}
