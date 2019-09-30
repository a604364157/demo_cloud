package com.jjx.cloudnacosprovider.controller;


import com.jjx.cloudnacosprovider.entity.Login;
import com.jjx.cloudnacosprovider.service.ILoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jiangjx
 * @since 2019-09-30
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private ILoginService loginService;

    @GetMapping("list")
    public List<Login> list() {
        return loginService.list();
    }

}

