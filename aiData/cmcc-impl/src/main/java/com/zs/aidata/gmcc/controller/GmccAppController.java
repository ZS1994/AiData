package com.zs.aidata.gmcc.controller;

import com.zs.aidata.core.BaseCoreService;
import com.zs.aidata.dccp.vo.DccpOutVO;
import com.zs.aidata.dccp.vo.DccpQueryVO;
import com.zs.aidata.gmcc.service.IGmccAppService;
import com.zs.aidata.gmcc.vo.GmccLoginVO;
import com.zs.aidata.gmcc.vo.GmccOutVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * @author 张顺
 * @since 2020/10/18
 */
@Api(tags = {"广东移动"})
@RestController
@RequestMapping(value = "cmcc/gmcc", headers = "Accept=application/json", produces = "application/json;charset=UTF-8")
public class GmccAppController extends BaseCoreService {

    @Inject
    private IGmccAppService iGmccAppService;


    @PostMapping("sendSmsCode")
    @ApiOperation(value = "发送验证码", notes = "发送验证码")
    public GmccOutVO sendSmsCode(GmccLoginVO queryVO) throws Exception {
        checkNotEmpty(queryVO, "phone");
        return iGmccAppService.sendSmsCode(queryVO.getPhone());
    }


    @PostMapping("login")
    @ApiOperation(value = "登录", notes = "登录")
    public GmccOutVO login(GmccLoginVO inputVo) throws Exception {
        checkNotEmpty(inputVo, "phone", "smsCode");
        return iGmccAppService.login(inputVo.getPhone(), inputVo.getSmsCode());
    }


}
