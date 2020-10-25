package com.zs.aidata.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体类的基类
 *
 * @author 张顺
 * @since 2020/10/18
 */
@Data
public class BaseEntityVO implements Serializable {
    // 物理主键
    private Integer pId;

    // 创建人id
    private String createById;

    // 创建人名称
    private String createByUser;

    // 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    // 修改人id
    private String updateById;

    // 修改人名称
    private String updateByUser;

    // 修改时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateDate;


}
