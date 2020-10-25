package com.zs.aidata.gmcc.dao;

import com.zs.aidata.gmcc.vo.BasUserSessionDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IBasUserSessionDao {
    int deleteByPrimaryKey(Integer pId);

    int insert(BasUserSessionDO record);

    int insertSelective(BasUserSessionDO record);

    BasUserSessionDO selectByPrimaryKey(Integer pId);

    int updateByPrimaryKeySelective(BasUserSessionDO record);

    int updateByPrimaryKey(BasUserSessionDO record);

    List<BasUserSessionDO> selectList(BasUserSessionDO record);
}