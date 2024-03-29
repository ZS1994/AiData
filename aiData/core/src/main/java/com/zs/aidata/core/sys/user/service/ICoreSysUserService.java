package com.zs.aidata.core.sys.user.service;

import com.github.pagehelper.PageInfo;
import com.zs.aidata.core.sys.user.vo.CoreSysUserDO;
import com.zs.aidata.core.tools.AiDataApplicationException;

/**
 * 用户表的service
 *
 * @author 张顺
 * @since 2021/3/1
 */
public interface ICoreSysUserService {

    /**
     * 通过账号查用户
     *
     * @param appId
     * @param userNumber
     * @return
     */
    CoreSysUserDO selectByUserNumber(String appId, String userNumber);


    /**
     * 分页查询
     *
     * @param queryVO
     * @param pageSize
     * @param currPage
     * @throws ArrayIndexOutOfBoundsException
     */
    PageInfo<CoreSysUserDO> findListByPage(CoreSysUserDO queryVO, Integer pageSize,
                                           Integer currPage) throws AiDataApplicationException;


    /**
     * 添加用户
     *
     * @param userDO
     */
    void insertUser(CoreSysUserDO userDO);


    /**
     * 修改用户
     *
     * @param userDO
     */
    void updateUser(CoreSysUserDO userDO);

    /**
     * 删除用户
     *
     * @param pId
     */
    void deleteUser(Integer pId);
}
