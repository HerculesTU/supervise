/*
 * Copyright (c) 2000 www.cnnetchina.com All rights reserved.18888
 * ZW PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.housoo.platform.supervise.controller;

import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.core.util.*;
import com.housoo.platform.supervise.service.SuperviseApproveService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述 督办环节配置业务相关Controller
 *
 * @author cjr
 * @version 1.0
 * @created 2020-04-13 18:20:59
 */
@Controller
@RequestMapping("/supervise/SuperviseApproveController")
public class SuperviseApproveController extends BaseController {
    /**
     * SuperviseApproveService
     */
    @Resource
    private SuperviseApproveService superviseApproveService;
    /**
     * 系统日志Service
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除督办环节配置数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        String[] ids = selectColValues.split(",");
        for (String id : ids) {
            Map<String, Object> temp = superviseApproveService.getRecord("TB_SUPERVISE_APPROVE", new String[]{"RECORD_ID"}, new Object[]{id});
            if (temp != null) {
                temp.put("DEL_FLAG", "2");
                superviseApproveService.saveOrUpdate("TB_SUPERVISE_APPROVE",
                        temp, SysConstants.ID_GENERATOR_UUID, null);
            }
        }
        //superviseApproveService.deleteRecords("TB_SUPERVISE_APPROVE", "RECORD_ID", selectColValues.split(","));
        sysLogService.saveBackLog("督办环节", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的督办环节配置", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改督办环节配置数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> superviseApprove = PlatBeanUtil.getMapFromRequest(request);
        String RECORD_ID = (String) superviseApprove.get("RECORD_ID");
        if (StringUtils.isNotEmpty(RECORD_ID)) {
            superviseApprove.put("UPDATE_BY", PlatAppUtil.getBackPlatLoginUser().get("SYSUSER_ID"));
            superviseApprove.put("UPDATE_TIME", PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        } else {
            superviseApprove.put("CREATE_BY", PlatAppUtil.getBackPlatLoginUser().get("SYSUSER_ID"));
            superviseApprove.put("CREATE_TIME", PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        }
        superviseApprove = superviseApproveService.saveOrUpdate("TB_SUPERVISE_APPROVE",
                superviseApprove, SysConstants.ID_GENERATOR_UUID, null);
        //如果是保存树形结构表数据,请调用下面的接口,而注释掉上面的接口代码
        //superviseApprove = superviseApproveService.saveOrUpdateTreeData("TB_SUPERVISE_APPROVE",
        //        superviseApprove, SysConstants.ID_GENERATOR_UUID,null);
        if (StringUtils.isNotEmpty(RECORD_ID)) {
            sysLogService.saveBackLog("督办环节", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为[" + RECORD_ID + "]督办环节配置", request);
        } else {
            RECORD_ID = (String) superviseApprove.get("RECORD_ID");
            sysLogService.saveBackLog("督办环节", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为[" + RECORD_ID + "]督办环节配置", request);
        }
        superviseApprove.put("success", true);
        this.printObjectJsonString(superviseApprove, response);
    }

    /**
     * 跳转到督办环节配置表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String RECORD_ID = request.getParameter("RECORD_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> superviseApprove = null;
        if (StringUtils.isNotEmpty(RECORD_ID)) {
            superviseApprove = this.superviseApproveService.getRecord("TB_SUPERVISE_APPROVE"
                    , new String[]{"RECORD_ID"}, new Object[]{RECORD_ID});
        } else {
            superviseApprove = new HashMap<String, Object>();
        }
        request.setAttribute("superviseApprove", superviseApprove);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
        //如果是跳转到树形表单录入界面,请开放以下代码,,而注释掉上面的代码
        /*String RECORD_ID = request.getParameter("RECORD_ID");
        String SUPERVISEAPPROVE_PARENTID = request.getParameter("SUPERVISEAPPROVE_PARENTID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String,Object> superviseApprove = null;
        if(StringUtils.isNotEmpty(RECORD_ID)){
            superviseApprove = this.superviseApproveService.getRecord("TB_SUPERVISE_APPROVE"
                    , new String[]{"RECORD_ID"}, new Object[]{RECORD_ID});
            SUPERVISEAPPROVE_PARENTID = (String) superviseApprove.get("SuperviseApprove_PARENTID");
        }
        Map<String,Object> parentSuperviseApprove = null;
        if(SUPERVISEAPPROVE_PARENTID.equals("0")){
            parentSuperviseApprove = new HashMap<String,Object>();
            parentSuperviseApprove.put("RECORD_ID", SUPERVISEAPPROVE_PARENTID);
            parentSuperviseApprove.put("SUPERVISEAPPROVE_NAME", "督办环节配置树");
        }else{
            parentSuperviseApprove = this.superviseApproveService.getRecord("TB_SUPERVISE_APPROVE",
                    new String[]{"RECORD_ID"}, new Object[]{SUPERVISEAPPROVE_PARENTID});
        }
        if(superviseApprove==null){
            superviseApprove = new HashMap<String,Object>();
        }
        superviseApprove.put("SUPERVISEAPPROVE_PARENTID", parentSuperviseApprove.get("RECORD_ID"));
        superviseApprove.put("SUPERVISEAPPROVE_PARENTNAME", parentSuperviseApprove.get("SUPERVISEAPPROVE_NAME"));
        request.setAttribute("superviseApprove", superviseApprove);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);*/
    }
}
