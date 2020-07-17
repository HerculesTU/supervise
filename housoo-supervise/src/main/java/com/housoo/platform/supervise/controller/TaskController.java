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
import com.housoo.platform.supervise.service.TaskService;


/**
 * 
 * 描述 督办任务业务相关Controller
 *
 * @author tudeodng
 * @version 1.0
 * @created 2020-05-07 10:00:03
 */
@Controller  
@RequestMapping("/supervise/TaskController")  
public class TaskController extends BaseController {
    /**
     * TaskService
     */
    @Resource
    private TaskService taskService;
    /**
     * 系统日志Service
     */
    @Resource
    private SysLogService sysLogService;
    
    /**
     * 删除督办任务数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
            HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        taskService.deleteRecords("TB_SUPERVISE_TASK", "RECORD_ID", selectColValues.split(","));
        sysLogService.saveBackLog("测试模块", SysLogService.OPER_TYPE_DEL,
                "删除了ID为["+selectColValues+"]的督办任务", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }
    
    /**
     * 新增或者修改督办任务数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
            HttpServletResponse response) {
        Map<String,Object> task = PlatBeanUtil.getMapFromRequest(request);
        String RECORD_ID = (String) task.get("RECORD_ID");
        task = taskService.saveOrUpdate("TB_SUPERVISE_TASK",
                task, SysConstants.ID_GENERATOR_UUID,null);
        //如果是保存树形结构表数据,请调用下面的接口,而注释掉上面的接口代码
        //task = taskService.saveOrUpdateTreeData("TB_SUPERVISE_TASK",
        //        task, SysConstants.ID_GENERATOR_UUID,null);
        if(StringUtils.isNotEmpty(RECORD_ID)){
            sysLogService.saveBackLog("测试模块", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为["+RECORD_ID+"]督办任务", request);
        }else{
            RECORD_ID = (String) task.get("RECORD_ID");
            sysLogService.saveBackLog("测试模块", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为["+RECORD_ID+"]督办任务", request);
        }
        task.put("success", true);
        this.printObjectJsonString(task, response);
    }
    
    /**
     * 跳转到督办任务表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String RECORD_ID = request.getParameter("RECORD_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String,Object> task = null;
        if(StringUtils.isNotEmpty(RECORD_ID)){
            task = this.taskService.getRecord("TB_SUPERVISE_TASK"
                    , new String[]{"RECORD_ID"}, new Object[]{RECORD_ID});
        }else{
            task = new HashMap<String,Object>();
        }
        request.setAttribute("task", task);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
        //如果是跳转到树形表单录入界面,请开放以下代码,,而注释掉上面的代码
        /*String RECORD_ID = request.getParameter("RECORD_ID");
        String TASK_PARENTID = request.getParameter("TASK_PARENTID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String,Object> task = null;
        if(StringUtils.isNotEmpty(RECORD_ID)){
            task = this.taskService.getRecord("TB_SUPERVISE_TASK"
                    , new String[]{"RECORD_ID"}, new Object[]{RECORD_ID});
            TASK_PARENTID = (String) task.get("Task_PARENTID");
        }
        Map<String,Object> parentTask = null;
        if(TASK_PARENTID.equals("0")){
            parentTask = new HashMap<String,Object>();
            parentTask.put("RECORD_ID", TASK_PARENTID);
            parentTask.put("TASK_NAME", "督办任务树");
        }else{
            parentTask = this.taskService.getRecord("TB_SUPERVISE_TASK",
                    new String[]{"RECORD_ID"}, new Object[]{TASK_PARENTID});
        }
        if(task==null){
            task = new HashMap<String,Object>();
        }
        task.put("TASK_PARENTID", parentTask.get("RECORD_ID"));
        task.put("TASK_PARENTNAME", parentTask.get("TASK_NAME"));
        request.setAttribute("task", task);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);*/
    }
}
