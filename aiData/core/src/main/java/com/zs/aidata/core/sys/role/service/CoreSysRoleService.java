package com.zs.aidata.core.sys.role.service;

import com.github.pagehelper.PageInfo;
import com.zs.aidata.core.sys.role.dao.ICoreSysRoleDao;
import com.zs.aidata.core.sys.role.vo.CoreSysRoleDO;
import com.zs.aidata.core.tools.AiDataApplicationException;
import com.zs.aidata.core.tools.BaseCoreService;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author 张顺
 * @since 2021/3/1
 */
@Named
public class CoreSysRoleService extends BaseCoreService implements ICoreSysRoleService {

    @Inject
    private ICoreSysRoleDao iCoreSysRoleDao;

    @Override
    public PageInfo<CoreSysRoleDO> findListByPage(CoreSysRoleDO queryVO, Integer pageSize, Integer currPage) throws AiDataApplicationException {
        checkNotEmpty(queryVO);
        checkNotEmpty(pageSize);
        checkNotEmpty(currPage);
        return iCoreSysRoleDao.buildFindListByPage(() -> iCoreSysRoleDao.selectList(queryVO), pageSize, currPage);
    }

    @Override
    public void insert(CoreSysRoleDO userDO) {
        checkNotEmpty(userDO, "appId", "roleName", "roleCode");
        initBaseFieldByCreate(userDO);
        iCoreSysRoleDao.insert(userDO);
    }

    @Override
    public void update(CoreSysRoleDO userDO) {
        checkNotEmpty(userDO, "pId", "appId", "roleName", "roleCode");
        initBaseFieldByUpdate(userDO);
        iCoreSysRoleDao.updateByPrimaryKey(userDO);
    }

    @Override
    public void delete(Integer pId) {
        checkNotEmpty(pId);
        iCoreSysRoleDao.deleteByPrimaryKey(pId);
    }
}
