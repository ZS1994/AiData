package com.zs.aidata.core.sys.role.dao;

import com.zs.aidata.core.sys.role.vo.CoreSysRolePermissionRelDO;
import com.zs.aidata.core.tools.BaseCoreDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ICoreSysRolePermissionRelDao extends BaseCoreDao<CoreSysRolePermissionRelDO> {
    int deleteByPrimaryKey(Integer pId);

    int insert(CoreSysRolePermissionRelDO record);

    int insertSelective(CoreSysRolePermissionRelDO record);

    CoreSysRolePermissionRelDO selectByPrimaryKey(Integer pId);

    int updateByPrimaryKeySelective(CoreSysRolePermissionRelDO record);

    int updateByPrimaryKey(CoreSysRolePermissionRelDO record);

    /**
     * 根据权限批量删除关系
     *
     * @param appId
     * @param permCodeList
     * @return
     */
    int deleteByPermCodeList(String appId, List<String> permCodeList);
}