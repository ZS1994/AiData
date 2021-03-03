package com.zs.aidata.core.sys.permission.vo;

import com.zs.aidata.core.tools.BaseEntityVO;
import lombok.Data;

import java.util.List;

/**
 * 自动更新所有权限入参封装
 *
 * @author 张顺
 * @since 2021/3/3
 */
@Data
public class CoreSysUpdatePermissionInVO extends BaseEntityVO {

    /**
     *
     */
    String coreSysPermissionDOListJsonArr;


}
