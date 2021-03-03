package com.zs.aidata.core.sys.permission.service;

import com.zs.aidata.core.sys.permission.dao.ICoreSysPermissionDao;
import com.zs.aidata.core.sys.permission.vo.CoreSysPermissionDO;
import com.zs.aidata.core.sys.permission.vo.CoreSysUpdatePermissionInVO;
import com.zs.aidata.core.sys.role.dao.ICoreSysRoleDao;
import com.zs.aidata.core.sys.role.dao.ICoreSysRolePermissionRelDao;
import com.zs.aidata.core.sys.user.dao.ICoreSysUserDao;
import com.zs.aidata.core.sys.user.dao.ICoreSysUserRoleRelDao;
import com.zs.aidata.core.sys.user.vo.CoreSysUserDO;
import com.zs.aidata.core.sys.user.vo.CoreSysUserRoleRelDO;
import com.zs.aidata.core.tools.AiDataApplicationException;
import com.zs.aidata.core.tools.BaseCoreService;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限
 *
 * @author 张顺
 * @since 2021/3/1
 */
@Named
public class CoreSysPermissionService extends BaseCoreService implements ICoreSysPermissionService {

    @Inject
    private ICoreSysPermissionDao iCoreSysPermissionDao;

    @Inject
    private ICoreSysUserDao iCoreSysUserDao;

    @Inject
    private ICoreSysRoleDao iCoreSysRoleDao;

    @Inject
    private ICoreSysUserRoleRelDao iCoreSysUserRoleRelDao;

    @Inject
    private ICoreSysRolePermissionRelDao iCoreSysRolePermissionRelDao;

    @Override
    public List<CoreSysPermissionDO> selectListByUserNumber(String appId, String userNumber) {
        checkNotEmpty(userNumber);
        CoreSysUserDO sysUserDO = iCoreSysUserDao.selectByUserNumber(appId, userNumber);
        if (isEmpty(sysUserDO)) {
            throw new AiDataApplicationException("用户{0}不存在", userNumber);
        }
        // 根据用户账号查所有的角色
        CoreSysUserRoleRelDO userRoleRelQuery = new CoreSysUserRoleRelDO();
        userRoleRelQuery.setAppId(appId);
        userRoleRelQuery.setUserNumber(userNumber);
        // 获取这个账号的所有权限
        List<CoreSysUserRoleRelDO> userRoleRelList = iCoreSysUserRoleRelDao.selectList(userRoleRelQuery);
        List<String> roleCodeList = userRoleRelList.stream().map(CoreSysUserRoleRelDO::getRoleCode).collect(Collectors.toList());
        List<CoreSysPermissionDO> permissionList = iCoreSysPermissionDao.selectListByRoleCodeList(appId, roleCodeList);
        return permissionList;
    }

    @Override
    public void updateAllPermissionByAuto(CoreSysUpdatePermissionInVO inVO) throws AiDataApplicationException {
        checkNotEmpty(inVO, "appId", "coreSysPermissionDOList");
        // 查一下当前app下的所有权限
        CoreSysPermissionDO queryDO = new CoreSysPermissionDO();
        queryDO.setAppId(inVO.getAppId());
        List<CoreSysPermissionDO> oldPermissionList = iCoreSysPermissionDao.selectList(queryDO);


    }
}
