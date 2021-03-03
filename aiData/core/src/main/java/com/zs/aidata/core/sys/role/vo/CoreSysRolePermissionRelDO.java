package com.zs.aidata.core.sys.role.vo;

import com.zs.aidata.core.tools.BaseEntityVO;
import lombok.Data;


@Data
public class CoreSysRolePermissionRelDO extends BaseEntityVO {

    private String roleCode;

    private String permCode;

}