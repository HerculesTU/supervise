/*
 * Copyright (c) 2005, 2017, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.controller;

import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.workflow.model.FlowNode;
import com.housoo.platform.workflow.service.FlowDefService;
import com.housoo.platform.workflow.service.NodeBindService;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 描述
 *
 * @author 胡裕
 * @created 2017年5月6日 上午8:27:11
 */
@Controller
@RequestMapping("/workflow/FlowNodeController")
public class FlowNodeController extends BaseController {
    /**
     *
     */
    @Resource
    private FlowDefService flowDefService;
    /**
     *
     */
    @Resource
    private NodeBindService nodeBindService;


    /**
     * 跳转到流程环节配置界面
     *
     * @return
     */
    @RequestMapping(params = "goFlowNodeConfig")
    public ModelAndView goFlowNodeConfig(HttpServletRequest request) {
        String FLOWDEF_ID = request.getParameter("FLOWDEF_ID");
        String FLOWDEF_VERSION = request.getParameter("FLOWDEF_VERSION");
        String NODE_KEY = request.getParameter("NODE_KEY");
        String NODE_TYPE = request.getParameter("NODE_TYPE");
        String IS_SUBPROCESS = request.getParameter("IS_SUBPROCESS");
        if (NODE_TYPE.equals(FlowNode.NODETYPE_START) ||
                NODE_TYPE.equals(FlowNode.NODETYPE_TASK)) {
            Map<String, Object> nodeBind = nodeBindService.getRecord("JBPM6_NODEBIND",
                    new String[]{"NODEBIND_TYPE", "NODEBIND_FLOWDFEID", "NODEBIND_FLOWVERSION", "NODEBIND_NODEKEY"},
                    new Object[]{NodeBindService.BINDTYPE_FORM, FLOWDEF_ID, FLOWDEF_VERSION, NODE_KEY});
            String formId = (String) nodeBind.get("NODEBIND_RECORDID");
            nodeBindService.updateFieldAuthJson(formId, FLOWDEF_ID,
                    Integer.parseInt(FLOWDEF_VERSION), NODE_KEY);
        }
        Map<String, Object> nodeBind = nodeBindService.getRecord("JBPM6_NODEBIND",
                new String[]{"NODEBIND_FLOWDFEID", "NODEBIND_FLOWVERSION", "NODEBIND_TYPE", "NODEBIND_NODEKEY"},
                new Object[]{FLOWDEF_ID, FLOWDEF_VERSION, NodeBindService.BINDTYPE_NODECONIG, NODE_KEY}
        );
        if (nodeBind != null) {
            request.setAttribute("NODEBIND_TASKNATURE", nodeBind.get("NODEBIND_TASKNATURE"));
            request.setAttribute("NODEBIND_HANDUPTYPE", nodeBind.get("NODEBIND_HANDUPTYPE"));
            request.setAttribute("NODEBIND_HANDUPDAYS", nodeBind.get("NODEBIND_HANDUPDAYS"));
            request.setAttribute("NODEBIND_BACKTYPE", nodeBind.get("NODEBIND_BACKTYPE"));
            request.setAttribute("NODEBIND_SUBDEFID", nodeBind.get("NODEBIND_SUBDEFID"));
        }
        request.setAttribute("FLOWDEF_ID", FLOWDEF_ID);
        request.setAttribute("FLOWDEF_VERSION", FLOWDEF_VERSION);
        request.setAttribute("NODE_KEY", NODE_KEY);
        request.setAttribute("IS_SUBPROCESS", IS_SUBPROCESS);
        return PlatUICompUtil.goDesignUI("flownodeconfig", request);
    }

    /**
     * 跳转到流程节点选择界面
     *
     * @return
     */
    @RequestMapping(params = "goSelectNodes")
    public ModelAndView goSelectNodes(HttpServletRequest request) {
        String FLOWDEF_ID = request.getParameter("FLOWDEF_ID");
        String FLOWDEF_VERSION = request.getParameter("FLOWDEF_VERSION");
        Map<String, Object> flowDef = flowDefService.getFlowDefInfo(FLOWDEF_ID,
                Integer.parseInt(FLOWDEF_VERSION));
        //获取定义的XML
        String defJson = (String) flowDef.get("FLOWDEF_JSON");
        flowDef.put("FLOWDEF_JSON", StringEscapeUtils.escapeHtml3(defJson));
        request.setAttribute("flowDef", flowDef);
        return new ModelAndView("background/workflow/flowdef/flownodeselector");
    }

}
