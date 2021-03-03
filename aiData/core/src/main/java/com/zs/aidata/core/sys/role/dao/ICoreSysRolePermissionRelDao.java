package com.zs.aidata.core.sys.role.dao;

import com.zs.aidata.core.sys.role.vo.CoreSysRolePermissionRelDO;
import com.zs.aidata.core.tools.BaseCoreDao;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ICoreSysRolePermissionRelDao extends BaseCoreDao<CoreSysRolePermissionRelDO> {
    int deleteByPrimaryKey(Integer pId);

    int insert(CoreSysRolePermissionRelDO record);

    int insertSelective(CoreSysRolePermissionRelDO record);

    CoreSysRolePermissionRelDO selectByPrimaryKey(Integer pId);

    int updateByPrimaryKeySelective(CoreSysRolePermissionRelDO record);

    int updateByPrimaryKey(CoreSysRolePermissionRelDO record);
}