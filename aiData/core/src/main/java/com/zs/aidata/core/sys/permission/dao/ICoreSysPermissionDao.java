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
     * 通过permIdList来查权限
     *
     * @param appId
     * @param permIdList
     * @return
     */
    List<CoreSysPermissionDO> selectListByPermIdList(@Param("appId") String appId, @Param("permIdList") List<String> permIdList);

}