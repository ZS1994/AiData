package com.zs.aidata.bas.controller;

import com.zs.aidata.bas.vo.HelloVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Named;

/**
 * @author 张顺
 * @since 2020/10/11
 */
@Api(tags = {"测试接口服务"})
@RestController
@RequestMapping(value = "cmcc/basTest", headers = "Accept=application/json", produces = "application/json;charset=UTF-8")
public class BasTestController {

    @GetMapping("hello")
    @ApiOperation(value = "测试请求是否能通", notes = "备注说明。。。。省略")
    public HelloVO hello() {
        HelloVO helloVO = new HelloVO();
        helloVO.setMessage("测试成功，该接口是通的");
        return helloVO;
    }
}