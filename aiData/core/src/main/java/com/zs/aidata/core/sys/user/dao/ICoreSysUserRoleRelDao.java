package com.zs.aidata.core.sys.user.dao;

import com.zs.aidata.core.sys.user.vo.CoreSysUserRoleRelDO;
import com.zs.aidata.core.tools.BaseCoreDao;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ICoreSysUserRoleRelDao extends BaseCoreDao<CoreSysUserRoleRelDO> {
    int deleteByPrimaryKey(Integer pId);

    int insert(CoreSysUserRoleRelDO record);

    int insertSelective(CoreSysUserRoleRelDO record);

    CoreSysUserRoleRelDO selectByPrimaryKey(Integer pId);

    int updateByPrimaryKeySelective(CoreSysUserRoleRelDO record);

    int updateByPrimaryKey(CoreSysUserRoleRelDO record);
}