package com.jjx.cloudnacosprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author jiangjx
 */
@EnableSwagger2
@EnableDiscoveryClient
@SpringBootApplication
public class CloudNacosProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudNacosProviderApplication.class, args);
    }

}
