package com.zs.aidata.core.sys.menu.vo;

import com.zs.aidata.core.tools.BaseEntityVO;
import lombok.Data;

import java.util.List;


@Data
public class CoreSysMenuDO extends BaseEntityVO {

    private String menuCode;

    private String menuName;

    private String menuPath;

    private Integer menuOrder;

    private String parentMenuCode;

    private String permCode;

    //-------额外属性-------------

    private List<CoreSysMenuDO> childMenuList;

}