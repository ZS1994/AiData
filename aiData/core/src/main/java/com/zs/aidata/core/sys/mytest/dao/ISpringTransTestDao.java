package com.zs.aidata.core.sys.mytest.dao;

import com.zs.aidata.core.sys.mytest.vo.CoreSysTestDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 张顺
 * @since 2021/11/30
 */
@Mapper
public interface ISpringTransTestDao {

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_core_sys_test
     *
     * @mbg.generated Tue Nov 30 23:08:42 GMT+08:00 2021
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_core_sys_test
     *
     * @mbg.generated Tue Nov 30 23:08:42 GMT+08:00 2021
     */
    int insert(CoreSysTestDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_core_sys_test
     *
     * @mbg.generated Tue Nov 30 23:08:42 GMT+08:00 2021
     */
    int insertSelective(CoreSysTestDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_core_sys_test
     *
     * @mbg.generated Tue Nov 30 23:08:42 GMT+08:00 2021
     */
    CoreSysTestDO selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_core_sys_test
     *
     * @mbg.generated Tue Nov 30 23:08:42 GMT+08:00 2021
     */
    int updateByPrimaryKeySelective(CoreSysTestDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_core_sys_test
     *
     * @mbg.generated Tue Nov 30 23:08:42 GMT+08:00 2021
     */
    int updateByPrimaryKey(CoreSysTestDO record);

    void commit();

    List<CoreSysTestDO> selectList(@Param("vo") CoreSysTestDO record);

}
