package com.zs.aidata.core.sys.permission.service;

import com.zs.aidata.core.sys.permission.vo.CoreSysPermissionDO;

import java.util.List;

/**
 * @author 张顺
 * @since 2021/3/1
 */
public interface ICoreSysPermissionService {
    /**
     * 根据用户账号查询用户的所有权限
     *
     * @param appId
     * @param userNumber
     * @return
     */
    List<CoreSysPermissionDO> selectListByUserNumber(String appId, String userNumber);
}