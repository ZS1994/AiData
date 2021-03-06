package com.zs.aidata.core.sys.menu.dao;

import com.zs.aidata.core.sys.menu.vo.CoreSysMenuDO;
import com.zs.aidata.core.tools.BaseCoreDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ICoreSysMenuDao extends BaseCoreDao<CoreSysMenuDO> {
    int deleteByPrimaryKey(Integer pId);

    int insert(CoreSysMenuDO record);

    int insertSelective(CoreSysMenuDO record);

    CoreSysMenuDO selectByPrimaryKey(Integer pId);

    int updateByPrimaryKeySelective(CoreSysMenuDO record);

    int updateByPrimaryKey(CoreSysMenuDO record);

    /**
     * 通过父菜单代码查当前最大序号
     *
     * @param appId
     * @param parentMenuCode
     * @return
     */
    Integer selectMaxOrderByParentMenuCode(@Param("appId") String appId, @Param("parentMenuCode") String parentMenuCode);

}