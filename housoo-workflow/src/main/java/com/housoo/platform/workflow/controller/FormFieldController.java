/*
 * Copyright (c) 2005, 2014, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.controller;

import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.workflow.service.FormFieldService;
import com.housoo.platform.workflow.service.NodeBindService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述 表单字段业务相关Controller
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-08 15:00:57
 */
@Controller
@RequestMapping("/workflow/FormFieldController")
public class FormFieldController extends BaseController {
    /**
     *
     */
    @Resource
    private FormFieldService formFieldService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;
    /**
     *
     */
    @Resource
    private NodeBindService nodeBindService;

    /**
     * 删除表单字段数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        formFieldService.deleteRecords("JBPM6_FORMFIELD", "FORMFIELD_ID", selectColValues.split(","));
        sysLogService.saveBackLog("流程表单管理", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的表单字段", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改表单字段数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> formField = PlatBeanUtil.getMapFromRequest(request);
        formField = formFieldService.saveOrUpdate("JBPM6_FORMFIELD",
                formField, SysConstants.ID_GENERATOR_UUID, null);
        formField.put("success", true);
        this.printObjectJsonString(formField, response);
    }

    /**
     * 跳转到表单字段表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String FORMFIELD_ID = request.getParameter("FORMFIELD_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> formField = null;
        if (StringUtils.isNotEmpty(FORMFIELD_ID)) {
            formField = this.formFieldService.getRecord("JBPM6_FORMFIELD"
                    , new String[]{"FORMFIELD_ID"}, new Object[]{FORMFIELD_ID});
        } else {
            formField = new HashMap<String, Object>();
        }
        request.setAttribute("formField", formField);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }

    /**
     * 刷新表单字段权限
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "refreshFields")
    public void refreshFields(HttpServletRequest request,
                              HttpServletResponse response) {
        String FLOWDEF_ID = request.getParameter("FLOWDEF_ID");
        String FLOWDEF_VERSION = request.getParameter("FLOWDEF_VERSION");
        String NODE_KEY = request.getParameter("NODE_KEY");
        String formId = nodeBindService.getNodeBindFormId(FLOWDEF_ID,
                Integer.parseInt(FLOWDEF_VERSION), NODE_KEY);
        if (StringUtils.isNotEmpty(formId)) {
            formFieldService.saveBatch(formId);
            nodeBindService.updateFieldAuthJson(formId, FLOWDEF_ID,
                    Integer.parseInt(FLOWDEF_VERSION), NODE_KEY);
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }
}
