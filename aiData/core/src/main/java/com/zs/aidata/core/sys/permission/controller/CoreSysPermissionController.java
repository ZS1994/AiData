package com.zs.aidata.core.sys.permission.controller;

import com.zs.aidata.core.sys.permission.vo.CoreSysUpdatePermissionInVO;
import com.zs.aidata.core.tools.AiDataApplicationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限服务接口
 *
 * @author 张顺
 * @since 2021/3/3
 */
@Slf4j
@Api(tags = {"权限服务接口"})
@RestController
@RequestMapping(value = "core/coreSysPermissionController", headers = "Accept=application/json", produces = "application/json;charset=UTF-8")
public class CoreSysPermissionController {

    /**
     * 自动更新所有权限
     *
     * @param inVO
     * @throws AiDataApplicationException
     */
    @PostMapping("updateAllPermissionByAuto")
    @ApiOperation(value = "执行登录验证", notes = "执行登录验证")
    void updateAllPermissionByAuto(CoreSysUpdatePermissionInVO inVO) throws AiDataApplicationException {


    }


}
