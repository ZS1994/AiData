package com.zs.aidata.core.sys.role.vo;

import com.zs.aidata.core.tools.BaseEntityVO;
import lombok.Data;

@Data
public class CoreSysRoleDO extends BaseEntityVO {

    private String roleName;

    private String roleDesc;

    private String roleCode;
}