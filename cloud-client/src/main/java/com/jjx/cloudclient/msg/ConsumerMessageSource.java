package com.jjx.cloudclient.msg;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author jiangjx
 */
public interface ConsumerMessageSource {

    String LOG_IN_PUT = "log-in-put";

    @Input(LOG_IN_PUT)
    SubscribableChannel inPut();

}
