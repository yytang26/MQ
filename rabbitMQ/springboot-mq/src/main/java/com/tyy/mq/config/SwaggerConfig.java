package com.tyy.mq.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author:tyy
 * @date:2021/10/12
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket webApiConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webApi")
                .apiInfo(webApiInfo())
                .select().build();
    }

    public ApiInfo webApiInfo() {
        return new ApiInfoBuilder()
                .title("rabbitmq接口文档")
                .description("描述")
                .version("1.0")
                .contact(new Contact("tyy", "123", "123"))
                .build();
    }
}
