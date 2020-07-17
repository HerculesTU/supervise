package com.housoo.platform.system.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.model.TableColumn;
import com.housoo.platform.core.model.TableInfo;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatDateTimeUtil;
import com.housoo.platform.core.util.PlatEhcacheUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.DicTypeService;

/**
 * 描述
 *
 * @author housoo
 * @created 2017年2月1日 上午10:50:40
 */
@Controller
@RequestMapping("/system/DicTypeController")
public class DicTypeController extends BaseController {
    /**
     * 缓存KEY
     */
    private static final String CACHE_KEY = "DIC_TYPETREE";
    /**
     *
     */
    @Resource
    private DicTypeService dicTypeService;

    /**
     * 返回字典类别树JSON
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "tree")
    public void tree(HttpServletRequest request,
                     HttpServletResponse response) {
        String treeJson = dicTypeService.getTreeJson(request);
        this.printJsonString(treeJson, response);
    }

    /**
     * 跳转到表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goform")
    public ModelAndView goform(HttpServletRequest request) {
        String dicTypeId = request.getParameter("DICTYPE_ID");
        String parentId = request.getParameter("parentId");
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> dicType = null;
        if (StringUtils.isNotEmpty(dicTypeId)) {
            dicType = this.dicTypeService.getRecord("PLAT_SYSTEM_DICTYPE",
                    new String[]{"DICTYPE_ID"}, new Object[]{dicTypeId});
            parentId = (String) dicType.get("DICTYPE_PARENTID");
        }
        Map<String, Object> parentDicType = null;
        if ("0".equals(parentId)) {
            parentDicType = new HashMap<String, Object>();
            parentDicType.put("DICTYPE_ID", parentId);
            parentDicType.put("DICTYPE_NAME", "字典类别树");
        } else {
            parentDicType = this.dicTypeService.getRecord("PLAT_SYSTEM_DICTYPE",
                    new String[]{"DICTYPE_ID"}, new Object[]{parentId});
        }
        if (dicType == null) {
            dicType = new HashMap<String, Object>();
        }
        dicType.put("DICTYPE_PARENTID", parentDicType.get("DICTYPE_ID"));
        dicType.put("DICTYPE_PARENTNAME", parentDicType.get("DICTYPE_NAME"));
        request.setAttribute("dicType", dicType);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }

    /**
     * 新增或者修改字典类别
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> dicType = PlatBeanUtil.getMapFromRequest(request);
        dicType = dicTypeService.saveOrUpdateTreeData("PLAT_SYSTEM_DICTYPE",
                dicType, SysConstants.ID_GENERATOR_UUID, null);
        PlatEhcacheUtil.moveEhcacheByKey(CACHE_KEY);
        dicType.put("success", true);
        this.printObjectJsonString(dicType, response);
    }

    /**
     * 删除字典类别数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "delRecord")
    public void delRecord(HttpServletRequest request,
                          HttpServletResponse response) {
        String dicTypeId = request.getParameter("treeNodeId");
        dicTypeService.deleteDicTypeCascade(dicTypeId);
        PlatEhcacheUtil.moveEhcacheByKey(CACHE_KEY);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }
}
