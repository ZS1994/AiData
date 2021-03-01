package com.zs.aidata.core.sys.login.service;

import com.alibaba.fastjson.JSONArray;
import com.zs.aidata.core.sys.login.vo.AuthVO;
import com.zs.aidata.core.sys.login.vo.LoginInputVO;
import com.zs.aidata.core.sys.permission.service.ICoreSysPermissionService;
import com.zs.aidata.core.sys.permission.vo.CoreSysPermissionDO;
import com.zs.aidata.core.sys.role.service.ICoreSysRoleService;
import com.zs.aidata.core.sys.user.service.ICoreSysUserService;
import com.zs.aidata.core.sys.user.vo.CoreSysUserDO;
import com.zs.aidata.core.tools.AiDataApplicationException;
import com.zs.aidata.core.tools.BaseCoreService;
import com.zs.aidata.core.tools.Constans;
import com.zs.aidata.core.tools.JwtUtil;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 张顺
 * @since 2021/3/1
 */
@Slf4j
@Named
public class LoginService extends BaseCoreService implements ILoginService {

    @Inject
    private ICoreSysUserService iCoreSysUserService;
    @Inject
    private ICoreSysRoleService iCoreSysRoleService;
    @Inject
    private ICoreSysPermissionService iCoreSysPermissionService;

    @Override
    public AuthVO login(LoginInputVO inputVO) throws AiDataApplicationException {
        checkNotEmpty(inputVO, "appId", "userNumber", "userPassword");
        log.info(inputVO.toString());
        AuthVO out = new AuthVO();
        // 账号密码认证
        CoreSysUserDO sysUserDO = iCoreSysUserService.selectByUserNumber(inputVO.getAppId(), inputVO.getUserNumber());
        if (isEmpty(sysUserDO)) {
            out.setMessage("用户不存在");
            out.setStatus(Constans.STATUS_ERROR);
            return out;
        }
        if (!inputVO.getUserPassword().equals(sysUserDO.getUserPassword())) {
            out.setMessage("密码错误");
            out.setStatus(Constans.STATUS_ERROR);
            return out;
        }
        // 认证通过，生成jwt的token返回
        Map<String, String> map = new HashMap<>();
        map.put(Constans.JWT_USER_NUMBER, inputVO.getUserNumber());
        // 查询用户的权限
        List<CoreSysPermissionDO> permissionList = iCoreSysPermissionService.selectListByUserNumber(inputVO.getAppId(), inputVO.getUserNumber());
        map.put(Constans.JWT_PERMISSION_LIST, JSONArray.toJSONString(permissionList));
        String token = JwtUtil.createToken(map);
        out.setStatus(Constans.STATUS_SUCCESS);
        out.setMessage("登录成功");
        out.setToken(token);
        return out;
    }
}
