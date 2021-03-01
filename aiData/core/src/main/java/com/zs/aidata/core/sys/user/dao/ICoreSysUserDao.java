package com.zs.aidata.core.sys.user.dao;

import com.zs.aidata.core.sys.user.vo.CoreSysUserDO;
import com.zs.aidata.core.tools.BaseCoreDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ICoreSysUserDao extends BaseCoreDao<CoreSysUserDO> {
    int deleteByPrimaryKey(Integer pId);

    int insert(CoreSysUserDO record);

    int insertSelective(CoreSysUserDO record);

    CoreSysUserDO selectByPrimaryKey(Integer pId);

    int updateByPrimaryKeySelective(CoreSysUserDO record);

    int updateByPrimaryKey(CoreSysUserDO record);

    /**
     * 根据用户账号查找
     *
     * @param appId
     * @param userNumber
     * @return
     */
    CoreSysUserDO selectByUserNumber(@Param("appId") String appId, @Param("userNumber") String userNumber);
}