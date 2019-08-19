package com.jjx.cloudclient.controller;

import com.jjx.cloudclientapi.dto.HelloInDTO;
import com.jjx.cloudclientapi.dto.HelloOutDTO;
import com.jjx.cloudclientapi.inter.IHelloApi;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jiangjx
 */
@RestController
public class HelloController implements IHelloApi {

    @Override
    public HelloOutDTO hello(@RequestBody HelloInDTO in) {
        HelloOutDTO out = new HelloOutDTO();
        out.setMsg("hello " + in.getName());
        return out;
    }
}
