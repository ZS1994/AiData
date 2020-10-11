package com.zs.aidata.test.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 张顺
 * @since 2020/10/11
 */
@RestController
public class HelloController {


    @RequestMapping("/hello")
    public String hello() {
        return "你好";
    }


}
