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
import com.housoo.platform.workflow.service.FieldModifyService;
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
 * 描述 字段修改业务相关Controller
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-11-02 09:40:47
 */
@Controller
@RequestMapping("/workflow/FieldModifyController")
public class FieldModifyController extends BaseController {
    /**
     *
     */
    @Resource
    private FieldModifyService fieldModifyService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除字段修改数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        fieldModifyService.deleteRecords("JBPM6_FIELDMODIFY", "FIELDMODIFY_ID", selectColValues.split(","));
        sysLogService.saveBackLog("字段修改日志", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的字段修改", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改字段修改数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> fieldModify = PlatBeanUtil.getMapFromRequest(request);
        String FIELDMODIFY_ID = (String) fieldModify.get("FIELDMODIFY_ID");
        fieldModify = fieldModifyService.saveOrUpdate("JBPM6_FIELDMODIFY",
                fieldModify, SysConstants.ID_GENERATOR_UUID, null);
        //如果是保存树形结构表数据,请调用下面的接口,而注释掉上面的接口代码
        //fieldModify = fieldModifyService.saveOrUpdateTreeData("JBPM6_FIELDMODIFY",
        //        fieldModify,SysConstants.ID_GENERATOR_UUID,null);
        if (StringUtils.isNotEmpty(FIELDMODIFY_ID)) {
            sysLogService.saveBackLog("字段修改日志", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为[" + FIELDMODIFY_ID + "]字段修改", request);
        } else {
            FIELDMODIFY_ID = (String) fieldModify.get("FIELDMODIFY_ID");
            sysLogService.saveBackLog("字段修改日志", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为[" + FIELDMODIFY_ID + "]字段修改", request);
        }
        fieldModify.put("success", true);
        this.printObjectJsonString(fieldModify, response);
    }

    /**
     * 跳转到字段修改表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String FIELDMODIFY_ID = request.getParameter("FIELDMODIFY_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> fieldModify = null;
        if (StringUtils.isNotEmpty(FIELDMODIFY_ID)) {
            fieldModify = this.fieldModifyService.getRecord("JBPM6_FIELDMODIFY"
                    , new String[]{"FIELDMODIFY_ID"}, new Object[]{FIELDMODIFY_ID});
        } else {
            fieldModify = new HashMap<String, Object>();
        }
        request.setAttribute("fieldModify", fieldModify);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
        //如果是跳转到树形表单录入界面,请开放以下代码,,而注释掉上面的代码
        /*String FIELDMODIFY_ID = request.getParameter("FIELDMODIFY_ID");
        String FIELDMODIFY_PARENTID = request.getParameter("FIELDMODIFY_PARENTID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String,Object> fieldModify = null;
        if(StringUtils.isNotEmpty(FIELDMODIFY_ID)){
            fieldModify = this.fieldModifyService.getRecord("JBPM6_FIELDMODIFY"
                    ,new String[]{"FIELDMODIFY_ID"},new Object[]{FIELDMODIFY_ID});
            FIELDMODIFY_PARENTID = (String) fieldModify.get("FieldModify_PARENTID");
        }
        Map<String,Object> parentFieldModify = null;
        if(FIELDMODIFY_PARENTID.equals("0")){
            parentFieldModify = new HashMap<String,Object>();
            parentFieldModify.put("FIELDMODIFY_ID",FIELDMODIFY_PARENTID);
            parentFieldModify.put("FIELDMODIFY_NAME","字段修改树");
        }else{
            parentFieldModify = this.fieldModifyService.getRecord("JBPM6_FIELDMODIFY",
                    new String[]{"FIELDMODIFY_ID"}, new Object[]{FIELDMODIFY_PARENTID});
        }
        if(fieldModify==null){
            fieldModify = new HashMap<String,Object>();
        }
        fieldModify.put("FIELDMODIFY_PARENTID",parentFieldModify.get("FIELDMODIFY_ID"));
        fieldModify.put("FIELDMODIFY_PARENTNAME",parentFieldModify.get("FIELDMODIFY_NAME"));
        request.setAttribute("fieldModify", fieldModify);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);*/
    }
}
