/*
 * Copyright (c) 2005, 2014, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.controller;

import com.housoo.platform.core.util.*;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.DepartService;
import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.core.service.SysUserService;
import com.housoo.platform.core.model.JbpmFlowInfo;
import com.housoo.platform.workflow.model.NodeAssigner;
import com.housoo.platform.workflow.service.HandlerConfigService;
import com.housoo.platform.workflow.service.NodeAssignerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述 办理人业务相关Controller
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-10 15:50:11
 */
@Controller
@RequestMapping("/workflow/HandlerConfigController")
public class HandlerConfigController extends BaseController {
    /**
     *
     */
    @Resource
    private HandlerConfigService handlerConfigService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;
    /**
     *
     */
    @Resource
    private NodeAssignerService nodeAssignerService;
    /**
     *
     */
    @Resource
    private DepartService departService;
    /**
     *
     */
    @Resource
    private SysUserService sysUserService;

    /**
     * 删除办理人数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        handlerConfigService.deleteRecords("JBPM6_HANDLERCONFIG", "HANDLERCONFIG_ID", selectColValues.split(","));
        sysLogService.saveBackLog("流程办理人配置", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的办理人", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改办理人数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> handlerConfig = PlatBeanUtil.getMapFromRequest(request);
        handlerConfig = handlerConfigService.saveOrUpdate("JBPM6_HANDLERCONFIG",
                handlerConfig, SysConstants.ID_GENERATOR_UUID, null);
        handlerConfig.put("success", true);
        this.printObjectJsonString(handlerConfig, response);
    }

    /**
     * 跳转到办理人表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String HANDLERCONFIG_ID = request.getParameter("HANDLERCONFIG_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> handlerConfig = null;
        if (StringUtils.isNotEmpty(HANDLERCONFIG_ID)) {
            handlerConfig = this.handlerConfigService.getRecord("JBPM6_HANDLERCONFIG"
                    , new String[]{"HANDLERCONFIG_ID"}, new Object[]{HANDLERCONFIG_ID});
        } else {
            handlerConfig = new HashMap<String, Object>();
        }
        request.setAttribute("handlerConfig", handlerConfig);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }

    /**
     * 跳转到角色人员选择器
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goRoleUserSelector")
    public ModelAndView goRoleUserSelector(HttpServletRequest request) {
        String flowToken = request.getParameter("flowToken");
        if (StringUtils.isNotEmpty(flowToken)) {
            String nextNodeKey = request.getParameter("nextNodeKey");
            Map<String, Object> flowVars = (Map<String, Object>) PlatAppUtil.getSessionCache(flowToken);
            JbpmFlowInfo jbpmFlowInfo = (JbpmFlowInfo) PlatBeanUtil.mapToBean(new JbpmFlowInfo(), flowVars);
            Map<String, Object> nodeAssigner = nodeAssignerService.getRecord("JBPM6_NODEASSIGNER",
                    new String[]{"NODEASSIGNER_NODEKEY", "NODEASSIGNER_NEXTNODEKEY",
                            "NODEASSIGNER_DEFID", "NODEASSIGNER_DEFVERSION"},
                    new Object[]{jbpmFlowInfo.getJbpmOperingNodeKey(),
                            nextNodeKey, jbpmFlowInfo.getJbpmDefId(), jbpmFlowInfo.getJbpmDefVersion()
                    });
            String ROLE_IDS = (String) nodeAssigner.get("NODEASSIGNER_VARVALUES");
            request.setAttribute("ROLE_IDS", ROLE_IDS);
            request.setAttribute("flowToken", flowToken);
            String filterRule = (String) nodeAssigner.get("NODEASSIGNER_FILTERRULE");
            if (StringUtils.isNotEmpty(filterRule) &&
                    filterRule.equals(NodeAssigner.FILTERRULE_SAMECOMPANY)) {
                String companyId = (String) PlatAppUtil.getBackPlatLoginUser().get("SYSUSER_COMPANYID");
                List<String> userIdList = sysUserService.findUserIds(companyId, ROLE_IDS);
                if (userIdList != null && userIdList.size() > 0) {
                    String SHOW_USERIDS = PlatStringUtil.getListStringSplit(userIdList);
                    request.setAttribute("SHOW_USERIDS", SHOW_USERIDS);
                }
            }
            /*if(StringUtils.isNotEmpty(filterRule)&&
                    filterRule.equals(NodeAssigner.FILTERRULE_SAMELEVEL)){
                String SECRET_LEVEL = (String) flowVars.get("SECRET_LEVEL");
                List<String> secretUserList = departService.findGrantUserIds(SECRET_LEVEL);
                if(secretUserList!=null&&secretUserList.size()>0){
                    String SHOW_USERIDS = PlatStringUtil.getListStringSplit(secretUserList);
                    request.setAttribute("SHOW_USERIDS", SHOW_USERIDS);
                }
                
            }*/
            if (StringUtils.isNotEmpty(filterRule)) {
                request.setAttribute("filterRule", filterRule);
            }
        }
        return PlatUICompUtil.goDesignUI("roleuserselector", request);
    }

    /**
     * 跳转到人员选择器
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goSysUserSelector")
    public ModelAndView goSysUserSelector(HttpServletRequest request) {
        String flowToken = request.getParameter("flowToken");
        if (StringUtils.isNotEmpty(flowToken)) {
            String nextNodeKey = request.getParameter("nextNodeKey");
            Map<String, Object> flowVars = (Map<String, Object>) PlatAppUtil.getSessionCache(flowToken);
            JbpmFlowInfo jbpmFlowInfo = (JbpmFlowInfo) PlatBeanUtil.mapToBean(new JbpmFlowInfo(), flowVars);
            Map<String, Object> nodeAssigner = nodeAssignerService.getRecord("JBPM6_NODEASSIGNER",
                    new String[]{"NODEASSIGNER_NODEKEY", "NODEASSIGNER_NEXTNODEKEY",
                            "NODEASSIGNER_DEFID", "NODEASSIGNER_DEFVERSION"},
                    new Object[]{jbpmFlowInfo.getJbpmOperingNodeKey(),
                            nextNodeKey, jbpmFlowInfo.getJbpmDefId(), jbpmFlowInfo.getJbpmDefVersion()
                    });
            request.setAttribute("flowToken", flowToken);
            String filterRule = (String) nodeAssigner.get("NODEASSIGNER_FILTERRULE");
            if (StringUtils.isNotEmpty(filterRule) &&
                    filterRule.equals(NodeAssigner.FILTERRULE_SAMECOMPANY)) {

            }
            if (StringUtils.isNotEmpty(filterRule)) {
                request.setAttribute("filterRule", filterRule);
            }
        }
        return PlatUICompUtil.goDesignUI("userselector", request);
    }
}
