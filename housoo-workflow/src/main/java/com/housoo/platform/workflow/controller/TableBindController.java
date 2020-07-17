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
import com.housoo.platform.workflow.service.TableBindService;
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
 * 描述 流程列表绑定业务相关Controller
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-12 09:53:34
 */
@Controller
@RequestMapping("/workflow/TableBindController")
public class TableBindController extends BaseController {
    /**
     *
     */
    @Resource
    private TableBindService tableBindService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除流程列表绑定数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        tableBindService.deleteRecords("JBPM6_TABLEBIND", "TABLEBIND_ID", selectColValues.split(","));
        sysLogService.saveBackLog("流程定义管理", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的流程列表绑定", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改流程列表绑定数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> tableBind = PlatBeanUtil.getMapFromRequest(request);
        tableBind = tableBindService.saveOrUpdate("JBPM6_TABLEBIND",
                tableBind, SysConstants.ID_GENERATOR_UUID, null);
        tableBind.put("success", true);
        this.printObjectJsonString(tableBind, response);
    }

    /**
     * 跳转到流程列表绑定表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String TABLEBIND_ID = request.getParameter("TABLEBIND_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        String FLOWDEF_ID = request.getParameter("FLOWDEF_ID");
        Map<String, Object> tableBind = null;
        if (StringUtils.isNotEmpty(TABLEBIND_ID)) {
            tableBind = this.tableBindService.getRecord("JBPM6_TABLEBIND"
                    , new String[]{"TABLEBIND_ID"}, new Object[]{TABLEBIND_ID});
        } else {
            tableBind = new HashMap<String, Object>();
            tableBind.put("TABLEBIND_DEFID", FLOWDEF_ID);
        }
        request.setAttribute("tableBind", tableBind);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }
}
