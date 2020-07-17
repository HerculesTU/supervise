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
import com.housoo.platform.supervise.service.OpinionService;

/**
 * 描述 常用意见配置业务相关Controller
 *
 * @author zxl
 * @version 1.0
 * @created 2020-05-09 16:23:23
 */
@Controller
@RequestMapping("/supervise/OpinionController")
public class OpinionController extends BaseController {
    /**
     * OpinionService
     */
    @Resource
    private OpinionService opinionService;
    /**
     * 系统日志Service
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除常用意见配置数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        opinionService.deleteRecords("TB_COMMON_OPINION", "OPINION_ID", selectColValues.split(","));
        sysLogService.saveBackLog("督查督办", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的常用意见配置", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改常用意见配置数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> user = PlatAppUtil.getBackPlatLoginUser();
        Map<String, Object> opinion = PlatBeanUtil.getMapFromRequest(request);
        String OPINION_ID = (String) opinion.get("OPINION_ID");
        opinion.put("OPINION_CREATORID", user.get("SYSUSER_ID"));
        opinion.put("OPINION_CREATETIME", PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        opinion = opinionService.saveOrUpdate("TB_COMMON_OPINION",
                opinion, SysConstants.ID_GENERATOR_UUID, null);
        if (StringUtils.isNotEmpty(OPINION_ID)) {
            sysLogService.saveBackLog("督查督办", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为[" + OPINION_ID + "]常用意见配置", request);
        } else {
            OPINION_ID = (String) opinion.get("OPINION_ID");
            sysLogService.saveBackLog("督查督办", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为[" + OPINION_ID + "]常用意见配置", request);
        }
        opinion.put("success", true);
        this.printObjectJsonString(opinion, response);
    }

    /**
     * 跳转到常用意见选择列表
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goSelector")
    public ModelAndView goSelector(HttpServletRequest request) {
        return PlatUICompUtil.goDesignUI("common_option", request);
    }

    /**
     * 跳转到常用意见配置表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String OPINION_ID = request.getParameter("OPINION_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> opinion = null;
        if (StringUtils.isNotEmpty(OPINION_ID)) {
            opinion = this.opinionService.getRecord("TB_COMMON_OPINION"
                    , new String[]{"OPINION_ID"}, new Object[]{OPINION_ID});
        } else {
            opinion = new HashMap<String, Object>();
        }
        request.setAttribute("opinion", opinion);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }
}
