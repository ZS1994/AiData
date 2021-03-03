package com.zs.aidata.core.sys.user.vo;

import com.zs.aidata.core.tools.BaseEntityVO;
import lombok.Data;


@Data
public class CoreSysUserRoleRelDO extends BaseEntityVO {

    private String userNumber;

    private String roleCode;

}