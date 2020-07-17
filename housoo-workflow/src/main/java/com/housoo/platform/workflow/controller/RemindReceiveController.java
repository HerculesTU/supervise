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
import com.housoo.platform.workflow.service.RemindReceiveService;
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
 * 描述 催办接收人信息业务相关Controller
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-11-16 08:50:10
 */
@Controller
@RequestMapping("/workflow/RemindReceiveController")
public class RemindReceiveController extends BaseController {
    /**
     *
     */
    @Resource
    private RemindReceiveService remindReceiveService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除催办接收人信息数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        remindReceiveService.deleteRecords("JBPM6_REMINDRECEIVE", "REMINDRECEIVE_ID", selectColValues.split(","));
        sysLogService.saveBackLog("我的申请", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的催办接收人信息", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改催办接收人信息数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> remindReceive = PlatBeanUtil.getMapFromRequest(request);
        String REMINDRECEIVE_ID = (String) remindReceive.get("REMINDRECEIVE_ID");
        remindReceive = remindReceiveService.saveOrUpdate("JBPM6_REMINDRECEIVE",
                remindReceive, SysConstants.ID_GENERATOR_UUID, null);
        //如果是保存树形结构表数据,请调用下面的接口,而注释掉上面的接口代码
        //remindReceive = remindReceiveService.saveOrUpdateTreeData("JBPM6_REMINDRECEIVE",
        //        remindReceive,SysConstants.ID_GENERATOR_UUID,null);
        if (StringUtils.isNotEmpty(REMINDRECEIVE_ID)) {
            sysLogService.saveBackLog("我的申请", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为[" + REMINDRECEIVE_ID + "]催办接收人信息", request);
        } else {
            REMINDRECEIVE_ID = (String) remindReceive.get("REMINDRECEIVE_ID");
            sysLogService.saveBackLog("我的申请", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为[" + REMINDRECEIVE_ID + "]催办接收人信息", request);
        }
        remindReceive.put("success", true);
        this.printObjectJsonString(remindReceive, response);
    }

    /**
     * 跳转到催办接收人信息表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String REMINDRECEIVE_ID = request.getParameter("REMINDRECEIVE_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> remindReceive = null;
        if (StringUtils.isNotEmpty(REMINDRECEIVE_ID)) {
            remindReceive = this.remindReceiveService.getRecord("JBPM6_REMINDRECEIVE"
                    , new String[]{"REMINDRECEIVE_ID"}, new Object[]{REMINDRECEIVE_ID});
        } else {
            remindReceive = new HashMap<String, Object>();
        }
        request.setAttribute("remindReceive", remindReceive);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
        //如果是跳转到树形表单录入界面,请开放以下代码,,而注释掉上面的代码
        /*String REMINDRECEIVE_ID = request.getParameter("REMINDRECEIVE_ID");
        String REMINDRECEIVE_PARENTID = request.getParameter("REMINDRECEIVE_PARENTID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String,Object> remindReceive = null;
        if(StringUtils.isNotEmpty(REMINDRECEIVE_ID)){
            remindReceive = this.remindReceiveService.getRecord("JBPM6_REMINDRECEIVE"
                    ,new String[]{"REMINDRECEIVE_ID"},new Object[]{REMINDRECEIVE_ID});
            REMINDRECEIVE_PARENTID = (String) remindReceive.get("RemindReceive_PARENTID");
        }
        Map<String,Object> parentRemindReceive = null;
        if(REMINDRECEIVE_PARENTID.equals("0")){
            parentRemindReceive = new HashMap<String,Object>();
            parentRemindReceive.put("REMINDRECEIVE_ID",REMINDRECEIVE_PARENTID);
            parentRemindReceive.put("REMINDRECEIVE_NAME","催办接收人信息树");
        }else{
            parentRemindReceive = this.remindReceiveService.getRecord("JBPM6_REMINDRECEIVE",
                    new String[]{"REMINDRECEIVE_ID"}, new Object[]{REMINDRECEIVE_PARENTID});
        }
        if(remindReceive==null){
            remindReceive = new HashMap<String,Object>();
        }
        remindReceive.put("REMINDRECEIVE_PARENTID",parentRemindReceive.get("REMINDRECEIVE_ID"));
        remindReceive.put("REMINDRECEIVE_PARENTNAME",parentRemindReceive.get("REMINDRECEIVE_NAME"));
        request.setAttribute("remindReceive", remindReceive);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);*/
    }
}
