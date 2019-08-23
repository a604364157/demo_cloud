package com.jjx.cloudlog.msg;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;

/**
 * @author jiangjx
 */
@EnableBinding(LogMessageSource.class)
public class LogMessageConsumer {

    @StreamListener(LogMessageSource.LOG_IN_PUT)
    public void messageInPut(Message<String> message) {
        System.out.println("消息接收成功："+ message.getPayload());
    }

}
