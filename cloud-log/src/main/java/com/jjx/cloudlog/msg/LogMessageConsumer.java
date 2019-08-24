package com.jjx.cloudlog.msg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;

/**
 * @author jiangjx
 */
@Slf4j
@EnableBinding(LogMessageSource.class)
public class LogMessageConsumer {

    @StreamListener(LogMessageSource.LOG_IN_PUT)
    public void messageInPut(Message<String> message) {
        log.info("消息接收成功："+ message.getPayload());
    }

}
