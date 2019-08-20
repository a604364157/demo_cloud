package com.jjx.cloudclientapi.inter;

import com.jjx.cloudclientapi.common.Constant;
import com.jjx.cloudclientapi.dto.HelloInDTO;
import com.jjx.cloudclientapi.dto.HelloOutDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author jiangjx
 */
@Api(tags = "测试接口")
@RequestMapping("client")
@FeignClient(name = Constant.CLIENT_SERVIEC_ID)
public interface IHelloApi {

    /**
     * 测试接口
     * @param in 入参
     * @return 出参
     */
    @PostMapping("hello")
    @ApiOperation("测试接口")
    HelloOutDTO hello(HelloInDTO in);

}
