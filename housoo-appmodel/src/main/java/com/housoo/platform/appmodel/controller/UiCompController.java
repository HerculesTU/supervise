package com.housoo.platform.appmodel.controller;

import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.core.service.UiCompService;
import com.housoo.platform.common.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述 UiComp业务相关Controller
 *
 * @author housoo
 * @version 1.0
 * @created 2017-02-21 10:11:07
 */
@Controller
@RequestMapping("/appmodel/UiCompController")
public class UiCompController extends BaseController {
    /**
     *
     */
    @Resource
    private UiCompService uiCompService;

    /**
     * 获取列表的JSON数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "datagrid")
    public void datagrid(HttpServletRequest request,
                         HttpServletResponse response) {
        SqlFilter filter = new SqlFilter(request);
        filter.addFilter("O_P.COMP_CREATETIME", "DESC", SqlFilter.FILTER_TYPE_ORDER);
        filter.addFilter("O_D.DIC_SN", "ASC", SqlFilter.FILTER_TYPE_ORDER);
        List<Map<String, Object>> list = uiCompService.findBySqlFilter(filter);
        this.printListJsonString(filter.getPagingBean(), list, response);
    }

    /**
     * 跳转到表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String COMP_ID = request.getParameter("COMP_ID");
        String COMP_TYPECODE = request.getParameter("COMP_TYPECODE");
        Map<String, Object> uicomp = null;
        if (StringUtils.isNotEmpty(COMP_ID)) {
            uicomp = uiCompService.getRecord("PLAT_APPMODEL_UICOMP",
                    new String[]{"COMP_ID"}, new Object[]{COMP_ID});
        } else {
            uicomp = new HashMap<String, Object>();
            uicomp.put("COMP_TYPECODE", COMP_TYPECODE);
        }
        request.setAttribute("uicomp", uicomp);
        return PlatUICompUtil.goDesignUI("uicompform", request);
    }

    /**
     * 新增或者修改组件信息
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> uicomp = PlatBeanUtil.getMapFromRequest(request);
        uicomp = uiCompService.saveOrUpdate("PLAT_APPMODEL_UICOMP", uicomp,
                SysConstants.ID_GENERATOR_UUID, null);
        uicomp.put("success", true);
        this.printObjectJsonString(uicomp, response);
    }

    /**
     * 获取ui组件列表
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "getuicompList")
    public ModelAndView getuicompList(HttpServletRequest request) {
        String compTypeCode = request.getParameter("compTypeCode");
        if ("0".equals(compTypeCode)) {
            compTypeCode = null;
        }
        List<Map<String, Object>> uicompList = uiCompService.findByCompTypeCode(compTypeCode);
        request.setAttribute("uicompList", uicompList);
        return new ModelAndView("background/appmodel/formcontrol/uicomp_list");
    }

    /**
     * 跳转到Sql构建器界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goSqlConfigForm")
    public ModelAndView goSqlConfigForm(HttpServletRequest request) {
        String FORMCONTROL_DESIGN_ID = request.getParameter("FORMCONTROL_DESIGN_ID");
        Map<String, Object> sqlConfig = new HashMap<String, Object>();
        sqlConfig.put("FORMCONTROL_DESIGN_ID", FORMCONTROL_DESIGN_ID);
        request.setAttribute("sqlConfig", sqlConfig);
        return new ModelAndView("common/compdesign/jqgrid/sqlconfig_form");
    }

    /**
     * 删除字典信息
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        uiCompService.deleteRecords("PLAT_APPMODEL_UICOMP", "COMP_ID", selectColValues.split(","));
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }


}
