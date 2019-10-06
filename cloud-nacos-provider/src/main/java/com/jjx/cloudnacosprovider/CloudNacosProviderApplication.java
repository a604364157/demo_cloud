package com.jjx.cloudnacosprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author jiangjx
 */
@EnableDiscoveryClient
@SpringBootApplication
public class CloudNacosProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudNacosProviderApplication.class, args);
    }

}
