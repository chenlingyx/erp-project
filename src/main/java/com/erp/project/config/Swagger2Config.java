package com.erp.project.config;

import cn.hutool.core.util.StrUtil;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.RequestHandler;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
* @ClassName : Swagger2Config
* @Description :
* @author : chenling
* @Date : 2019/6/5 16:52
* @since : v1.0.0
**/
@EnableSwagger2
@Configuration
@Slf4j
public class Swagger2Config {

    @Value("${swagger.base.package:}")
    private String swaggerBasePackage;

    private static final String SPLITOR = ",";

    @Bean
    public Docket productApi() {
        StringBuilder basePackage = new StringBuilder("com.erp.project.controller");
        if (StrUtil.isNotBlank(swaggerBasePackage)) {
            basePackage.append(SPLITOR);
            basePackage.append(swaggerBasePackage);
        }
        log.info("swagger扫描路径：{}", basePackage);
        return new Docket(DocumentationType.SWAGGER_2)
                .genericModelSubstitutes(DeferredResult.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(false)
                .select()
                .apis(basePackage(basePackage.toString()))
                .build()
                .apiInfo(productApiInfo());
    }

    private ApiInfo productApiInfo() {
        return new ApiInfo("流程引擎接口文档",
                "文档描述。。。",
                "v1.0.0",
                "API TERMS URL",
                "联系人邮箱",
                "license",
                "license url");
    }


    /**
     * 参照swagger的做法，自定义扫描路径
     * @param basePackage
     * @return
     */
    public static Predicate<RequestHandler> basePackage(final String basePackage) {
        return input -> declaringClass(input).transform(handlerPackage(basePackage)).or(true);
    }

    private static Function<Class<?>, Boolean> handlerPackage(final String basePackage) {
        return input -> {
            // 循环判断匹配
            for (String strPackage : basePackage.split(SPLITOR)) {
                boolean isMatch = input.getPackage().getName().startsWith(strPackage);
                if (isMatch) {
                    return true;
                }
            }
            return false;
        };
    }

    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.fromNullable(input.declaringClass());
    }


}
