/*
 * Copyright (c) 2000 www.cnnetchina.com All rights reserved.18888
 * ZW PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.housoo.platform.demo.controller;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.core.util.PlatDateTimeUtil;
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
import com.housoo.platform.demo.service.ProjectService;

/**
 * 
 * 描述 项目信息业务相关Controller
 *
 * @author gf
 * @version 1.0
 * @created 2020-03-11 16:34:21
 */
@Controller  
@RequestMapping("/demo/ProjectController")  
public class ProjectController extends BaseController {
    /**
     * ProjectService
     */
    @Resource
    private ProjectService projectService;
    /**
     * 系统日志Service
     */
    @Resource
    private SysLogService sysLogService;
    
    /**
     * 删除项目信息数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
            HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        projectService.deleteRecords("PLAT_DEMO_PROJECT", "PROJECT_ID", selectColValues.split(","));
        sysLogService.saveBackLog("流程例子模块", SysLogService.OPER_TYPE_DEL,
                "删除了ID为["+selectColValues+"]的项目信息", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }
    
    /**
     * 新增或者修改项目信息数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
            HttpServletResponse response) {
        Map<String,Object> project = PlatBeanUtil.getMapFromRequest(request);
        project.put("CREATE_TIME", PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        String PROJECT_ID = (String) project.get("PROJECT_ID");
        project = projectService.saveOrUpdate("PLAT_DEMO_PROJECT",
                project, SysConstants.ID_GENERATOR_UUID,null);
        //如果是保存树形结构表数据,请调用下面的接口,而注释掉上面的接口代码
        //project = projectService.saveOrUpdateTreeData("PLAT_DEMO_PROJECT",
        //        project, SysConstants.ID_GENERATOR_UUID,null);
        if(StringUtils.isNotEmpty(PROJECT_ID)){
            sysLogService.saveBackLog("流程例子模块", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为["+PROJECT_ID+"]项目信息", request);
        }else{
            PROJECT_ID = (String) project.get("PROJECT_ID");
            sysLogService.saveBackLog("流程例子模块", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为["+PROJECT_ID+"]项目信息", request);
        }
        project.put("success", true);
        this.printObjectJsonString(project, response);
    }
    
    /**
     * 跳转到项目信息表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String PROJECT_ID = request.getParameter("PROJECT_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String,Object> project = null;
        if(StringUtils.isNotEmpty(PROJECT_ID)){
            project = this.projectService.getRecord("PLAT_DEMO_PROJECT"
                    , new String[]{"PROJECT_ID"}, new Object[]{PROJECT_ID});
        }else{
            project = new HashMap<String,Object>();
        }
        request.setAttribute("project", project);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
        //如果是跳转到树形表单录入界面,请开放以下代码,,而注释掉上面的代码
        /*String PROJECT_ID = request.getParameter("PROJECT_ID");
        String PROJECT_PARENTID = request.getParameter("PROJECT_PARENTID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String,Object> project = null;
        if(StringUtils.isNotEmpty(PROJECT_ID)){
            project = this.projectService.getRecord("PLAT_DEMO_PROJECT"
                    , new String[]{"PROJECT_ID"}, new Object[]{PROJECT_ID});
            PROJECT_PARENTID = (String) project.get("Project_PARENTID");
        }
        Map<String,Object> parentProject = null;
        if(PROJECT_PARENTID.equals("0")){
            parentProject = new HashMap<String,Object>();
            parentProject.put("PROJECT_ID", PROJECT_PARENTID);
            parentProject.put("PROJECT_NAME", "项目信息树");
        }else{
            parentProject = this.projectService.getRecord("PLAT_DEMO_PROJECT",
                    new String[]{"PROJECT_ID"}, new Object[]{PROJECT_PARENTID});
        }
        if(project==null){
            project = new HashMap<String,Object>();
        }
        project.put("PROJECT_PARENTID", parentProject.get("PROJECT_ID"));
        project.put("PROJECT_PARENTNAME", parentProject.get("PROJECT_NAME"));
        request.setAttribute("project", project);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);*/
    }
}
