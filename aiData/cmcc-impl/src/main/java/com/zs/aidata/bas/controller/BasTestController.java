package com.zs.aidata.bas.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Named;

/**
 * @author 张顺
 * @since 2020/10/11
 */
@Named
public class BasTestController implements IBasTestControllerFacade {

    @Override
    public String hello() {
        return "测试成功，该接口是通的";
    }
}
