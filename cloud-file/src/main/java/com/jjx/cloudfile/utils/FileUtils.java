package com.jjx.cloudfile.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 原则上文件应该存文件主机上
 * 实现一个FTP或者sftp工具类
 * 进行文件的上传下载
 * 我这测试，而且我个人没有云服务器做文件主机
 *
 * 可以根据实际需求和环境修改
 *
 * @author jiangjx
 */
@Slf4j
@SuppressWarnings("unused")
public class FileUtils {

    /**
     * 文件上传工具类
     *
     * @param input    文件输入流
     * @param path     存储路径
     * @param fileName 文件名
     */
    public static void uploadFile(InputStream input, String path, String fileName) {
        if (StringUtils.isEmpty(input)) {
            throw new RuntimeException("上传文件为空");
        }
        if (StringUtils.isEmpty(path)) {
            throw new RuntimeException("上传路径配置错误");
        }
        if (StringUtils.isEmpty(fileName)) {
            throw new RuntimeException("保存文件名错误");
        }
        File dir = new File(path);
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                log.debug("生成目录成功");
            }
        }
        try (BufferedInputStream bis = new BufferedInputStream(input);
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path + File.separator + fileName))) {
            int len;
            byte[] bytes = new byte[1024];
            while ((len = bis.read(bytes)) != -1) {
                bos.write(bytes, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("文件读写失败");
        }
    }

    public static InputStream byte2Input(byte[] buf) {
        return new ByteArrayInputStream(buf);
    }

    public static byte[] input2byte(InputStream inStream) {
        int one = 1024;
        byte[] buff = new byte[one];
        int rc;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            while ((rc = inStream.read(buff, 0, one)) > 0) {
                bos.write(buff, 0, rc);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            return new byte[0];
        }
    }

    public static InputStream downloadFile(String fileName, String path) {
        try {
            return new FileInputStream(new File(path + File.separator + fileName));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("下载的文件不存在");
        }
    }
}
