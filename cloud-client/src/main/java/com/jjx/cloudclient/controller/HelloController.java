package com.jjx.cloudclient.controller;

import com.jjx.cloudclientapi.dto.HelloInDTO;
import com.jjx.cloudclientapi.dto.HelloOutDTO;
import com.jjx.cloudclientapi.inter.IHelloApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jiangjx
 */
@Slf4j
@RestController
public class HelloController implements IHelloApi {

    @Override
    public HelloOutDTO hello(@RequestBody HelloInDTO in) {
        HelloOutDTO out = new HelloOutDTO();
        out.setMsg("hello "+ in.getName());
        return out;
    }

    private ConcurrentHashMap<String, AtomicInteger> map = new ConcurrentHashMap<>();

    @Override
    public int exCount(@RequestParam("key")String key, @RequestParam(name ="count", defaultValue ="3")int count) {
        AtomicInteger num = map.computeIfAbsent(key, s -> new AtomicInteger());
        int i = num.incrementAndGet();
        log.warn("Retry count: "+i);
        if (i < count) {
            throw new RuntimeException("temporarily broken");
        }
        return i;
    }
}
