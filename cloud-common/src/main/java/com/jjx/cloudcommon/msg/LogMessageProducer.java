package com.jjx.cloudcommon.msg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

/**
 * @author jiangjx
 */
@Slf4j
@EnableBinding(LogMessageSource.class)
public class LogMessageProducer {

    @Autowired(required = false)
    @Output(LogMessageSource.LOG_OUT_PUT)
    private MessageChannel channel;

    public void sendMsg(String msg) {
        channel.send(MessageBuilder.withPayload(msg).build());
        log.info("消息发送成功");
    }
}
