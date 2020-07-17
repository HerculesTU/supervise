/*
 * Copyright (c) 2000 www.cnnetchina.com All rights reserved.18888
 * ZW PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.housoo.platform.supervise.controller;

import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.supervise.service.SuperviseClazzService;
import com.housoo.platform.supervise.service.SuperviseMqService;
import com.housoo.platform.supervise.service.SuperviseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 描述 督查督办业务相关Controller
 *
 * @author zxl
 * @version 1.0
 * @created 2020-04-14 15:30:11
 */
@Controller
@RequestMapping("/supervise/SuperviseController")
public class SuperviseController extends BaseController {
    /**
     * SuperviseService
     */
    @Resource
    private SuperviseService superviseService;
    /**
     * SuperviseClazzService
     */
    @Resource
    private SuperviseClazzService superviseClazzService;
    /**
     * 系统日志Service
     */
    @Resource
    private SysLogService sysLogService;
    /**
     * SuperviseMqService
     */
    @Resource
    private SuperviseMqService superviseMqService;

    /**
     * 删除督查督办数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        superviseService.deleteRecords("TB_SUPERVISE", "RECORD_ID", selectColValues.split(","));
        sysLogService.saveBackLog("督查督办", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的督查督办", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        result.put("parentFrameId", request.getParameter("parentFrameId"));
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改督查督办数据
     * 说明：
     * 在提交表单时 需要验证的信息：
     * 若 督查督办 需要审批 验证所选部门是否有承办人 审批人
     * 若 督察督办 不需要审批 验证所选部门是否有承办人
     * 需要在四张表中存储信息 tb_supervise tb_supervise_task tb_supervise_node_info plat_system_fileattach
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> supervise = PlatBeanUtil.getMapFromRequest(request);
        Map<String, Object> result = new HashMap();
        String RECORD_ID = (String) supervise.get("RECORD_ID");
        //校验信息
        boolean flag = superviseService.verifySuperviseInfo(request.getParameter("TAKER_DEPART_ID"));
        if (flag) {
            supervise = superviseService.saveSuperviseInfo(supervise);
            //推送到消息队列
            /*Map<Object, Object> pushInfo = superviseMqService.pushSponsorCreateInfo(supervise);
            if ((Boolean) pushInfo.get("success")) {
                result.put("success", true);
            } else {
                result.put("msg", "消息推送失败！");
                result.put("success", false);
            }*/
            result.put("success", true);
            if (StringUtils.isNotEmpty(RECORD_ID)) {
                sysLogService.saveBackLog("督查督办", SysLogService.OPER_TYPE_EDIT,
                        "修改了ID为[" + RECORD_ID + "]督查督办", request);
            } else {
                RECORD_ID = (String) supervise.get("RECORD_ID");
                sysLogService.saveBackLog("督查督办", SysLogService.OPER_TYPE_ADD,
                        "新增了ID为[" + RECORD_ID + "]督查督办", request);
            }

        } else {
            result.put("success", false);
            result.put("msg", "当前承办单位未设置承办人或审批人，请检查！");
        }
        this.printObjectJsonString(result, response);
    }

    /**
     * 跳转到督查督办表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String RECORD_ID = request.getParameter("RECORD_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> supervise = null;
        if (StringUtils.isNotEmpty(RECORD_ID)) {
            supervise = this.superviseService.getRecord("TB_SUPERVISE"
                    , new String[]{"RECORD_ID"}, new Object[]{RECORD_ID});
        } else {
            supervise = new HashMap<String, Object>();
        }
        request.setAttribute("supervise", supervise);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }

    /**
     * 督查督办信息存储为草稿信息
     */
    @RequestMapping(params = "saveDraftInfo")
    public void saveDraftInfo(HttpServletRequest request,
                              HttpServletResponse response) {
        Map<String, Object> supervise = PlatBeanUtil.getMapFromRequest(request);

        Iterator<String> iter = supervise.keySet().iterator();
        String value = "";
        boolean flag = false;
        while (iter.hasNext()) {
            value = (String) supervise.get(iter.next());
            if (StringUtils.isNotEmpty(value)) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            supervise.put("success", false);
            supervise.put("msg", "未录入信息！");
        } else {
            //草稿状态
            supervise.put("STATUS", "0");
            supervise = superviseService.saveDraftInfo(supervise);
            supervise.put("success", true);
        }
        this.printObjectJsonString(supervise, response);
    }

    /**
     * 根据督办事项ID初始化督办事项的督办流程
     * cjr
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "initSuperviseProgress")
    public void initSuperviseProgress(HttpServletRequest request,
                                      HttpServletResponse response) {
        List<Map<String, Object>> list = this.superviseService.initSuperviseProgress(request);
        this.printObjectJsonString(list, response);
    }

    /**
     * 获取指定用户建立的所有督察督办事项
     */
    @RequestMapping(params = "getSuperviseInfo")
    public void getSuperviseInfo(HttpServletRequest request,
                                 HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        String userId = request.getParameter("userId");
        String clazzId = request.getParameter("clazzId");
        String status = request.getParameter("status");
        List<List<Map<String, Object>>> superviseInfo = superviseService.getSuperviseInfo(userId, clazzId, status);
        result.put("superviseInfo", superviseInfo);
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

}
