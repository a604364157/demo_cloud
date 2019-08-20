package com.jjx.cloudgateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 自定义过滤器
 * <p>
 * 本过滤器用于统计服务调用时间
 *
 * @author jiangjx
 */
@Slf4j
public class ElapsedFilter implements GatewayFilter, Ordered {

    private static final String SERVER_BEGIN_TIME = "serverBeginTime";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        exchange.getAttributes().put(SERVER_BEGIN_TIME, System.currentTimeMillis());
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            Long startTime = exchange.getAttribute(SERVER_BEGIN_TIME);
            if (startTime != null) {
                //通过日志打印服务响应时间
                log.info(exchange.getRequest().getURI().getRawPath() + ": " + (System.currentTimeMillis() - startTime) + "ms");
            }
        }));
    }

    @Override
    public int getOrder() {
        //这个是设置过滤器优先级
        return Ordered.LOWEST_PRECEDENCE;
    }
}
