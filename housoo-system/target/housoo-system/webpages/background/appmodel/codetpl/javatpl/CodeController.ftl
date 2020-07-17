/*
 * Copyright (c) 2000 www.cnnetchina.com All rights reserved.18888
 * ZW PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.housoo.platform.${PACK_NAME}.controller;

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
import com.housoo.platform.system.service.SysLogService;
import com.housoo.platform.${PACK_NAME}.service.${CLASS_NAME}Service;

/**
 * 
 * 描述 ${CN_NAME}业务相关Controller
 *
 * @author ${AUTHOR}
 * @version 1.0
 * @created ${FILE_CREATETIME}
 */
@Controller  
@RequestMapping("/${PACK_NAME}/${CLASS_NAME}Controller")  
public class ${CLASS_NAME}Controller extends BaseController {
    /**
     * ${CLASS_NAME}Service
     */
    @Resource
    private ${CLASS_NAME}Service ${CLASS_NAME?uncap_first}Service;
    /**
     * 系统日志Service
     */
    @Resource
    private SysLogService sysLogService;
    
    /**
     * 删除${CN_NAME}数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
            HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        ${CLASS_NAME?uncap_first}Service.deleteRecords("${TABLE_NAME}", "${TABLE_PKNAME}", selectColValues.split(","));
        sysLogService.saveBackLog("${MODULE_NAME}", SysLogService.OPER_TYPE_DEL,
                "删除了ID为["+selectColValues+"]的${CN_NAME}", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }
    
    /**
     * 新增或者修改${CN_NAME}数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
            HttpServletResponse response) {
        Map<String,Object> ${CLASS_NAME?uncap_first} = PlatBeanUtil.getMapFromRequest(request);
        String ${TABLE_PKNAME} = (String) ${CLASS_NAME?uncap_first}.get("${TABLE_PKNAME}");
        ${CLASS_NAME?uncap_first} = ${CLASS_NAME?uncap_first}Service.saveOrUpdate("${TABLE_NAME}",
                ${CLASS_NAME?uncap_first}, SysConstants.ID_GENERATOR_UUID,null);
        //如果是保存树形结构表数据,请调用下面的接口,而注释掉上面的接口代码
        //${CLASS_NAME?uncap_first} = ${CLASS_NAME?uncap_first}Service.saveOrUpdateTreeData("${TABLE_NAME}",
        //        ${CLASS_NAME?uncap_first}, SysConstants.ID_GENERATOR_UUID,null);
        if(StringUtils.isNotEmpty(${TABLE_PKNAME})){
            sysLogService.saveBackLog("${MODULE_NAME}", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为["+${TABLE_PKNAME}+"]${CN_NAME}", request);
        }else{
            ${TABLE_PKNAME} = (String) ${CLASS_NAME?uncap_first}.get("${TABLE_PKNAME}");
            sysLogService.saveBackLog("${MODULE_NAME}", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为["+${TABLE_PKNAME}+"]${CN_NAME}", request);
        }
        ${CLASS_NAME?uncap_first}.put("success", true);
        this.printObjectJsonString(${CLASS_NAME?uncap_first}, response);
    }
    
    /**
     * 跳转到${CN_NAME}表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String ${TABLE_PKNAME} = request.getParameter("${TABLE_PKNAME}");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String,Object> ${CLASS_NAME?uncap_first} = null;
        if(StringUtils.isNotEmpty(${TABLE_PKNAME})){
            ${CLASS_NAME?uncap_first} = this.${CLASS_NAME?uncap_first}Service.getRecord("${TABLE_NAME}"
                    , new String[]{"${TABLE_PKNAME}"}, new Object[]{${TABLE_PKNAME}});
        }else{
            ${CLASS_NAME?uncap_first} = new HashMap<String,Object>();
        }
        request.setAttribute("${CLASS_NAME?uncap_first}", ${CLASS_NAME?uncap_first});
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
        //如果是跳转到树形表单录入界面,请开放以下代码,,而注释掉上面的代码
        /*String ${TABLE_PKNAME} = request.getParameter("${TABLE_PKNAME}");
        String ${CLASS_NAME?upper_case}_PARENTID = request.getParameter("${CLASS_NAME?upper_case}_PARENTID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String,Object> ${CLASS_NAME?uncap_first} = null;
        if(StringUtils.isNotEmpty(${TABLE_PKNAME})){
            ${CLASS_NAME?uncap_first} = this.${CLASS_NAME?uncap_first}Service.getRecord("${TABLE_NAME}"
                    , new String[]{"${TABLE_PKNAME}"}, new Object[]{${TABLE_PKNAME}});
            ${CLASS_NAME?upper_case}_PARENTID = (String) ${CLASS_NAME?uncap_first}.get("${CLASS_NAME}_PARENTID");
        }
        Map<String,Object> parent${CLASS_NAME} = null;
        if(${CLASS_NAME?upper_case}_PARENTID.equals("0")){
            parent${CLASS_NAME} = new HashMap<String,Object>();
            parent${CLASS_NAME}.put("${TABLE_PKNAME}", ${CLASS_NAME?upper_case}_PARENTID);
            parent${CLASS_NAME}.put("${CLASS_NAME?upper_case}_NAME", "${CN_NAME}树");
        }else{
            parent${CLASS_NAME} = this.${CLASS_NAME?uncap_first}Service.getRecord("${TABLE_NAME}",
                    new String[]{"${TABLE_PKNAME}"}, new Object[]{${CLASS_NAME?upper_case}_PARENTID});
        }
        if(${CLASS_NAME?uncap_first}==null){
            ${CLASS_NAME?uncap_first} = new HashMap<String,Object>();
        }
        ${CLASS_NAME?uncap_first}.put("${CLASS_NAME?upper_case}_PARENTID", parent${CLASS_NAME}.get("${TABLE_PKNAME}"));
        ${CLASS_NAME?uncap_first}.put("${CLASS_NAME?upper_case}_PARENTNAME", parent${CLASS_NAME}.get("${CLASS_NAME?upper_case}_NAME"));
        request.setAttribute("${CLASS_NAME?uncap_first}", ${CLASS_NAME?uncap_first});
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);*/
    }
}
