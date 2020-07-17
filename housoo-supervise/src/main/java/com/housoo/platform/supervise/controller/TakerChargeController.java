/*
 * Copyright (c) 2000 www.cnnetchina.com All rights reserved.18888
 * ZW PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.housoo.platform.supervise.controller;

import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatDateTimeUtil;
import com.housoo.platform.supervise.service.SuperviseClazzService;
import com.housoo.platform.supervise.service.SuperviseMqService;
import com.housoo.platform.supervise.service.TakerChargeService;
import com.housoo.platform.supervise.service.TakerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述 承办部门负责人业务相关Controller
 *
 * @author cjr
 * @version 1.0
 * @created 2020-04-14 15:30:11
 */
@Controller
@RequestMapping("/supervise/TakerChargeController")
public class TakerChargeController extends BaseController {
    /**
     * SuperviseService
     */
    @Resource
    private TakerChargeService takerChargeService;
    /**
     * SuperviseClazzService
     */
    @Resource
    private SuperviseClazzService superviseClazzService;
    /**
     * SuperviseMqService
     */
    @Resource
    private SuperviseMqService superviseMqService;
    /**
     * 系统日志Service
     */
    @Resource
    private SysLogService sysLogService;


    /*--------------------------------------督办事项列表相关-------------------------------------*/

    /**
     * 跳转到督办事项列表页（承办部门负责人）
     *
     * @param request
     */
    @RequestMapping(params = "goTakerSuperviseList")
    public ModelAndView goTakerSuperviseList(HttpServletRequest request) {
        String clazz = request.getParameter("clazz");
        request.setAttribute("clazz", clazz);
        return new ModelAndView("background/supervise/takerChargeMatter");
    }

