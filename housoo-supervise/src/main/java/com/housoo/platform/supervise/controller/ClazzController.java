/*
 * Copyright (c) 2000 www.cnnetchina.com All rights reserved.18888
 * ZW PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.housoo.platform.supervise.controller;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.housoo.platform.core.util.PlatAppUtil;
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
import com.housoo.platform.supervise.service.ClazzService;

/**
 * 
 * 描述 督办类型业务相关Controller
 *
 * @author tudeodng
 * @version 1.0
 * @created 2020-05-06 14:36:28
 */
@Controller  
@RequestMapping("/supervise/ClazzController")  
public class ClazzController extends BaseController {
    /**
     * ClazzService
     */
    @Resource
    private ClazzService clazzService;
    /**
     * 系统日志Service
     */
    @Resource
    private SysLogService sysLogService;
    
    /**
     * 删除督办类型数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
            HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        clazzService.deleteRecords("TB_SUPERVISE_CLAZZ", "RECORD_ID", selectColValues.split(","));
        sysLogService.saveBackLog("督办类型", SysLogService.OPER_TYPE_DEL,
                "删除了ID为["+selectColValues+"]的督办类型", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }
    
    /**
     * 新增或者修改督办类型数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
            HttpServletResponse response) {
        Map<String,Object> clazz = PlatBeanUtil.getMapFromRequest(request);
        String RECORD_ID = (String) clazz.get("RECORD_ID");
        Map<String, Object> loginUser = PlatAppUtil.getBackPlatLoginUser();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (StringUtils.isNotEmpty(RECORD_ID)) {
            clazz.put("update_by", loginUser.get("SYSUSER_NAME"));
            clazz.put("update_time", simpleDateFormat.format(new Date()));
        } else {
            clazz.put("create_by", loginUser.get("SYSUSER_NAME"));
            clazz.put("create_time", simpleDateFormat.format(new Date()));
            clazz.put("update_by", "");
            clazz.put("update_time", "");
        }
        clazz = clazzService.saveOrUpdate("TB_SUPERVISE_CLAZZ",
                clazz, SysConstants.ID_GENERATOR_UUID,null);
        //如果是保存树形结构表数据,请调用下面的接口,而注释掉上面的接口代码
        //clazz = clazzService.saveOrUpdateTreeData("TB_SUPERVISE_CLAZZ",
        //        clazz, SysConstants.ID_GENERATOR_UUID,null);
        if(StringUtils.isNotEmpty(RECORD_ID)){
            sysLogService.saveBackLog("督办类型", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为["+RECORD_ID+"]督办类型", request);
        }else{
            RECORD_ID = (String) clazz.get("RECORD_ID");
            sysLogService.saveBackLog("督办类型", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为["+RECORD_ID+"]督办类型", request);
        }
        clazz.put("success", true);
        this.printObjectJsonString(clazz, response);
    }
    
    /**
     * 跳转到督办类型表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String RECORD_ID = request.getParameter("RECORD_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String,Object> clazz = null;
        if(StringUtils.isNotEmpty(RECORD_ID)){
            clazz = this.clazzService.getRecord("TB_SUPERVISE_CLAZZ"
                    , new String[]{"RECORD_ID"}, new Object[]{RECORD_ID});
        }else{
            clazz = new HashMap<String,Object>();
        }
        request.setAttribute("clazz", clazz);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
        //如果是跳转到树形表单录入界面,请开放以下代码,,而注释掉上面的代码
        /*String RECORD_ID = request.getParameter("RECORD_ID");
        String CLAZZ_PARENTID = request.getParameter("CLAZZ_PARENTID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String,Object> clazz = null;
        if(StringUtils.isNotEmpty(RECORD_ID)){
            clazz = this.clazzService.getRecord("TB_SUPERVISE_CLAZZ"
                    , new String[]{"RECORD_ID"}, new Object[]{RECORD_ID});
            CLAZZ_PARENTID = (String) clazz.get("Clazz_PARENTID");
        }
        Map<String,Object> parentClazz = null;
        if(CLAZZ_PARENTID.equals("0")){
            parentClazz = new HashMap<String,Object>();
            parentClazz.put("RECORD_ID", CLAZZ_PARENTID);
            parentClazz.put("CLAZZ_NAME", "督办类型树");
        }else{
            parentClazz = this.clazzService.getRecord("TB_SUPERVISE_CLAZZ",
                    new String[]{"RECORD_ID"}, new Object[]{CLAZZ_PARENTID});
        }
        if(clazz==null){
            clazz = new HashMap<String,Object>();
        }
        clazz.put("CLAZZ_PARENTID", parentClazz.get("RECORD_ID"));
        clazz.put("CLAZZ_PARENTNAME", parentClazz.get("CLAZZ_NAME"));
        request.setAttribute("clazz", clazz);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);*/
    }
}
