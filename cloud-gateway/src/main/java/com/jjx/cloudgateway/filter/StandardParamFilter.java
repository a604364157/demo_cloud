package com.jjx.cloudgateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jjx.cloudcommom.dto.InDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

/**
 * 准备一个全局过滤器，用于标准化参数结构
 * 添加部分参数（可以是客户端信息，认证参数等）
 * 这个全局过滤器是仿造modifyRequestBody写的
 * 实际上，修改参数体有默认的过滤工厂
 * 配置单路由可以在配置类中进行配置（modifyRequestBody）
 * 配置全局只能通过配置文件，目前官方对于这个的说明不详细
 * 所以我们可以将ModifyRequestBodyGatewayFilterFactory的
 * 实现方式抽取出来实现一个自己的全局过滤器
 *
 * @author jiangjx
 */
@Slf4j
public class StandardParamFilter implements GlobalFilter, Ordered {

    private final List<HttpMessageReader<?>> messageReaders = HandlerStrategies.withDefaults().messageReaders();

    @Override
    @SuppressWarnings("unchecked")
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取请求的请求方式和contentType
        ServerRequest oldRequest = ServerRequest.create(exchange, this.messageReaders);
        URI uri = oldRequest.uri();
        String methodType = oldRequest.methodName().toUpperCase();
        MediaType mediaType = oldRequest.headers().contentType().orElse(MediaType.APPLICATION_FORM_URLENCODED);
        //根据其进行不同的入参处理
        if (HttpMethod.GET.matches(methodType)) {
            //GET请求
            log.info(uri.toString());
        } else {
            //其它请求类型（包含post,put,patch,delete等）
            if (MediaType.APPLICATION_JSON_UTF8.equalsTypeAndSubtype(mediaType) || MediaType.APPLICATION_JSON.equalsTypeAndSubtype(mediaType)) {
                //只处理JSON数据类型
                //处理请求体
                Mono<String> oldBody = oldRequest.bodyToMono(String.class).flatMap(body -> Mono.just(anewRequestBody(body)));
                BodyInserter bodyInserter = BodyInserters.fromPublisher(oldBody, String.class);
                HttpHeaders headers = new HttpHeaders();
                headers.putAll(exchange.getRequest().getHeaders());
                headers.remove(HttpHeaders.CONTENT_LENGTH);
                //这个类在原java包中不是public的，所以需要copy一份出来用
                CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
                return bodyInserter.insert(outputMessage, new BodyInserterContext())
                        .then(Mono.defer(() -> {
                            ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(exchange.getRequest()) {
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
                                    return outputMessage.getBody();
                                }
                            };
                            return chain.filter(exchange.mutate().request(decorator).build());
                        }));
            }
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -9;
    }

    /**
     * 重组入参结构
     *
     * @param bodyStr 请求参数
     * @return 新入参
     */
    private String anewRequestBody(String bodyStr) {
        //重组入参结构，适配入参框架
        JSONObject body = JSON.parseObject(bodyStr);
        InDTO<JSONObject> in = new InDTO<>();
        in.setBody(body);
        //这里可以添加一些公共信息，登录工号信息，客户端信息等（从session和请求头获取）
        in.setHeader(new JSONObject());
        return JSON.toJSONString(in);
    }

}
