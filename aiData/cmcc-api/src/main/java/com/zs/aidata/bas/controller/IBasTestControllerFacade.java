package com.zs.aidata.bas.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author 张顺
 * @since 2020/10/11
 */
@Api("IBasTestControllerFacade")
@RestController
@RequestMapping(value = "cmcc/basTest", headers = "Accept=application/json", produces = "application/json;charset=UTF-8")
public interface IBasTestControllerFacade {

    @GetMapping("hello")
    @ApiOperation("测试请求是否能通")
    String hello();

}
