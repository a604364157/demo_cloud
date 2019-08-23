package com.jjx.cloudcommon.msg;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @author jiangjx
 */
public interface LogMessageSource {

    String LOG_OUT_PUT = "log-out-put";

    @Output(LOG_OUT_PUT)
    MessageChannel outPut();

}
