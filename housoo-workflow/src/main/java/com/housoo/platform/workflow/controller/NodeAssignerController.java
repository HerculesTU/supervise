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
import com.housoo.platform.workflow.service.NodeAssignerService;
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
 * 描述 下一环节办理人业务相关Controller
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-10 17:05:56
 */
@Controller
@RequestMapping("/workflow/NodeAssignerController")
public class NodeAssignerController extends BaseController {
    /**
     *
     */
    @Resource
    private NodeAssignerService nodeAssignerService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除下一环节办理人数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        nodeAssignerService.deleteRecords("JBPM6_NODEASSIGNER", "NODEASSIGNER_ID", selectColValues.split(","));
        sysLogService.saveBackLog("流程定义管理", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的下一环节办理人", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改下一环节办理人数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> nodeAssigner = PlatBeanUtil.getMapFromRequest(request);
        nodeAssigner = nodeAssignerService.saveOrUpdate("JBPM6_NODEASSIGNER",
                nodeAssigner, SysConstants.ID_GENERATOR_UUID, null);
        nodeAssigner.put("success", true);
        this.printObjectJsonString(nodeAssigner, response);
    }

    /**
     * 跳转到下一环节办理人表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String NODEASSIGNER_ID = request.getParameter("NODEASSIGNER_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        String FLOWDEF_ID = request.getParameter("FLOWDEF_ID");
        String FLOWDEF_VERSION = request.getParameter("FLOWDEF_VERSION");
        String NODE_KEY = request.getParameter("NODE_KEY");
        Map<String, Object> nodeAssigner = null;
        if (StringUtils.isNotEmpty(NODEASSIGNER_ID)) {
            nodeAssigner = this.nodeAssignerService.getRecord("JBPM6_NODEASSIGNER"
                    , new String[]{"NODEASSIGNER_ID"}, new Object[]{NODEASSIGNER_ID});
        } else {
            nodeAssigner = new HashMap<String, Object>();
            nodeAssigner.put("NODEASSIGNER_NODEKEY", NODE_KEY);
            nodeAssigner.put("NODEASSIGNER_DEFID", FLOWDEF_ID);
            nodeAssigner.put("NODEASSIGNER_DEFVERSION", FLOWDEF_VERSION);
        }
        request.setAttribute("nodeAssigner", nodeAssigner);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }

    /**
     * 跳转到系统岗位选择界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goPositionSelector")
    public ModelAndView goPositionSelector(HttpServletRequest request) {
        String NODEASSIGNER_ID = request.getParameter("NODEASSIGNER_ID");
        StringBuffer selectedRecordIds = new StringBuffer("");
        if (StringUtils.isNotEmpty(NODEASSIGNER_ID)) {
            Map<String, Object> nodeAssigner = this.nodeAssignerService.getRecord("JBPM6_NODEASSIGNER"
                    , new String[]{"NODEASSIGNER_ID"}, new Object[]{NODEASSIGNER_ID});
            String NODEASSIGNER_VARVALUES = (String) nodeAssigner.get("NODEASSIGNER_VARVALUES");
            if (StringUtils.isNotEmpty(NODEASSIGNER_VARVALUES)) {
                selectedRecordIds.append(NODEASSIGNER_VARVALUES);
            }
        }
        request.setAttribute("selectedRecordIds", selectedRecordIds.toString());
        return PlatUICompUtil.goDesignUI("position_select", request);
    }

    /**
     * 跳转到系统角色选择界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goRoleSelector")
    public ModelAndView goRoleSelector(HttpServletRequest request) {
        String NODEASSIGNER_ID = request.getParameter("NODEASSIGNER_ID");
        StringBuffer selectedRecordIds = new StringBuffer("");
        if (StringUtils.isNotEmpty(NODEASSIGNER_ID)) {
            Map<String, Object> nodeAssigner = this.nodeAssignerService.getRecord("JBPM6_NODEASSIGNER"
                    , new String[]{"NODEASSIGNER_ID"}, new Object[]{NODEASSIGNER_ID});
            String NODEASSIGNER_VARVALUES = (String) nodeAssigner.get("NODEASSIGNER_VARVALUES");
            if (StringUtils.isNotEmpty(NODEASSIGNER_VARVALUES)) {
                selectedRecordIds.append(NODEASSIGNER_VARVALUES);
            }
        }
        request.setAttribute("selectedRecordIds", selectedRecordIds.toString());
        return PlatUICompUtil.goDesignUI("roleselector", request);
    }

    /**
     * 跳转到系统用户选择界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goUserSelector")
    public ModelAndView goUserSelector(HttpServletRequest request) {
        String NODEASSIGNER_ID = request.getParameter("NODEASSIGNER_ID");
        StringBuffer selectedRecordIds = new StringBuffer("");
        if (StringUtils.isNotEmpty(NODEASSIGNER_ID)) {
            Map<String, Object> nodeAssigner = this.nodeAssignerService.getRecord("JBPM6_NODEASSIGNER"
                    , new String[]{"NODEASSIGNER_ID"}, new Object[]{NODEASSIGNER_ID});
            String NODEASSIGNER_VARVALUES = (String) nodeAssigner.get("NODEASSIGNER_VARVALUES");
            if (StringUtils.isNotEmpty(NODEASSIGNER_VARVALUES)) {
                selectedRecordIds.append(NODEASSIGNER_VARVALUES);
            }
        }
        request.setAttribute("selectedRecordIds", selectedRecordIds.toString());
        return PlatUICompUtil.goDesignUI("userselector", request);
    }

    /**
     * 获取下一环节办理人控件界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "getStepHandler")
    public ModelAndView getStepHandler(HttpServletRequest request) {
        Map<String, Object> stepHandler = PlatBeanUtil.getMapFromRequest(request);
        request.setAttribute("stepHandler", stepHandler);
        return new ModelAndView("background/workflow/execution/dynaNextStepHandler");
    }
}
