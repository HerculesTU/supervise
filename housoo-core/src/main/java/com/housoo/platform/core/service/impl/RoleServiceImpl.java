package com.housoo.platform.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.util.*;
import com.housoo.platform.core.dao.RoleDao;
import com.housoo.platform.core.service.RoleRightService;
import com.housoo.platform.core.service.RoleService;
import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.core.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 描述 角色业务相关service实现类
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-04-15 15:20:40
 */
@Service("roleService")
public class RoleServiceImpl extends BaseServiceImpl implements RoleService {

    /**
     * 所引入的dao
     */
    @Resource
    private RoleDao dao;
    /**
     *
     */
    @Resource
    private SysUserService sysUserService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;
    /**
     *
     */
    @Resource
    private RoleRightService roleRightService;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 保存角色用户信息
     *
     * @param roleId
     * @param userIds
     */
    @Override
    public void saveUsers(String roleId, List<String> userIds) {
        //获取当前登录用户
        Map<String, Object> sysUser = PlatAppUtil.getBackPlatLoginUser();
        if (sysUser != null) {
            String SYSUSER_COMPANYID = (String) sysUser.get("SYSUSER_COMPANYID");
            //先删除用户角色中间表的数据
            StringBuffer delSql = new StringBuffer("DELETE FROM PLAT_SYSTEM_SYSUSERROLE ");
            delSql.append("WHERE ROLE_ID=? AND SYSUSER_ID IN (SELECT T.SYSUSER_ID ");
            delSql.append("FROM PLAT_SYSTEM_SYSUSER T LEFT JOIN PLAT_SYSTEM_COMPANY C ");
            delSql.append("ON T.SYSUSER_COMPANYID=C.COMPANY_ID WHERE C.COMPANY_PATH LIKE ? )");
            dao.executeSql(delSql.toString(), new Object[]{roleId, "%" + SYSUSER_COMPANYID + "%"});
        } else {
            StringBuffer delSql = new StringBuffer("DELETE FROM PLAT_SYSTEM_SYSUSERROLE ");
            delSql.append("WHERE ROLE_ID=? ");
            dao.executeSql(delSql.toString(), new Object[]{roleId});
        }
        StringBuffer sql = new StringBuffer("INSERT INTO PLAT_SYSTEM_SYSUSERROLE");
        sql.append("(SYSUSER_ID,ROLE_ID) VALUES(?,?) ");
        for (String userId : userIds) {
            boolean isExistsUser = sysUserService.isExistsUser(userId);
            if (isExistsUser) {
                dao.executeSql(sql.toString(), new Object[]{userId, roleId});
            }
        }
    }

    /**
     * 根据角色组ID获取角色列表
     *
     * @param groupId:如果为空，那么获取所有的角色
     * @return
     */
    @Override
    public List<Map<String, Object>> findByGroupId(String groupId) {
        StringBuffer sql = new StringBuffer("select T.ROLE_ID,T.ROLE_NAME");
        sql.append(" from PLAT_SYSTEM_ROLE T ");
        //获取当前登录用户
        Map<String, Object> sysUser = PlatAppUtil.getBackPlatLoginUser();
        boolean isAdmin = (boolean) sysUser.get(SysUserService.ISADMIN_KEY);
        if (!isAdmin) {
            Set<String> grantGroupIds = (Set<String>) sysUser.get(SysUserService.GROUPIDSET_KEY);
            sql.append(" WHERE T.ROLE_GROUPID IN ").append(PlatStringUtil.getSqlInCondition(grantGroupIds));
        }
        List params = new ArrayList();
        if (StringUtils.isNotEmpty(groupId)) {
            if (!isAdmin) {
                sql.append(" AND ");
            } else {
                sql.append(" WHERE ");
            }
            sql.append(" T.ROLE_GROUPID=? ");
            params.add(groupId);
        }
        List<Map<String, Object>> roleList = dao.findBySql(sql.toString(), params.toArray(), null);
        return roleList;
    }

