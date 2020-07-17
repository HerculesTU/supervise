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
import com.housoo.platform.workflow.service.FlowBtnService;
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
 * 描述 流程按钮业务相关Controller
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-07 07:09:57
 */
@Controller
@RequestMapping("/workflow/FlowBtnController")
public class FlowBtnController extends BaseController {
    /**
     *
     */
    @Resource
    private FlowBtnService flowBtnService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除流程按钮数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        flowBtnService.deleteRecords("JBPM6_FLOWBTN", "FLOWBTN_ID", selectColValues.split(","));
        sysLogService.saveBackLog("流程按钮管理", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的流程按钮", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改流程按钮数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> flowBtn = PlatBeanUtil.getMapFromRequest(request);
        String FLOWBTN_ID = (String) flowBtn.get("FLOWBTN_ID");
        flowBtn = flowBtnService.saveOrUpdate("JBPM6_FLOWBTN",
                flowBtn, SysConstants.ID_GENERATOR_UUID, null);
        //如果是保存树形结构表数据,请调用下面的接口,而注释掉上面的接口代码
        //flowBtn = flowBtnService.saveOrUpdateTreeData("JBPM6_FLOWBTN",
        //        flowBtn,SysConstants.ID_GENERATOR_UUID,null);
        if (StringUtils.isNotEmpty(FLOWBTN_ID)) {
            sysLogService.saveBackLog("流程按钮管理", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为[" + FLOWBTN_ID + "]流程按钮", request);
        } else {
            FLOWBTN_ID = (String) flowBtn.get("FLOWBTN_ID");
            sysLogService.saveBackLog("流程按钮管理", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为[" + FLOWBTN_ID + "]流程按钮", request);
        }
        flowBtn.put("success", true);
        this.printObjectJsonString(flowBtn, response);
    }

    /**
     * 跳转到流程按钮表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String FLOWBTN_ID = request.getParameter("FLOWBTN_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> flowBtn = null;
        if (StringUtils.isNotEmpty(FLOWBTN_ID)) {
            flowBtn = this.flowBtnService.getRecord("JBPM6_FLOWBTN"
                    , new String[]{"FLOWBTN_ID"}, new Object[]{FLOWBTN_ID});
        } else {
            flowBtn = new HashMap<String, Object>();
        }
        request.setAttribute("flowBtn", flowBtn);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
        //如果是跳转到树形表单录入界面,请开放以下代码,,而注释掉上面的代码
        /*String FLOWBTN_ID = request.getParameter("FLOWBTN_ID");
        String FLOWBTN_PARENTID = request.getParameter("FLOWBTN_PARENTID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String,Object> flowBtn = null;
        if(StringUtils.isNotEmpty(FLOWBTN_ID)){
            flowBtn = this.flowBtnService.getRecord("JBPM6_FLOWBTN"
                    ,new String[]{"FLOWBTN_ID"},new Object[]{FLOWBTN_ID});
            FLOWBTN_PARENTID = (String) flowBtn.get("FlowBtn_PARENTID");
        }
        Map<String,Object> parentFlowBtn = null;
        if(FLOWBTN_PARENTID.equals("0")){
            parentFlowBtn = new HashMap<String,Object>();
            parentFlowBtn.put("FLOWBTN_ID",FLOWBTN_PARENTID);
            parentFlowBtn.put("FLOWBTN_NAME","流程按钮树");
        }else{
            parentFlowBtn = this.flowBtnService.getRecord("JBPM6_FLOWBTN",
                    new String[]{"FLOWBTN_ID"}, new Object[]{FLOWBTN_PARENTID});
        }
        if(flowBtn==null){
            flowBtn = new HashMap<String,Object>();
        }
        flowBtn.put("FLOWBTN_PARENTID",parentFlowBtn.get("FLOWBTN_ID"));
        flowBtn.put("FLOWBTN_PARENTNAME",parentFlowBtn.get("FLOWBTN_NAME"));
        request.setAttribute("flowBtn", flowBtn);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);*/
    }
}
