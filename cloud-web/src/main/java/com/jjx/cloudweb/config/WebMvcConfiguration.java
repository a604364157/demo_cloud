package com.jjx.cloudweb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * web应用的统一配置类
 * 用于对所有web工程的统一配置
 * 如果工程有自己的特殊配置，则需要重写该类
 * <p>
 * 目前只重写了RequestMappingHandlerMapping
 * 防止api的直接继承实现导致的启动冲突
 *
 * @author jiangjx
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Override
    @NonNull
    protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
        return new RequestMappingHandler();
    }

}
