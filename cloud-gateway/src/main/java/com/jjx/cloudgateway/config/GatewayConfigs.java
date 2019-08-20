package com.jjx.cloudgateway.config;

import com.jjx.cloudgateway.filter.ElapsedFilter;
import com.jjx.cloudgateway.filter.ElapsedGatewayFilterFactory;
import com.jjx.cloudgateway.filter.TokenFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类
 *
 * @author jiangjx
 */
@Configuration
public class GatewayConfigs {

    /**
     * 路由配置
     *
     * @param builder 路由构建器
     * @return 路由
     */
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                //这里添加一个单独的路由，为了和自动配置的service_id区分，加一个-1
                //加入一个过滤器用来测试路由是否生效
                .route(r -> r.path("/cloud-client-1/**")
                        .filters(f -> f.stripPrefix(1)
                                //添加一个给请求头加参数
                                .addRequestHeader("X-Request-IP", "127.0.0.1")
                                //添加一个给响应头加参数
                                .addResponseHeader("X-Request-IP", "127.0.0.1")
                                //添加一个自定义过滤器
                                .filter(new ElapsedFilter())
                        ).uri("lb://cloud-client").order(0).id("client-1"))
                .build();
    }

    /**
     * 全局过滤器
     *
     * @return 实例
     */
    @Bean
    public TokenFilter tokenFilter() {
        return new TokenFilter();
    }

    /**
     * 自定义过滤工厂
     *
     * @return 实例
     */
    @Bean
    public ElapsedGatewayFilterFactory filterFactory() {
        return new ElapsedGatewayFilterFactory();
    }

}
