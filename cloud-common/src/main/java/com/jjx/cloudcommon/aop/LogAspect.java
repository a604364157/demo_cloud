package com.jjx.cloudcommon.aop;

import com.alibaba.fastjson.JSON;
import com.jjx.cloudcommon.annotation.ParamLog;
import com.jjx.cloudcommon.constant.Constants;
import com.jjx.cloudcommon.msg.MessageProducer;
import com.jjx.cloudcommon.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * 这里说明一下
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
@EnableScheduling
public class LogAspect {

    @Autowired
    private MessageProducer producer;

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
