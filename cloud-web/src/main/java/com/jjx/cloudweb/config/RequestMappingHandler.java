package com.jjx.cloudweb.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 重写RequestMappingHandlerMapping的isHandler
 * 将接口或带feign的接口从mapping中排除
 * 防止api包直接继承实现而导致的启动冲突
 *
 * @author jiangjx
 */
public class RequestMappingHandler extends RequestMappingHandlerMapping {

    @Override
    protected boolean isHandler(Class<?> beanType) {
        return super.isHandler(beanType) && (!beanType.isInterface() || !AnnotatedElementUtils.hasAnnotation(beanType, FeignClient.class));
    }
}
