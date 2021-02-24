package com.zs.aidata.sys.controller;

import com.zs.aidata.core.AiDataApplication;
import com.zs.aidata.sys.vo.AuthVO;
import com.zs.aidata.sys.vo.LoginInputVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 登录认证
 *
 * @author 张顺
 * @since 2021/2/22
 */
@Slf4j
@Api(tags = {"登录认证"})
@RestController
@RequestMapping(value = "core/sys/auth", headers = "Accept=application/json", produces = "application/json;charset=UTF-8")
public class LoginController {

    @PostMapping("login")
    @ApiOperation(value = "执行登录验证", notes = "执行登录验证")
    public AuthVO login(LoginInputVO inputVO) throws AiDataApplication {
        System.out.println(inputVO);
        // 获取权限操作对象，利用这个对象来完成登录操作
        Subject subject = SecurityUtils.getSubject();

        // 是否是认证过的
        if (!subject.isAuthenticated()) {
            // 没有认证过
            // 创建用户认证时的身份令牌，并设置我们从页面中传递账号和密码
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(inputVO.getUsername(), inputVO.getPassword());
            // 执行用户登录，会自动调用realm对象中的认证方法，如果登录失败会抛出相应的异常
            try {
                subject.login(usernamePasswordToken);
            } catch (Exception e) {
                throw new AiDataApplication(e.getMessage());
            }
        }
        return new AuthVO("认证成功");
    }

    @PostMapping("logout")
    @ApiOperation(value = "登出", notes = "登出")
    public AuthVO logout() throws AiDataApplication {
        Subject subject = SecurityUtils.getSubject();
        // 登出当前账号，清空当前账号的缓存，否则无法重新登录
        subject.logout();
        return new AuthVO("登出成功");
    }

}
