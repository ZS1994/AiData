package com.zs.aidata.core.sys.login.controller;

import com.zs.aidata.core.sys.login.service.ILoginService;
import com.zs.aidata.core.tools.AiDataApplicationException;
import com.zs.aidata.core.sys.login.vo.AuthVO;
import com.zs.aidata.core.sys.login.vo.LoginInputVO;
import com.zs.aidata.core.tools.BaseCoreService;
import com.zs.aidata.core.tools.Constans;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;


/**
 * 登录认证
 *
 * @author 张顺
 * @since 2021/2/22
 */
@Slf4j
@Api(tags = {"登录认证"})
@RestController
@RequestMapping(value = "core/loginController", headers = "Accept=application/json", produces = "application/json;charset=UTF-8")
public class LoginController extends BaseCoreService {

    @Inject
    private ILoginService iLoginService;

    /**
     * @param inputVO
     * @return
     * @throws AiDataApplicationException
     * @RequiresRoles()是shiro提供的，用于标记当前类获取当前方法访问时，必须需要哪些角色 value 为一个或多个角色
     * logical and 或者是 or 。 默认是 AND，表示当前用户必须同时拥有多个角色
     */
    @PostMapping("login")
    @ApiOperation(value = "执行登录验证", notes = "执行登录验证")
    public AuthVO login(LoginInputVO inputVO) throws AiDataApplicationException {
        return iLoginService.login(inputVO);
    }

    @PostMapping("logout")
    @ApiOperation(value = "登出", notes = "登出")
    public AuthVO logout() throws AiDataApplicationException {
        Subject subject = SecurityUtils.getSubject();
        // 登出当前账号，清空当前账号的缓存，否则无法重新登录
        subject.logout();
        return new AuthVO("登出成功");
    }

    @RequiresPermissions("test1")
    @PostMapping("test1")
    @ApiOperation(value = "测试1", notes = "测试1")
    public AuthVO test1() {
        log.info("----------test1测试成功----------");
        return new AuthVO("test1测试成功");
    }


    @RequiresPermissions("test2")
    @PostMapping("test2")
    @ApiOperation(value = "测试2", notes = "测试2")
    public AuthVO test2() {
        log.info("----------test2测试成功----------");
        return new AuthVO("test2测试成功");
    }

}