    /**
     * 获取角色和用户的树形JSON
     *
     * @param params
     * @return
     */
    @Override
    public String getRoleAndUserJson(Map<String, Object> params) {
        String groupId = (String) params.get("ROLE_GROUPID");
        String needCheckIds = (String) params.get("needCheckIds");
        Set<String> needCheckIdSet = new HashSet<String>();
        if (StringUtils.isNotEmpty(needCheckIds)) {
            needCheckIdSet = new HashSet<String>(Arrays.asList(needCheckIds.split(",")));
        }
        Map<String, Object> rootNode = new HashMap<String, Object>();
        rootNode.put("id", "0");
        rootNode.put("name", "角色用户树");
        rootNode.put("nocheck", true);
        List<Map<String, Object>> roleList = this.findByGroupId(groupId);
        for (Map<String, Object> role : roleList) {
            String roleId = (String) role.get("ROLE_ID");
            role.put("id", roleId);
            role.put("name", role.get("ROLE_NAME"));
            role.put("nocheck", true);
            List<Map<String, Object>> userlist = sysUserService.findByRoleId(roleId);
            if (userlist != null && userlist.size() > 0) {
                for (Map<String, Object> user : userlist) {
                    String userId = (String) user.get("SYSUSER_ID");
                    if (needCheckIdSet.contains(userId)) {
                        user.put("checked", true);
                    }
                    user.put("id", user.get("SYSUSER_ID"));
                    user.put("name", user.get("SYSUSER_NAME"));
                }
                role.put("children", userlist);
            }
        }
        rootNode.put("children", roleList);
        return JSON.toJSONString(rootNode);
    }

    /**
     * 获取自动补全的角色用户数据
     *
     * @param filter
     * @return
     */
    @Override
    public List<Map<String, Object>> findAutoRoleUser(SqlFilter filter) {
        String roleGroupId = filter.getRequest().getParameter("ROLE_GROUPID");
        StringBuffer sql = new StringBuffer("SELECT R.ROLE_NAME AS value,R.ROLE_NAME AS label");
        sql.append(" FROM PLAT_SYSTEM_ROLE R ");
        List params = new ArrayList();
        if (StringUtils.isNotEmpty(roleGroupId)) {
            sql.append("WHERE R.ROLE_GROUPID=? ");
            params.add(roleGroupId);
        }
        sql.append(" ORDER BY R.ROLE_CREATETIME DESC");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> roleList = dao.findBySql(sql.toString(),
                params.toArray(), null);
        list.addAll(roleList);
        List<Map<String, Object>> userList = this.sysUserService.findByRoleGroupId(roleGroupId);
        if (userList != null && userList.size() > 0) {
            for (Map<String, Object> user : userList) {
                user.put("label", user.get("SYSUSER_NAME"));
                user.put("value", user.get("SYSUSER_NAME"));
            }
            list.addAll(userList);
        }
        return list;
    }


    /**
     * 根据filter获取网格项目
     *
     * @param sqlFilter
     * @return
     */
    @Override
    public List<Map<String, Object>> findGridItemList(SqlFilter sqlFilter) {
        StringBuffer sql = new StringBuffer("SELECT T.ROLE_ID,T.ROLE_NAME");
        sql.append(",T.ROLE_CODE FROM PLAT_SYSTEM_ROLE T ");
        String selectedRoleIds = sqlFilter.getRequest().getParameter("selectedRecordIds");
        String iconfont = sqlFilter.getRequest().getParameter("iconfont");
        String itemconf = sqlFilter.getRequest().getParameter("itemconf");
        Map<String, String> getGridItemConf = PlatUICompUtil.getGridItemConfMap(itemconf);
        if (StringUtils.isNotEmpty(selectedRoleIds)) {
            sql.append(" WHERE T.ROLE_ID IN ");
            sql.append(PlatStringUtil.getSqlInCondition(selectedRoleIds));
            sql.append(" ORDER BY T.ROLE_CREATETIME DESC");
            List<Map<String, Object>> list = dao.findBySql(sql.toString(), null, null);
            list = PlatUICompUtil.getGridItemList("ROLE_ID", iconfont, getGridItemConf, list);
            return list;
        } else {
            return null;
        }
    }


    /**
     * 根据用户ID获取关联的角色IDS
     *
     * @param userId
     * @return
     */
    @Override
    public List<String> findRoleIds(String userId) {
        return dao.findRoleIds(userId);
    }

