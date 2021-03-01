package com.zs.aidata.core.sys.user.service;

import com.zs.aidata.core.sys.user.vo.CoreSysUserDO;

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

}
