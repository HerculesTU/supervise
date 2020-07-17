/*
 * Copyright (c) 2005, 2014, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.service.impl;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.model.JbpmFlowInfo;
import com.housoo.platform.core.service.impl.BaseServiceImpl;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.UUIDGenerator;
import com.housoo.platform.workflow.dao.FormFieldDao;
import com.housoo.platform.workflow.service.FlowFormService;
import com.housoo.platform.workflow.service.FormFieldService;
import com.housoo.platform.workflow.service.NodeBindService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述 表单字段业务相关service实现类
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-08 15:00:57
 */
@Service("formFieldService")
public class FormFieldServiceImpl extends BaseServiceImpl implements FormFieldService {

    /**
     * 所引入的dao
     */
    @Resource
    private FormFieldDao dao;
    /**
     *
     */
    @Resource
    private FlowFormService flowFormService;
    /**
     *
     */
    @Resource
    private NodeBindService nodeBindService;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 批量保存表单的字段信息
     *
     * @param formId
     */
    @Override
    public void saveBatch(String formId) {
        //清除旧字段
        StringBuffer sql = new StringBuffer("DELETE FROM JBPM6_FORMFIELD");
        sql.append(" WHERE FORMFIELD_FORMID=? ");
        dao.executeSql(sql.toString(), new Object[]{formId});
        List<Map<String, Object>> authControls = flowFormService.findAuthAbleControl(formId);
        sql = new StringBuffer("SELECT F.FIELDCONFIG_FIELDNAME,F.FIELDCONFIG_FIELDVALUE");
        sql.append(" FROM PLAT_APPMODEL_FIELDCONFIG F ");
        sql.append("WHERE F.FIELDCONFIG_FORMCONTROLID=? AND F.FIELDCONFIG_FIELDNAME ");
        sql.append("IN ('PLAT_COMPNAME','CONTROL_NAME','ALLOW_BLANK')");
        List<Map<String, Object>> formFieldList = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> authControl : authControls) {
            String FORMCONTROL_ID = (String) authControl.get("FORMCONTROL_ID");
            List<Map<String, Object>> list = dao.findBySql(sql.toString(), new Object[]{FORMCONTROL_ID}, null);
            Map<String, Object> formField = new HashMap<String, Object>();
            formField.put("FORMFIELD_FORMID", formId);
            formField.put("FORMFIELD_ID", UUIDGenerator.getUUID());
            for (Map<String, Object> field : list) {
                String FIELDCONFIG_FIELDNAME = (String) field.get("FIELDCONFIG_FIELDNAME");
                String FIELDCONFIG_FIELDVALUE = (String) field.get("FIELDCONFIG_FIELDVALUE");
                if ("PLAT_COMPNAME".equals(FIELDCONFIG_FIELDNAME)) {
                    formField.put("FORMFIELD_CNNAME", FIELDCONFIG_FIELDVALUE);
                } else if ("CONTROL_NAME".equals(FIELDCONFIG_FIELDNAME)) {
                    formField.put("FORMFIELD_NAME", FIELDCONFIG_FIELDVALUE);
                } else if ("ALLOW_BLANK".equals(FIELDCONFIG_FIELDNAME)) {
                    formField.put("FORMFIELD_ALLOWBLANK", FIELDCONFIG_FIELDVALUE);
                }
            }
            formFieldList.add(formField);
        }
        if (formFieldList.size() > 0) {
            dao.saveBatch(formFieldList, "JBPM6_FORMFIELD");
        }
    }

    /**
     * 更新表单ID获取字段列表
     *
     * @param formId
     * @return
     */
    @Override
    public List<Map<String, Object>> findByFormId(String formId) {
        StringBuffer sql = new StringBuffer("SELECT * FROM ");
        sql.append("JBPM6_FORMFIELD F WHERE F.FORMFIELD_FORMID=?");
        sql.append(" ORDER BY F.FORMFIELD_CREATETIME ASC ");
        return dao.findBySql(sql.toString(), new Object[]{formId}, null);
    }

    /**
     * 设置流程表单字段权限
     *
     * @param request
     * @param jbpmFlowInfo
     */
    @Override
    public void setFlowFormFieldAuth(HttpServletRequest request, JbpmFlowInfo jbpmFlowInfo) {
        String defId = jbpmFlowInfo.getJbpmDefId();
        String defVersion = jbpmFlowInfo.getJbpmDefVersion();
        String nodeKey = jbpmFlowInfo.getJbpmOperingNodeKey();
        Map<String, Object> nodeBind = nodeBindService.getNodeBind(defId, defVersion,
                nodeKey, NodeBindService.BINDTYPE_FORM);
        String NODEBIND_FIELDAUTHJSON = (String) nodeBind.get("NODEBIND_FIELDAUTHJSON");
        if (StringUtils.isNotEmpty(NODEBIND_FIELDAUTHJSON)) {
            List<Map> authList = JSON.parseArray(NODEBIND_FIELDAUTHJSON, Map.class);
            Map<String, String[]> changeCompAuthMap = new HashMap<String, String[]>();
            for (Map<String, Object> auth : authList) {
                String FORMFIELD_NAME = (String) auth.get("FORMFIELD_NAME");
                String FORMFIELD_ALLOWBLANK = (String) auth.get("FORMFIELD_ALLOWBLANK");
                String FORMFIELD_AUTH = (String) auth.get("FORMFIELD_AUTH");
                changeCompAuthMap.put(FORMFIELD_NAME, new String[]{FORMFIELD_ALLOWBLANK, FORMFIELD_AUTH});
            }
            request.setAttribute(SysConstants.CHANGE_COMP_AUTH_MAP_KEY, changeCompAuthMap);
        }
        //获取自定义控件节点配置
        Map<String, Object> defCtrlBind = nodeBindService.getNodeBind(defId, defVersion,
                nodeKey, NodeBindService.BINDTYPE_DEFCTRL);
        if (defCtrlBind != null) {
            NODEBIND_FIELDAUTHJSON = (String) defCtrlBind.get("NODEBIND_FIELDAUTHJSON");
            if (StringUtils.isNotEmpty(NODEBIND_FIELDAUTHJSON)) {
                List<Map> authList = JSON.parseArray(NODEBIND_FIELDAUTHJSON, Map.class);
                Map<String, String> changeCompAuthMap = new HashMap<String, String>();
                for (Map<String, Object> auth : authList) {
                    String CTRL_ENNAME = (String) auth.get("CTRL_ENNAME");
                    String CTRL_RIGHTSEN = (String) auth.get("CTRL_RIGHTSEN");
                    changeCompAuthMap.put(CTRL_ENNAME, CTRL_RIGHTSEN);
                }
                request.setAttribute(SysConstants.CHANGE_DEF_CTRL_AUTH_MAP_KEY, changeCompAuthMap);
            }
        }
    }

}
