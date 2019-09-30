package com.jjx.cloudplus.service.impl;

import com.jjx.cloudplus.service.IDemoService;
import com.jjx.cloudplus.utils.SpringUtil;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author jiangjx
 */
@Service("demoService")
public class DemoServiceImpl implements IDemoService {

    @Override
    public String test1() {
        Future<String> future = SpringUtil.getBean(IDemoService.class).test2();
        try {
            String ret = future.get();
            System.out.println(ret);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("test1执行完成");
        return null;
    }

    @Async
    @Override
    public Future<String> test2() {
        System.out.println("test2执行完成");
        return AsyncResult.forValue("test2的异步返回参数");
    }
}
