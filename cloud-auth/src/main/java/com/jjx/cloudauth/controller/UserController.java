package com.jjx.cloudauth.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author jiangjx
 */
@RestController
public class UserController {

    @RequestMapping("user")
    public Principal user(Authentication authentication) {
        return authentication;
    }

}
