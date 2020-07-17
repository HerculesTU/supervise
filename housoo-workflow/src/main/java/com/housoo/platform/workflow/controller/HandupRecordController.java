/*
 * Copyright (c) 2005, 2014, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.controller;

import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.workflow.service.HandupRecordService;
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
 * 描述 挂起任务业务相关Controller
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-06-01 09:29:36
 */
@Controller
@RequestMapping("/workflow/HandupRecordController")
public class HandupRecordController extends BaseController {
    /**
     *
     */
    @Resource
    private HandupRecordService handupRecordService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除挂起任务数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "restart")
    public void restart(HttpServletRequest request,
                        HttpServletResponse response) {
        String taskIds = request.getParameter("selectColValues");
        handupRecordService.restartJbpmTasks(taskIds);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改挂起任务数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> handupRecord = PlatBeanUtil.getMapFromRequest(request);
        handupRecord = handupRecordService.saveOrUpdate("JBPM6_HANDUPRECORD",
                handupRecord, SysConstants.ID_GENERATOR_UUID, null);
        handupRecord.put("success", true);
        this.printObjectJsonString(handupRecord, response);
    }

    /**
     * 跳转到挂起任务表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String RECORD_ID = request.getParameter("RECORD_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> handupRecord = null;
        if (StringUtils.isNotEmpty(RECORD_ID)) {
            handupRecord = this.handupRecordService.getRecord("JBPM6_HANDUPRECORD"
                    , new String[]{"RECORD_ID"}, new Object[]{RECORD_ID});
        } else {
            handupRecord = new HashMap<String, Object>();
        }
        request.setAttribute("handupRecord", handupRecord);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }

    /**
     * 重启流程任务
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "startHandUp")
    public void startHandUp(HttpServletRequest request,
                            HttpServletResponse response) {
        Map<String, Object> handupRecord = PlatBeanUtil.getMapFromRequest(request);
        handupRecord.put("success", true);
        this.printObjectJsonString(handupRecord, response);
    }

    /**
     * 判断是否允许挂起
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "allowHandleUp")
    public void allowHandleUp(HttpServletRequest request,
                              HttpServletResponse response) {
        String exeId = request.getParameter("jbpmExeId");
        String taskId = request.getParameter("jbpmOperingTaskId");
        Map<String, Object> jbpmTask = handupRecordService.getRecord("JBPM6_TASK",
                new String[]{"TASK_ID"}, new Object[]{taskId});
        String nodeKey = "";
        if (jbpmTask != null) {
            nodeKey = (String) jbpmTask.get("TASK_NODEKEY");
        }
        Map<String, Object> sysUser = PlatAppUtil.getBackPlatLoginUser();
        String sysUserId = (String) sysUser.get("SYSUSER_ID");
        Map<String, Object> result = new HashMap<String, Object>();
        boolean isExists = this.handupRecordService.isExists(exeId, sysUserId, nodeKey);
        if (isExists) {
            result.put("success", false);
        } else {
            result.put("success", true);
        }
        this.printObjectJsonString(result, response);
    }
}
