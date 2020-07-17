/*
 * Copyright (c) 2005, 2014, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.controller;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.workflow.service.NodeBindService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 描述 流程绑定业务相关Controller
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-06 13:24:42
 */
@Controller
@RequestMapping("/workflow/NodeBindController")
public class NodeBindController extends BaseController {
    /**
     *
     */
    @Resource
    private NodeBindService nodeBindService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 更新环节任务性质
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "updateTaskNature")
    public void updateTaskNature(HttpServletRequest request,
                                 HttpServletResponse response) {
        String FLOWDEF_ID = request.getParameter("FLOWDEF_ID");
        String FLOWDEF_VERSION = request.getParameter("FLOWDEF_VERSION");
        String NODE_KEY = request.getParameter("NODE_KEY");
        Map<String, Object> nodeBind = PlatBeanUtil.getMapFromRequest(request);
        nodeBindService.updateNodeBindConfig(FLOWDEF_ID, FLOWDEF_VERSION, NODE_KEY,
                nodeBind);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 删除流程绑定数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        nodeBindService.deleteRecords("JBPM6_NODEBIND", "NODEBIND_ID", selectColValues.split(","));
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 保存或者修改表单环节绑定数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveNodeForm")
    public void saveNodeForm(HttpServletRequest request,
                             HttpServletResponse response) {
        String FLOWDEF_ID = request.getParameter("FLOWDEF_ID");
        String FLOWDEF_VERSION = request.getParameter("FLOWDEF_VERSION");
        String FORM_ID = request.getParameter("FORM_ID");
        String NODE_KEYS = request.getParameter("NODE_KEYS");
        String IS_EDIT = request.getParameter("IS_EDIT");
        String OLDFORM_ID = request.getParameter("OLDFORM_ID");
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        if ("false".equals(IS_EDIT)) {
            boolean isHaveSameBindForm = nodeBindService.haveBindSame(FORM_ID, FLOWDEF_ID,
                    Integer.parseInt(FLOWDEF_VERSION), NodeBindService.BINDTYPE_FORM);
            if (isHaveSameBindForm) {
                result.put("success", false);
                result.put("msg", "存在相同表单重复绑定问题,绑定失败!");
                this.printObjectJsonString(result, response);
                return;
            }
            boolean haveBindMultNode = false;
            for (String nodeKey : NODE_KEYS.split(",")) {
                haveBindMultNode = nodeBindService.haveBindMultNode(nodeKey, FLOWDEF_ID,
                        Integer.parseInt(FLOWDEF_VERSION), NodeBindService.BINDTYPE_FORM);
                if (haveBindMultNode) {
                    break;
                }
            }
            if (haveBindMultNode) {
                result.put("success", false);
                result.put("msg", "存在节点被多个表单绑定问题,绑定失败!");
                this.printObjectJsonString(result, response);
                return;
            }
        }
        nodeBindService.saveFormNodesBind(FLOWDEF_ID, FLOWDEF_VERSION, FORM_ID, NODE_KEYS, OLDFORM_ID);
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 删除环节表单绑定数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "multiDelFormNode")
    public void multiDelFormNode(HttpServletRequest request,
                                 HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        String FLOWDEF_ID = request.getParameter("FLOWDEF_ID");
        String FLOWDEF_VERSION = request.getParameter("FLOWDEF_VERSION");
        String[] formIds = selectColValues.split(",");
        for (String formId : formIds) {
            nodeBindService.deleteRecord("JBPM6_NODEBIND", new String[]{"NODEBIND_TYPE", "NODEBIND_RECORDID"
                            , "NODEBIND_FLOWDFEID", "NODEBIND_FLOWVERSION"},
                    new Object[]{NodeBindService.BINDTYPE_FORM, formId, FLOWDEF_ID, FLOWDEF_VERSION});
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 跳转到环节表单绑定界面
     *
     * @return
     */
    @RequestMapping(params = "goFormNodeBind")
    public ModelAndView goFormNodeBind(HttpServletRequest request) {
        String FLOWDEF_ID = request.getParameter("FLOWDEF_ID");
        String FLOWDEF_VERSION = request.getParameter("FLOWDEF_VERSION");
        String FORM_ID = request.getParameter("FORM_ID");
        Map<String, Object> formNode = new HashMap<String, Object>();
        formNode.put("FLOWDEF_ID", FLOWDEF_ID);
        formNode.put("FLOWDEF_VERSION", FLOWDEF_VERSION);
        formNode.put("FORM_ID", FORM_ID);
        if (StringUtils.isNotEmpty(FORM_ID)) {
            List<Map<String, Object>> bindNodeList = nodeBindService.findBindNodeList(FLOWDEF_ID,
                    Integer.parseInt(FLOWDEF_VERSION), FORM_ID, NodeBindService.BINDTYPE_FORM);
            StringBuffer NODE_KEYS = new StringBuffer("");
            StringBuffer NODE_NAMES = new StringBuffer("");
            for (int i = 0; i < bindNodeList.size(); i++) {
                if (i > 0) {
                    NODE_KEYS.append(",");
                    NODE_NAMES.append(",");
                }
                NODE_KEYS.append(bindNodeList.get(i).get("NODE_KEY"));
                NODE_NAMES.append(bindNodeList.get(i).get("NODE_NAME"));
            }
            formNode.put("NODE_KEYS", NODE_KEYS.toString());
            formNode.put("NODE_NAMES", NODE_NAMES.toString());
            formNode.put("IS_EDIT", "true");
        } else {
            formNode.put("IS_EDIT", "false");
        }
        request.setAttribute("formNode", formNode);
        return PlatUICompUtil.goDesignUI("formnodebindform", request);
    }

    /**
     * 更新字段权限
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "updateFieldAuth")
    public void updateFieldAuth(HttpServletRequest request,
                                HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        String authValue = request.getParameter("authValue");
        String FLOWDEF_ID = request.getParameter("FLOWDEF_ID");
        String FLOWDEF_VERSION = request.getParameter("FLOWDEF_VERSION");
        String NODE_KEY = request.getParameter("NODE_KEY");
        nodeBindService.updateFieldAuth(FLOWDEF_ID, FLOWDEF_VERSION, NODE_KEY, selectColValues, authValue);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 更新字段字段必填非必填
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "updateFieldMust")
    public void updateFieldMust(HttpServletRequest request,
                                HttpServletResponse response) {
        String mustOrNot = request.getParameter("mustOrNot");
        String selectColValues = request.getParameter("selectColValues");
        String FLOWDEF_ID = request.getParameter("FLOWDEF_ID");
        String FLOWDEF_VERSION = request.getParameter("FLOWDEF_VERSION");
        String NODE_KEY = request.getParameter("NODE_KEY");
        String allowBlank = "false";
        if ("false".equals(mustOrNot)) {
            allowBlank = "true";
        }
        nodeBindService.updateFieldMustOrNot(FLOWDEF_ID, FLOWDEF_VERSION, NODE_KEY, selectColValues, allowBlank);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 跳转到自定义控件界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goDefCtrl")
    public ModelAndView goDefCtrl(HttpServletRequest request) {
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        String CTRL_ENNAME = request.getParameter("CTRL_ENNAME");
        String FLOWDEF_ID = request.getParameter("FLOWDEF_ID");
        String FLOWDEF_VERSION = request.getParameter("FLOWDEF_VERSION");
        String NODE_KEY = request.getParameter("NODE_KEY");
        Map<String, Object> defCtrl = null;
        if (StringUtils.isNotEmpty(CTRL_ENNAME)) {
            Map<String, Object> nodeBind = nodeBindService.getRecord("JBPM6_NODEBIND",
                    new String[]{"NODEBIND_FLOWDFEID", "NODEBIND_FLOWVERSION",
                            "NODEBIND_NODEKEY", "NODEBIND_TYPE"},
                    new Object[]{FLOWDEF_ID, FLOWDEF_VERSION,
                            NODE_KEY, NodeBindService.BINDTYPE_DEFCTRL});
            String NODEBIND_FIELDAUTHJSON = (String) nodeBind.get("NODEBIND_FIELDAUTHJSON");
            List<Map> authList = JSON.parseArray(NODEBIND_FIELDAUTHJSON, Map.class);
            for (Map auth : authList) {
                String OLDNAME = (String) auth.get("CTRL_ENNAME");
                if (CTRL_ENNAME.equals(OLDNAME)) {
                    defCtrl = auth;
                    break;
                }
            }
        } else {
            defCtrl = new HashMap<String, Object>();
        }
        defCtrl.put("FLOWDEF_ID", FLOWDEF_ID);
        defCtrl.put("FLOWDEF_VERSION", FLOWDEF_VERSION);
        defCtrl.put("NODE_KEY", NODE_KEY);
        request.setAttribute("defCtrl", defCtrl);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }

    /**
     * 新增或者修改自定义控件权限
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdateDefCtrl")
    public void saveOrUpdateDefCtrl(HttpServletRequest request,
                                    HttpServletResponse response) {
        Map<String, Object> defCtrl = PlatBeanUtil.getMapFromRequest(request);
        nodeBindService.saveOrUpdateDefCtrl(defCtrl);
        defCtrl.put("success", true);
        this.printObjectJsonString(defCtrl, response);
    }

    /**
     * 删除自定义控件权限
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "delDefCtrl")
    public void delDefCtrl(HttpServletRequest request,
                           HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        String FLOWDEF_ID = request.getParameter("FLOWDEF_ID");
        String FLOWDEF_VERSION = request.getParameter("FLOWDEF_VERSION");
        String NODE_KEY = request.getParameter("NODE_KEY");
        Set<String> enNameSet = new HashSet<String>(Arrays.asList(selectColValues.split(",")));
        Map<String, Object> nodeBind = nodeBindService.getRecord("JBPM6_NODEBIND",
                new String[]{"NODEBIND_FLOWDFEID", "NODEBIND_FLOWVERSION",
                        "NODEBIND_NODEKEY", "NODEBIND_TYPE"},
                new Object[]{FLOWDEF_ID, FLOWDEF_VERSION,
                        NODE_KEY, NodeBindService.BINDTYPE_DEFCTRL});
        String NODEBIND_FIELDAUTHJSON = (String) nodeBind.get("NODEBIND_FIELDAUTHJSON");
        List<Map> authList = JSON.parseArray(NODEBIND_FIELDAUTHJSON, Map.class);
        List<Map> newAuthList = new ArrayList<Map>();
        for (Map auth : authList) {
            String CTRL_ENNAME = (String) auth.get("CTRL_ENNAME");
            if (!enNameSet.contains(CTRL_ENNAME)) {
                newAuthList.add(auth);
            }
        }
        if (newAuthList.size() > 0) {
            NODEBIND_FIELDAUTHJSON = JSON.toJSONString(newAuthList);
        } else {
            NODEBIND_FIELDAUTHJSON = "";
        }
        nodeBind.put("NODEBIND_FIELDAUTHJSON", NODEBIND_FIELDAUTHJSON);
        nodeBindService.saveOrUpdate("JBPM6_NODEBIND", nodeBind,
                SysConstants.ID_GENERATOR_UUID, null);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

}
