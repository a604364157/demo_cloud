package com.jjx.cloudcommon.dto;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author jiangjx
 */
@SuppressWarnings("unused")
public class HeaderBody<T> implements Serializable {

    private static final long serialVersionUID = 6326761814046206916L;
    @JSONField(name = "BODY")
    @JsonProperty("BODY")
    private T body;
    @JSONField(name = "HEADER")
    @JsonProperty("HEADER")
    private JSONObject header;

    public T getBody() {
        return this.body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public JSONObject getHeader() {
        return this.header;
    }

    public void setHeader(JSONObject header) {
        this.header = header;
    }
}
