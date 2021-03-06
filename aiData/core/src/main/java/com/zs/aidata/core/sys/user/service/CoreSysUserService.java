package com.zs.aidata.core.sys.user.service;

import com.github.pagehelper.PageInfo;
import com.zs.aidata.core.sys.user.dao.ICoreSysUserDao;
import com.zs.aidata.core.sys.user.vo.CoreSysUserDO;
import com.zs.aidata.core.tools.BaseCoreService;
import com.zs.aidata.core.tools.Constans;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * user的service
 *
 * @author 张顺
 * @since 2021/3/1
 */
@Named
public class CoreSysUserService extends BaseCoreService implements ICoreSysUserService {

    @Inject
    private ICoreSysUserDao iCoreSysUserDao;

    @Override
    public CoreSysUserDO selectByUserNumber(String appId, String userNumber) {
        return iCoreSysUserDao.selectByUserNumber(appId, userNumber);
    }

    @Override
    public PageInfo<CoreSysUserDO> findListByPage(CoreSysUserDO queryVO, Integer pageSize, Integer currPage) throws ArrayIndexOutOfBoundsException {
        return iCoreSysUserDao.buildFindListByPage(() -> iCoreSysUserDao.selectList(queryVO), pageSize, currPage);
    }

    @Override
    public void insertUser(CoreSysUserDO userDO) {
        checkNotEmpty(userDO, "appId", "userNumber", "userPassword", "userName");
        userDO.setDeleteFlag(Constans.FLAG_N);
        initBaseFieldByCreate(userDO);
        iCoreSysUserDao.insert(userDO);
    }

    @Override
    public void updateUser(CoreSysUserDO userDO) {
        userDO.getpId();
        checkNotEmpty(userDO, "pId", "appId", "userNumber", "userPassword", "userName");
        iCoreSysUserDao.updateByPrimaryKey(userDO);
    }

    @Override
    public void deleteUser(Integer pId) {
        checkNotEmpty(pId);
        iCoreSysUserDao.deleteByPrimaryKey(pId);
    }
}
