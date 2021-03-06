package com.zs.aidata.core.sys.menu.service;

import com.github.pagehelper.PageInfo;
import com.zs.aidata.core.sys.menu.vo.CoreSysMenuDO;
import com.zs.aidata.core.tools.AiDataApplicationException;

import java.util.List;

/**
 * @author 张顺
 * @since 2021/3/1
 */
public interface ICoreSysMenuService {


    /**
     * 分页查询
     *
     * @param queryVO
     * @param pageSize
     * @param currPage
     * @throws ArrayIndexOutOfBoundsException
     */
    PageInfo<CoreSysMenuDO> findListByPage(CoreSysMenuDO queryVO, Integer pageSize,
                                           Integer currPage) throws AiDataApplicationException;


    /**
     * 添加
     *
     * @param userDO
     */
    void insert(CoreSysMenuDO userDO);


    /**
     * 修改
     *
     * @param userDO
     */
    void update(CoreSysMenuDO userDO);

    /**
     * 删除
     *
     * @param pId
     */
    void delete(Integer pId);


    /**
     * 查询某个应用的所有菜单，并且为树型结构的数据
     *
     * @param queryVO
     * @return
     * @throws AiDataApplicationException
     */
    List<CoreSysMenuDO> selectTreeMenu(CoreSysMenuDO queryVO) throws AiDataApplicationException;
}
