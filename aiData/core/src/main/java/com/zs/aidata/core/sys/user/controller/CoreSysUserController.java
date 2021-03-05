package com.zs.aidata.core.sys.user.controller;

import com.github.pagehelper.PageInfo;
import com.zs.aidata.core.sys.permission.vo.CoreSysPermissionDO;
import com.zs.aidata.core.sys.user.service.ICoreSysUserService;
import com.zs.aidata.core.sys.user.vo.CoreSysUserDO;
import com.zs.aidata.core.tools.AiDataApplicationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * 用户服务接口
 *
 * @author 张顺
 * @since 2021/3/6
 */
@Slf4j
@Api(tags = {"用户服务接口"})
@RestController
@RequestMapping(value = "core/coreSysUserController", headers = "Accept=application/json", produces = "application/json;charset=UTF-8")
public class CoreSysUserController {

    @Inject
    private ICoreSysUserService iCoreSysUserService;

    /**
     * 分页查询
     *
     * @param queryVO
     * @return
     * @throws AiDataApplicationException
     */
    @PostMapping("findListByPage/{pageSize}/{currPage}")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    PageInfo<CoreSysUserDO> findListByPage(CoreSysUserDO queryVO, @PathVariable("pageSize") Integer pageSize,
                                           @PathVariable("currPage") Integer currPage) throws AiDataApplicationException {
        return iCoreSysUserService.findListByPage(queryVO, pageSize, currPage);
    }


}
