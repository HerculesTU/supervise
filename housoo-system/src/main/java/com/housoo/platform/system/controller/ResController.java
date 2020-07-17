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
import com.housoo.platform.core.service.CommonUIService;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.ResService;

/**
 * 描述 系统资源业务相关Controller
 *
 * @author housoo
 * @version 1.0
 * @created 2017-04-05 17:12:34
 */
@Controller
@RequestMapping("/system/ResController")
public class ResController extends BaseController {
    /**
     *
     */
    @Resource
    private ResService resService;
    /**
     *
     */
    @Resource
    private CommonUIService commonUIService;

    /**
     * 跳转到系统资源表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goView")
    public ModelAndView goView(HttpServletRequest request) {

        return new ModelAndView("background/system/res/list");
    }

    /**
     * 删除系统资源数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String resId = request.getParameter("treeNodeId");
        resService.deleteAndCascadeAssoical(resId);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 获取数据列表
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "datagrid")
    public void datagrid(HttpServletRequest request,
                         HttpServletResponse response) {
        StringBuffer paramsConfig = new StringBuffer("[TABLE_NAME:PLAT_SYSTEM_RES]");
        paramsConfig.append("[TREE_IDANDNAMECOL:RES_ID,RES_NAME]");
        paramsConfig.append("[TREE_QUERYFIELDS:RES_CODE,RES_PARENTID,RES_TYPE,RES_MENUICON,RES_MENUURL,RES_TREESN]");
        paramsConfig.append("[FILTERS:RES_PARENTID_EQ|0]");
        List<Map<String, Object>> list = commonUIService.findGenTreeSelectorDatas(paramsConfig.toString());
        if (list == null || list.size() == 0) {
            list = new ArrayList<Map<String, Object>>();
        }
        Map<String, Object> rootMap = new HashMap<String, Object>();
        rootMap.put("expanded", true);
        if (list.size() == 0) {
            rootMap.put("isLeaf", true);
        } else {
            rootMap.put("isLeaf", false);
        }
        rootMap.put("level", 0);
        rootMap.put("parent", "-1");
        rootMap.put("RES_ID", "0");
        rootMap.put("RES_NAME", "全部资源");
        rootMap.put("RES_CODE", "");
        list.add(0, rootMap);
        this.printListJsonString(null, list, response);
    }

    /**
     * 新增或者修改系统资源数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> res = PlatBeanUtil.getMapFromRequest(request);
        res = resService.saveUpdateResInfo(res);
        res.put("success", true);
        this.printObjectJsonString(res, response);
    }

    /**
     * 跳转到系统资源表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String RES_ID = request.getParameter("RES_ID");
        String RES_PARENTID = request.getParameter("RES_PARENTID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> res = null;
        if (StringUtils.isNotEmpty(RES_ID)) {
            res = this.resService.getRecord("PLAT_SYSTEM_RES"
                    , new String[]{"RES_ID"}, new Object[]{RES_ID});
            RES_PARENTID = (String) res.get("RES_PARENTID");
        }
        Map<String, Object> parentRes = null;
        if ("0".equals(RES_PARENTID)) {
            parentRes = new HashMap<String, Object>();
            parentRes.put("RES_ID", RES_PARENTID);
            parentRes.put("RES_NAME", "系统资源树");
        } else {
            parentRes = this.resService.getRecord("PLAT_SYSTEM_RES",
                    new String[]{"RES_ID"}, new Object[]{RES_PARENTID});
        }
        if (res == null) {
            res = new HashMap<String, Object>();
        }
        res.put("RES_PARENTID", parentRes.get("RES_ID"));
        res.put("RES_PARENTNAME", parentRes.get("RES_NAME"));
        request.setAttribute("res", res);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }
}
