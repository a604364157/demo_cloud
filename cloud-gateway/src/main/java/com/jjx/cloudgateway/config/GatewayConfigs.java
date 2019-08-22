package com.jjx.cloudgateway.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jjx.cloudcommom.dto.InDTO;
import com.jjx.cloudgateway.filter.ElapsedFilter;
import com.jjx.cloudgateway.filter.ElapsedGatewayFilterFactory;
import com.jjx.cloudgateway.filter.RateLimitByCpuGatewayFilter;
import com.jjx.cloudgateway.filter.RateLimitByIpGatewayFilter;
import com.jjx.cloudgateway.filter.RemoteAddrKeyResolver;
import com.jjx.cloudgateway.filter.StandardParamFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * 配置类
 * 参考windmt.com
 *
 * @author jiangjx
 */
@Configuration
public class GatewayConfigs {

    @Autowired
    private RateLimitByCpuGatewayFilter cpuGatewayFilter;

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
                                .addResponseHeader("X-Response-IP", "127.0.0.1")
                                //添加一个自定义过滤器
                                .filter(new ElapsedFilter())
                                //添加一个自定义限流（一分钟最多调10次）(理论如此，但实际限流器会在达到流量阈值时尝试给一个调用量)
                                .filter(new RateLimitByIpGatewayFilter(10, 10, Duration.ofSeconds(60)))
                                //添加一个按CPU占用的动态限流
                                .filter(cpuGatewayFilter)
                                //添加一个熔断器，熔断后的转发路径
                                .hystrix(config -> config.setFallbackUri("forward:/error/fallback"))
                                //添加一个路由重试，这里是遇到500错误重试两次
                                .retry(config -> config.setRetries(2).setStatuses(HttpStatus.INTERNAL_SERVER_ERROR))
                                //添加一个修改请求参数的过滤器
                                .modifyRequestBody(String.class, String.class, (exchange, s) -> {
                                    ServerHttpRequest request = exchange.getRequest();
                                    HttpMethod method = request.getMethod();
                                    MediaType mediaType = request.getHeaders().getContentType();
                                    boolean flag = !HttpMethod.GET.equals(method)
                                            && (MediaType.APPLICATION_JSON.equalsTypeAndSubtype(mediaType)
                                            || MediaType.APPLICATION_JSON_UTF8.equalsTypeAndSubtype(mediaType));
                                    if (flag) {
                                        return Mono.just(anewRequestBody(s));
                                    }
                                    return Mono.just(s);
                                })
                        ).uri("lb://cloud-client").order(0).id("client-1"))
                .build();
    }

    /**
     * 全局过滤器
     * 标准化参数结构
     *
     * @return 实例
     */
    @Bean
    public StandardParamFilter standardParamFilter() {
        return new StandardParamFilter();
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

    /**
     * 限流工厂的key规则配置
     * 这是按IP
     *
     * @return 实例
     */
    @Bean("remoteAddrKeyResolver")
    public RemoteAddrKeyResolver keyResolver() {
        return new RemoteAddrKeyResolver();
    }

    private String anewRequestBody(String bodyStr) {
        //这里是因为我已经实现了一个全局过滤器用于修改报文结构
        //这个单独配置的就不能再二次修改结构了
        //这里只是为了展示几种实现方式
        if (bodyStr.isEmpty()) {
            //重组入参结构，适配入参框架
            JSONObject body = JSON.parseObject(bodyStr);
            InDTO<JSONObject> in = new InDTO<>();
            in.setBody(body);
            //这里可以添加一些公共信息，登录工号信息，客户端信息等（从session和请求头获取）
            in.setHeader(new JSONObject());
            return JSON.toJSONString(in);
        } else {
            return bodyStr;
        }
    }
}
