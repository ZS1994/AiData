package com.zs.aidata.gmcc.vo;

import com.zs.aidata.core.BaseEntityVO;
import lombok.Data;

import java.util.Date;

@Data
public class BasUserSessionDO extends BaseEntityVO {

    private String phone;

    private String sessionId;

}