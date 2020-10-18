package com.zs.aidata.dccp.controller;

import com.zs.aidata.dccp.service.DccpService;
import com.zs.aidata.dccp.vo.DccpOutVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * @author 张顺
 * @since 2020/10/18
 */
@Api(tags = {"和彩云"})
@RestController
@RequestMapping(value = "cmcc/dccp", headers = "Accept=application/json", produces = "application/json;charset=UTF-8")
public class DccpController {

    @Inject
    private DccpService dccpService;


    @GetMapping("sendSmsCode")
    @ApiOperation(value = "发送验证码", notes = "测试用")
    public DccpOutVO sendSmsCode() {
        return dccpService.sendSmsCode();
    }


}
