package com.jjx.cloudgateway.filter;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;

/**
 * 这是给限流工厂用的规则配置
 * 这是按IP限流
 *
 * @author jiangjx
 */
public class RemoteAddrKeyResolver implements KeyResolver {
    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        InetSocketAddress address = exchange.getRequest().getRemoteAddress();
        if (address == null) {
            throw new RuntimeException("netSocketAddress is null");
        }
        return Mono.just(address.getAddress().getHostAddress());
    }
}
