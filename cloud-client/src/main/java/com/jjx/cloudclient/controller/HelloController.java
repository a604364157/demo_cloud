package com.jjx.cloudclient.controller;

import com.jjx.cloudclientapi.dto.HelloInDTO;
import com.jjx.cloudclientapi.dto.HelloOutDTO;
import com.jjx.cloudclientapi.inter.IHelloApi;
import com.jjx.cloudcommon.annotation.ParamLog;
import com.jjx.cloudcommon.dto.InDTO;
import com.jjx.cloudcommon.dto.OutDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jiangjx
 */
@Slf4j
@RefreshScope
@RestController
public class HelloController implements IHelloApi {

    /**
     * 这个值是启用的配置中心的配置
     * 本系统开启了配置刷新功能
     * 配置刷新需要配置文件和@RefreshScope注解
     * 这样也不会自动刷新配置
     * 需要调用post(host:port/actuator/refresh)服务来刷新配置
     * <p>
     * 要达到自动刷新，需要配置github的webhook功能来实现
     * 这个功能就是当收到某个事件后触发一些事件
     * 可以通过这个来自动调用刷新服务
     * <p>
     * 因为我这里使用的github且是公开的项目，就不配置了
     * <p>
     * <p>
     * 当然以上的方式也只适配于少量客户端，如果一个配置文件
     * 被很多客户端引用，我们一个一个去调用刷新是很麻烦的
     * 解决这个问题需要用到消息总线（spring cloud bus）
     * 在配置服务端和客户端都需要引入bus依赖和消息中间件，我选的rabbitMQ
     * 需要先安装erLang和RabbitMQ，默认口令是guest/guest
     * 和redis同样，spring cloud会有默认配置，不配置rabbitMQ，也会有默认的配置
     * 需要调用post(host:port/actuator/bus-refresh)服务来刷新配置
     * <p>
     * 使用bus后会导致配置在所有的客户端都同步，如果我们只想刷新部分
     * 可以使用（/actuator/bus-refresh/{service_id:port}）来指向刷新
     * <p>
     * 可以使用get（/actuator/httptrace）【前提配置 bus.trace.enabled=true】
     * 来看消息总线的日志
     *
     * 应用也增加了zipkin进行链路跟踪
     * 使用localhost:9411/zipkin/来进入服务管理页面
     */
    @Value("${name}")
    private String name;

    @Override
    @ParamLog
    public OutDTO<HelloOutDTO> hello(@RequestBody InDTO<HelloInDTO> in) {
        String name = Optional.ofNullable(in).map(InDTO::getBody).map(HelloInDTO::getName).orElse("");
        HelloOutDTO out = new HelloOutDTO();
        out.setMsg("hello " + name);
        return OutDTO.build(out);
    }

    @Override
    public String hello() {
        return "hello " + name;
    }

    private ConcurrentHashMap<String, AtomicInteger> map = new ConcurrentHashMap<>();

    @Override
    public int exCount(@RequestParam("key") String key, @RequestParam(name = "count", defaultValue = "3") int count) {
        AtomicInteger num = map.computeIfAbsent(key, s -> new AtomicInteger());
        int i = num.incrementAndGet();
        log.warn("Retry count: " + i);
        if (i < count) {
            throw new RuntimeException("temporarily broken");
        }
        return i;
    }

}
