package com.jjx.cloudfile.api.inter;

import com.jjx.cloudfile.api.common.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 文件服务
 *
 * @author jiangjx
 */
@Api(tags = "文件服务")
@RequestMapping(Constant.SERVICE_PREFIX)
public interface IDownFileApi {

    /**
     * 文件下载
     * feign的文件下载不太一样，实现有点区别
     * 这个服务和真实的文件下载的调用路径需要保持一致
     * <p>
     * 因为我的设计是API包定义接口，同时实现feign的定义
     * 但是这个文件下载，feign的函数定义和实现的函数定义
     * 结构不一致，所以只能分开了。
     * <p>
     * 其实还有一种解决方案，就是设计一个兼容的出参类型
     * 这个呢反而会在feign转码上更麻烦，所以就不弄了
     * <p>
     * 现在这样只是文件下载服务和服务API的定义原则
     *
     * @param fileName 文件别名
     * @return 响应
     */
    @ApiOperation("文件下载")
    @GetMapping("download")
    ResponseEntity<byte[]> downloadFile(@RequestParam String fileName);

}
