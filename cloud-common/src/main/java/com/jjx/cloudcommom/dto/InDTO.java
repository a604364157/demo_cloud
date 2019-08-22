package com.jjx.cloudcommom.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
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

    public InDTO() {
        this.headerBody = new HeaderBody<>();
    }

    private void createHeaderBody() {
        if (this.headerBody == null) {
            this.headerBody = new HeaderBody<>();
        }
    }

    private HeaderBody<T> getHeaderBody() {
        return this.headerBody;
    }

    public void setHeaderBody(HeaderBody<T> headerBody) {
        this.headerBody = headerBody;
    }

    public T getBody() {
        createHeaderBody();
        return this.headerBody.getBody();
    }

    public void setBody(T t) {
        createHeaderBody();
        this.headerBody.setBody(t);
        this.setHeaderBody(headerBody);
    }

    public void setHeader(JSONObject header) {
        createHeaderBody();
        JSONObject newHeader = header;
        if (this.getHeader() == null) {
            newHeader = new JSONObject();
            if (header != null) {
                newHeader.putAll(header);
            }
        }
        this.getHeaderBody().setHeader(newHeader);
    }

    public JSONObject getHeader() {
        createHeaderBody();
        return this.headerBody.getHeader();
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
