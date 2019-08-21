package com.jjx.cloudclientapi.inter;

import com.jjx.cloudclientapi.common.Constant;
import com.jjx.cloudclientapi.dto.HelloInDTO;
import com.jjx.cloudclientapi.dto.HelloOutDTO;
import com.jjx.cloudcommom.dto.InDTO;
import com.jjx.cloudcommom.dto.OutDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * API的作用是定义的接口在API包内
 * 直接定义为feign接口，且使用的DTO也在API包内
 * 这样不同服务间调用函数时，只需要引入相应的API包
 *
 * @author jiangjx
 */
@Api(tags = "测试接口")
@RequestMapping("client")
@FeignClient(name = Constant.CLIENT_SERVICE_ID)
public interface IHelloApi {

    /**
     * 测试接口
     *
     * @param in 入参
     * @return 出参
     */
    @PostMapping("hello")
    @ApiOperation("测试接口")
    OutDTO<HelloOutDTO> hello(@RequestBody InDTO<HelloInDTO> in);

    /**
     * 测试接口
     *
     * @return 出参
     */
    @GetMapping("hello")
    @ApiOperation("测试接口")
    String hello();

    /**
     * 测试接口
     *
     * @param key   key
     * @param count 次数
     * @return 次数
     */
    @GetMapping("exCount")
    @ApiOperation("测试接口")
    int exCount(@RequestParam("key") String key, @RequestParam(name = "count", defaultValue = "3") int count);
}
