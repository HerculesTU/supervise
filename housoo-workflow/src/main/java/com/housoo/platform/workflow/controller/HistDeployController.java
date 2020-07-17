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
import com.housoo.platform.workflow.service.HistDeployService;
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
 * 描述 流程历史部署业务相关Controller
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-04 14:34:24
 */
@Controller
@RequestMapping("/workflow/HistDeployController")
public class HistDeployController extends BaseController {
    /**
     *
     */
    @Resource
    private HistDeployService histDeployService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除流程历史部署数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        histDeployService.deleteRecords("JBPM6_HIST_DEPLOY", "DEPLOY_ID", selectColValues.split(","));
        sysLogService.saveBackLog("流程定义管理", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的流程历史部署", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改流程历史部署数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> histDeploy = PlatBeanUtil.getMapFromRequest(request);
        String DEPLOY_ID = (String) histDeploy.get("DEPLOY_ID");
        histDeploy = histDeployService.saveOrUpdate("JBPM6_HIST_DEPLOY",
                histDeploy, SysConstants.ID_GENERATOR_UUID, null);
        //如果是保存树形结构表数据,请调用下面的接口,而注释掉上面的接口代码
        //histDeploy = histDeployService.saveOrUpdateTreeData("JBPM6_HIST_DEPLOY",
        //        histDeploy,SysConstants.ID_GENERATOR_UUID,null);
        if (StringUtils.isNotEmpty(DEPLOY_ID)) {
            sysLogService.saveBackLog("流程定义管理", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为[" + DEPLOY_ID + "]流程历史部署", request);
        } else {
            DEPLOY_ID = (String) histDeploy.get("DEPLOY_ID");
            sysLogService.saveBackLog("流程定义管理", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为[" + DEPLOY_ID + "]流程历史部署", request);
        }
        histDeploy.put("success", true);
        this.printObjectJsonString(histDeploy, response);
    }

    /**
     * 跳转到流程历史部署表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String DEPLOY_ID = request.getParameter("DEPLOY_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> histDeploy = null;
        if (StringUtils.isNotEmpty(DEPLOY_ID)) {
            histDeploy = this.histDeployService.getRecord("JBPM6_HIST_DEPLOY"
                    , new String[]{"DEPLOY_ID"}, new Object[]{DEPLOY_ID});
        } else {
            histDeploy = new HashMap<String, Object>();
        }
        request.setAttribute("histDeploy", histDeploy);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
        //如果是跳转到树形表单录入界面,请开放以下代码,,而注释掉上面的代码
        /*String DEPLOY_ID = request.getParameter("DEPLOY_ID");
        String HISTDEPLOY_PARENTID = request.getParameter("HISTDEPLOY_PARENTID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String,Object> histDeploy = null;
        if(StringUtils.isNotEmpty(DEPLOY_ID)){
            histDeploy = this.histDeployService.getRecord("JBPM6_HIST_DEPLOY"
                    ,new String[]{"DEPLOY_ID"},new Object[]{DEPLOY_ID});
            HISTDEPLOY_PARENTID = (String) histDeploy.get("HistDeploy_PARENTID");
        }
        Map<String,Object> parentHistDeploy = null;
        if(HISTDEPLOY_PARENTID.equals("0")){
            parentHistDeploy = new HashMap<String,Object>();
            parentHistDeploy.put("DEPLOY_ID",HISTDEPLOY_PARENTID);
            parentHistDeploy.put("HISTDEPLOY_NAME","流程历史部署树");
        }else{
            parentHistDeploy = this.histDeployService.getRecord("JBPM6_HIST_DEPLOY",
                    new String[]{"DEPLOY_ID"}, new Object[]{HISTDEPLOY_PARENTID});
        }
        if(histDeploy==null){
            histDeploy = new HashMap<String,Object>();
        }
        histDeploy.put("HISTDEPLOY_PARENTID",parentHistDeploy.get("DEPLOY_ID"));
        histDeploy.put("HISTDEPLOY_PARENTNAME",parentHistDeploy.get("HISTDEPLOY_NAME"));
        request.setAttribute("histDeploy", histDeploy);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);*/
    }
}
