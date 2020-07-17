/*
 * Copyright (c) 2000 www.cnnetchina.com All rights reserved.18888
 * ZW PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.housoo.platform.supervise.controller;

import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.core.util.*;
import com.housoo.platform.supervise.service.SuperviseSourceService;
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
 * 描述 督办来源业务相关Controller
 *
 * @author cjr
 * @version 1.0
 * @created 2020-04-13 11:14:38
 */
@Controller
@RequestMapping("/supervise/SuperviseSourceController")
public class SuperviseSourceController extends BaseController {
    /**
     * SuperviseSourceService
     */
    @Resource
    private SuperviseSourceService superviseSourceService;
    /**
     * 系统日志Service
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除督办来源数据
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
            Map<String, Object> temp = superviseSourceService.getRecord("TB_SUPERVISE_SOURCE", new String[]{"RECORD_ID"}, new Object[]{id});
            if (temp != null) {
                temp.put("DEL_FLAG", "2");
                superviseSourceService.saveOrUpdate("TB_SUPERVISE_SOURCE",
                        temp, SysConstants.ID_GENERATOR_UUID, null);
            }
        }
        //superviseSourceService.deleteRecords("TB_SUPERVISE_SOURCE", "RECORD_ID", selectColValues.split(","));
        sysLogService.saveBackLog("督办来源", SysLogService.OPER_TYPE_DEL,
                "标记了ID为[" + selectColValues + "]的督办来源为删除状态", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改督办来源数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> temp = new HashMap<>();
        Map<String, Object> superviseSource = PlatBeanUtil.getMapFromRequest(request);
        String RECORD_ID = (String) superviseSource.get("RECORD_ID");
        if (StringUtils.isNotEmpty(RECORD_ID)) {
            superviseSource.put("UPDATE_BY", PlatAppUtil.getBackPlatLoginUser().get("SYSUSER_ID"));
            superviseSource.put("UPDATE_TIME", PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        } else {
            superviseSource.put("CREATE_BY", PlatAppUtil.getBackPlatLoginUser().get("SYSUSER_ID"));
            superviseSource.put("CREATE_TIME", PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        }
        superviseSource = superviseSourceService.saveOrUpdate("TB_SUPERVISE_SOURCE",
                superviseSource, SysConstants.ID_GENERATOR_UUID, null);
        //如果是保存树形结构表数据,请调用下面的接口,而注释掉上面的接口代码
        //superviseSource = superviseSourceService.saveOrUpdateTreeData("TB_SUPERVISE_SOURCE",
        //        superviseSource, SysConstants.ID_GENERATOR_UUID,null);
        if (StringUtils.isNotEmpty(RECORD_ID)) {
            sysLogService.saveBackLog("督办来源", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为[" + RECORD_ID + "]督办来源", request);
        } else {
            RECORD_ID = (String) superviseSource.get("RECORD_ID");
            sysLogService.saveBackLog("督办来源", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为[" + RECORD_ID + "]督办来源", request);
        }
        superviseSource.put("success", true);
        this.printObjectJsonString(superviseSource, response);
    }

    /**
     * 跳转到督办来源表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String RECORD_ID = request.getParameter("RECORD_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> superviseSource = null;
        if (StringUtils.isNotEmpty(RECORD_ID)) {
            superviseSource = this.superviseSourceService.getRecord("TB_SUPERVISE_SOURCE"
                    , new String[]{"RECORD_ID"}, new Object[]{RECORD_ID});
        } else {
            superviseSource = new HashMap<String, Object>();
        }
        request.setAttribute("superviseSource", superviseSource);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
        //如果是跳转到树形表单录入界面,请开放以下代码,,而注释掉上面的代码
        /*String RECORD_ID = request.getParameter("RECORD_ID");
        String SUPERVISESOURCE_PARENTID = request.getParameter("SUPERVISESOURCE_PARENTID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String,Object> superviseSource = null;
        if(StringUtils.isNotEmpty(RECORD_ID)){
            superviseSource = this.superviseSourceService.getRecord("TB_SUPERVISE_SOURCE"
                    , new String[]{"RECORD_ID"}, new Object[]{RECORD_ID});
            SUPERVISESOURCE_PARENTID = (String) superviseSource.get("SuperviseSource_PARENTID");
        }
        Map<String,Object> parentSuperviseSource = null;
        if(SUPERVISESOURCE_PARENTID.equals("0")){
            parentSuperviseSource = new HashMap<String,Object>();
            parentSuperviseSource.put("RECORD_ID", SUPERVISESOURCE_PARENTID);
            parentSuperviseSource.put("SUPERVISESOURCE_NAME", "督办来源树");
        }else{
            parentSuperviseSource = this.superviseSourceService.getRecord("TB_SUPERVISE_SOURCE",
                    new String[]{"RECORD_ID"}, new Object[]{SUPERVISESOURCE_PARENTID});
        }
        if(superviseSource==null){
            superviseSource = new HashMap<String,Object>();
        }
        superviseSource.put("SUPERVISESOURCE_PARENTID", parentSuperviseSource.get("RECORD_ID"));
        superviseSource.put("SUPERVISESOURCE_PARENTNAME", parentSuperviseSource.get("SUPERVISESOURCE_NAME"));
        request.setAttribute("superviseSource", superviseSource);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);*/
    }
}
