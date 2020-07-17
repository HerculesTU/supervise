/*
 * Copyright (c) 2005, 2014, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.service.impl.BaseServiceImpl;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatStringUtil;
import com.housoo.platform.workflow.dao.FlowFormDao;
import com.housoo.platform.workflow.service.FlowFormService;
import com.housoo.platform.workflow.service.FormFieldService;
import com.housoo.platform.workflow.service.NodeBindService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 描述 流程表单业务相关service实现类
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-05 10:54:04
 */
@Service("flowFormService")
public class FlowFormServiceImpl extends BaseServiceImpl implements FlowFormService {

    /**
     * 所引入的dao
     */
    @Resource
    private FlowFormDao dao;
    /**
     *
     */
    @Resource
    private FormFieldService formFieldService;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 获取可选设计表单列表
     *
     * @param param
     * @return
     */
    @Override
    public List<Map<String, Object>> findForSelect(String param) {
        StringBuffer sql = new StringBuffer("SELECT");
        sql.append(" F.FLOWFORM_ID AS VALUE,F.FLOWFORM_NAME AS LABEL");
        sql.append(" FROM JBPM6_FLOWFORM F ");
        return dao.findBySql(sql.toString(), null, null);
    }

    /**
     * 根据表单ID获取可见的控件配置信息
     *
     * @param formId
     * @return
     */
    @Override
    public List<Map<String, Object>> findAuthAbleControl(String formId) {
        String[] compcodeArray = new String[]{"inputcomp", "selectcomp", "datetime", "radiocomp"
                , "textareacomp", "checkboxcomp", "ewebeditor", "numbercomp", "winselector"};
        StringBuffer sql = new StringBuffer("SELECT F.FORMCONTROL_ID,F.FORMCONTROL_COMPCODE");
        sql.append(" FROM PLAT_APPMODEL_FORMCONTROL F WHERE ");
        sql.append(" F.FORMCONTROL_DESIGN_ID=(SELECT D.DESIGN_ID FROM PLAT_APPMODEL_DESIGN D WHERE D.DESIGN_CODE=");
        sql.append("(select F.FLOWFORM_DESIGNCODE from JBPM6_FLOWFORM F");
        sql.append(" WHERE F.FLOWFORM_ID=?)) ");
        sql.append("AND F.FORMCONTROL_COMPCODE IN ");
        sql.append(PlatStringUtil.getSqlInCondition(compcodeArray));
        sql.append(" ORDER BY F.FORMCONTROL_CREATETIME ASC");
        return dao.findBySql(sql.toString(), new Object[]{formId}, null);
    }

    /**
     * 保存流程表单并且级联保存表单字段
     *
     * @param flowForm
     * @return
     */
    @Override
    public Map<String, Object> saveFormCascadeFields(Map<String, Object> flowForm) {
        flowForm = dao.saveOrUpdate("JBPM6_FLOWFORM",
                flowForm, SysConstants.ID_GENERATOR_UUID, null);
        String FLOWFORM_ID = (String) flowForm.get("FLOWFORM_ID");
        formFieldService.saveBatch(FLOWFORM_ID);
        return flowForm;
    }

    /**
     * 获取当前环节绑定的表单信息
     *
     * @param nodeBindList
     * @return
     */
    @Override
    public Map<String, Object> getCurrentNodeBindForm(List<Map<String, Object>> nodeBindList) {
        String bindFormId = null;
        for (Map<String, Object> nodeBind : nodeBindList) {
            int NODEBIND_TYPE = Integer.parseInt(nodeBind.get("NODEBIND_TYPE").toString());
            if (NODEBIND_TYPE == NodeBindService.BINDTYPE_FORM) {
                bindFormId = (String) nodeBind.get("NODEBIND_RECORDID");
                break;
            }
        }
        Map<String, Object> flowForm = dao.getRecord("JBPM6_FLOWFORM",
                new String[]{"FLOWFORM_ID"}, new Object[]{bindFormId});
        return flowForm;
    }
}
