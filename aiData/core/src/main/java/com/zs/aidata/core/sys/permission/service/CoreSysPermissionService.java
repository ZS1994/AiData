package com.zs.aidata.core.sys.permission.service;

import com.alibaba.fastjson.JSONArray;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void updateAllPermissionByAuto(CoreSysUpdatePermissionInVO inVO) throws AiDataApplicationException {
        checkNotEmpty(inVO, "appId", "coreSysPermissionDOListJsonArr");
        // 查一下当前app下的所有权限
        CoreSysPermissionDO queryDO = new CoreSysPermissionDO();
        queryDO.setAppId(inVO.getAppId());
        List<CoreSysPermissionDO> oldPermissionList = iCoreSysPermissionDao.selectList(queryDO);

        // 这个app的新权限
        List<CoreSysPermissionDO> newPermissionList = JSONArray.parseArray(inVO.getCoreSysPermissionDOListJsonArr(), CoreSysPermissionDO.class);

        // 找一下
        List<CoreSysPermissionDO> insertNewPermList = new ArrayList<>();
        List<CoreSysPermissionDO> updateNewPermList = new ArrayList<>();
        for (CoreSysPermissionDO newPermTmp : newPermissionList) {
            // 是否已经存在
            boolean isExist = false;
            // 是否已经存在，但是其他信息变了
            boolean isExistButUpdate = false;
            for (CoreSysPermissionDO oldPermTmp : oldPermissionList) {
                if (newPermTmp.getPermCode().equals(oldPermTmp.getPermCode())) {
                    isExist = true;
                    if (!newPermTmp.getPermMethod().equals(oldPermTmp.getPermMethod())
                            || !newPermTmp.getPermName().equals(oldPermTmp.getPermName())
                            || !newPermTmp.getPermUrl().equals(oldPermTmp.getPermUrl())
                    ) {
                        isExistButUpdate = true;
                        oldPermTmp.setPermMethod(newPermTmp.getPermMethod());
                        oldPermTmp.setPermName(newPermTmp.getPermName());
                        oldPermTmp.setPermUrl(newPermTmp.getPermUrl());
                        newPermTmp = oldPermTmp;
                    }
                    break;
                }
            }
            if (isExist == false) {
                initBaseFieldByCreate(newPermTmp);
                insertNewPermList.add(newPermTmp);
            } else if (isExist == true && isExistButUpdate == true) {
                updateNewPermList.add(newPermTmp);
            }
        }
        // 执行批量更新和批量插入
        iCoreSysPermissionDao.createOnceGo(insertNewPermList);
        iCoreSysPermissionDao.updateOnceGo(updateNewPermList);

        // 找出哪些是删除的权限
        List<CoreSysPermissionDO> deleteOldPermList = new ArrayList<>();
        // 此时要再查一遍
        oldPermissionList = iCoreSysPermissionDao.selectList(queryDO);
        for (CoreSysPermissionDO oldPermTmp : oldPermissionList) {
            // 是否已经存在
            boolean isExist = false;
            for (CoreSysPermissionDO newPermTmp : newPermissionList) {
                if (newPermTmp.getPermCode().equals(oldPermTmp.getPermCode())) {
                    isExist = true;
                }
            }
            if (isExist == false) {
                deleteOldPermList.add(oldPermTmp);
            }
        }
        iCoreSysPermissionDao.deleteOnceGo(deleteOldPermList);

        // 清理不存在的 角色-权限 关系
        List<String> deleteOldPermCodeList = deleteOldPermList
                .stream().map(CoreSysPermissionDO::getPermCode)
                .collect(Collectors.toList());

        iCoreSysRolePermissionRelDao.deleteByPermCodeList(inVO.getAppId(), deleteOldPermCodeList);
    }
}
