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
import com.housoo.platform.core.service.FullTextService;
import com.housoo.platform.core.service.SysLogService;

/**
 * 描述 全文检索业务相关Controller
 *
 * @author housoo
 * @version 1.0
 * @created 2017-07-01 10:15:45
 */
@Controller
@RequestMapping("/system/FullTextController")
public class FullTextController extends BaseController {
    /**
     *
     */
    @Resource
    private FullTextService fullTextService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除全文检索数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        fullTextService.deleteCascadeIndex(selectColValues.split(","));
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改全文检索数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> fullText = PlatBeanUtil.getMapFromRequest(request);
        fullText = fullTextService.saveOrUpdate("PLAT_SYSTEM_FULLTEXT",
                fullText, SysConstants.ID_GENERATOR_UUID, null);
        fullText.put("success", true);
        this.printObjectJsonString(fullText, response);
    }

    /**
     * 跳转到全文检索表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String FULLTEXT_ID = request.getParameter("FULLTEXT_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> fullText = null;
        if (StringUtils.isNotEmpty(FULLTEXT_ID)) {
            fullText = this.fullTextService.getRecord("PLAT_SYSTEM_FULLTEXT"
                    , new String[]{"FULLTEXT_ID"}, new Object[]{FULLTEXT_ID});
        } else {
            fullText = new HashMap<String, Object>();
        }
        request.setAttribute("fullText", fullText);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }

    /**
     * 重建全文索引数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "rebuild")
    public void rebuild(HttpServletRequest request,
                        HttpServletResponse response) {
        fullTextService.rebuildIndex();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }
}
