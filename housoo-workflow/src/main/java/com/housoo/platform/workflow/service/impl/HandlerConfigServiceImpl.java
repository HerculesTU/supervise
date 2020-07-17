/*
 * Copyright (c) 2005, 2014, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.model.JbpmFlowInfo;
import com.housoo.platform.core.service.PositionService;
import com.housoo.platform.core.service.impl.BaseServiceImpl;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.util.PlatStringUtil;
import com.housoo.platform.core.service.SysUserService;
import com.housoo.platform.workflow.dao.HandlerConfigDao;
import com.housoo.platform.workflow.model.FlowNextHandler;
import com.housoo.platform.workflow.model.NodeAssigner;
import com.housoo.platform.workflow.service.HandlerConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 描述 办理人业务相关service实现类
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-10 15:50:11
 */
@Service("handlerConfigService")
public class HandlerConfigServiceImpl extends BaseServiceImpl implements HandlerConfigService {

    /**
     * 所引入的dao
     */
    @Resource
    private HandlerConfigDao dao;
    /**
     * 系统用户Service
     */
    @Resource
    private SysUserService sysUserService;
    /**
     * 岗位service
     */
    @Resource
    private PositionService positionService;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 获取指定岗位审核人
     *
     * @param flowVars
     * @param nodeAssigner
     * @param jbpmFlowInfo
     * @return
     */
    @Override
    public List<FlowNextHandler> getAssignPositionHandlers(Map<String, Object> flowVars,
                                                           NodeAssigner nodeAssigner, JbpmFlowInfo jbpmFlowInfo) {
        StringBuffer sql = new StringBuffer(positionService.getUserInfoPreSql());
        sql.append(" AND T.SYSUSER_ID IN (");
        sql.append("SELECT UP.SYSUSER_ID FROM PLAT_SYSTEM_SYSUSERPOS UP ");
        sql.append("WHERE UP.POSITION_ID IN ").append(PlatStringUtil.getValueArray(nodeAssigner.getVarValues()));
        sql.append(" )");
        List<Object> params = new ArrayList<Object>();
        params.add(SysUserService.SYSUSER_STATUS_DEL);
        String filterRule = nodeAssigner.getFilterRule();
        if (StringUtils.isNotEmpty(filterRule) && filterRule.equals(NodeAssigner.FILTERRULE_SAMECOMPANY)) {
            //获取当前登录用户
            Map<String, Object> backLoginUser = PlatAppUtil.getBackPlatLoginUser();
            String SYSUSER_COMPANYID = (String) backLoginUser.get("SYSUSER_COMPANYID");
            sql.append(" AND T.SYSUSER_COMPANYID=?");
            params.add(SYSUSER_COMPANYID);
        }
        sql.append(" ORDER BY T.SYSUSER_CREATETIME DESC");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(), params.toArray(), null);
        List<FlowNextHandler> handerList = new ArrayList<FlowNextHandler>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> sysUser = list.get(i);
            String SYSUSER_ID = (String) sysUser.get("SYSUSER_ID");
            String SYSUSER_NAME = (String) sysUser.get("SYSUSER_NAME");
            int orderSn = i + 1;
            FlowNextHandler nextHandler = new FlowNextHandler();
            nextHandler.setAssignerId(SYSUSER_ID);
            nextHandler.setAssignerName(SYSUSER_NAME);
            if ("1".equals(nodeAssigner.getIsOrder())) {
                nextHandler.setTaskOrder(orderSn);
            } else {
                nextHandler.setTaskOrder(-1);
            }
            handerList.add(nextHandler);
        }
        return handerList.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 获取指定角色审核人
     *
     * @param flowVars
     * @param nodeAssigner
     * @param jbpmFlowInfo
     * @return
     */
    @Override
    public List<FlowNextHandler> getAssignRoleHandlers(Map<String, Object> flowVars,
                                                       NodeAssigner nodeAssigner, JbpmFlowInfo jbpmFlowInfo) {
        StringBuffer sql = new StringBuffer(sysUserService.getUserInfoPreSql());
        sql.append(" AND T.SYSUSER_ID IN (");
        sql.append("SELECT UR.SYSUSER_ID FROM  PLAT_SYSTEM_SYSUSERROLE UR ");
        sql.append("WHERE UR.ROLE_ID IN ").append(PlatStringUtil.getValueArray(nodeAssigner.getVarValues()));
        sql.append(" )");
        List<Object> params = new ArrayList<Object>();
        params.add(SysUserService.SYSUSER_STATUS_DEL);
        String filterRule = nodeAssigner.getFilterRule();
        if (StringUtils.isNotEmpty(filterRule) && filterRule.equals(NodeAssigner.FILTERRULE_SAMECOMPANY)) {
            //获取当前登录用户
            Map<String, Object> backLoginUser = PlatAppUtil.getBackPlatLoginUser();
            String SYSUSER_COMPANYID = (String) backLoginUser.get("SYSUSER_COMPANYID");
            sql.append(" AND T.SYSUSER_COMPANYID=?");
            params.add(SYSUSER_COMPANYID);
        }
        sql.append(" ORDER BY T.SYSUSER_CREATETIME DESC");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(), params.toArray(), null);
        List<FlowNextHandler> handerList = new ArrayList<FlowNextHandler>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> sysUser = list.get(i);
            String SYSUSER_ID = (String) sysUser.get("SYSUSER_ID");
            String SYSUSER_NAME = (String) sysUser.get("SYSUSER_NAME");
            int orderSn = i + 1;
            FlowNextHandler nextHandler = new FlowNextHandler();
            nextHandler.setAssignerId(SYSUSER_ID);
            nextHandler.setAssignerName(SYSUSER_NAME);
            if ("1".equals(nodeAssigner.getIsOrder())) {
                nextHandler.setTaskOrder(orderSn);
            } else {
                nextHandler.setTaskOrder(-1);
            }
            handerList.add(nextHandler);
        }
        return handerList;
    }

    /**
     * 获取下拉框数据源
     *
     * @param parmaJson
     * @return
     */
    @Override
    public List<Map<String, Object>> findForSelect(String parmaJson) {
        StringBuffer sql = new StringBuffer("SELECT H.HANDLERCONFIG_CODE ");
        sql.append("AS VALUE,H.HANDLERCONFIG_NAME AS LABEL,");
        sql.append("H.HANDLERCONFIG_TYPE,H.HANDLERCONFIG_INTER ");
        sql.append("FROM JBPM6_HANDLERCONFIG H");
        sql.append(" ORDER BY H.HANDLERCONFIG_CREATETIME ASC ");
        return dao.findBySql(sql.toString(), null, null);
    }

    /**
     * 获取发起人作为审核人
     *
     * @param flowVars
     * @param nodeAssigner
     * @param jbpmFlowInfo
     * @return
     */
    @Override
    public List<FlowNextHandler> getStartHandlers(Map<String, Object> flowVars,
                                                  NodeAssigner nodeAssigner, JbpmFlowInfo jbpmFlowInfo) {
        Map<String, Object> sysUser = dao.getRecord("PLAT_SYSTEM_SYSUSER",
                new String[]{"SYSUSER_ID"}, new Object[]{jbpmFlowInfo.getJbpmCreatorId()});
        String SYSUSER_ID = (String) sysUser.get("SYSUSER_ID");
        String SYSUSER_NAME = (String) sysUser.get("SYSUSER_NAME");
        List<FlowNextHandler> handerList = new ArrayList<FlowNextHandler>();
        FlowNextHandler nextHandler = new FlowNextHandler();
        nextHandler.setAssignerId(SYSUSER_ID);
        nextHandler.setAssignerName(SYSUSER_NAME);
        nextHandler.setTaskOrder(-1);
        handerList.add(nextHandler);
        return handerList;
    }

    /**
     * 获取指定具体用户数据列表
     *
     * @param flowVars
     * @param nodeAssigner
     * @param jbpmFlowInfo
     * @return
     */
    @Override
    public List<FlowNextHandler> getAssignHandlers(Map<String, Object> flowVars,
                                                   NodeAssigner nodeAssigner, JbpmFlowInfo jbpmFlowInfo) {
        String userIds = nodeAssigner.getVarValues();
        List<FlowNextHandler> handerList = new ArrayList<FlowNextHandler>();
        if (StringUtils.isNotEmpty(userIds)) {
            String[] userIdArray = userIds.split(",");
            for (String userId : userIdArray) {
                Map<String, Object> sysUser = dao.getRecord("PLAT_SYSTEM_SYSUSER",
                        new String[]{"SYSUSER_ID"}, new Object[]{userId});
                String SYSUSER_ID = (String) sysUser.get("SYSUSER_ID");
                String SYSUSER_NAME = (String) sysUser.get("SYSUSER_NAME");
                FlowNextHandler nextHandler = new FlowNextHandler();
                nextHandler.setAssignerId(SYSUSER_ID);
                nextHandler.setAssignerName(SYSUSER_NAME);
                nextHandler.setTaskOrder(-1);
                handerList.add(nextHandler);
            }
        }
        return handerList;
    }

}
