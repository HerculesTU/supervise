/*
 * Copyright (c) 2000 www.cnnetchina.com All rights reserved.18888
 * ZW PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.housoo.platform.supervise.controller;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.housoo.platform.core.util.PlatAppUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.supervise.service.SuperviseItemService;

/**
 * 
 * 描述 督办事项分类业务相关Controller
 *
 * @author zxl
 * @version 1.0
 * @created 2020-04-13 14:45:11
 */
@Controller  
@RequestMapping("/supervise/SuperviseItemController")  
public class SuperviseItemController extends BaseController {
    /**
     * SuperviseItemService
     */
    @Resource
    private SuperviseItemService superviseItemService;
    /**
     * 系统日志Service
     */
    @Resource
    private SysLogService sysLogService;
    
    /**
     * 删除督办事项分类数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
            HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        superviseItemService.deleteRecords("TB_SUPERVISE_ITEM", "RECORD_ID", selectColValues.split(","));
        sysLogService.saveBackLog("督办事项分类", SysLogService.OPER_TYPE_DEL,
                "删除了ID为["+selectColValues+"]的督办事项分类", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }
    
    /**
     * 新增或者修改督办事项分类数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
            HttpServletResponse response) {
        Map<String,Object> superviseItem = PlatBeanUtil.getMapFromRequest(request);
        String RECORD_ID = (String) superviseItem.get("RECORD_ID");
        Map<String, Object> loginUser = PlatAppUtil.getBackPlatLoginUser();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (StringUtils.isNotEmpty(RECORD_ID)) {
            superviseItem.put("update_by", loginUser.get("SYSUSER_NAME"));
            superviseItem.put("update_time", simpleDateFormat.format(new Date()));
        } else {
            superviseItem.put("create_by", loginUser.get("SYSUSER_NAME"));
            superviseItem.put("create_time", simpleDateFormat.format(new Date()));
            superviseItem.put("update_by", "");
            superviseItem.put("update_time", "");
        }
        superviseItem = superviseItemService.saveOrUpdate("TB_SUPERVISE_ITEM",
                superviseItem, SysConstants.ID_GENERATOR_UUID,null);
        //如果是保存树形结构表数据,请调用下面的接口,而注释掉上面的接口代码
        //superviseItem = superviseItemService.saveOrUpdateTreeData("TB_SUPERVISE_ITEM",
        //        superviseItem, SysConstants.ID_GENERATOR_UUID,null);
        if(StringUtils.isNotEmpty(RECORD_ID)){
            sysLogService.saveBackLog("督办事项分类", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为["+RECORD_ID+"]督办事项分类", request);
        }else{
            RECORD_ID = (String) superviseItem.get("RECORD_ID");
            sysLogService.saveBackLog("督办事项分类", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为["+RECORD_ID+"]督办事项分类", request);
        }
        superviseItem.put("success", true);
        this.printObjectJsonString(superviseItem, response);
    }
    
    /**
     * 跳转到督办事项分类表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String RECORD_ID = request.getParameter("RECORD_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String,Object> superviseItem = null;
        if(StringUtils.isNotEmpty(RECORD_ID)){
            superviseItem = this.superviseItemService.getRecord("TB_SUPERVISE_ITEM"
                    , new String[]{"RECORD_ID"}, new Object[]{RECORD_ID});
        }else{
            superviseItem = new HashMap<String,Object>();
        }
        request.setAttribute("superviseItem", superviseItem);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
        //如果是跳转到树形表单录入界面,请开放以下代码,,而注释掉上面的代码
        /*String RECORD_ID = request.getParameter("RECORD_ID");
        String SUPERVISEITEM_PARENTID = request.getParameter("SUPERVISEITEM_PARENTID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String,Object> superviseItem = null;
        if(StringUtils.isNotEmpty(RECORD_ID)){
            superviseItem = this.superviseItemService.getRecord("TB_SUPERVISE_ITEM"
                    , new String[]{"RECORD_ID"}, new Object[]{RECORD_ID});
            SUPERVISEITEM_PARENTID = (String) superviseItem.get("SuperviseItem_PARENTID");
        }
        Map<String,Object> parentSuperviseItem = null;
        if(SUPERVISEITEM_PARENTID.equals("0")){
            parentSuperviseItem = new HashMap<String,Object>();
            parentSuperviseItem.put("RECORD_ID", SUPERVISEITEM_PARENTID);
            parentSuperviseItem.put("SUPERVISEITEM_NAME", "督办事项分类树");
        }else{
            parentSuperviseItem = this.superviseItemService.getRecord("TB_SUPERVISE_ITEM",
                    new String[]{"RECORD_ID"}, new Object[]{SUPERVISEITEM_PARENTID});
        }
        if(superviseItem==null){
            superviseItem = new HashMap<String,Object>();
        }
        superviseItem.put("SUPERVISEITEM_PARENTID", parentSuperviseItem.get("RECORD_ID"));
        superviseItem.put("SUPERVISEITEM_PARENTNAME", parentSuperviseItem.get("SUPERVISEITEM_NAME"));
        request.setAttribute("superviseItem", superviseItem);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);*/
    }


}
