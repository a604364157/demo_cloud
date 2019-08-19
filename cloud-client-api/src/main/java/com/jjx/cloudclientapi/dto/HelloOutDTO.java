package com.jjx.cloudclientapi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author jiangjx
 */
@Data
@ApiModel("hello出参类")
public class HelloOutDTO implements Serializable {

    @ApiModelProperty("返回信息")
    private String msg;

}