    /**
     * 根据用户ID获取管理的角色列表
     *
     * @param userId
     * @return
     */
    @Override
    public List<Map<String, Object>> findByUserId(String userId) {
        StringBuffer sql = new StringBuffer("select T.ROLE_ID,T.ROLE_NAME,");
        sql.append("T.ROLE_CODE from PLAT_SYSTEM_ROLE t");
        sql.append(" WHERE T.ROLE_ID IN (SELECT UR.ROLE_ID FROM ");
        sql.append("PLAT_SYSTEM_SYSUSERROLE UR WHERE UR.SYSUSER_ID=? ");
        sql.append(") ORDER BY T.ROLE_CREATETIME DESC");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(),
                new Object[]{userId}, null);
        return list;
    }

    /**
     * 删除角色信息并且级联删除相关中间表
     *
     * @param roleIds
     */
    @Override
    public void deleteCascadeUserAndRes(String roleIds) {
        //级联删除角色用户中间表
        StringBuffer sql = new StringBuffer("DELETE FROM ");
        sql.append("PLAT_SYSTEM_SYSUSERROLE WHERE ROLE_ID ");
        sql.append(" IN ").append(PlatStringUtil.getSqlInCondition(roleIds));
        dao.executeSql(sql.toString(), null);
        ;
        //级联删除角色资源中间表
        sql = new StringBuffer("DELETE FROM PLAT_SYSTEM_ROLERIGHT ");
        sql.append(" WHERE ROLE_ID IN ").append(PlatStringUtil.getSqlInCondition(roleIds));
        dao.executeSql(sql.toString(), null);
        ;
        //删除角色数据
        sql = new StringBuffer("DELETE FROM PLAT_SYSTEM_ROLE ");
        sql.append("WHERE ROLE_ID IN ").append(PlatStringUtil.getSqlInCondition(roleIds));
        dao.executeSql(sql.toString(), null);
        ;
    }

    /**
     * 根据用户ID获取被授权的角色编码集合
     *
     * @param userId
     * @return
     */
    @Override
    public Set<String> getGrantRoleCodeSet(String userId) {
        List<String> roleCodeList = dao.findGrantRoleCodes(userId);
        Set<String> codeSet = new HashSet<String>();
        if (roleCodeList.size() > 0) {
            codeSet = new HashSet<String>(roleCodeList);
        }
        return codeSet;
    }

    /**
     * 新增或者修改角色信息
     *
     * @param request
     * @param postParams
     * @return
     */
    @Override
    public Map<String, Object> saveOrUpdateRole(HttpServletRequest request,
                                                Map<String, Object> postParams) {
        dao.saveOrUpdate("PLAT_SYSTEM_ROLE",
                postParams, SysConstants.ID_GENERATOR_ASSIGNED, null);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        return result;
    }

    /**
     * 删除角色信息
     *
     * @param request
     * @param postParams
     * @return
     */
    @Override
    public Map<String, Object> deleteRole(HttpServletRequest request,
                                          Map<String, Object> postParams) {
        String ROLE_IDS = (String) postParams.get("ROLE_IDS");
        this.deleteCascadeUserAndRes(ROLE_IDS);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        return result;
    }

    /**
     * 分配用户
     *
     * @param request
     * @param postParams
     * @return
     */
    @Override
    public Map<String, Object> grantUsers(HttpServletRequest request,
                                          Map<String, Object> postParams) {
        String ROLE_ID = request.getParameter("ROLE_ID");
        String GRANTUSER_IDS = request.getParameter("GRANTUSER_IDS");
        this.saveUsers(ROLE_ID, Arrays.asList(GRANTUSER_IDS.split(",")));
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        return result;
    }

    /**
     *
     */
    @Override
    public int getNextRoleSn() {
        int nextRoleSn = 1;
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT MAX(R.ROLE_SN) ROLE_SN FROM PLAT_SYSTEM_ROLE R ");
        Map<String, Object> map = dao.getBySql(sql.toString(), null);
        if (map != null) {
            if (map.get("ROLE_SN") != null) {
                int ROLE_SN = Integer.parseInt(map.get("ROLE_SN").toString());
                nextRoleSn = ROLE_SN + 1;
            }
        }
        return nextRoleSn;
    }

    /**
     *
     */
    @Override
    public void updateSn(String[] roleIds) {
        dao.updateSn(roleIds);

    }

    /**
     * 新增或修改角色信息
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> saveOrUpdateRole(HttpServletRequest request) {
        Map<String, Object> role = PlatBeanUtil.getMapFromRequest(request);
        //获取前端传递过来的字段变更JSON
        String formfieldModifyArrayJson = request.getParameter("formfieldModifyArrayJson");
        //解析成修改字段数组
        List<Map> formfieldModifyArray = JSON.parseArray(formfieldModifyArrayJson, Map.class);
        String ROLE_ID = (String) role.get("ROLE_ID");
        if (StringUtils.isEmpty(ROLE_ID)) {
            int nextRoleSn = this.getNextRoleSn();
            role.put("ROLE_SN", nextRoleSn);
        }
        role = this.saveOrUpdate("PLAT_SYSTEM_ROLE",
                role, SysConstants.ID_GENERATOR_UUID, null);
        if (StringUtils.isNotEmpty(ROLE_ID)) {
            sysLogService.saveBackLog("角色管理", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为[" + ROLE_ID + "]的角色信息", request, formfieldModifyArray, null, null);
        } else {
            ROLE_ID = (String) role.get("ROLE_ID");
            sysLogService.saveBackLog("角色管理", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为[" + ROLE_ID + "]的角色信息", request, formfieldModifyArray, null, null);
        }
        role.put("success", true);
        return role;
    }

    /**
     * 删除角色接口
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> delRole(HttpServletRequest request) {
        String selectColValues = request.getParameter("selectColValues");
        String sql = PlatDbUtil.getDiskSqlContent("system/role/001", null);
        List<List<String>> roleList = dao.findListBySql(sql + PlatStringUtil.getSqlInCondition(selectColValues), null, null);
        this.deleteCascadeUserAndRes(selectColValues);
        sysLogService.saveBackLog("角色管理", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的角色信息", request, null, "角色ID,角色编码,角色名称,角色描述", roleList);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        return result;
    }

    /**
     * 分配对象权限接口
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> grantRights(HttpServletRequest request) {
        String ROLE_ID = request.getParameter("ROLE_ID");
        String resIds = request.getParameter("resIds");
        String groupIds = request.getParameter("groupIds");
        String typedefIds = request.getParameter("typedefIds");
        String tableName = request.getParameter("tableName");
        List<String> resIdArray = new ArrayList<String>();
        if (StringUtils.isNotEmpty(resIds)) {
            resIdArray = Arrays.asList(resIds.split(","));
        }
        List<String> groupIdArray = new ArrayList<String>();
        if (StringUtils.isNotEmpty(groupIds)) {
            groupIdArray = Arrays.asList(groupIds.split(","));
        }
        List<String> typeAndDefIdArray = new ArrayList<String>();
        if (StringUtils.isNotEmpty(typedefIds)) {
            typeAndDefIdArray = Arrays.asList(typedefIds.split(","));
        }
        roleRightService.saveRights(ROLE_ID, resIdArray, groupIdArray, typeAndDefIdArray, tableName);
        String pkName = null;
        String moduleName = "";
        if ("PLAT_SYSTEM_ROLE".equals(tableName)) {
            moduleName = "角色管理";
            pkName = "ROLE_ID";
        } else if ("PLAT_SYSTEM_SYSUSER".equals(tableName)) {
            moduleName = "系统用户管理";
            pkName = "SYSUSER_ID";
        } else if ("PLAT_SYSTEM_USERGROUP".equals(tableName)) {
            moduleName = "用户组管理";
            pkName = "USERGROUP_ID";
        }
        Map<String, Object> busObj = this.getRecord(tableName, new String[]{pkName}, new Object[]{ROLE_ID});
        StringBuffer logContent = new StringBuffer("");
        if ("PLAT_SYSTEM_ROLE".equals(tableName)) {
            logContent.append("对角色[").append(busObj.get("ROLE_NAME")).append("]进行了授权操作");
        } else if ("PLAT_SYSTEM_SYSUSER".equals(tableName)) {
            logContent.append("对用户[").append(busObj.get("SYSUSER_NAME")).append("]进行了授权操作");
        } else if ("PLAT_SYSTEM_USERGROUP".equals(tableName)) {
            logContent.append("对用户组[").append(busObj.get("USERGROUP_NAME")).append("]进行了授权操作");
        }
        sysLogService.saveBackLog(moduleName, SysLogService.OPER_TYPE_GRANTRIGHTS, logContent.toString(), request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        return result;
    }

    /**
     * 分配用户接口
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> grantUsers(HttpServletRequest request) {
        String ROLE_ID = request.getParameter("ROLE_ID");
        String checkUserIds = request.getParameter("checkUserIds");
        this.saveUsers(ROLE_ID, Arrays.asList(checkUserIds.split(",")));
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        return result;
    }
}
