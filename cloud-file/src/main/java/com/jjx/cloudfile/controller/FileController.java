package com.jjx.cloudfile.controller;

import com.jjx.cloudcommon.dto.InDTO;
import com.jjx.cloudcommon.dto.OutBody;
import com.jjx.cloudcommon.dto.OutDTO;
import com.jjx.cloudfile.utils.FileUtils;
import com.jjx.cloudfile.api.common.Constant;
import com.jjx.cloudfile.api.inter.IFileApi;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @author jiangjx
 */
@RestController(Constant.SERVICE_PREFIX)
public class FileController implements IFileApi {

    @Override
    public OutDTO<String> test(@RequestBody InDTO<String> in) {
        return OutDTO.build(in.getBody());
    }

    @Override
    public OutDTO<String> uploadFile(@RequestParam(required = false) String param, @RequestParam MultipartFile file) {
        //原则上业务应该写的service，我这目前只是测试
        OutBody<String> outBody = new OutBody<>();
        if (file != null) {
            String fileName = file.getOriginalFilename();
            InputStream fileData;
            try {
                fileData = file.getInputStream();
            } catch (IOException e) {
                throw new RuntimeException("文件流异常");
            }
            String newFileName = UUID.randomUUID() + "_" + fileName;
            FileUtils.uploadFile(fileData, Constant.FILE_PATH, newFileName);
            outBody.setOutData(newFileName);
        }
        outBody.setStatus("0");
        outBody.setMsg("文件上传成功");
        return OutDTO.build(outBody);
    }

    @ApiOperation("文件下载")
    @GetMapping("download")
    public void downloadFile(@RequestParam String fileName, HttpServletResponse response) {
        if (StringUtils.isBlank(fileName)) {
            throw new RuntimeException("文件名称不能为空");
        }
        try (ServletOutputStream servletOutputStream = response.getOutputStream()) {
            InputStream is = FileUtils.downloadFile(fileName, Constant.FILE_PATH);
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            String encodeName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
            response.setHeader("Content-Disposition", "attachment;filename=" + encodeName);
            IOUtils.copy(is, servletOutputStream);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
