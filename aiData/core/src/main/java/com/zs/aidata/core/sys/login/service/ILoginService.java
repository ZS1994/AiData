package com.zs.aidata.core.sys.login.service;

import com.zs.aidata.core.sys.login.vo.AuthVO;
import com.zs.aidata.core.sys.login.vo.LoginInputVO;
import com.zs.aidata.core.tools.AiDataApplicationException;

/**
 * 登录服务
 *
 * @author 张顺
 * @since 2021/3/1
 */
public interface ILoginService {
    /**
     * 登录
     *
     * @param inputVO
     * @return
     * @throws AiDataApplicationException
     */
    AuthVO login(LoginInputVO inputVO) throws AiDataApplicationException;
}
