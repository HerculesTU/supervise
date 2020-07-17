package com.housoo.platform.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.dao.SecAuditDao;
import com.housoo.platform.core.service.ResService;
import com.housoo.platform.core.service.SecAuditService;
import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.core.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 描述 安全审核记录业务相关service实现类
 *
 * @author housoo
 * @version 1.0
 * @created 2018-05-13 16:40:58
 */
@Service("secAuditService")
public class SecAuditServiceImpl extends BaseServiceImpl implements SecAuditService {

    /**
     * 所引入的dao
     */
    @Resource
    private SecAuditDao dao;
    /**
     *
     */
    @Resource
    private ResService resService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 保存基本信息
     *
     * @param objId
     * @param delColNames
     * @param delColJson
     * @param postParams
     * @param busType
     * @param objType
     * @return
     */
    private Map<String, Object> saveSecAuditInfo(String objId, String delColNames,
                                                 String delColJson, Map<String, Object> postParams, int busType, int objType,
                                                 String FIELD_JSONLIST, String POST_RESCODE) {
        //获取当前后台登录用户
        Map<String, Object> backLoginUser = PlatAppUtil.getBackPlatLoginUser();
        String userId = (String) backLoginUser.get("SYSUSER_ID");
        postParams.put("DOSYSLOG_USERID", userId);
        Map<String, Object> secAudit = null;
        if (busType == SecAuditService.BUSTYPE_EDIT) {
            //获取旧数据
            secAudit = this.getRecord("PLAT_SYSTEM_SECAUDIT",
                    new String[]{"SECAUDIT_USERID", "SECAUDIT_STATUS",
                            "SECAUDIT_OBJTYPE", "SECAUDIT_OBJID", "SECAUDIT_BUSTYPE"},
                    new Object[]{userId, 0, objType, objId, busType});
        }
        if (busType == SecAuditService.BUSTYPE_GRANTUSER) {
            //获取旧数据
            secAudit = this.getRecord("PLAT_SYSTEM_SECAUDIT",
                    new String[]{"SECAUDIT_USERID", "SECAUDIT_STATUS",
                            "SECAUDIT_OBJTYPE", "SECAUDIT_OBJID", "SECAUDIT_BUSTYPE"},
                    new Object[]{userId, 0, objType, objId, busType});
        }
        if (secAudit == null) {
            secAudit = new HashMap<String, Object>();
            secAudit.put("SECAUDIT_TIME", PlatDateTimeUtil.formatDate(new Date(),
                    "yyyy-MM-dd HH:mm:ss"));
        }
        secAudit.put("SECAUDIT_USERID", userId);
        secAudit.put("SECAUDIT_STATUS", 0);
        secAudit.put("SECAUDIT_BUSTYPE", busType);
        secAudit.put("SECAUDIT_OBJTYPE", objType);
        secAudit.put("SECAUDIT_OBJID", objId);
        if (StringUtils.isNotEmpty(delColNames)) {
            secAudit.put("DEL_COLNAMES", delColNames);
        }
        if (StringUtils.isNotEmpty(delColJson)) {
            secAudit.put("DEL_COLJSON", delColJson);
        }
        if (StringUtils.isNotEmpty(FIELD_JSONLIST)) {
            secAudit.put("FIELD_JSONLIST", FIELD_JSONLIST);
        }
        secAudit.put("POSTPARAM_JSON", JSON.toJSONString(postParams));
        secAudit.put("POST_RESCODE", POST_RESCODE);
        List<String> grantedUserIds = resService.findGrantedUserIds("inchangeauditmanager");
        if (grantedUserIds.size() > 0) {
            secAudit.put("SECAUDIT_ABLEIDS", PlatStringUtil.getListStringSplit(grantedUserIds));
        }
        secAudit = this.saveOrUpdate("PLAT_SYSTEM_SECAUDIT", secAudit,
                SysConstants.ID_GENERATOR_UUID, null);
        return secAudit;
    }


