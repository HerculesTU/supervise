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
import com.housoo.platform.workflow.service.TimeLimitService;
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
 * 描述 环节时限业务相关Controller
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-10 10:06:52
 */
@Controller
@RequestMapping("/workflow/TimeLimitController")
public class TimeLimitController extends BaseController {
    /**
     *
     */
    @Resource
    private TimeLimitService timeLimitService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除环节时限数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        timeLimitService.deleteRecords("JBPM6_TIMELIMIT", "TIMELIMIT_ID", selectColValues.split(","));
        sysLogService.saveBackLog("流程定义管理", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的环节时限", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改环节时限数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> timeLimit = PlatBeanUtil.getMapFromRequest(request);
        String START_NODENAME = (String) timeLimit.get("START_NODEKEY_LABELS");
        String END_NODENAME = (String) timeLimit.get("END_NODEKEY_LABELS");
        String MIDDLE_NODENAMES = (String) timeLimit.get("MIDDLE_NODEKEYS_LABELS");
        timeLimit.put("START_NODENAME", START_NODENAME);
        timeLimit.put("END_NODENAME", END_NODENAME);
        timeLimit.put("MIDDLE_NODENAMES", MIDDLE_NODENAMES);
        timeLimit = timeLimitService.saveOrUpdate("JBPM6_TIMELIMIT",
                timeLimit, SysConstants.ID_GENERATOR_UUID, null);
        timeLimit.put("success", true);
        this.printObjectJsonString(timeLimit, response);
    }

    /**
     * 跳转到环节时限表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String TIMELIMIT_ID = request.getParameter("TIMELIMIT_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        String FLOWDEF_ID = request.getParameter("FLOWDEF_ID");
        String FLOWDEF_VERSION = request.getParameter("FLOWDEF_VERSION");
        Map<String, Object> timeLimit = null;
        if (StringUtils.isNotEmpty(TIMELIMIT_ID)) {
            timeLimit = this.timeLimitService.getRecord("JBPM6_TIMELIMIT"
                    , new String[]{"TIMELIMIT_ID"}, new Object[]{TIMELIMIT_ID});
        } else {
            timeLimit = new HashMap<String, Object>();
            timeLimit.put("TIMELIMIT_FLOWDEFID", FLOWDEF_ID);
        }
        timeLimit.put("FLOWDEF_VERSION", FLOWDEF_VERSION);
        request.setAttribute("timeLimit", timeLimit);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }
}
