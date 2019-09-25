package com.jjx.cloudweb.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author jiangjx
 */
public class RequestMappingHandler extends RequestMappingHandlerMapping {

    @Override
    protected boolean isHandler(Class<?> beanType) {
        return super.isHandler(beanType) && (!beanType.isInterface() || !AnnotatedElementUtils.hasAnnotation(beanType, FeignClient.class));
    }
}
