package com.jjx.cloudlog.msg;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author jiangjx
 */
public interface LogMessageSource {

    String LOG_IN_PUT = "log-in-put";

    /**
     * 日志消息的消费管道
     *
     * @return channel
     */
    @Input(LOG_IN_PUT)
    SubscribableChannel inPut();

}
