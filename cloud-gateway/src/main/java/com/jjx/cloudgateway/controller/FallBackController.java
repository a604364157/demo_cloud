package com.jjx.cloudgateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author jiangjx
 */
@RestController
public class FallBackController {

    @RequestMapping("error/fallback")
    public Mono<String> fallback() {
        return Mono.just("service error, jump fallback");
    }

}
