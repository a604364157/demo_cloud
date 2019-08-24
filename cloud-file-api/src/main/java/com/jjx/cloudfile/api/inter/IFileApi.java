package com.jjx.cloudfile.api.inter;

import com.jjx.cloudcommon.dto.InDTO;
import com.jjx.cloudcommon.dto.OutDTO;
import com.jjx.cloudfile.api.common.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件服务
 *
 * @author jiangjx
 */
@Api(tags = "文件服务")
@RequestMapping(Constant.SERVICE_PREFIX)
public interface IFileApi {

    /**
     * 测试接口
     *
     * @param in 入参
     * @return 出参
     */
    @PostMapping("test")
    @ApiOperation("测试接口")
    OutDTO<String> test(InDTO<String> in);

    /**
     * 文件上传
     *
     * @param param 业务参数
     * @param file  文件
     * @return 生成的文件别名
     */
    @PostMapping("upload")
    @ApiOperation("文件上传")
    OutDTO<String> uploadFile(@RequestParam(required = false) String param, @RequestParam MultipartFile file);

}
