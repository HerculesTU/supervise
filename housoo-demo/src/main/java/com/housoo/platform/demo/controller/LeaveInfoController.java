/*
 * Copyright (c) 2005, 2014, STOOGES Technology Co.,Ltd. All rights reserved.
 * STOOGES PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.demo.controller;

import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.dynadatasource.DynamicDataSourceHolder;
import com.housoo.platform.core.dynadatasource.DynamicDataSourceInterceptor;
import com.housoo.platform.demo.service.LeaveInfoService;
import com.housoo.platform.core.service.SysLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述 请假信息业务相关Controller
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-05 14:58:45
 */
@Controller
@RequestMapping("/demo/LeaveInfoController")
public class LeaveInfoController extends BaseController {
    /**
     *
     */
    @Resource
    private LeaveInfoService leaveInfoService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除请假信息数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        leaveInfoService.deleteRecords("PLAT_DEMO_LEAVEINFO", "LEAVEINFO_ID", selectColValues.split(","));
        sysLogService.saveBackLog("流程例子模块", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的请假信息", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改请假信息数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> leaveInfo = PlatBeanUtil.getMapFromRequest(request);
        String LEAVEINFO_ID = (String) leaveInfo.get("LEAVEINFO_ID");
        leaveInfo = leaveInfoService.saveOrUpdate("PLAT_DEMO_LEAVEINFO",
                leaveInfo, SysConstants.ID_GENERATOR_UUID, null);
        //如果是保存树形结构表数据,请调用下面的接口,而注释掉上面的接口代码
        //leaveInfo = leaveInfoService.saveOrUpdateTreeData("PLAT_DEMO_LEAVEINFO",
        //        leaveInfo,AllConstants.IDGENERATOR_UUID,null);
        if (StringUtils.isNotEmpty(LEAVEINFO_ID)) {
            sysLogService.saveBackLog("流程例子模块", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为[" + LEAVEINFO_ID + "]请假信息", request);
        } else {
            LEAVEINFO_ID = (String) leaveInfo.get("LEAVEINFO_ID");
            sysLogService.saveBackLog("流程例子模块", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为[" + LEAVEINFO_ID + "]请假信息", request);
        }
        leaveInfo.put("success", true);
        this.printObjectJsonString(leaveInfo, response);
    }

    /**
     * 跳转到请假信息表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String LEAVEINFO_ID = request.getParameter("LEAVEINFO_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> leaveInfo = null;
        if (StringUtils.isNotEmpty(LEAVEINFO_ID)) {
            leaveInfo = this.leaveInfoService.getRecord("PLAT_DEMO_LEAVEINFO"
                    , new String[]{"LEAVEINFO_ID"}, new Object[]{LEAVEINFO_ID});
        } else {
            leaveInfo = new HashMap<String, Object>();
        }
        request.setAttribute("leaveInfo", leaveInfo);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
        //如果是跳转到树形表单录入界面,请开放以下代码,,而注释掉上面的代码
        /*String LEAVEINFO_ID = request.getParameter("LEAVEINFO_ID");
        String LEAVEINFO_PARENTID = request.getParameter("LEAVEINFO_PARENTID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String,Object> leaveInfo = null;
        if(StringUtils.isNotEmpty(LEAVEINFO_ID)){
            leaveInfo = this.leaveInfoService.getRecord("PLAT_DEMO_LEAVEINFO"
                    ,new String[]{"LEAVEINFO_ID"},new Object[]{LEAVEINFO_ID});
            LEAVEINFO_PARENTID = (String) leaveInfo.get("LeaveInfo_PARENTID");
        }
        Map<String,Object> parentLeaveInfo = null;
        if(LEAVEINFO_PARENTID.equals("0")){
            parentLeaveInfo = new HashMap<String,Object>();
            parentLeaveInfo.put("LEAVEINFO_ID",LEAVEINFO_PARENTID);
            parentLeaveInfo.put("LEAVEINFO_NAME","请假信息树");
        }else{
            parentLeaveInfo = this.leaveInfoService.getRecord("PLAT_DEMO_LEAVEINFO",
                    new String[]{"LEAVEINFO_ID"}, new Object[]{LEAVEINFO_PARENTID});
        }
        if(leaveInfo==null){
            leaveInfo = new HashMap<String,Object>();
        }
        leaveInfo.put("LEAVEINFO_PARENTID",parentLeaveInfo.get("LEAVEINFO_ID"));
        leaveInfo.put("LEAVEINFO_PARENTNAME",parentLeaveInfo.get("LEAVEINFO_NAME"));
        request.setAttribute("leaveInfo", leaveInfo);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);*/
    }

    /**
     * 跳转到批量处理流程界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goBatchWindow")
    public ModelAndView goBatchWindow(HttpServletRequest request) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("flowTitle", "请假标题1");
        map1.put("flowMainRecordId", "402881e65c6d4a47015c6d4beddb0005");
        map1.put("flowExeId", "");
        map1.put("flowTaskId", "");
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("flowTitle", "请假标题2");
        map2.put("flowMainRecordId", "402881e65c70fa25015c70ff52b00035");
        map2.put("flowExeId", "");
        map2.put("flowTaskId", "");
        list.add(map1);
        list.add(map2);
        request.setAttribute("flowHandleList", list);
        request.setAttribute("jbpmDefCode", "leaveinfo");
        return new ModelAndView("background/workflow/execution/batchsubmitflow");
    }

    /**
     * @param request
     * @param response
     */
    @RequestMapping(params = "dynamicDataTest")
    public void dynamicDataTest(HttpServletRequest request,
                                HttpServletResponse response) {
        String dataCode = request.getParameter("dataCode");
        DynamicDataSourceInterceptor.setDbType(dataCode);
        DynamicDataSourceHolder.setDatasource(dataCode);
        this.leaveInfoService.dynamicDataTest(dataCode);
    }

}
