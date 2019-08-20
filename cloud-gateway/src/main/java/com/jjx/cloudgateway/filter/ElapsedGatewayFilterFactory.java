package com.jjx.cloudgateway.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

/**
 * 过滤工厂
 * 过滤工厂使用需要依赖配置文件
 * 命名 xxxGatewayFilterFactory
 * 配置文件需要添加xxx过滤器
 * 例如
 *       default-filters:
 *         - Elapsed=true
 *
 * @author jiangjx
 */
@Slf4j
public class ElapsedGatewayFilterFactory extends AbstractGatewayFilterFactory<ElapsedGatewayFilterFactory.Config> {

    private static final String SERVER_BEGIN_TIME = "serverBeginTime";
    private static final String KEY = "type";

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            exchange.getAttributes().put(SERVER_BEGIN_TIME, System.currentTimeMillis());
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                        Long startTime = exchange.getAttribute(SERVER_BEGIN_TIME);
                        if (startTime != null) {
                            StringBuilder sb = new StringBuilder(exchange.getRequest().getURI().getRawPath())
                                    .append(": ")
                                    .append(System.currentTimeMillis() - startTime)
                                    .append("ms");
                            //这里的实现和自定义过滤器是一样的
                            //下面是根据配置参数打印入参
                            if (config.isType()) {
                                sb.append(" params:").append(exchange.getRequest().getQueryParams());
                            }
                            log.info(sb.toString());
                        }
                    })
            );
        };
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.singletonList(KEY);
    }

    public ElapsedGatewayFilterFactory() {
        //这里必须传出，否则泛型无法识别报错
        super(Config.class);
    }

    /**
     * 用于接收参数
     * 参数名必须和shortcutFieldOrder内保持一致
     */
    @Getter
    @Setter
    public static class Config {
        private boolean type;
    }

}
