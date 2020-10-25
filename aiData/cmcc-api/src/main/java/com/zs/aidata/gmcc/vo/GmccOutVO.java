package com.zs.aidata.gmcc.vo;

import com.zs.aidata.core.BaseEntityVO;
import lombok.Data;

/**
 * @author 张顺
 * @since 2020/10/22
 */
@Data
public class GmccOutVO extends BaseEntityVO {

    private String message;

    private String result;

    private String desc;
}
