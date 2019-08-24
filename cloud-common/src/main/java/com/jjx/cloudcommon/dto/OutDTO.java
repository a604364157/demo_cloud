package com.jjx.cloudcommon.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Optional;

/**
 * @author jiangjx
 */
@SuppressWarnings("unused")
public class OutDTO<T> implements Serializable {

    private static final long serialVersionUID = 4602714741495982191L;
    @JSONField(name = "ROOT")
    @JsonProperty("ROOT")
    private HeaderBody<OutBody<T>> headerBody;

    public HeaderBody<OutBody<T>> getHeaderBody() {
        return headerBody;
    }

    public void setHeaderBody(HeaderBody<OutBody<T>> headerBody) {
        this.headerBody = headerBody;
    }

    private void createHeaderBody() {
        this.headerBody = this.headerBody != null ? this.headerBody : new HeaderBody<>();
    }

    public static <T> OutDTO<T> build(T t) {
        OutDTO<T> out = new OutDTO<>();
        out.setData(t);
        return out;
    }

    public static <T> OutDTO<T> build(OutBody<T> body) {
        OutDTO<T> out = new OutDTO<>();
        out.setBody(body);
        return out;
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
        this.headerBody.setHeader(header);
    }

    @JsonIgnore
    @JSONField(serialize = false)
    public OutBody<T> getBody() {
        createHeaderBody();
        return this.headerBody.getBody();
    }

    @JsonIgnore
    @JSONField(serialize = false)
    public void setBody(OutBody<T> body) {
        createHeaderBody();
        this.headerBody.setBody(body);
    }

    @JsonIgnore
    @JSONField(serialize = false)
    public T getData() {
        createHeaderBody();
        return Optional.of(this).map(OutDTO::getBody).map(OutBody::getOutData).orElse(null);
    }

    @JsonIgnore
    @JSONField(serialize = false)
    public void setData(T t) {
        createHeaderBody();
        if (this.getBody() == null) {
            this.setBody(new OutBody<>());
        }
        this.getBody().setOutData(t);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
