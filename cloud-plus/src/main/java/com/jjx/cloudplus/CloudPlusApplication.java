package com.jjx.cloudplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author jiangjx
 */
@EnableAsync
@SpringBootApplication
@MapperScan("com.jjx")
public class CloudPlusApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudPlusApplication.class, args);
    }

}
