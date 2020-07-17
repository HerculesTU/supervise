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
import com.housoo.platform.demo.service.ProductService;
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
 * 描述 产品信息业务相关Controller
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-07-10 13:48:25
 */
@Controller
@RequestMapping("/demo/ProductController")
public class ProductController extends BaseController {
    /**
     *
     */
    @Resource
    private ProductService productService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除产品信息数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        productService.deleteRecords("PLAT_DEMO_PRODUCT", "PRODUCT_ID", selectColValues.split(","));
        sysLogService.saveBackLog("产品管理", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的产品信息", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改产品信息数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> product = PlatBeanUtil.getMapFromRequest(request);
        String PRODUCT_ID = (String) product.get("PRODUCT_ID");
        product = productService.saveOrUpdate("PLAT_DEMO_PRODUCT",
                product, SysConstants.ID_GENERATOR_UUID, null);
        if (StringUtils.isNotEmpty(PRODUCT_ID)) {
            sysLogService.saveBackLog("产品管理", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为[" + PRODUCT_ID + "]产品信息", request);
        } else {
            PRODUCT_ID = (String) product.get("PRODUCT_ID");
            sysLogService.saveBackLog("产品管理", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为[" + PRODUCT_ID + "]产品信息", request);
        }
        product.put("success", true);
        this.printObjectJsonString(product, response);
    }

    /**
     * 跳转到产品信息表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String PRODUCT_ID = request.getParameter("PRODUCT_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> product = null;
        if (StringUtils.isNotEmpty(PRODUCT_ID)) {
            product = this.productService.getRecord("PLAT_DEMO_PRODUCT"
                    , new String[]{"PRODUCT_ID"}, new Object[]{PRODUCT_ID});
        } else {
            product = new HashMap<String, Object>();
        }
        request.setAttribute("product", product);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }

}
