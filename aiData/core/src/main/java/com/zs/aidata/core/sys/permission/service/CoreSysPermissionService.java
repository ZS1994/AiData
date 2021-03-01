package com.zs.aidata.core.sys.permission.service;

import com.zs.aidata.core.sys.permission.dao.ICoreSysPermissionDao;
import com.zs.aidata.core.sys.permission.vo.CoreSysPermissionDO;
import com.zs.aidata.core.sys.role.dao.ICoreSysRoleDao;
import com.zs.aidata.core.sys.role.vo.CoreSysRoleDO;
import com.zs.aidata.core.sys.user.dao.ICoreSysUserDao;
import com.zs.aidata.core.sys.user.vo.CoreSysUserDO;
import com.zs.aidata.core.tools.AiDataApplicationException;
import com.zs.aidata.core.tools.BaseCoreService;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.Array;
import java.util.*;

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


    @Override
    public List<CoreSysPermissionDO> selectListByUserNumber(String appId, String userNumber) {
        checkNotEmpty(userNumber);
        CoreSysUserDO sysUserDO = iCoreSysUserDao.selectByUserNumber(appId, userNumber);
        if (isEmpty(sysUserDO)) {
            throw new AiDataApplicationException("用户{0}不存在", userNumber);
        }
        String rids = sysUserDO.getRids();
        if (isNotEmpty(rids)) {
            List<String> ridList = Arrays.asList(rids.split(","));
            List<CoreSysRoleDO> roleList = iCoreSysRoleDao.selectListByRidList(appId, ridList);
            // set去重
            Set<String> permIdList = new HashSet<>();
            for (CoreSysRoleDO roleDO : roleList) {
                if (isEmpty(roleDO.getPids())) {
                    continue;
                }
                String permIdArr[] = roleDO.getPids().split(",");
                permIdList.addAll(Arrays.asList(permIdArr));
            }
            List<CoreSysPermissionDO> permissionList = iCoreSysPermissionDao.selectListByPermIdList(appId, new ArrayList<>(permIdList));
            return permissionList;
        }
        return new ArrayList<>();
    }
}
