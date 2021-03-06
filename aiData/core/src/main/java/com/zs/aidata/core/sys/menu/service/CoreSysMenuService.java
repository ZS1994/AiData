package com.zs.aidata.core.sys.menu.service;

import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.PageInfo;
import com.zs.aidata.core.sys.menu.dao.ICoreSysMenuDao;
import com.zs.aidata.core.sys.menu.vo.CoreSysMenuDO;
import com.zs.aidata.core.tools.AiDataApplicationException;
import com.zs.aidata.core.tools.BaseCoreService;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 张顺
 * @since 2021/3/1
 */
@Named
public class CoreSysMenuService extends BaseCoreService implements ICoreSysMenuService {

    @Inject
    private ICoreSysMenuDao iCoreSysMenuDao;

    @Override
    public PageInfo<CoreSysMenuDO> findListByPage(CoreSysMenuDO queryVO, Integer pageSize, Integer currPage) throws AiDataApplicationException {
        checkNotEmpty(queryVO);
        checkNotEmpty(pageSize);
        checkNotEmpty(currPage);
        return iCoreSysMenuDao.buildFindListByPage(() -> iCoreSysMenuDao.selectList(queryVO), pageSize, currPage);
    }

    @Override
    public void insert(CoreSysMenuDO menuDO) {
        checkNotEmpty(menuDO, "appId", "menuName");
        // 查询当前最大序号
        Integer nowMaxOrder = iCoreSysMenuDao.selectMaxOrderByParentMenuCode(menuDO.getAppId(), menuDO.getParentMenuCode());
        if (isEmpty(nowMaxOrder)) {
            nowMaxOrder = 0;
        }
        // 将序号+1
        nowMaxOrder += 1;
        menuDO.setMenuOrder(nowMaxOrder);
        // 自动生成menuCode
        String menuCode = IdUtil.fastSimpleUUID();
        menuDO.setMenuCode(menuCode);
        initBaseFieldByCreate(menuDO);
        iCoreSysMenuDao.insert(menuDO);
    }

    @Override
    public void update(CoreSysMenuDO menuDO) {
        checkNotEmpty(menuDO, "pId", "appId", "menuName", "menuCode");
        initBaseFieldByUpdate(menuDO);
        iCoreSysMenuDao.updateByPrimaryKey(menuDO);
    }

    @Override
    public void delete(Integer pId) {
        checkNotEmpty(pId);
        iCoreSysMenuDao.deleteByPrimaryKey(pId);
    }

    @Override
    public List<CoreSysMenuDO> selectTreeMenu(CoreSysMenuDO queryVO) throws AiDataApplicationException {
        checkNotEmpty(queryVO, "appId");
        List<CoreSysMenuDO> allMenuList = iCoreSysMenuDao.selectList(queryVO);

        // 在这些里面找出顶级节点
        List<CoreSysMenuDO> firstMenuList = getFirstMenu(allMenuList);

        // 已经找过的节点，防止无限递归
        List<CoreSysMenuDO> out = new ArrayList<>();
        List<Integer> searchIdList = new ArrayList<>();
        for (CoreSysMenuDO firstMenu : firstMenuList) {
            CoreSysMenuDO firstMenuSub = initChildMenu(allMenuList, firstMenu, searchIdList);
            out.add(firstMenuSub);
        }
        return out;
    }

    /**
     * 寻找顶级菜单并排序
     *
     * @param allMenu
     * @return
     */
    private List<CoreSysMenuDO> getFirstMenu(List<CoreSysMenuDO> allMenu) {
        List<CoreSysMenuDO> out = new ArrayList<>();
        for (CoreSysMenuDO menuDO : allMenu) {
            if (isEmpty(menuDO.getParentMenuCode())) {
                out.add(menuDO);
            }
        }
        // 按照序号正序排序排序
        out = out.stream().sorted((v1, v2) -> v1.getMenuOrder().compareTo(v2.getMenuOrder())).collect(Collectors.toList());
        return out;
    }

    /**
     * 装填子节点，并对子节点排序
     *
     * @param allMenuList
     * @param parentMenu
     * @param searchIdList
     * @return
     */
    private CoreSysMenuDO initChildMenu(List<CoreSysMenuDO> allMenuList, CoreSysMenuDO parentMenu, List<Integer> searchIdList) {
        searchIdList.add(parentMenu.getpId());
        List<CoreSysMenuDO> childList = new ArrayList<>();
        for (CoreSysMenuDO menuDO : allMenuList) {
            // 已经找过的节点不要再找了
            if (searchIdList.contains(menuDO.getpId())) {
                continue;
            }
            // 找自己的子节点
            if (parentMenu.getMenuCode().equals(menuDO.getParentMenuCode())) {
                CoreSysMenuDO menuSubVO = initChildMenu(allMenuList, menuDO, searchIdList);
                childList.add(menuSubVO);
            }
        }
        // 按照序号正序排序排序
        childList = childList.stream().sorted((v1, v2) -> v1.getMenuOrder().compareTo(v2.getMenuOrder())).collect(Collectors.toList());
        parentMenu.setChildMenuList(childList);
        return parentMenu;
    }

}