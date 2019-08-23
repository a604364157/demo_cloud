package com.jjx.cloudcommon.aop;

import com.alibaba.fastjson.JSON;
import com.jjx.cloudcommon.annotation.ParamLog;
import com.jjx.cloudcommon.constant.Constants;
import com.jjx.cloudcommon.msg.LogMessageProducer;
import com.jjx.cloudcommon.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * 这里说明一下
 * common包集成了redis，rabbitMQ（写说明的时候，之后可能还会添加其他）
 * 所以，其他应用引用到common，并且开启了common包扫描后，引用需要添加
 * 两者的相关配置，就我的设计而言，我开启的扫描后，肯定是会用到这两者的，
 * 所以我会配置。本身的配置并没有写到common包内，避免和引用产生冲突
 * 原则上，如果不配置，在spring cloud环境下，会使用默认配置
 * 但是如果你的redis，rabbit没安装，没启动，地址和密码什么的不是默认的
 * 就会一直报错，可能导致服务启动不了，所以要注意一下
 * <p>
 * 这样虽然会对应用产生一些强耦的依赖注入，但是也是在设计上实现的公共功能
 *
 * <p>
 * 这个AOP增强是用作日志记录的
 * Around("execution(* com.jjx.*.controller..*.*(..))")
 * 是切入到所有的controller包下的函数。
 * <p>
 * 这里的能力是提供在公共包下和应用本身的目录不匹配
 * 要让功能生效必须在启动类的SpringBootApplication(scanBasePackages = "com.jjx")
 * 加上路径，让spring能扫描到这个包
 * <p>
 * 实际情况下，应用引用到common包，但是不准备启动这写功能，就不要改变扫描包
 * 这些功能就无效
 * <p>
 * 本功能还启用了spring-cloud-stream-binder-rabbit 整合stream和rabbitMQ
 * 产生的日志也是通过消息发送，然后给消费者
 * 如果要发消息，需要函数打上ParamLog注解
 * <p>
 * 因为是公共包，日志消息的的生产者配置在本包写死了。
 * 生产者的管道channel名称只能是（log-out-put）
 * binder名称可以自行定义，注意生产者与消费者需要对应
 * <p>
 * 我这里实现的生产者是cloud-client 消费者是cloud-log
 * 消费者获取到消息后存入redis中，实际生产的化最好存入
 * mysql之类的关系数据库或者Hbase。便于数据的持久化
 * 虽然redis也能持久化，但它本身就不是干这个的
 *
 * @author jiangjx
 */
@Slf4j
@Aspect
@Configuration
public class LogAspect {

    @Autowired
    private LogMessageProducer producer;

    @Around("execution(* com.jjx.*.controller..*.*(..))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        long t1 = System.currentTimeMillis();
        Object obj = joinPoint.proceed();
        long t2 = System.currentTimeMillis();
        Object[] objects = joinPoint.getArgs();
        Class<?>[] argTypes = new Class[joinPoint.getArgs().length];
        for (int i = 0; i < objects.length; i++) {
            argTypes[i] = objects[i].getClass();
        }
        Signature s = joinPoint.getSignature();
        String now = DateUtil.getDateStr(new Date(), DateUtil.DATE_MODEL_2);
        String logStr = now + "：" + s.toString() + "运行耗时：" + (t2 - t1) + " 入参：" + JSON.toJSONString(objects);
        log.info(logStr);
        Method method;
        try {
            method = joinPoint.getTarget().getClass().getMethod(s.getName(), argTypes);
            ParamLog paramLog = method.getAnnotation(ParamLog.class);
            if (paramLog != null) {
                if (Constants.Y.equals(paramLog.value())) {
                    producer.sendMsg(logStr);
                }
            }
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        return obj;
    }

}
