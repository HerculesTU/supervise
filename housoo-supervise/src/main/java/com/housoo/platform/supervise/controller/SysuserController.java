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
import com.housoo.platform.supervise.service.SysuserService;

/**
 * 
 * 描述 系统用户业务相关Controller
 *
 * @author tudeodng
 * @version 1.0
 * @created 2020-05-07 08:46:27
 */
@Controller  
@RequestMapping("/system/SysuserController")  
public class SysuserController extends BaseController {
    /**
     * SysuserService
     */
    @Resource
    private SysuserService sysuserService;
    /**
     * 系统日志Service
     */
    @Resource
    private SysLogService sysLogService;
    
    /**
     * 删除系统用户数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
            HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        sysuserService.deleteRecords("PLAT_SYSTEM_SYSUSER", "SYSUSER_ID", selectColValues.split(","));
        sysLogService.saveBackLog("督办环节", SysLogService.OPER_TYPE_DEL,
                "删除了ID为["+selectColValues+"]的系统用户", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }
    
    /**
     * 新增或者修改系统用户数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
            HttpServletResponse response) {
        Map<String,Object> sysuser = PlatBeanUtil.getMapFromRequest(request);
        String SYSUSER_ID = (String) sysuser.get("SYSUSER_ID");
        sysuser = sysuserService.saveOrUpdate("PLAT_SYSTEM_SYSUSER",
                sysuser, SysConstants.ID_GENERATOR_UUID,null);
        //如果是保存树形结构表数据,请调用下面的接口,而注释掉上面的接口代码
        //sysuser = sysuserService.saveOrUpdateTreeData("PLAT_SYSTEM_SYSUSER",
        //        sysuser, SysConstants.ID_GENERATOR_UUID,null);
        if(StringUtils.isNotEmpty(SYSUSER_ID)){
            sysLogService.saveBackLog("督办环节", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为["+SYSUSER_ID+"]系统用户", request);
        }else{
            SYSUSER_ID = (String) sysuser.get("SYSUSER_ID");
            sysLogService.saveBackLog("督办环节", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为["+SYSUSER_ID+"]系统用户", request);
        }
        sysuser.put("success", true);
        this.printObjectJsonString(sysuser, response);
    }
    
    /**
     * 跳转到系统用户表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String SYSUSER_ID = request.getParameter("SYSUSER_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String,Object> sysuser = null;
        if(StringUtils.isNotEmpty(SYSUSER_ID)){
            sysuser = this.sysuserService.getRecord("PLAT_SYSTEM_SYSUSER"
                    , new String[]{"SYSUSER_ID"}, new Object[]{SYSUSER_ID});
        }else{
            sysuser = new HashMap<String,Object>();
        }
        request.setAttribute("sysuser", sysuser);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
        //如果是跳转到树形表单录入界面,请开放以下代码,,而注释掉上面的代码
        /*String SYSUSER_ID = request.getParameter("SYSUSER_ID");
        String SYSUSER_PARENTID = request.getParameter("SYSUSER_PARENTID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String,Object> sysuser = null;
        if(StringUtils.isNotEmpty(SYSUSER_ID)){
            sysuser = this.sysuserService.getRecord("PLAT_SYSTEM_SYSUSER"
                    , new String[]{"SYSUSER_ID"}, new Object[]{SYSUSER_ID});
            SYSUSER_PARENTID = (String) sysuser.get("Sysuser_PARENTID");
        }
        Map<String,Object> parentSysuser = null;
        if(SYSUSER_PARENTID.equals("0")){
            parentSysuser = new HashMap<String,Object>();
            parentSysuser.put("SYSUSER_ID", SYSUSER_PARENTID);
            parentSysuser.put("SYSUSER_NAME", "系统用户树");
        }else{
            parentSysuser = this.sysuserService.getRecord("PLAT_SYSTEM_SYSUSER",
                    new String[]{"SYSUSER_ID"}, new Object[]{SYSUSER_PARENTID});
        }
        if(sysuser==null){
            sysuser = new HashMap<String,Object>();
        }
        sysuser.put("SYSUSER_PARENTID", parentSysuser.get("SYSUSER_ID"));
        sysuser.put("SYSUSER_PARENTNAME", parentSysuser.get("SYSUSER_NAME"));
        request.setAttribute("sysuser", sysuser);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);*/
    }
}
