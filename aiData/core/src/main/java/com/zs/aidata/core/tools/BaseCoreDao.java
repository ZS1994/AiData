package com.zs.aidata.core.tools;

import cn.hutool.core.collection.CollUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.zs.aidata.core.sys.permission.vo.CoreSysPermissionDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 基础dao，dao都要继承它
 *
 * @author 张顺
 * @since 2021/2/28
 */
public interface BaseCoreDao<T> {

    /**
     * 批量操作时，一次性处理的集合最大条数，减少sql过长的风险
     */
    int LIST_MAX_SIZE = 500;

    /**
     * 默认通用查询集合方法
     *
     * @param queryVo 查询条件
     * @return 结果集
     */
    List<T> selectList(@Param("v") T queryVo);


    /**
     * 批量创建底层方法
     *
     * @param list 集合
     * @return 条数
     */
    int createOnce(@Param("list") List<T> list);

    /**
     * 批量修改底层方法
     *
     * @param list 集合
     * @return 条数
     */
    int updateOnce(@Param("list") List<T> list);

    /**
     * 批量删除底层方法
     *
     * @param list 集合
     * @return 条数
     */
    int deleteOnce(@Param("list") List<T> list);

    /**
     * 批量创建对外提供服务的方法
     *
     * @param list 集合
     * @return 条数
     */
    default int createOnceGo(List<T> list) {
        List<List<T>> partList = Lists.partition(list, LIST_MAX_SIZE);
        int row = 0;
        for (List<T> item : partList) {
            row += createOnce(item);
        }
        return row;
    }

    /**
     * 批量修改对外提供服务的方法
     *
     * @param list 集合
     * @return 条数
     */
    default int updateOnceGo(List<T> list) {
        List<List<T>> partList = Lists.partition(list, LIST_MAX_SIZE);
        int row = 0;
        for (List<T> item : partList) {
            row += updateOnce(item);
        }
        return row;
    }

    /**
     * 批量删除对外提供服务的方法
     *
     * @param list 集合
     * @return 条数
     */
    default int deleteOnceGo(List<T> list) {
        List<List<T>> partList = Lists.partition(list, LIST_MAX_SIZE);
        int row = 0;
        for (List<T> item : partList) {
            row += deleteOnce(item);
        }
        return row;
    }

    /**
     * 快速构建分页查询方法
     *
     * @param function
     * @param pageSize
     * @param currPage
     * @return
     */
    default PageInfo<T> buildFindListByPage(Supplier<List<T>> function, Integer pageSize, Integer currPage) {
        // 查询第一页，每一页显示2条记录
        PageHelper.startPage(currPage, pageSize);
        // 目标操作：查询用户数据
        List<T> list = function.get();
        // 封装分页结果数据：PageInfo
        PageInfo<T> pageInfo = new PageInfo<T>(list);
        return pageInfo;
    }

}
