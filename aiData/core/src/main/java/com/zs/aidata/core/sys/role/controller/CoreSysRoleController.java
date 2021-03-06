package com.zs.aidata.core.sys.role.controller;

import com.github.pagehelper.PageInfo;
import com.zs.aidata.core.sys.role.service.ICoreSysRoleService;
import com.zs.aidata.core.sys.role.vo.CoreSysRoleDO;
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
 * 角色服务接口
 *
 * @author 张顺
 * @since 2021/3/6
 */
@Slf4j
@Api(tags = {"角色服务接口"})
@RestController
@RequestMapping(value = "core/coreSysRoleController", headers = "Accept=application/json", produces = "application/json;charset=UTF-8")
public class CoreSysRoleController {

    @Inject
    private ICoreSysRoleService iCoreSysRoleService;

    /**
     * 分页查询
     *
     * @param queryVO
     * @return
     * @throws AiDataApplicationException
     */
    @PostMapping("findListByPage/{pageSize}/{currPage}")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    PageInfo<CoreSysRoleDO> findListByPage(CoreSysRoleDO queryVO, @PathVariable("pageSize") Integer pageSize,
                                           @PathVariable("currPage") Integer currPage) throws AiDataApplicationException {
        return iCoreSysRoleService.findListByPage(queryVO, pageSize, currPage);
    }

    @PostMapping("insert")
    @ApiOperation(value = "添加", notes = "添加")
    void insert(CoreSysRoleDO userDO) {
        iCoreSysRoleService.insert(userDO);
    }

    @PostMapping("update")
    @ApiOperation(value = "修改", notes = "修改")
    void update(CoreSysRoleDO userDO) {
        iCoreSysRoleService.update(userDO);
    }

    @PostMapping("delete/{pId}")
    @ApiOperation(value = "删除", notes = "删除")
    void delete(@PathVariable("pId") Integer pId) {
        iCoreSysRoleService.delete(pId);
    }

}
