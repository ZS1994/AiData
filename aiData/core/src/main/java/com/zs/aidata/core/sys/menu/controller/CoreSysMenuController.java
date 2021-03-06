package com.zs.aidata.core.sys.menu.controller;

import com.github.pagehelper.PageInfo;
import com.zs.aidata.core.sys.menu.service.ICoreSysMenuService;
import com.zs.aidata.core.sys.menu.vo.CoreSysMenuDO;
import com.zs.aidata.core.tools.AiDataApplicationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

/**
 * 角色服务接口
 *
 * @author 张顺
 * @since 2021/3/6
 */
@Slf4j
@Api(tags = {"菜单服务接口"})
@RestController
@RequestMapping(value = "core/coreSysMenuController", headers = "Accept=application/json", produces = "application/json;charset=UTF-8")
public class CoreSysMenuController {

    @Inject
    private ICoreSysMenuService iCoreSysMenuService;

    /**
     * 分页查询
     *
     * @param queryVO
     * @return
     * @throws AiDataApplicationException
     */
    @PostMapping("findListByPage/{pageSize}/{currPage}")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    PageInfo<CoreSysMenuDO> findListByPage(CoreSysMenuDO queryVO, @PathVariable("pageSize") Integer pageSize,
                                           @PathVariable("currPage") Integer currPage) throws AiDataApplicationException {
        return iCoreSysMenuService.findListByPage(queryVO, pageSize, currPage);
    }

    @PostMapping("insert")
    @ApiOperation(value = "添加", notes = "添加")
    void insert(CoreSysMenuDO userDO) {
        iCoreSysMenuService.insert(userDO);
    }

    @PostMapping("update")
    @ApiOperation(value = "修改", notes = "修改")
    void update(CoreSysMenuDO userDO) {
        iCoreSysMenuService.update(userDO);
    }

    @PostMapping("delete/{pId}")
    @ApiOperation(value = "删除", notes = "删除")
    void delete(@PathVariable("pId") Integer pId) {
        iCoreSysMenuService.delete(pId);
    }


    /**
     * 查询某个应用的所有菜单，并且为树型结构的数据
     *
     * @param queryVO
     * @return
     * @throws AiDataApplicationException
     */
    @PostMapping("selectTreeMenu")
    @ApiOperation(value = "查询某个应用的所有菜单并且为树型结构的数据", notes = "查询某个应用的所有菜单并且为树型结构的数据")
    List<CoreSysMenuDO> selectTreeMenu(CoreSysMenuDO queryVO) throws AiDataApplicationException {
        return iCoreSysMenuService.selectTreeMenu(queryVO);
    }

}
