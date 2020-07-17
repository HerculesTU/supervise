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
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.DicAttachService;
import com.housoo.platform.core.service.SysLogService;

/**
 * 描述 字典附加属性业务相关Controller
 *
 * @author housoo
 * @version 1.0
 * @created 2017-04-22 09:56:24
 */
@Controller
@RequestMapping("/system/DicAttachController")
public class DicAttachController extends BaseController {
    /**
     *
     */
    @Resource
    private DicAttachService dicAttachService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除字典附加属性数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        dicAttachService.deleteRecords("PLAT_SYSTEM_DICATTACH", "DICATTACH_ID", selectColValues.split(","));
        sysLogService.saveBackLog("字典管理", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的字典附加属性", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改字典附加属性数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> dicAttach = PlatBeanUtil.getMapFromRequest(request);
        String DICATTACH_ID = (String) dicAttach.get("DICATTACH_ID");
        dicAttach = dicAttachService.saveOrUpdate("PLAT_SYSTEM_DICATTACH",
                dicAttach, SysConstants.ID_GENERATOR_UUID, null);
        //如果是保存树形结构表数据,请调用下面的接口,而注释掉上面的接口代码
        //dicAttach = dicAttachService.saveOrUpdateTreeData("PLAT_SYSTEM_DICATTACH",
        //        dicAttach,SysConstants.ID_GENERATOR_UUID,null);
        if (StringUtils.isNotEmpty(DICATTACH_ID)) {
            sysLogService.saveBackLog("字典管理", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为[" + DICATTACH_ID + "]字典附加属性", request);
        } else {
            DICATTACH_ID = (String) dicAttach.get("DICATTACH_ID");
            sysLogService.saveBackLog("字典管理", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为[" + DICATTACH_ID + "]字典附加属性", request);
        }
        dicAttach.put("success", true);
        this.printObjectJsonString(dicAttach, response);
    }

    /**
     * 跳转到字典附加属性表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String DICATTACH_ID = request.getParameter("DICATTACH_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> dicAttach = null;
        if (StringUtils.isNotEmpty(DICATTACH_ID)) {
            dicAttach = this.dicAttachService.getRecord("PLAT_SYSTEM_DICATTACH"
                    , new String[]{"DICATTACH_ID"}, new Object[]{DICATTACH_ID});
        } else {
            dicAttach = new HashMap<String, Object>();
        }
        request.setAttribute("dicAttach", dicAttach);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
        //如果是跳转到树形表单录入界面,请开放以下代码,,而注释掉上面的代码
        /*String DICATTACH_ID = request.getParameter("DICATTACH_ID");
        String DICATTACH_PARENTID = request.getParameter("DICATTACH_PARENTID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String,Object> dicAttach = null;
        if(StringUtils.isNotEmpty(DICATTACH_ID)){
            dicAttach = this.dicAttachService.getRecord("PLAT_SYSTEM_DICATTACH"
                    ,new String[]{"DICATTACH_ID"},new Object[]{DICATTACH_ID});
            DICATTACH_PARENTID = (String) dicAttach.get("DicAttach_PARENTID");
        }
        Map<String,Object> parentDicAttach = null;
        if(DICATTACH_PARENTID.equals("0")){
            parentDicAttach = new HashMap<String,Object>();
            parentDicAttach.put("DICATTACH_ID",DICATTACH_PARENTID);
            parentDicAttach.put("DICATTACH_NAME","字典附加属性树");
        }else{
            parentDicAttach = this.dicAttachService.getRecord("PLAT_SYSTEM_DICATTACH",
                    new String[]{"DICATTACH_ID"}, new Object[]{DICATTACH_PARENTID});
        }
        if(dicAttach==null){
            dicAttach = new HashMap<String,Object>();
        }
        dicAttach.put("DICATTACH_PARENTID",parentDicAttach.get("DICATTACH_ID"));
        dicAttach.put("DICATTACH_PARENTNAME",parentDicAttach.get("DICATTACH_NAME"));
        request.setAttribute("dicAttach", dicAttach);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);*/
    }
}
