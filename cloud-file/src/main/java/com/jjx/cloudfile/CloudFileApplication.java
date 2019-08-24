package com.jjx.cloudfile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author jiangjx
 */
@EnableSwagger2
@EnableFeignClients
@SpringBootApplication
public class CloudFileApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudFileApplication.class, args);
    }

}
