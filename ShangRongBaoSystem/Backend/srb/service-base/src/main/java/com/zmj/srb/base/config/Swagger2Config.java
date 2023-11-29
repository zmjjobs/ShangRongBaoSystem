package com.zmj.srb.base.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author : mjzhud
 * @create 2023/11/7 18:17
 */
@Configuration
@EnableSwagger2 //开启Swagger2服务
public class Swagger2Config {
    @Bean
    public Docket adminApiConfig(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("adminApi")
                .apiInfo(adminApiInfo())
                .select()
                .paths(Predicates.and(PathSelectors.regex("/admin/.*")))
                .build();
    }
    private ApiInfo adminApiInfo() {
        return new ApiInfoBuilder().title("SRB后台管理系统API文档")
                .description("本文档描述了SRB后台管理系统各个模块的接口调用方式")
                .version("1.6")
                .contact(new Contact("朱梦君","http://dreams.com","dream98job@126.com"))
                .build();
    }

    @Bean
    public Docket webApiConfig(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webApi")
                .apiInfo(webApiInfo())
                .select()
                .paths(Predicates.and(PathSelectors.regex("/api/.*")))
                .build();
    }
    private ApiInfo webApiInfo() {
        return new ApiInfoBuilder().title("SRB网站API文档")
                .description("本文档描述了SRB网站各个模块的接口调用方式")
                .version("1.6")
                .contact(new Contact("朱梦君","http://dreams.com","dream98job@126.com"))
                .build();
    }
}
