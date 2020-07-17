/*
 * Copyright (c) 2005, 2014, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.service.impl;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.service.impl.BaseServiceImpl;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.util.PlatStringUtil;
import com.housoo.platform.core.service.SysUserService;
import com.housoo.platform.workflow.dao.FlowTypeDao;
import com.housoo.platform.workflow.service.FlowDefService;
import com.housoo.platform.workflow.service.FlowTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 描述 流程类别业务相关service实现类
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-03 16:12:37
 */
@Service("flowTypeService")
public class FlowTypeServiceImpl extends BaseServiceImpl implements FlowTypeService {

    /**
     * 所引入的dao
     */
    @Resource
    private FlowTypeDao dao;
    /**
     *
     */
    @Resource
    private FlowDefService flowDefService;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 删除流程类别级联更新流程定义数据
     *
     * @param typeId
     */
    @Override
    public void deleteCascadeFlowDef(String typeId) {
        dao.deleteRecords("JBPM6_FLOWTYPE", "FLOWTYPE_ID", new String[]{typeId});
        StringBuffer sql = new StringBuffer("UPDATE JBPM6_FLOWDEF ");
        sql.append(" SET FLOWTYPE_ID=null WHERE FLOWTYPE_ID=? ");
        dao.executeSql(sql.toString(), new Object[]{typeId});
    }

