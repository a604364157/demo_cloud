package com.jjx.cloudclient.controller;

import com.jjx.cloudclient.api.dto.HelloInDTO;
import com.jjx.cloudclient.api.dto.HelloOutDTO;
import com.jjx.cloudclient.api.inter.IHelloApi;
import com.jjx.cloudclient.feign.IFileApi;
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
     * <p>
     * 应用也增加了sleuth+zipkin进行链路跟踪
     * 使用localhost:9411/zipkin/来进入服务管理页面
     * <p>
     * 值得一提的是，因为我们在依赖中已经使用了rabbitMQ做消息总线，日志收集
     * 而且是引入的spring-cloud-stream-binder-rabbit依赖
     * 我们再引入spring-cloud-starter-sleuth和spring-cloud-starter-zipkin
     * 应用会自动进行整合。我们配置了rabbitmq后，zipkin直接配置一个路径
     * 就没有任何用处了，因为采集的日志已经通过mq发送了，不再直接送往配置的zipkin地址
     * <p>
     * zipkin新的版本已不再支持自建服务器了，而实直接给编译好的jar文件来用
     * 用是挺好用，就是启动的参数配置就麻烦了。需要在启动命令后给相应的参数用于修改jar包里的默认配置
     * 我们最好写一个启动脚本来启动zipkin服务。
     * <p>
     * 因为我们的客户端已经集成了rabbitmq，所以服务端也必须集成，不然没有办法收到调用链的消息。
     * 所以我们启动的时候需要修改参数，是服务端连上mq
     * java -jar zipkin.jar --zipkin.collector.rabbitmq.addresses=localhost
     * 就这样去启动，就能连上本地的rabbitmq,
     * 当然我没改什么，所以我这样启动需要mq的端口，口令是默认的才行
     * 而且mq的消息队列名（zipkin）也得是默认的才行
     * 这些东西修改了，都要相应的在服务端和客户端修改对应的配置才可以
     */
    @Value("${name}")
    private String name;

    private final IFileApi fileApi;

    public HelloController(IFileApi fileApi) {
        this.fileApi = fileApi;
    }

    @Override
    @ParamLog
    public OutDTO<HelloOutDTO> hello(@RequestBody InDTO<HelloInDTO> in) {
        String name = Optional.ofNullable(in).map(InDTO::getBody).map(HelloInDTO::getName).orElse("");
        HelloOutDTO out = new HelloOutDTO();
        InDTO<String> param = new InDTO<>();
        param.setBody("hello " + name);
        OutDTO<String> dto = fileApi.test(param);
        out.setMsg(dto.getData());
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
