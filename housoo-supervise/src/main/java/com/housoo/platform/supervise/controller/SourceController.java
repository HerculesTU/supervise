/*
 * Copyright (c) 2000 www.cnnetchina.com All rights reserved.18888
 * ZW PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.housoo.platform.supervise.controller;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.housoo.platform.core.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.supervise.service.SourceService;

/**
 * 描述 督办来源业务相关Controller
 *
 * @author tudeodng
 * @version 1.0
 * @created 2020-05-06 11:23:36
 */
@Controller
@RequestMapping("/supervise/SourceController")
public class SourceController extends BaseController {
    /**
     * SourceService
     */
    @Resource
    private SourceService sourceService;
    /**
     * 系统日志Service
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除督办来源数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        sourceService.deleteRecords("TB_SUPERVISE_SOURCE", "RECORD_ID", selectColValues.split(","));
        sysLogService.saveBackLog("督办来源", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的督办来源", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改督办来源数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> source = PlatBeanUtil.getMapFromRequest(request);
        //获取页面请求的参数
        // Map<String, Object> superviseSource = PlatBeanUtil.getMapFromRequest(request);
        String RECORD_ID = (String) source.get("RECORD_ID");
        if (StringUtils.isNotEmpty(RECORD_ID)) {
            source.put("UPDATE_BY", PlatAppUtil.getBackPlatLoginUser().get("SYSUSER_ID"));
            source.put("UPDATE_TIME", PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        } else {
            source.put("CREATE_BY", PlatAppUtil.getBackPlatLoginUser().get("SYSUSER_ID"));
            source.put("CREATE_TIME", PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            source.put("UPDATE_BY", "");
            source.put("UPDATE_TIME", "");
        }
        source = sourceService.saveOrUpdate("TB_SUPERVISE_SOURCE",
                source, SysConstants.ID_GENERATOR_UUID, null);
        //如果是保存树形结构表数据,请调用下面的接口,而注释掉上面的接口代码
        //source = sourceService.saveOrUpdateTreeData("TB_SUPERVISE_SOURCE",
        //        source, SysConstants.ID_GENERATOR_UUID,null);
        if (StringUtils.isNotEmpty(RECORD_ID)) {
            sysLogService.saveBackLog("督办来源", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为[" + RECORD_ID + "]督办来源", request);
        } else {
            RECORD_ID = (String) source.get("RECORD_ID");
            sysLogService.saveBackLog("督办来源", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为[" + RECORD_ID + "]督办来源", request);
        }
        source.put("success", true);
        this.printObjectJsonString(source, response);
    }

    /**
     * 跳转到督办来源表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String RECORD_ID = request.getParameter("RECORD_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> source = null;
        if (StringUtils.isNotEmpty(RECORD_ID)) {
            source = this.sourceService.getRecord("TB_SUPERVISE_SOURCE"
                    , new String[]{"RECORD_ID"}, new Object[]{RECORD_ID});
        } else {
            source = new HashMap<String, Object>();
        }
        request.setAttribute("source", source);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
        //如果是跳转到树形表单录入界面,请开放以下代码,,而注释掉上面的代码
        /*String RECORD_ID = request.getParameter("RECORD_ID");
        String SOURCE_PARENTID = request.getParameter("SOURCE_PARENTID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String,Object> source = null;
        if(StringUtils.isNotEmpty(RECORD_ID)){
            source = this.sourceService.getRecord("TB_SUPERVISE_SOURCE"
                    , new String[]{"RECORD_ID"}, new Object[]{RECORD_ID});
            SOURCE_PARENTID = (String) source.get("Source_PARENTID");
        }
        Map<String,Object> parentSource = null;
        if(SOURCE_PARENTID.equals("0")){
            parentSource = new HashMap<String,Object>();
            parentSource.put("RECORD_ID", SOURCE_PARENTID);
            parentSource.put("SOURCE_NAME", "督办来源树");
        }else{
            parentSource = this.sourceService.getRecord("TB_SUPERVISE_SOURCE",
                    new String[]{"RECORD_ID"}, new Object[]{SOURCE_PARENTID});
        }
        if(source==null){
            source = new HashMap<String,Object>();
        }
        source.put("SOURCE_PARENTID", parentSource.get("RECORD_ID"));
        source.put("SOURCE_PARENTNAME", parentSource.get("SOURCE_NAME"));
        request.setAttribute("source", source);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);*/
    }
}
