package com.jjx.cloudclient.msg;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;

/**
 * @author jiangjx
 */
@EnableBinding(ConsumerMessageSource.class)
public class MessageConsumer {

    @StreamListener(ConsumerMessageSource.LOG_IN_PUT)
    public void messageInPut(Message<String> message) {
        System.out.println("消息接收成功："+ message.getPayload());
    }

}
