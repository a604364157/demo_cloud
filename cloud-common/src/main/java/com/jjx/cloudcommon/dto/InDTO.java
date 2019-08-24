package com.jjx.cloudcommon.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author jiangjx
 */
@SuppressWarnings("unused")
public class InDTO<T> implements Serializable {

    private static final long serialVersionUID = -1189223960511421729L;
    @JSONField(name = "ROOT")
    @JsonProperty("ROOT")
    private HeaderBody<T> headerBody;

    public HeaderBody<T> getHeaderBody() {
        return headerBody;
    }

    public void setHeaderBody(HeaderBody<T> headerBody) {
        this.headerBody = headerBody;
    }

    public static <T> InDTO<T> body(T t) {
        InDTO<T> in = new InDTO<>();
        in.setBody(t);
        return in;
    }

    public static <T> InDTO<T> headerBody(Header header, T t) {
        InDTO<T> in = new InDTO<>();
        in.setHeader(header);
        in.setBody(t);
        return in;
    }

    private void createHeaderBody() {
        if (this.headerBody == null) {
            this.headerBody = new HeaderBody<>();
        }
    }

    @JsonIgnore
    @JSONField(serialize = false)
    public T getBody() {
        createHeaderBody();
        return this.headerBody.getBody();
    }

    @JsonIgnore
    @JSONField(serialize = false)
    public void setBody(T t) {
        createHeaderBody();
        this.headerBody.setBody(t);
    }

    @JsonIgnore
    @JSONField(serialize = false)
    public Header getHeader() {
        createHeaderBody();
        return this.headerBody.getHeader();
    }

    @JsonIgnore
    @JSONField(serialize = false)
    public void setHeader(Header header) {
        createHeaderBody();
        if (header == null) {
            return;
        }
        if (this.getHeader() != null) {
            header.putAll(this.getHeader());
        }
        this.headerBody.setHeader(header);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