    /**
     * 获取承办部门负责人承办任务列表
     * cjr
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "findTakerSuperviseList")
    public void findTakerSuperviseList(HttpServletRequest request,
                                       HttpServletResponse response) {
        List<Map<String, Object>> list = this.takerChargeService.findTakerSuperviseList(request);
        this.printObjectJsonString(list, response);
    }

    /**
     * 跳转到督办事项详情页（承办部门负责人）
     *
     * @param request
     */
    @RequestMapping(params = "goTakerSuperviseInfo")
    public ModelAndView goTakerSuperviseInfo(HttpServletRequest request) {
        String isRead = request.getParameter("isRead");
        String nodeId = request.getParameter("nodeId");
        String id = request.getParameter("id");
        request.setAttribute("nodeId", nodeId);
        request.setAttribute("isRead", isRead);
        //根据ID获取督办事项详情
        Map<String, Object> supervise = takerChargeService.getRecord("tb_supervise", new String[]{"RECORD_ID"}, new Object[]{id});
        //获取督办部门
        String dbDepartName = (String) takerChargeService.getRecord("plat_system_depart", new String[]{"DEPART_ID"}, new Object[]{supervise.get("DEPART_ID")}).get("DEPART_NAME");
        supervise.put("DEPART_NAME", dbDepartName);
        /*//根据节点ID 获取最近一次反馈内容
        Map<String, Object> feedback = this.takerChargeService.getFeedbackInfo(id, nodeId, "DESC");
        request.setAttribute("feedback", feedback);

        long intervalTime = PlatDateTimeUtil.getIntervalTime((String) supervise.get("CREATE_TIME"),
                PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
        if (intervalTime <= 72) {
            long days = (72 - intervalTime) / 24;
            long hours = (72 - intervalTime) - days * 24;
            request.setAttribute("restTime1", "还剩" + days + "天" + hours + "小时");
        } else {
            request.setAttribute("restTime1", "已逾期");
        }
        //获取落实反馈的剩余期限
        intervalTime = PlatDateTimeUtil.getIntervalTime((String) supervise.get("CREATE_TIME"),
                PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
        if ("4".equals(nodeId)) {
            feedback = this.takerChargeService.getFeedbackInfo(id, "3", "ASC");
        }
        if ("5".equals(nodeId)) {
            feedback = this.takerChargeService.getFeedbackInfo(id, "4", "ASC");
        }
        if (feedback != null) {
            intervalTime = PlatDateTimeUtil.getIntervalTime((String) supervise.get("CREATE_TIME"),
                    (String) feedback.get("CREATE_TIME"), "yyyy-MM-dd HH:mm:ss", 2);
        }
        if (intervalTime <= Integer.parseInt(String.valueOf(supervise.get("HANDLE_LIMIT"))) * 24) {
            long days = (Integer.parseInt(String.valueOf(supervise.get("HANDLE_LIMIT"))) * 24 - intervalTime) / 24;
            long hours = (Integer.parseInt(String.valueOf(supervise.get("HANDLE_LIMIT"))) * 24 - intervalTime) - days * 24;
            request.setAttribute("restTime2", "还剩" + String.valueOf(days + "天" + hours + "小时"));//获取落实反馈的剩余期限
            request.setAttribute("restTime3", "还剩" + String.valueOf(days + "天" + hours + "小时"));//获取办结反馈的剩余期限
        } else {
            request.setAttribute("restTime2", "已逾期");
            request.setAttribute("restTime3", "已逾期");
        }

        //获取督办确认内容
        Map<String, Object> confirm = takerChargeService.getRecord("tb_supervise_confirm", new String[]{"SUPERVISE_ID", "NODE_ID"}, new Object[]{supervise.get("RECORD_ID"), nodeId});
        request.setAttribute("confirm", confirm);
        //判断督办确认时限
        intervalTime = PlatDateTimeUtil.getIntervalTime((String) supervise.get("CREATE_TIME"),
                PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
        if (intervalTime <= 24) {
            long days = (24 - intervalTime) / 24;
            long hours = (24 - intervalTime) - days * 24;
            request.setAttribute("restTime4", "还剩" + String.valueOf(days + "天" + hours + "小时"));
        } else {
            request.setAttribute("restTime4", "已逾期");
        }
        request.setAttribute("supervise", supervise);
        request.setAttribute("parentFrameId", request.getParameter("parentFrameId"));
*/
        request.setAttribute("supervise", supervise);
        request.setAttribute("parentFrameId", request.getParameter("parentFrameId"));

        int HANDLE_LIMIT = (Integer) supervise.get("HANDLE_LIMIT");//办理时限

        long intervalTime = 0L;
        long intervalTime1 = 0L;
        long intervalTime2 = 0L;
        if ("2".equals(nodeId)) {
            intervalTime = PlatDateTimeUtil.getIntervalTime((String) supervise.get("CREATE_TIME"),
                    PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
            //获取督办确认内容
            Map<String, Object> confirm = takerChargeService.getRecord("tb_supervise_confirm", new String[]{"SUPERVISE_ID", "NODE_ID"}, new Object[]{supervise.get("RECORD_ID"), nodeId});
            request.setAttribute("confirm", confirm);
            //判断督办确认时限
            intervalTime = PlatDateTimeUtil.getIntervalTime((String) supervise.get("CREATE_TIME"),
                    PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
            if (intervalTime <= 24) {
                long days = (24 - intervalTime) / 24;
                long hours = (24 - intervalTime) - days * 24;
                request.setAttribute("restTime4", "还剩" + String.valueOf(days + "天" + hours + "小时"));
            } else {
                request.setAttribute("restTime4", "已逾期");
            }
        }
        if ("3".equals(nodeId)) {
            Map<String, Object> feedback = this.takerChargeService.getFeedbackInfo(id, nodeId, "DESC");
            request.setAttribute("feedback", feedback);
            intervalTime1 = PlatDateTimeUtil.getIntervalTime((String) supervise.get("CREATE_TIME"),
                    PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
            if (intervalTime1 <= (HANDLE_LIMIT * 24 - intervalTime)) {
                long days = (HANDLE_LIMIT * 24 - intervalTime1) / 24;
                long hours = (HANDLE_LIMIT * 24 - intervalTime1) - days * 24;
                request.setAttribute("restTime1", "还剩" + days + "天" + hours + "小时");
            } else {
                request.setAttribute("restTime1", "已逾期");
            }
        }
        if ("5".equals(nodeId)) {
            Map<String, Object> feedback = this.takerChargeService.getFeedbackInfo(id, nodeId, "DESC");
            request.setAttribute("feedback", feedback);
            intervalTime2 = PlatDateTimeUtil.getIntervalTime((String) supervise.get("CREATE_TIME"),
                    PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
            if (intervalTime2 <= (HANDLE_LIMIT * 24 - intervalTime - intervalTime1)) {
                long days = (HANDLE_LIMIT * 24 - intervalTime - intervalTime1) / 24;
                long hours = (HANDLE_LIMIT * 24 - intervalTime - intervalTime1) - days * 24;
                request.setAttribute("restTime2", "还剩" + days + "天" + hours + "小时");
            } else {
                request.setAttribute("restTime2", "已逾期");
            }
        }
        return new ModelAndView("background/supervise/approve");
    }

    /**
     * 跳转到事项办结 督办详情页（承办部门负责人）
     *
     * @param request
     */
    @RequestMapping(params = "goTakerSupEndInfo")
    public ModelAndView goTakerSupEndInfo(HttpServletRequest request) {
        String id = request.getParameter("id");
        //根据ID获取督办事项详情
        Map<String, Object> supervise = takerChargeService.getRecord("tb_supervise", new String[]{"RECORD_ID"}, new Object[]{id});
        //获取督办部门
        String dbDepartName = (String) takerChargeService.getRecord("plat_system_depart", new String[]{"DEPART_ID"}, new Object[]{supervise.get("DEPART_ID")}).get("DEPART_NAME");
        supervise.put("DEPART_NAME", dbDepartName);
        request.setAttribute("supervise", supervise);
        //获取最新的反馈信息
        Map<String, Object> blfk = takerChargeService.getFeedbackInfo(id, "3", "DESC");
        request.setAttribute("blfk", blfk);
        Map<String, Object> lsfk = takerChargeService.getFeedbackInfo(id, "4", "DESC");
        request.setAttribute("lsfk", lsfk);
        Map<String, Object> bjfk = takerChargeService.getFeedbackInfo(id, "5", "DESC");
        request.setAttribute("bjfk", bjfk);
        return new ModelAndView("background/supervise/takerSupEnd");
    }

    /*--------------------------------------督办审批相关-------------------------------------*/

    /**
     * 承办部门负责人审批
     * cjr
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "takerChargeApprove")
    public void takerChargeApprove(HttpServletRequest request,
                                    HttpServletResponse response) {
        Map<String, Object> result = this.takerChargeService.takerChargeApprove(request);
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 督办流程节点联动右侧反馈内容
     * cjr
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "loadTakerFeedbackInfo")
    public void loadTakerFeedbackInfo(HttpServletRequest request,
                                      HttpServletResponse response) {
        Map<String, Object> map = this.takerChargeService.loadTakerFeedbackInfo(request);
        map.put("success", true);
        this.printObjectJsonString(map, response);
    }

}