    /**
     * 创建删除部门的安全审核记录
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> saveDeleteDep(HttpServletRequest request) {
        //获取所有提交的参数
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> postParams = PlatBeanUtil.getMapFromRequest(request);
        postParams.remove("PLAT_RESCODE");
        String depId = request.getParameter("treeNodeId");
        Map<String, Object> backLoginUser = PlatAppUtil.getBackPlatLoginUser();
        String userId = (String) backLoginUser.get("SYSUSER_ID");
        Map<String, Object> secAudit = this.getRecord("PLAT_SYSTEM_SECAUDIT",
                new String[]{"SECAUDIT_USERID", "SECAUDIT_STATUS",
                        "SECAUDIT_OBJTYPE", "SECAUDIT_OBJID", "SECAUDIT_BUSTYPE"},
                new Object[]{userId, 0, SecAuditService.BUSOBJ_DEP, depId, SecAuditService.BUSTYPE_DEL});
        if (secAudit != null) {
            result.put("success", false);
            result.put("msg", "您已经提交过该部门删除申请,禁止重复提交!");
        } else {
            String sql = PlatDbUtil.getDiskSqlContent("system/depart/001", null);
            List<List<String>> depList = dao.findListBySql(sql + PlatStringUtil.getSqlInCondition(depId), null, null);
            String delColJson = null;
            if (depList != null && depList.size() > 0) {
                delColJson = JSON.toJSONString(depList);
            }
            this.saveSecAuditInfo(depId, "所属单位,部门表主键值,部门名称,部门编码",
                    delColJson, postParams,
                    SecAuditService.BUSTYPE_DEL, SecAuditService.BUSOBJ_DEP, null, "system_dep02");

            result.put("success", true);
            result.put("msg", "提交成功,需经过安全审核才可生效!");
        }
        return result;
    }

    /**
     * 创建部门或修改
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> saveOrUpdateDep(HttpServletRequest request) {
        //获取所有提交的参数
        Map<String, Object> result = new HashMap<String, Object>();
        //获取变更的JSON字符串
        String formfieldModifyArrayJson = request.getParameter("formfieldModifyArrayJson");
        //解析成修改字段数组
        List<Map> formfieldModifyArray = JSON.parseArray(formfieldModifyArrayJson, Map.class);
        Map<String, Object> postParams = PlatBeanUtil.getMapFromRequest(request);
        postParams.remove("PLAT_RESCODE");
        String DEPART_ID = request.getParameter("DEPART_ID");
        int busType = 0;
        String objId = null;
        if (StringUtils.isNotEmpty(DEPART_ID)) {
            busType = SecAuditService.BUSTYPE_EDIT;
            objId = DEPART_ID;
        } else {
            busType = SecAuditService.BUSTYPE_ADD;
        }
        List<Map<String, Object>> fieldList = sysLogService.getFieldList(
                formfieldModifyArray, busType);
        String FIELD_JSONLIST = JSON.toJSONString(fieldList);
        this.saveSecAuditInfo(objId, null, null, postParams,
                busType, SecAuditService.BUSOBJ_DEP, FIELD_JSONLIST, "system_dep01");
        //获取部门名称
        result.put("success", true);
        result.put("msg", "提交成功,需经过安全审核才可生效!");
        return result;
    }


    /**
     * 获取状态信息列表
     *
     * @param param
     * @return
     */
    @Override
    public List<Map<String, Object>> findStatusList(String param) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> allType = new HashMap<String, Object>();
        allType.put("GROUP_FONT", "fa fa-home");
        allType.put("LABEL", "全部审批结果");
        allType.put("VALUE", "-2");
        list.add(allType);
        int count = this.getCountByStatus(0);
        Map<String, Object> data1 = new HashMap<String, Object>();
        data1.put("LABEL", "未审核<font color='red'><b>(" + count + ")</b></font>");
        data1.put("VALUE", "0");
        data1.put("GROUP_FONT", "fa fa-asterisk");
        Map<String, Object> data2 = new HashMap<String, Object>();
        data2.put("LABEL", "审核通过");
        data2.put("VALUE", "1");
        data2.put("GROUP_FONT", "fa fa-asterisk");
        Map<String, Object> data3 = new HashMap<String, Object>();
        data3.put("LABEL", "审核不通过");
        data3.put("VALUE", "-1");
        data3.put("GROUP_FONT", "fa fa-asterisk");
        list.add(data1);
        list.add(data2);
        list.add(data3);
        return list;
    }

    /**
     * 根据审批状态获取数量
     *
     * @param auditStatus
     * @return
     */
    @Override
    public int getCountByStatus(int auditStatus) {
        String sql = PlatDbUtil.getDiskSqlContent("system/secaudit/001", null);
        int count = dao.getIntBySql(sql, new Object[]{auditStatus});
        return count;
    }

    /**
     * 删除用户
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> saveDeleteUser(HttpServletRequest request) {
        //获取所有提交的参数
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> postParams = PlatBeanUtil.getMapFromRequest(request);
        postParams.remove("PLAT_RESCODE");
        String deleteUserIds = request.getParameter("selectColValues");
        String sql = PlatDbUtil.getDiskSqlContent("system/sysuser/001", null);
        List<List<String>> userList = dao.findListBySql(sql + PlatStringUtil.getSqlInCondition(deleteUserIds), null, null);
        String delColJson = null;
        if (userList != null && userList.size() > 0) {
            delColJson = JSON.toJSONString(userList);
        }
        this.saveSecAuditInfo(deleteUserIds, "用户表主键值,用户账号,用户姓名,手机号,所在单位,所在部门",
                delColJson, postParams,
                SecAuditService.BUSTYPE_DEL, SecAuditService.BUSOBJ_USER, null, "system_sysuser_del");

        result.put("success", true);
        result.put("msg", "提交成功,需经过安全审核才可生效!");
        return result;
    }

    /**
     * 新增或者修改用户
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> saveOrUpdateUser(HttpServletRequest request) {
        //获取所有提交的参数
        Map<String, Object> result = new HashMap<String, Object>();
        //获取变更的JSON字符串
        String formfieldModifyArrayJson = request.getParameter("formfieldModifyArrayJson");
        //解析成修改字段数组
        List<Map> formfieldModifyArray = JSON.parseArray(formfieldModifyArrayJson, Map.class);
        Map<String, Object> postParams = PlatBeanUtil.getMapFromRequest(request);
        postParams.remove("PLAT_RESCODE");
        String SYSUSER_ID = request.getParameter("SYSUSER_ID");
        int busType = 0;
        String objId = null;
        if (StringUtils.isNotEmpty(SYSUSER_ID)) {
            busType = SecAuditService.BUSTYPE_EDIT;
            objId = SYSUSER_ID;
        } else {
            busType = SecAuditService.BUSTYPE_ADD;
        }
        List<Map<String, Object>> fieldList = sysLogService.getFieldList(
                formfieldModifyArray, busType);
        String FIELD_JSONLIST = JSON.toJSONString(fieldList);
        this.saveSecAuditInfo(objId, null, null, postParams,
                busType, SecAuditService.BUSOBJ_USER, FIELD_JSONLIST,
                "system_sysuser_addorupdate");
        //获取部门名称
        result.put("success", true);
        result.put("msg", "提交成功,需经过安全审核才可生效!");
        return result;
    }

    /**
     * 授权对象权限
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> grantsRightRole(HttpServletRequest request) {
        //获取所有提交的参数
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> postParams = PlatBeanUtil.getMapFromRequest(request);
        postParams.remove("PLAT_RESCODE");
        //获取对象ID
        String objId = request.getParameter("ROLE_ID");
        String tableName = request.getParameter("tableName");
        int objType = 0;
        Map<String, Object> busObj = null;
        String pkName = null;
        if ("PLAT_SYSTEM_ROLE".equals(tableName)) {
            objType = SecAuditService.BUSOBJ_ROLE;
            pkName = "ROLE_ID";
        } else if ("PLAT_SYSTEM_SYSUSER".equals(tableName)) {
            objType = SecAuditService.BUSOBJ_USER;
            pkName = "SYSUSER_ID";
        } else if ("PLAT_SYSTEM_USERGROUP".equals(tableName)) {
            objType = SecAuditService.BUSOBJ_USERGROUP;
            pkName = "USERGROUP_ID";
        }
        busObj = this.getRecord(tableName, new String[]{pkName}, new Object[]{objId});
        List<Map<String, Object>> fieldList = new ArrayList<Map<String, Object>>();
        if (objType == SecAuditService.BUSOBJ_ROLE) {
            Map<String, Object> f1 = this.getGrantFieldInfo("角色名称", busObj.get("ROLE_NAME"));
            Map<String, Object> f2 = this.getGrantFieldInfo("角色编码", busObj.get("ROLE_CODE"));
            Map<String, Object> f3 = this.getGrantFieldInfo("角色描述", busObj.get("ROLE_DESC"));
            fieldList.add(f1);
            fieldList.add(f2);
            fieldList.add(f3);
        } else if (objType == SecAuditService.BUSOBJ_USER) {
            Map<String, Object> f1 = this.getGrantFieldInfo("用户账号", busObj.get("SYSUSER_ACCOUNT"));
            Map<String, Object> f2 = this.getGrantFieldInfo("用户姓名", busObj.get("SYSUSER_NAME"));
            fieldList.add(f1);
            fieldList.add(f2);
        } else if (objType == SecAuditService.BUSOBJ_USERGROUP) {
            Map<String, Object> f1 = this.getGrantFieldInfo("用户组名称", busObj.get("USERGROUP_NAME"));
            fieldList.add(f1);
        }
        String FIELD_JSONLIST = JSON.toJSONString(fieldList);
        this.saveSecAuditInfo(objId, null, null, postParams,
                SecAuditService.BUSTYPE_GRANT, objType, FIELD_JSONLIST, "system_role_grantright");
        //获取部门名称
        result.put("success", true);
        result.put("msg", "提交成功,需经过安全审核才可生效!");
        return result;
    }

    /**
     * @param cnName
     * @param fieldValue
     * @return
     */
    private Map<String, Object> getGrantFieldInfo(String cnName, Object fieldValue) {
        Map<String, Object> f1 = new HashMap<String, Object>();
        f1.put("SECFIELD_VAL", fieldValue);
        f1.put("SECFIELD_UPVAL", fieldValue);
        f1.put("SECFIELD_CN", cnName);
        f1.put("SECFIELD_ISUPADE", "-1");
        f1.put("SECFIELD_TYPE", "1");
        return f1;
    }

    /**
     * 创建单位的安全审核记录
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> saveDelCompany(HttpServletRequest request) {
        //获取所有提交的参数
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> postParams = PlatBeanUtil.getMapFromRequest(request);
        postParams.remove("PLAT_RESCODE");
        String companyId = request.getParameter("treeNodeId");
        Map<String, Object> backLoginUser = PlatAppUtil.getBackPlatLoginUser();
        String userId = (String) backLoginUser.get("SYSUSER_ID");
        Map<String, Object> secAudit = this.getRecord("PLAT_SYSTEM_SECAUDIT",
                new String[]{"SECAUDIT_USERID", "SECAUDIT_STATUS",
                        "SECAUDIT_OBJTYPE", "SECAUDIT_OBJID", "SECAUDIT_BUSTYPE"},
                new Object[]{userId, 0, SecAuditService.BUSOBJ_COMPANY, companyId, SecAuditService.BUSTYPE_DEL});
        if (secAudit != null) {
            result.put("success", false);
            result.put("msg", "您已经提交过该单位删除申请,禁止重复提交!");
        } else {
            String sql = PlatDbUtil.getDiskSqlContent("system/company/001", null);
            List<List<String>> comList = dao.findListBySql(sql + PlatStringUtil.getSqlInCondition(companyId), null, null);
            String delColJson = null;
            if (comList != null && comList.size() > 0) {
                delColJson = JSON.toJSONString(comList);
            }
            this.saveSecAuditInfo(companyId, "单位表主键值,单位名称",
                    delColJson, postParams,
                    SecAuditService.BUSTYPE_DEL, SecAuditService.BUSOBJ_COMPANY, null, "system_company_del");

            result.put("success", true);
            result.put("msg", "提交成功,需经过安全审核才可生效!");
        }
        return result;
    }

    /**
     * 创建或者修改单位
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> saveOrUpdateCompany(HttpServletRequest request) {
        //获取所有提交的参数
        Map<String, Object> result = new HashMap<String, Object>();
        //获取变更的JSON字符串
        String formfieldModifyArrayJson = request.getParameter("formfieldModifyArrayJson");
        //解析成修改字段数组
        List<Map> formfieldModifyArray = JSON.parseArray(formfieldModifyArrayJson, Map.class);
        Map<String, Object> postParams = PlatBeanUtil.getMapFromRequest(request);
        postParams.remove("PLAT_RESCODE");
        String COMPANY_ID = request.getParameter("COMPANY_ID");
        int busType = 0;
        String objId = null;
        if (StringUtils.isNotEmpty(COMPANY_ID)) {
            busType = SecAuditService.BUSTYPE_EDIT;
            objId = COMPANY_ID;
        } else {
            busType = SecAuditService.BUSTYPE_ADD;
        }
        List<Map<String, Object>> fieldList = sysLogService.getFieldList(
                formfieldModifyArray, busType);
        String FIELD_JSONLIST = JSON.toJSONString(fieldList);
        this.saveSecAuditInfo(objId, null, null, postParams,
                busType, SecAuditService.BUSOBJ_COMPANY, FIELD_JSONLIST,
                "system_company_saveupdate");
        //获取部门名称
        result.put("success", true);
        result.put("msg", "提交成功,需经过安全审核才可生效!");
        return result;
    }

    /**
     * 创建部门的分配用户审核操作
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> saveGrantUserToDep(HttpServletRequest request) {
        //获取所有提交的参数
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> postParams = PlatBeanUtil.getMapFromRequest(request);
        postParams.remove("PLAT_RESCODE");
        String depId = request.getParameter("DEPART_ID");
        //获取部门信息
        Map<String, Object> depart = this.getRecord("PLAT_SYSTEM_DEPART",
                new String[]{"DEPART_ID"}, new Object[]{depId});
        String departName = (String) depart.get("DEPART_NAME");
        String checkUserIds = request.getParameter("checkUserIds");
        String sql = "SELECT '" + departName + "',";
        sql = sql + PlatDbUtil.getDiskSqlContent("system/depart/002", null);
        List<List<String>> userList = dao.findListBySql(sql + PlatStringUtil.getSqlInCondition(checkUserIds), null, null);
        String delColJson = null;
        if (userList != null && userList.size() > 0) {
            delColJson = JSON.toJSONString(userList);
        }
        this.saveSecAuditInfo(depId, "所分配部门,用户账号,用户姓名,用户手机号,所处单位",
                delColJson, postParams,
                SecAuditService.BUSTYPE_GRANTUSER, SecAuditService.BUSOBJ_DEP, null, "system_dep_grantusers");

        result.put("success", true);
        result.put("msg", "提交成功,需经过安全审核才可生效!");
        return result;
    }

    /**
     * 创建角色的分配用户审核操作
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> saveGrantUserToRole(HttpServletRequest request) {
        //获取所有提交的参数
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> postParams = PlatBeanUtil.getMapFromRequest(request);
        postParams.remove("PLAT_RESCODE");
        String ROLE_ID = request.getParameter("ROLE_ID");
        //获取角色信息
        Map<String, Object> role = this.getRecord("PLAT_SYSTEM_ROLE",
                new String[]{"ROLE_ID"}, new Object[]{ROLE_ID});
        String roleName = (String) role.get("ROLE_NAME");
        String checkUserIds = request.getParameter("checkUserIds");
        String sql = "SELECT '" + roleName + "',";
        sql = sql + PlatDbUtil.getDiskSqlContent("system/depart/002", null);
        List<List<String>> userList = dao.findListBySql(sql + PlatStringUtil.getSqlInCondition(checkUserIds), null, null);
        String delColJson = null;
        if (userList != null && userList.size() > 0) {
            delColJson = JSON.toJSONString(userList);
        }
        this.saveSecAuditInfo(ROLE_ID, "所分配角色,用户账号,用户姓名,用户手机号,所处单位",
                delColJson, postParams,
                SecAuditService.BUSTYPE_GRANTUSER, SecAuditService.BUSOBJ_ROLE, null, "system_role_grantuser");

        result.put("success", true);
        result.put("msg", "提交成功,需经过安全审核才可生效!");
        return result;
    }

    /**
     * 创建用户组的分配用户审核操作
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> saveGrantUserToGroup(HttpServletRequest request) {
        //获取所有提交的参数
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> postParams = PlatBeanUtil.getMapFromRequest(request);
        postParams.remove("PLAT_RESCODE");
        String USERGROUP_ID = request.getParameter("USERGROUP_ID");
        //获取角色信息
        Map<String, Object> group = this.getRecord("PLAT_SYSTEM_USERGROUP",
                new String[]{"USERGROUP_ID"}, new Object[]{USERGROUP_ID});
        String USERGROUP_NAME = (String) group.get("USERGROUP_NAME");
        String checkUserIds = request.getParameter("checkUserIds");
        String sql = "SELECT '" + USERGROUP_NAME + "',";
        sql = sql + PlatDbUtil.getDiskSqlContent("system/depart/002", null);
        List<List<String>> userList = dao.findListBySql(sql + PlatStringUtil.getSqlInCondition(checkUserIds), null, null);
        String delColJson = null;
        if (userList != null && userList.size() > 0) {
            delColJson = JSON.toJSONString(userList);
        }
        this.saveSecAuditInfo(USERGROUP_ID, "所分配用户组,用户账号,用户姓名,用户手机号,所处单位",
                delColJson, postParams,
                SecAuditService.BUSTYPE_GRANTUSER, SecAuditService.BUSOBJ_USERGROUP, null, "system_usergroup_grantuser");
        result.put("success", true);
        result.put("msg", "提交成功,需经过安全审核才可生效!");
        return result;
    }

    /**
     * 创建岗位的分配用户审核操作
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> saveGrantUserToPos(HttpServletRequest request) {
        //获取所有提交的参数
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> postParams = PlatBeanUtil.getMapFromRequest(request);
        postParams.remove("PLAT_RESCODE");
        String POSITION_ID = request.getParameter("POSITION_ID");
        //获取角色信息
        Map<String, Object> pos = this.getRecord("PLAT_SYSTEM_POSITION",
                new String[]{"POSITION_ID"}, new Object[]{POSITION_ID});
        String POSITION_NAME = (String) pos.get("POSITION_NAME");
        String checkUserIds = request.getParameter("checkUserIds");
        String sql = "SELECT '" + POSITION_NAME + "',";
        sql = sql + PlatDbUtil.getDiskSqlContent("system/depart/002", null);
        List<List<String>> userList = dao.findListBySql(sql + PlatStringUtil.getSqlInCondition(checkUserIds), null, null);
        String delColJson = null;
        if (userList != null && userList.size() > 0) {
            delColJson = JSON.toJSONString(userList);
        }
        this.saveSecAuditInfo(POSITION_ID, "所分配岗位,用户账号,用户姓名,用户手机号,所处单位",
                delColJson, postParams,
                SecAuditService.BUSTYPE_GRANTUSER, SecAuditService.BUSOBJ_POS, null, "system_pos_grantuser");
        result.put("success", true);
        result.put("msg", "提交成功,需经过安全审核才可生效!");
        return result;
    }

    /**
     * 创建删除日志的安全审核记录
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> saveDeleteLog(HttpServletRequest request) {
        //获取所有提交的参数
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> postParams = PlatBeanUtil.getMapFromRequest(request);
        postParams.remove("PLAT_RESCODE");
        String selectLogIds = request.getParameter("selectColValues");
        String sql = PlatDbUtil.getDiskSqlContent("system/syslog/001", null);
        List<List<String>> logList = dao.findListBySql(sql + PlatStringUtil.getSqlInCondition(selectLogIds), null, null);
        String delColJson = null;
        if (logList != null && logList.size() > 0) {
            delColJson = JSON.toJSONString(logList);
        }
        this.saveSecAuditInfo(selectLogIds, "日志主键,浏览器,操作时间,用户姓名,用户账号,IP地址,模块名称,操作类型",
                delColJson, postParams,
                SecAuditService.BUSTYPE_DEL, SecAuditService.BUSOBJ_SYSLOG, null, "system_syslog_del");

        result.put("success", true);
        result.put("msg", "提交成功,需经过安全审核才可生效!");
        return result;
    }

    /**
     * 获取审核操作明细
     *
     * @param secId
     * @return
     */
    @Override
    public Map<String, Object> getSecAuditInfo(String secId) {
        String sql = PlatDbUtil.getDiskSqlContent("system/secaudit/002", null);
        Map<String, Object> info = this.getBySql(sql, new Object[]{secId});
        return info;
    }

    /**
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> saveDeleteRole(HttpServletRequest request) {
        //获取所有提交的参数
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> postParams = PlatBeanUtil.getMapFromRequest(request);
        postParams.remove("PLAT_RESCODE");
        String selectColValues = request.getParameter("selectColValues");
        String sql = PlatDbUtil.getDiskSqlContent("system/role/002", null);
        List<List<String>> dataList = dao.findListBySql(sql + PlatStringUtil.getSqlInCondition(selectColValues), null, null);
        String delColJson = null;
        if (dataList != null && dataList.size() > 0) {
            delColJson = JSON.toJSONString(dataList);
        }
        this.saveSecAuditInfo(selectColValues, "角色主键,角色名称,角色编码,角色描述",
                delColJson, postParams,
                SecAuditService.BUSTYPE_DEL, SecAuditService.BUSOBJ_ROLE, null, "system_role_del");

        result.put("success", true);
        result.put("msg", "提交成功,需经过安全审核才可生效!");
        return result;
    }

    /**
     * 创建或者修改角色
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> saveOrUpdateRole(HttpServletRequest request) {
        //获取所有提交的参数
        Map<String, Object> result = new HashMap<String, Object>();
        //获取变更的JSON字符串
        String formfieldModifyArrayJson = request.getParameter("formfieldModifyArrayJson");
        //解析成修改字段数组
        List<Map> formfieldModifyArray = JSON.parseArray(formfieldModifyArrayJson, Map.class);
        Map<String, Object> postParams = PlatBeanUtil.getMapFromRequest(request);
        postParams.remove("PLAT_RESCODE");
        String ROLE_ID = request.getParameter("ROLE_ID");
        int busType = 0;
        String objId = null;
        if (StringUtils.isNotEmpty(ROLE_ID)) {
            busType = SecAuditService.BUSTYPE_EDIT;
            objId = ROLE_ID;
        } else {
            busType = SecAuditService.BUSTYPE_ADD;
        }
        List<Map<String, Object>> fieldList = sysLogService.getFieldList(
                formfieldModifyArray, busType);
        String FIELD_JSONLIST = JSON.toJSONString(fieldList);
        this.saveSecAuditInfo(objId, null, null, postParams,
                busType, SecAuditService.BUSOBJ_ROLE, FIELD_JSONLIST,
                "system_role_addupdate");
        //获取部门名称
        result.put("success", true);
        result.put("msg", "提交成功,需经过安全审核才可生效!");
        return result;
    }

    /**
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> saveDeleteGroup(HttpServletRequest request) {
        //获取所有提交的参数
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> postParams = PlatBeanUtil.getMapFromRequest(request);
        postParams.remove("PLAT_RESCODE");
        String selectColValues = request.getParameter("selectColValues");
        String sql = PlatDbUtil.getDiskSqlContent("system/usergroup/001", null);
        List<List<String>> dataList = dao.findListBySql(sql + PlatStringUtil.getSqlInCondition(selectColValues), null, null);
        String delColJson = null;
        if (dataList != null && dataList.size() > 0) {
            delColJson = JSON.toJSONString(dataList);
        }
        this.saveSecAuditInfo(selectColValues, "用户组主键,用户组名称",
                delColJson, postParams,
                SecAuditService.BUSTYPE_DEL, SecAuditService.BUSOBJ_USERGROUP, null, "system_usergroup_delete");

        result.put("success", true);
        result.put("msg", "提交成功,需经过安全审核才可生效!");
        return result;
    }

    /**
     * 创建或者修改用户组
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> saveOrUpdateGroup(HttpServletRequest request) {
        //获取所有提交的参数
        Map<String, Object> result = new HashMap<String, Object>();
        //获取变更的JSON字符串
        String formfieldModifyArrayJson = request.getParameter("formfieldModifyArrayJson");
        //解析成修改字段数组
        List<Map> formfieldModifyArray = JSON.parseArray(formfieldModifyArrayJson, Map.class);
        Map<String, Object> postParams = PlatBeanUtil.getMapFromRequest(request);
        postParams.remove("PLAT_RESCODE");
        String USERGROUP_ID = request.getParameter("USERGROUP_ID");
        int busType = 0;
        String objId = null;
        if (StringUtils.isNotEmpty(USERGROUP_ID)) {
            busType = SecAuditService.BUSTYPE_EDIT;
            objId = USERGROUP_ID;
        } else {
            busType = SecAuditService.BUSTYPE_ADD;
        }
        List<Map<String, Object>> fieldList = sysLogService.getFieldList(
                formfieldModifyArray, busType);
        String FIELD_JSONLIST = JSON.toJSONString(fieldList);
        this.saveSecAuditInfo(objId, null, null, postParams,
                busType, SecAuditService.BUSOBJ_USERGROUP, FIELD_JSONLIST,
                "system_usergroup_saveupdate");
        //获取部门名称
        result.put("success", true);
        result.put("msg", "提交成功,需经过安全审核才可生效!");
        return result;
    }

    /**
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> saveDeletePos(HttpServletRequest request) {
        //获取所有提交的参数
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> postParams = PlatBeanUtil.getMapFromRequest(request);
        postParams.remove("PLAT_RESCODE");
        String selectColValues = request.getParameter("selectColValues");
        String sql = PlatDbUtil.getDiskSqlContent("system/position/001", null);
        List<List<String>> dataList = dao.findListBySql(sql + PlatStringUtil.getSqlInCondition(selectColValues), null, null);
        String delColJson = null;
        if (dataList != null && dataList.size() > 0) {
            delColJson = JSON.toJSONString(dataList);
        }
        this.saveSecAuditInfo(selectColValues, "岗位主键ID,岗位名称,岗位描述",
                delColJson, postParams,
                SecAuditService.BUSTYPE_DEL, SecAuditService.BUSOBJ_POS, null, "system_pos_del");

        result.put("success", true);
        result.put("msg", "提交成功,需经过安全审核才可生效!");
        return result;
    }

    /**
     * 创建或者修改用户组
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> saveOrUpdatePos(HttpServletRequest request) {
        //获取所有提交的参数
        Map<String, Object> result = new HashMap<String, Object>();
        //获取变更的JSON字符串
        String formfieldModifyArrayJson = request.getParameter("formfieldModifyArrayJson");
        //解析成修改字段数组
        List<Map> formfieldModifyArray = JSON.parseArray(formfieldModifyArrayJson, Map.class);
        Map<String, Object> postParams = PlatBeanUtil.getMapFromRequest(request);
        postParams.remove("PLAT_RESCODE");
        String POSITION_ID = request.getParameter("POSITION_ID");
        int busType = 0;
        String objId = null;
        if (StringUtils.isNotEmpty(POSITION_ID)) {
            busType = SecAuditService.BUSTYPE_EDIT;
            objId = POSITION_ID;
        } else {
            busType = SecAuditService.BUSTYPE_ADD;
        }
        List<Map<String, Object>> fieldList = sysLogService.getFieldList(
                formfieldModifyArray, busType);
        String FIELD_JSONLIST = JSON.toJSONString(fieldList);
        this.saveSecAuditInfo(objId, null, null, postParams,
                busType, SecAuditService.BUSOBJ_POS, FIELD_JSONLIST,
                "system_pos_save");
        //获取部门名称
        result.put("success", true);
        result.put("msg", "提交成功,需经过安全审核才可生效!");
        return result;
    }

    /**
     * 创建或者修改用户分管部门
     *
     * @param request
     * @return
     */
    public Map<String, Object> saveOrUpdateUserDepart(HttpServletRequest request) {
        //获取所有提交的参数
        Map<String, Object> result = new HashMap<String, Object>();
        //获取变更的JSON字符串
        String formfieldModifyArrayJson = request.getParameter("formfieldModifyArrayJson");
        //解析成修改字段数组
        List<Map> formfieldModifyArray = JSON.parseArray(formfieldModifyArrayJson, Map.class);
        Map<String, Object> postParams = PlatBeanUtil.getMapFromRequest(request);
        postParams.remove("PLAT_RESCODE");
        String userId = request.getParameter("SYSUSER_ID");
        String checkDepartIds = request.getParameter("checkUserIds");
        String checkDepartNames = "";
        if (StringUtils.isNotEmpty(checkDepartIds)) {
            for (String deptId : checkDepartIds.split(",")) {
                checkDepartNames += dao.getRecord("plat_system_depart", new String[]{"DEPART_ID"}, new Object[]{deptId}).get("DEPART_NAME") + ",";
            }
        }
        if (formfieldModifyArray == null) {
            formfieldModifyArray = new ArrayList<>();
        }
        boolean editFlag = false;
        int busType = 0;
        Map map = new HashMap();
        //获取用户原分管部门信息
        List<Map<String, Object>> userDeparts = new ArrayList<>();
        StringBuffer sql = new StringBuffer(" SELECT U.SYSUSER_ID,SU.SYSUSER_NAME,GROUP_CONCAT(D.DEPART_NAME) DEPART_NAME,GROUP_CONCAT(D.DEPART_ID) DEPART_ID ");
        sql.append(" FROM TB_USER_DEPART U ");
        sql.append(" LEFT JOIN plat_system_depart D ON U.DEPART_ID = D.DEPART_ID");
        sql.append(" LEFT JOIN plat_system_sysuser SU ON U.SYSUSER_ID = SU.SYSUSER_ID");
        sql.append(" WHERE U.SYSUSER_ID =? GROUP BY U.SYSUSER_ID");
        userDeparts = this.dao.findBySql(sql.toString(), new Object[]{userId}, null);
        if (userDeparts != null) {
            map.put("FIELD_CN", "用户ID");
            map.put("FIELD_EN", "SYSUSER_ID");
            map.put("FIELD_NEWVALUE", userId);
            map.put("FIELD_OLDVALUE", userId);
            formfieldModifyArray.add(map);
            map = new HashMap();
            map.put("FIELD_CN", "姓名");
            map.put("FIELD_EN", "SYSUSER_NAME");
            map.put("FIELD_NEWVALUE", userDeparts.get(0).get("SYSUSER_NAME"));
            map.put("FIELD_OLDVALUE", userDeparts.get(0).get("SYSUSER_NAME"));
            formfieldModifyArray.add(map);
            map = new HashMap();
            map.put("FIELD_CN", "部门");
            map.put("FIELD_EN", "DEPART_NAME");
            map.put("FIELD_NEWVALUE", checkDepartNames);
            map.put("FIELD_OLDVALUE", userDeparts.get(0).get("DEPART_NAME"));
            formfieldModifyArray.add(map);
            if (!checkDepartIds.equals(userDeparts.get(0).get("DEPART_ID"))) {
                editFlag = true;
            }
        }
        String objId = null;
        if (StringUtils.isNotEmpty(checkDepartIds)) {
            if (editFlag) {
                busType = SecAuditService.BUSTYPE_EDIT;
            } else {
                busType = SecAuditService.BUSTYPE_ADD;
            }
            objId = checkDepartIds;
        } else {
            busType = SecAuditService.BUSTYPE_DEL;
        }
        List<Map<String, Object>> fieldList = new ArrayList<>();
        if (formfieldModifyArray != null) {
            fieldList = sysLogService.getFieldList(
                    formfieldModifyArray, busType);
        }
        String FIELD_JSONLIST = JSON.toJSONString(fieldList);
        this.saveSecAuditInfo(objId, null, null, postParams,
                busType, SecAuditService.BUSOBJ_USERDEPART, FIELD_JSONLIST,
                "system_sysuser_setuserdepts1");
        //获取部门名称
        result.put("success", true);
        result.put("msg", "提交成功,需经过安全审核才可生效!");
        return result;
    }
}
