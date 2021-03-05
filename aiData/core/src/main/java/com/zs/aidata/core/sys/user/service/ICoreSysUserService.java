package com.zs.aidata.core.sys.user.service;

import com.github.pagehelper.PageInfo;
import com.zs.aidata.core.sys.permission.vo.CoreSysPermissionDO;
import com.zs.aidata.core.sys.user.vo.CoreSysUserDO;
import org.springframework.web.bind.annotation.PathVariable;

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
                                           Integer currPage) throws ArrayIndexOutOfBoundsException;
}
