package com.jjx.cloudlog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author jiangjx
 */
@SpringBootApplication(scanBasePackages = "com.jjx.cloudcommon")
public class CloudLogApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudLogApplication.class, args);
    }

}
