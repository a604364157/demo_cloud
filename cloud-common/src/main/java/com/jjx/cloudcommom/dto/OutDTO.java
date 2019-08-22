package com.jjx.cloudcommom.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author jiangjx
 */
@SuppressWarnings("unused")
public class OutDTO<T> implements Serializable {

    private static final long serialVersionUID = 4602714741495982191L;
    @JSONField(name = "ROOT")
    @JsonProperty("ROOT")
    public HeaderBody<OutBody<T>> headerBody;

    public OutDTO() {
        this.headerBody = this.createHeaderBody();
    }

    public OutDTO(OutBody<T> body) {
        this.headerBody = this.createHeaderBody();
        this.setBody(body);
    }

    public void setBodyOutData(T t) {
        this.setBody(new OutBody<>(t));
    }

    public void setBody(OutBody<T> body) {
        this.headerBody = this.createHeaderBody();
        this.headerBody.setBody(body);
    }

    public static <T> OutDTO<T> build(T t) {
        OutDTO<T> out = new OutDTO<>();
        out.setBodyOutData(t);
        return out;
    }

    private HeaderBody<OutBody<T>> createHeaderBody() {
        return this.headerBody != null ? this.headerBody : new HeaderBody<>();
    }

    public HeaderBody<OutBody<T>> getHeaderBody() {
        return headerBody;
    }

    public void setHeaderBody(HeaderBody<OutBody<T>> headerBody) {
        this.headerBody = headerBody;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