    /**
     * 获取可选流程类别下拉框数据源
     *
     * @param paramJson
     * @return
     */
    @Override
    public List<Map<String, Object>> findTypeSelect(String paramJson) {
        StringBuffer sql = new StringBuffer("select T.FLOWTYPE_ID AS VALUE,T.FLOWTYPE_NAME AS LABEL ");
        sql.append("from JBPM6_FLOWTYPE T ");
        //获取当前登录用户
        Map<String, Object> sysUser = PlatAppUtil.getBackPlatLoginUser();
        String grantTypeIds = (String) sysUser.get(SysUserService.FLOWDEFTYPEIDS_KEY);
        sql.append("WHERE T.FLOWTYPE_ID IN ").append(PlatStringUtil.getSqlInCondition(grantTypeIds));
        sql.append(" ORDER BY T.FLOWTYPE_CREATETIME ASC");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(), null, null);
        return list;
    }

    /**
     * 根据当前登录用户ID获取授权的流程
     *
     * @param filter
     * @param fieldInfo
     * @return
     */
    @Override
    public List<Map<String, Object>> getGrantFlowByUserId(SqlFilter filter, Map<String, Object> fieldInfo) {
        String userId = PlatAppUtil.getBackPlatLoginUser().get("SYSUSER_ID").toString();
        List<Map<String, Object>> grantFlowList = new ArrayList<>();
        StringBuffer sql = new StringBuffer(" SELECT D.FLOWDEF_ID,P.FLOWTYPE_NAME,D.FLOWDEF_NAME ");
        sql.append(" FROM JBPM6_FLOWDEF D ");
        sql.append(" LEFT JOIN JBPM6_FLOWTYPE P ON D.FLOWTYPE_ID = P.FLOWTYPE_ID ");
        sql.append(" WHERE D.FLOWTYPE_ID IN ");
        sql.append(" (SELECT FT.FLOWTYPE_ID FROM PLAT_SYSTEM_ROLERIGHT RR ");
        sql.append(" LEFT JOIN PLAT_SYSTEM_SYSUSERROLE UR ");
        sql.append(" ON RR.ROLE_ID = UR.ROLE_ID ");
        sql.append(" RIGHT JOIN JBPM6_FLOWTYPE FT ");
        sql.append(" ON RR.RE_RECORDID = FT.FLOWTYPE_ID ");
        sql.append(" WHERE UR.SYSUSER_ID = ? ) ");

        // 动态参数拼接
        List<Object> params = new ArrayList<Object>();
        params.add(userId);
        String FLOWDEF_NAME = filter.getRequest().getParameter("FLOWDEF_NAME");
        String FLOWTYPE_ID = filter.getRequest().getParameter("FLOWTYPE_ID");
        if (StringUtils.isNotEmpty(FLOWDEF_NAME)) {
            sql.append(" AND D.FLOWDEF_NAME = ? ");
            params.add(FLOWDEF_NAME);
        }
        if (StringUtils.isNotEmpty(FLOWTYPE_ID)) {
            sql.append(" AND D.FLOWTYPE_ID = ? ");
            params.add(FLOWTYPE_ID);
        }
        String exeSql = dao.getQuerySql(filter, sql.toString(), params);
        grantFlowList = dao.findBySql(exeSql, params.toArray(), filter.getPagingBean());
        return grantFlowList;
    }

    /**
     * 获取流程类别组列表数据源
     *
     * @param queryParamJson
     * @return
     */
    @Override
    public List<Map<String, Object>> findGroupList(String queryParamJson) {
        StringBuffer sql = new StringBuffer("select T.FLOWTYPE_ID AS VALUE,T.FLOWTYPE_NAME AS LABEL ");
        sql.append("from JBPM6_FLOWTYPE T ");
        //获取当前登录用户
        /*Map<String,Object> sysUser = PlatAppUtil.getBackPlatLoginUser();
        boolean isAdmin = (boolean) sysUser.get(SysUserService.ISADMIN_KEY);
        if(!isAdmin){
            Set<String> grantGroupIds = (Set<String>) sysUser.get(SysUserService.GROUPIDSET_KEY);
            sql.append("WHERE T.GROUP_ID IN ").append(PlatStringUtil.getSqlInCodition(grantGroupIds));
        }*/
        sql.append(" ORDER BY T.FLOWTYPE_CREATETIME ASC");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(), null, null);
        if (list == null) {
            list = new ArrayList<Map<String, Object>>();
        }
        Map<String, Object> allType = new HashMap<String, Object>();
        allType.put("LABEL", "全部流程类别");
        allType.put("VALUE", "0");
        list.add(0, allType);
        return list;
    }

    /**
     * 根据查询参数获取数据列表
     *
     * @param queryvalue
     * @return
     */
    @Override
    public List<Map<String, Object>> findForSelect(String queryvalue) {
        StringBuffer sql = new StringBuffer("select T.FLOWTYPE_ID AS VALUE,T.FLOWTYPE_NAME AS LABEL ");
        sql.append("from JBPM6_FLOWTYPE T ");
        sql.append(" ORDER BY T.FLOWTYPE_CREATETIME ASC");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(), null, null);
        return list;
    }

    /**
     * 获取流程类别列表
     *
     * @return
     */
    public List<Map<String, Object>> findTypeList() {
        StringBuffer sql = new StringBuffer("select T.FLOWTYPE_ID AS VALUE,T.FLOWTYPE_NAME AS LABEL ");
        sql.append("from JBPM6_FLOWTYPE T ");
        //获取当前登录用户
        Map<String, Object> backLoginUser = PlatAppUtil.getBackPlatLoginUser();
        boolean isAdmin = (boolean) backLoginUser.get(SysUserService.ISADMIN_KEY);
        if (!isAdmin) {
            String grantTypeDefIds = (String) backLoginUser.get(SysUserService.FLOWDEFTYPEIDS_KEY);
            if (StringUtils.isNotEmpty(grantTypeDefIds)) {
                sql.append(" WHERE T.FLOWTYPE_ID IN ");
                sql.append(PlatStringUtil.getSqlInCondition(grantTypeDefIds));
            } else {
                sql.append(" WHERE T.FLOWTYPE_ID='-1' ");
            }
        }
        sql.append(" ORDER BY T.FLOWTYPE_CREATETIME ASC");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(), null, null);
        return list;
    }

    /**
     * 获取流程类别和流程定义树形JSON
     *
     * @param params
     * @return
     */
    @Override
    public String getTypeAndDefJson(Map<String, Object> params) {
        String needCheckIds = (String) params.get("needCheckIds");
        String typeNoCheck = (String) params.get("typeNoCheck");
        Set<String> needCheckIdSet = new HashSet<String>();
        if (StringUtils.isNotEmpty(needCheckIds)) {
            needCheckIdSet = new HashSet<String>(Arrays.asList(needCheckIds.split(",")));
        }
        Map<String, Object> rootNode = new HashMap<String, Object>();
        rootNode.put("id", "0");
        rootNode.put("name", "流程事项树");
        rootNode.put("nocheck", true);
        rootNode.put("open", true);
        List<Map<String, Object>> typeList = this.findTypeList();
        for (Map<String, Object> type : typeList) {
            String typeId = (String) type.get("VALUE");
            String typeName = (String) type.get("LABEL");
            type.put("id", typeId);
            type.put("name", typeName);
            if (StringUtils.isNotEmpty(typeNoCheck) && "true".equals(typeNoCheck)) {
                type.put("nocheck", true);
            } else {
                if (needCheckIdSet.contains(typeId)) {
                    type.put("checked", true);
                }
            }
            List<Map<String, Object>> defList = flowDefService.findByTypeId(typeId);
            if (defList != null && defList.size() > 0) {
                for (Map<String, Object> def : defList) {
                    String defId = (String) def.get("FLOWDEF_ID");
                    if (needCheckIdSet.contains(defId)) {
                        def.put("checked", true);
                    }
                    def.put("id", def.get("FLOWDEF_ID"));
                    def.put("name", def.get("FLOWDEF_NAME"));
                }
                type.put("children", defList);
            }
        }
        rootNode.put("children", typeList);
        return JSON.toJSONString(rootNode);
    }

    /**
     * 获取自动补全的流程类别和定义数据
     *
     * @param filter
     * @return
     */
    @Override
    public List<Map<String, Object>> findAutoTypeDef(SqlFilter filter) {
        StringBuffer sql = new StringBuffer("SELECT R.FLOWTYPE_NAME AS value,R.FLOWTYPE_NAME AS label");
        sql.append(" FROM JBPM6_FLOWTYPE R ");
        //获取当前登录用户
        Map<String, Object> backLoginUser = PlatAppUtil.getBackPlatLoginUser();
        boolean isAdmin = (boolean) backLoginUser.get(SysUserService.ISADMIN_KEY);
        if (!isAdmin) {
            String grantTypeDefIds = (String) backLoginUser.get(SysUserService.FLOWDEFTYPEIDS_KEY);
            if (StringUtils.isNotEmpty(grantTypeDefIds)) {
                sql.append(" WHERE R.FLOWTYPE_ID IN ");
                sql.append(PlatStringUtil.getSqlInCondition(grantTypeDefIds));
            } else {
                sql.append(" WHERE R.FLOWTYPE_ID='-1' ");
            }
        }
        sql.append(" ORDER BY R.FLOWTYPE_CREATETIME DESC");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> typeList = dao.findBySql(sql.toString(), null, null);
        list.addAll(typeList);
        //获取所有定义数据
        sql = new StringBuffer("SELECT R.FLOWDEF_NAME AS value,R.FLOWDEF_NAME AS label");
        sql.append(" FROM JBPM6_FLOWDEF R ");
        if (!isAdmin) {
            String grantTypeDefIds = (String) backLoginUser.get(SysUserService.FLOWDEFTYPEIDS_KEY);
            if (StringUtils.isNotEmpty(grantTypeDefIds)) {
                sql.append(" WHERE R.FLOWDEF_ID IN ");
                sql.append(PlatStringUtil.getSqlInCondition(grantTypeDefIds));
            } else {
                sql.append(" WHERE R.FLOWDEF_ID='-1' ");
            }
        }
        sql.append(" ORDER BY R.FLOWDEF_CREATETIME DESC");
        List<Map<String, Object>> defList = dao.findBySql(sql.toString(), null, null);
        list.addAll(defList);
        return list;
    }

}
