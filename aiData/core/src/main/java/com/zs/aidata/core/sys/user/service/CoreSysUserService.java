package com.zs.aidata.core.sys.user.service;

import com.zs.aidata.core.sys.user.dao.ICoreSysUserDao;
import com.zs.aidata.core.sys.user.vo.CoreSysUserDO;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * user的service
 *
 * @author 张顺
 * @since 2021/3/1
 */
@Named
public class CoreSysUserService implements ICoreSysUserService {

    @Inject
    private ICoreSysUserDao iCoreSysUserDao;

    @Override
    public CoreSysUserDO selectByUserNumber(String appId, String userNumber) {
        return iCoreSysUserDao.selectByUserNumber(appId, userNumber);
    }
}
