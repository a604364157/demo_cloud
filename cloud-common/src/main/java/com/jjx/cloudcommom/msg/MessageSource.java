package com.jjx.cloudcommom.msg;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @author jiangjx
 */
public interface MessageSource {

    String LOG_OUT_PUT = "log-out-put";

    @Output(LOG_OUT_PUT)
    MessageChannel outPut();

}
