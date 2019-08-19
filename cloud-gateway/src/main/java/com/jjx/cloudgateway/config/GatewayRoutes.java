package com.jjx.cloudgateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jiangjx
 */
@Configuration
public class GatewayRoutes {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("cloud-api")
                        .filters(f-> f.hystrix(config -> config.setName("error").setFallbackUri("forward:/error/fallback")))
                        .uri("lb://cloud-api")).build();
    }

}
