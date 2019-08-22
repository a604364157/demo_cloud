package com.jjx.cloudcommom.aop;

import com.alibaba.fastjson.JSON;
import com.jjx.cloudcommom.annotation.ParamLog;
import com.jjx.cloudcommom.constant.Constants;
import com.jjx.cloudcommom.msg.MessageProducer;
import com.jjx.cloudcommom.utils.DateUtil;
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
