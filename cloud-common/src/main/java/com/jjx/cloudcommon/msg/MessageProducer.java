package com.jjx.cloudcommon.msg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

/**
 * @author jiangjx
 */
@EnableBinding(MessageSource.class)
public class MessageProducer {

    @Autowired(required = false)
    @Output(MessageSource.LOG_OUT_PUT)
    private MessageChannel channel;

    public void sendMsg(String msg) {
        channel.send(MessageBuilder.withPayload(msg).build());
    }
}
