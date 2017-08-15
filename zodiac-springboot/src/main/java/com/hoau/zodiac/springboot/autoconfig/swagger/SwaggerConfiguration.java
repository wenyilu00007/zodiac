package com.hoau.zodiac.springboot.autoconfig.swagger;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

/**
 * @author 刘德云
 * @version V1.0
 * @Title: SwaggerConfiguration
 * @Package com.hoau.leo.config.web
 * @date 2017/8/3
 */
@Configuration
@EnableConfigurationProperties(SwaggerProperties.class)
@ConditionalOnProperty(prefix = "zodiac.swagger", name = "enable")
@EnableSwagger2
public class SwaggerConfiguration {

    @Autowired
    SwaggerProperties swaggerProperties;

    @Bean
    public Docket swagger2Api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("doc")
                .genericModelSubstitutes(DeferredResult.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(false)
                .pathMapping("/")
                //控制哪些接口暴露给Swagger
                .select().paths(Predicates.or(generatePaths(swaggerProperties.getPaths())))
                .build()
                //用来创建该Api的基本信息(这些基本信息会展现在文档页面中)
                .apiInfo(apiInfo());
    }

    /**
     * 根据配置构建暴露的接口
     * @param paths
     * @return
     * @author 陈宇霖
     * @date 2017年08月15日13:42:41
     */
    private Predicate[] generatePaths(List<String> paths) {
        if (CollectionUtils.isEmpty(paths)) {
            return null;
        }
        Predicate[] predicates = new Predicate[paths.size()];
        for (int i = 0; i < paths.size(); i ++) {
            predicates[i] = PathSelectors.regex(paths.get(i)+"/.*");
        }
        return predicates;
    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo(
                swaggerProperties.getTitle(),//标题
                swaggerProperties.getDescription(),//副标题
                swaggerProperties.getVersion(),//版本
                swaggerProperties.getTermsOfServiceUrl(), //条款
                swaggerProperties.getContactName(),//作者
                swaggerProperties.getLicense(),//链接显示文字
                swaggerProperties.getLicenseUrl()//网站链接
        );
        return apiInfo;
    }

}
