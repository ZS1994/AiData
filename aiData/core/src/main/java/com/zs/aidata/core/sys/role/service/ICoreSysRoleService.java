package com.zs.aidata.core.sys.role.service;

import com.github.pagehelper.PageInfo;
import com.zs.aidata.core.sys.role.vo.CoreSysRoleDO;
import com.zs.aidata.core.tools.AiDataApplicationException;

import java.util.List;

/**
 * @author 张顺
 * @since 2021/3/1
 */
public interface ICoreSysRoleService {


    /**
     * 分页查询
     *
     * @param queryVO
     * @param pageSize
     * @param currPage
     * @throws ArrayIndexOutOfBoundsException
     */
    PageInfo<CoreSysRoleDO> findListByPage(CoreSysRoleDO queryVO, Integer pageSize,
                                           Integer currPage) throws AiDataApplicationException;


    /**
     * 添加
     *
     * @param userDO
     */
    void insert(CoreSysRoleDO userDO);


    /**
     * 修改
     *
     * @param userDO
     */
    void update(CoreSysRoleDO userDO);

    /**
     * 删除
     *
     * @param pId
     */
    void delete(Integer pId);
}
