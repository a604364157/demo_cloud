package com.jjx.cloudplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author jiangjx
 */
@SpringBootApplication
@MapperScan("com.jjx")
public class CloudPlusApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudPlusApplication.class, args);
    }

}
