package com.jjx.cloudclient.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author jiangjx
 */
@Data
@ApiModel("hello入参类")
public class HelloInDTO implements Serializable {

    @ApiModelProperty("名称")
    private String name;

}
