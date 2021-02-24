package com.zs.aidata.sys.vo;

import lombok.Data;

/**
 * 身份认证的出参
 *
 * @author 张顺
 * @since 2021/2/24
 */
@Data
public class AuthVO {

    // 提示消息
    private String message;


    public AuthVO(String message) {
        this.message = message;
    }

    public AuthVO() {
    }
}
