/*
 * Copyright (c) 2005, 2014, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.controller;

import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.workflow.service.FlowFormService;
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
 * 描述 流程表单业务相关Controller
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-05 10:54:04
 */
@Controller
@RequestMapping("/workflow/FlowFormController")
public class FlowFormController extends BaseController {
    /**
     *
     */
    @Resource
    private FlowFormService flowFormService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除流程表单数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        flowFormService.deleteRecords("JBPM6_FLOWFORM", "FLOWFORM_ID", selectColValues.split(","));
        sysLogService.saveBackLog("流程表单管理", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的流程表单", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改流程表单数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> flowForm = PlatBeanUtil.getMapFromRequest(request);
        flowForm = flowFormService.saveFormCascadeFields(flowForm);
        flowForm.put("success", true);
        this.printObjectJsonString(flowForm, response);
    }

    /**
     * 跳转到流程表单表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String FLOWFORM_ID = request.getParameter("FLOWFORM_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> flowForm = null;
        if (StringUtils.isNotEmpty(FLOWFORM_ID)) {
            flowForm = this.flowFormService.getRecord("JBPM6_FLOWFORM"
                    , new String[]{"FLOWFORM_ID"}, new Object[]{FLOWFORM_ID});
        } else {
            flowForm = new HashMap<String, Object>();
        }
        request.setAttribute("flowForm", flowForm);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }
}
