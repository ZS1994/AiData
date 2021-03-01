package com.zs.aidata.core.sys.role.dao;

import com.zs.aidata.core.sys.role.vo.CoreSysRoleDO;
import com.zs.aidata.core.tools.BaseCoreDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ICoreSysRoleDao extends BaseCoreDao<CoreSysRoleDO> {
    int deleteByPrimaryKey(Integer pId);

    int insert(CoreSysRoleDO record);

    int insertSelective(CoreSysRoleDO record);

    CoreSysRoleDO selectByPrimaryKey(Integer pId);

    int updateByPrimaryKeySelective(CoreSysRoleDO record);

    int updateByPrimaryKey(CoreSysRoleDO record);


    /**
     * 通过ridList查角色
     *
     * @param appId
     * @param ridList
     * @return
     */
    List<CoreSysRoleDO> selectListByRidList(@Param("appId") String appId, @Param("ridList") List<String> ridList);
}