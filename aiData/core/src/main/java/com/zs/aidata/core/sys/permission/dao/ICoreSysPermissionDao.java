package com.zs.aidata.core.sys.permission.dao;

import com.zs.aidata.core.sys.permission.vo.CoreSysPermissionDO;
import com.zs.aidata.core.tools.BaseCoreDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ICoreSysPermissionDao extends BaseCoreDao<CoreSysPermissionDO> {
    int deleteByPrimaryKey(Integer pId);

    int insert(CoreSysPermissionDO record);

    int insertSelective(CoreSysPermissionDO record);

    CoreSysPermissionDO selectByPrimaryKey(Integer pId);

    int updateByPrimaryKeySelective(CoreSysPermissionDO record);

    int updateByPrimaryKey(CoreSysPermissionDO record);

    /**
     * 通过permCodeList来查权限
     *
     * @param appId
     * @param permCodeList
     * @return
     */
    List<CoreSysPermissionDO> selectListByPermCodeList(@Param("appId") String appId, @Param("permCodeList") List<String> permCodeList);

    /**
     * 通过角色来查权限
     * @param appId
     * @param roleCodeList
     * @return
     */
    List<CoreSysPermissionDO> selectListByRoleCodeList(@Param("appId") String appId, @Param("roleCodeList") List<String> roleCodeList);


}