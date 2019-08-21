package com.jjx.cloudgateway.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jjx.cloudcommom.dto.InDTO;
import com.jjx.cloudgateway.filter.ElapsedFilter;
import com.jjx.cloudgateway.filter.ElapsedGatewayFilterFactory;
import com.jjx.cloudgateway.filter.RateLimitByCpuGatewayFilter;
import com.jjx.cloudgateway.filter.RateLimitByIpGatewayFilter;
import com.jjx.cloudgateway.filter.RemoteAddrKeyResolver;
import com.jjx.cloudgateway.filter.TokenFilter;
import io.netty.buffer.ByteBufAllocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

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
                                .addResponseHeader("X-Request-IP", "127.0.0.1")
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
                                //这里添加一个修改参数体的过滤器
                                //修改参数体依赖modifyRequestBody
                                .modifyRequestBody(Object.class, Void.class, (exchange, obj) -> {
                                    ServerHttpRequest serverHttpRequest = exchange.getRequest();
                                    HttpMethod method = serverHttpRequest.getMethod();
                                    MediaType mediaType = serverHttpRequest.getHeaders().getContentType();
                                    if (mediaType == null || method == null) {
                                        exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
                                        return exchange.getResponse().setComplete();
                                    }
                                    if (method.matches(HttpMethod.POST.toString()) && MediaType.APPLICATION_FORM_URLENCODED_VALUE.equalsIgnoreCase(mediaType.getType())) {
                                        //post请求表单数据，文件上传之类的
                                        String bodyStr = resolveBodyFromRequest(serverHttpRequest);
                                        if (bodyStr == null) {
                                            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
                                            return exchange.getResponse().setComplete();
                                        }
                                    }
                                    if (!method.matches(HttpMethod.GET.toString()) && MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(mediaType.getType())) {
                                        //非GET请求为JSON
                                        URI uri = serverHttpRequest.getURI();
                                        URI newUri = UriComponentsBuilder.fromUri(uri).build(true).toUri();
                                        ServerHttpRequest request = exchange.getRequest().mutate().uri(newUri).build();
                                        String bodyStr = modBodyJson(serverHttpRequest);
                                        DataBuffer bodyDataBuffer = stringBuffer(bodyStr);
                                        Flux<DataBuffer> bodyFlux = Flux.just(bodyDataBuffer);
                                        HttpHeaders headers = new HttpHeaders();
                                        headers.putAll(exchange.getRequest().getHeaders());
                                        int length = bodyStr.getBytes().length;
                                        headers.remove(HttpHeaders.CONTENT_LENGTH);
                                        headers.setContentLength(length);
                                        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                                        request = new ServerHttpRequestDecorator(request) {
                                            @Override
                                            public HttpHeaders getHeaders() {
                                                long contentLength = headers.getContentLength();
                                                HttpHeaders httpHeaders = new HttpHeaders();
                                                httpHeaders.putAll(super.getHeaders());
                                                if (contentLength > 0) {
                                                    httpHeaders.setContentLength(contentLength);
                                                } else {
                                                    httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                                                }
                                                return httpHeaders;
                                            }

                                            @Override
                                            public Flux<DataBuffer> getBody() {
                                                return bodyFlux;
                                            }
                                        };
                                        request.mutate().header(HttpHeaders.CONTENT_LENGTH, Integer.toString(bodyStr.length()));
                                    }
                                    return null;
                                })
                        ).uri("lb://cloud-client").order(0).id("client-1"))
                .build();
    }


    private String resolveBodyFromRequest(ServerHttpRequest serverHttpRequest) {
        Flux<DataBuffer> body = serverHttpRequest.getBody();
        AtomicReference<String> bodyRef = new AtomicReference<>();
        body.subscribe(buffer -> {
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
            DataBufferUtils.release(buffer);
            bodyRef.set(charBuffer.toString());
        });
        return bodyRef.get();
    }

    private DataBuffer stringBuffer(String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(ByteBufAllocator.DEFAULT);
        DataBuffer buffer = nettyDataBufferFactory.allocateBuffer(bytes.length);
        buffer.write(bytes);
        return buffer;
    }

    private String modBodyJson(ServerHttpRequest serverHttpRequest) {
        String body = resolveBodyFromRequest(serverHttpRequest);
        JSONObject object = JSON.parseObject(body);
        InDTO<Object> inDTO = new InDTO<>();
        inDTO.setBody(object);
        inDTO.setHeader(new JSONObject());
        return JSON.toJSONString(inDTO);
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
}
