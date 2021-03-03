package com.zs.aidata.core.sys.user.vo;

import com.zs.aidata.core.tools.BaseEntityVO;
import lombok.Data;


@Data
public class CoreSysUserDO extends BaseEntityVO {

    private String userNumber;

    private String userPassword;

    private String userName;

    private String mail;

    private String phone;

    private String deleteFlag;

    private String img;

}