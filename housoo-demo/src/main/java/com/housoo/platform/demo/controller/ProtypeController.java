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
import com.housoo.platform.demo.service.ProtypeService;
import com.housoo.platform.core.service.SysLogService;
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
 * 描述 产品类别业务相关Controller
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-07-11 16:28:06
 */
@Controller
@RequestMapping("/demo/ProtypeController")
public class ProtypeController extends BaseController {
    /**
     *
     */
    @Resource
    private ProtypeService protypeService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除产品类别数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        protypeService.deleteRecords("PLAT_DEMO_PROTYPE", "PROTYPE_ID", selectColValues.split(","));
        sysLogService.saveBackLog("产品管理", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的产品类别", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改产品类别数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> protype = PlatBeanUtil.getMapFromRequest(request);
        String PROTYPE_ID = (String) protype.get("PROTYPE_ID");
        //如果是保存树形结构表数据,请调用下面的接口,而注释掉上面的接口代码
        protype = protypeService.saveOrUpdateTreeData("PLAT_DEMO_PROTYPE",
                protype, SysConstants.ID_GENERATOR_UUID, null);
        if (StringUtils.isNotEmpty(PROTYPE_ID)) {
            sysLogService.saveBackLog("产品管理", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为[" + PROTYPE_ID + "]产品类别", request);
        } else {
            PROTYPE_ID = (String) protype.get("PROTYPE_ID");
            sysLogService.saveBackLog("产品管理", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为[" + PROTYPE_ID + "]产品类别", request);
        }
        protype.put("success", true);
        this.printObjectJsonString(protype, response);
    }

    private void newMethod() {
        System.out.println("调用了新的方法...");
    }

    /**
     * 跳转到产品类别表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        this.newMethod();
        //如果是跳转到树形表单录入界面,请开放以下代码,,而注释掉上面的代码
        String PROTYPE_ID = request.getParameter("PROTYPE_ID");
        String PROTYPE_PARENTID = request.getParameter("PROTYPE_PARENTID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        /*Map<String,Object> protype = null;
        if(StringUtils.isNotEmpty(PROTYPE_ID)){
            protype = this.protypeService.getRecord("PLAT_DEMO_PROTYPE"
                    ,new String[]{"PROTYPE_ID"},new Object[]{PROTYPE_ID});
            PROTYPE_PARENTID = (String) protype.get("Protype_PARENTID");
        }
        Map<String,Object> parentProtype = null;
        if(PROTYPE_PARENTID.equals("0")){
            parentProtype = new HashMap<String,Object>();
            parentProtype.put("PROTYPE_ID",PROTYPE_PARENTID);
            parentProtype.put("PROTYPE_NAME","产品类别树");
        }else{
            parentProtype = this.protypeService.getRecord("PLAT_DEMO_PROTYPE",
                    new String[]{"PROTYPE_ID"}, new Object[]{PROTYPE_PARENTID});
        }
        if(protype==null){
            protype = new HashMap<String,Object>();
        }
        protype.put("PROTYPE_PARENTID",parentProtype.get("PROTYPE_ID"));
        protype.put("PROTYPE_PARENTNAME",parentProtype.get("PROTYPE_NAME"));
        request.setAttribute("protype", protype);*/
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }
}
