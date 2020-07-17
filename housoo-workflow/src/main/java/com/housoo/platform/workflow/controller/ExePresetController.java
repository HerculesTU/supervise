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
import com.housoo.platform.workflow.service.ExePresetService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述 实例预设办理人业务相关Controller
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-12-26 09:04:38
 */
@Controller
@RequestMapping("/workflow/ExePresetController")
public class ExePresetController extends BaseController {
    /**
     *
     */
    @Resource
    private ExePresetService exePresetService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除实例预设办理人数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        exePresetService.deleteRecords("JBPM6_EXEPRESET", "EXEPRESET_ID", selectColValues.split(","));
        sysLogService.saveBackLog("流程监控管理", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的实例预设办理人", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改实例预设办理人数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> exePreset = PlatBeanUtil.getMapFromRequest(request);
        String EXEPRESET_EXEID = (String) exePreset.get("EXEPRESET_EXEID");
        Map<String, Object> oldExePreSet = exePresetService.
                getRecord("JBPM6_EXEPRESET", new String[]{"EXEPRESET_EXEID"},
                        new Object[]{EXEPRESET_EXEID});
        if (oldExePreSet != null) {
            exePreset.put("EXEPRESET_ID", oldExePreSet.get("EXEPRESET_ID"));
        }
        exePreset = exePresetService.saveOrUpdate("JBPM6_EXEPRESET",
                exePreset, SysConstants.ID_GENERATOR_UUID, null);
        exePreset.put("success", true);
        this.printObjectJsonString(exePreset, response);
    }

    /**
     * 跳转到实例预设办理人表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String EXEID = request.getParameter("EXEID");
        request.setAttribute("EXEID", EXEID);
        return PlatUICompUtil.goDesignUI("expresetlist", request);
    }
}
