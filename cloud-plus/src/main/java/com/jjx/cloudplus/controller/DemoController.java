package com.jjx.cloudplus.controller;

import com.jjx.cloudplus.service.IDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jiangjx
 */
@RestController
@RequestMapping("demo")
public class DemoController {

    @Autowired
    private IDemoService demoService;

    @GetMapping("test1")
    public String test1() {
        return demoService.test1();
    }

}
