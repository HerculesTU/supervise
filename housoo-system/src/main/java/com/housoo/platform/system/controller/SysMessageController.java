package com.housoo.platform.system.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.housoo.platform.core.service.SysMessageService;

/**
 * 描述 系统消息业务相关Controller
 *
 * @author housoo
 * @version 1.0
 * @created 2018-05-17 21:24:14
 */
@Controller
@RequestMapping("/system/SysMessageController")
public class SysMessageController extends BaseController {
    /**
     *
     */
    @Resource
    private SysMessageService sysMessageService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除系统消息数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        sysMessageService.deleteRecords("PLAT_SYSTEM_SYSMESSAGE", "SYSMESSAGE_ID", selectColValues.split(","));
        sysLogService.saveBackLog("ces", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的系统消息", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改系统消息数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> sysMessage = PlatBeanUtil.getMapFromRequest(request);
        String SYSMESSAGE_ID = (String) sysMessage.get("SYSMESSAGE_ID");
        sysMessage = sysMessageService.saveOrUpdate("PLAT_SYSTEM_SYSMESSAGE",
                sysMessage, SysConstants.ID_GENERATOR_UUID, null);
        //如果是保存树形结构表数据,请调用下面的接口,而注释掉上面的接口代码
        //sysMessage = sysMessageService.saveOrUpdateTreeData("PLAT_SYSTEM_SYSMESSAGE",
        //        sysMessage,SysConstants.ID_GENERATOR_UUID,null);
        if (StringUtils.isNotEmpty(SYSMESSAGE_ID)) {
            sysLogService.saveBackLog("ces", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为[" + SYSMESSAGE_ID + "]系统消息", request);
        } else {
            SYSMESSAGE_ID = (String) sysMessage.get("SYSMESSAGE_ID");
            sysLogService.saveBackLog("ces", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为[" + SYSMESSAGE_ID + "]系统消息", request);
        }
        sysMessage.put("success", true);
        this.printObjectJsonString(sysMessage, response);
    }

    /**
     * 跳转到系统消息表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String SYSMESSAGE_ID = request.getParameter("SYSMESSAGE_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> sysMessage = null;
        if (StringUtils.isNotEmpty(SYSMESSAGE_ID)) {
            sysMessage = this.sysMessageService.getRecord("PLAT_SYSTEM_SYSMESSAGE"
                    , new String[]{"SYSMESSAGE_ID"}, new Object[]{SYSMESSAGE_ID});
            this.sysMessageService.updateSeeTime(SYSMESSAGE_ID);
        } else {
            sysMessage = new HashMap<String, Object>();
        }
        request.setAttribute("sysMessage", sysMessage);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
        //如果是跳转到树形表单录入界面,请开放以下代码,,而注释掉上面的代码
        /*String SYSMESSAGE_ID = request.getParameter("SYSMESSAGE_ID");
        String SYSMESSAGE_PARENTID = request.getParameter("SYSMESSAGE_PARENTID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String,Object> sysMessage = null;
        if(StringUtils.isNotEmpty(SYSMESSAGE_ID)){
            sysMessage = this.sysMessageService.getRecord("PLAT_SYSTEM_SYSMESSAGE"
                    ,new String[]{"SYSMESSAGE_ID"},new Object[]{SYSMESSAGE_ID});
            SYSMESSAGE_PARENTID = (String) sysMessage.get("SysMessage_PARENTID");
        }
        Map<String,Object> parentSysMessage = null;
        if(SYSMESSAGE_PARENTID.equals("0")){
            parentSysMessage = new HashMap<String,Object>();
            parentSysMessage.put("SYSMESSAGE_ID",SYSMESSAGE_PARENTID);
            parentSysMessage.put("SYSMESSAGE_NAME","系统消息树");
        }else{
            parentSysMessage = this.sysMessageService.getRecord("PLAT_SYSTEM_SYSMESSAGE",
                    new String[]{"SYSMESSAGE_ID"}, new Object[]{SYSMESSAGE_PARENTID});
        }
        if(sysMessage==null){
            sysMessage = new HashMap<String,Object>();
        }
        sysMessage.put("SYSMESSAGE_PARENTID",parentSysMessage.get("SYSMESSAGE_ID"));
        sysMessage.put("SYSMESSAGE_PARENTNAME",parentSysMessage.get("SYSMESSAGE_NAME"));
        request.setAttribute("sysMessage", sysMessage);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);*/
    }

    /**
     * @param request
     * @param response
     */
    @RequestMapping(params = "getSystemMessageList")
    public void getSystemMessageList(HttpServletRequest request,
                                     HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String, Object>> list = this.sysMessageService.findNoReadList();
        result.put("messageList", list);
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }
}
