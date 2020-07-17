/*
 * Copyright (c) 2000 www.cnnetchina.com All rights reserved.18888
 * ZW PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.housoo.platform.supervise.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.supervise.service.ApproveService;

/**
 * 
 * 描述 督办环节配置业务相关Controller
 *
 * @author tudeodng
 * @version 1.0
 * @created 2020-05-07 08:46:27
 */
@Controller  
@RequestMapping("/supervise/ApproveController")  
public class ApproveController extends BaseController {
    /**
     * ApproveService
     */
    @Resource
    private ApproveService approveService;
    /**
     * 系统日志Service
     */
    @Resource
    private SysLogService sysLogService;
    
    /**
     * 删除督办环节配置数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
            HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        approveService.deleteRecords("TB_SUPERVISE_APPROVE", "RECORD_ID", selectColValues.split(","));
        sysLogService.saveBackLog("督办环节", SysLogService.OPER_TYPE_DEL,
                "删除了ID为["+selectColValues+"]的督办环节配置", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }
    
    /**
     * 新增或者修改督办环节配置数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
            HttpServletResponse response) {
        Map<String,Object> approve = PlatBeanUtil.getMapFromRequest(request);
        String RECORD_ID = (String) approve.get("RECORD_ID");
        approve = approveService.saveOrUpdate("TB_SUPERVISE_APPROVE",
                approve, SysConstants.ID_GENERATOR_UUID,null);
        //如果是保存树形结构表数据,请调用下面的接口,而注释掉上面的接口代码
        //approve = approveService.saveOrUpdateTreeData("TB_SUPERVISE_APPROVE",
        //        approve, SysConstants.ID_GENERATOR_UUID,null);
        if(StringUtils.isNotEmpty(RECORD_ID)){
            sysLogService.saveBackLog("督办环节", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为["+RECORD_ID+"]督办环节配置", request);
        }else{
            RECORD_ID = (String) approve.get("RECORD_ID");
            sysLogService.saveBackLog("督办环节", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为["+RECORD_ID+"]督办环节配置", request);
        }
        approve.put("success", true);
        this.printObjectJsonString(approve, response);
    }
    
    /**
     * 跳转到督办环节配置表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String RECORD_ID = request.getParameter("RECORD_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String,Object> approve = null;
        if(StringUtils.isNotEmpty(RECORD_ID)){
            approve = this.approveService.getRecord("TB_SUPERVISE_APPROVE"
                    , new String[]{"RECORD_ID"}, new Object[]{RECORD_ID});
        }else{
            approve = new HashMap<String,Object>();
        }
        request.setAttribute("approve", approve);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
        //如果是跳转到树形表单录入界面,请开放以下代码,,而注释掉上面的代码
        /*String RECORD_ID = request.getParameter("RECORD_ID");
        String APPROVE_PARENTID = request.getParameter("APPROVE_PARENTID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String,Object> approve = null;
        if(StringUtils.isNotEmpty(RECORD_ID)){
            approve = this.approveService.getRecord("TB_SUPERVISE_APPROVE"
                    , new String[]{"RECORD_ID"}, new Object[]{RECORD_ID});
            APPROVE_PARENTID = (String) approve.get("Approve_PARENTID");
        }
        Map<String,Object> parentApprove = null;
        if(APPROVE_PARENTID.equals("0")){
            parentApprove = new HashMap<String,Object>();
            parentApprove.put("RECORD_ID", APPROVE_PARENTID);
            parentApprove.put("APPROVE_NAME", "督办环节配置树");
        }else{
            parentApprove = this.approveService.getRecord("TB_SUPERVISE_APPROVE",
                    new String[]{"RECORD_ID"}, new Object[]{APPROVE_PARENTID});
        }
        if(approve==null){
            approve = new HashMap<String,Object>();
        }
        approve.put("APPROVE_PARENTID", parentApprove.get("RECORD_ID"));
        approve.put("APPROVE_PARENTNAME", parentApprove.get("APPROVE_NAME"));
        request.setAttribute("approve", approve);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);*/
    }
}
