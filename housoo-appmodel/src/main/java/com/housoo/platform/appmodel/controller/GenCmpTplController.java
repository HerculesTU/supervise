package com.housoo.platform.appmodel.controller;

import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.core.service.GenCmpTplService;
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
 * 描述 GenCmpTpl业务相关Controller
 *
 * @author housoo
 * @version 1.0
 * @created 2017-03-15 17:15:24
 */
@Controller
@RequestMapping("/appmodel/GenCmpTplController")
public class GenCmpTplController extends BaseController {
    /**
     *
     */
    @Resource
    private GenCmpTplService genCmpTplService;

    /**
     * 删除通用组件信息
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        genCmpTplService.deleteRecords("PLAT_APPMODEL_GENCMPTPL", "GENCMPTPL_ID", selectColValues.split(","));
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
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
        Map<String, Object> genCmpTpl = PlatBeanUtil.getMapFromRequest(request);
        genCmpTpl = genCmpTplService.saveOrUpdate("PLAT_APPMODEL_GENCMPTPL",
                genCmpTpl, SysConstants.ID_GENERATOR_UUID, null);
        //如果是保存树形结构表数据,请调用下面的接口,而注释掉上面的接口代码
        //genCmpTpl = genCmpTplService.saveOrUpdateTreeData("PLAT_APPMODEL_GENCMPTPL",
        //        genCmpTpl,AllConstants.IDGENERATOR_UUID,null);
        genCmpTpl.put("success", true);
        this.printObjectJsonString(genCmpTpl, response);
    }

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
        filter.addFilter("O_T.GENCMPTPL_CREATETIME", "DESC", SqlFilter.FILTER_TYPE_ORDER);
        List<Map<String, Object>> list = genCmpTplService.findBySqlFilter(filter);
        this.printListJsonString(filter.getPagingBean(), list, response);
    }

    /**
     * 跳转到查询条件配置界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String GENCMPTPL_ID = request.getParameter("GENCMPTPL_ID");
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> genCmpTpl = null;
        if (StringUtils.isNotEmpty(GENCMPTPL_ID)) {
            genCmpTpl = this.genCmpTplService.getRecord("PLAT_APPMODEL_GENCMPTPL"
                    , new String[]{"GENCMPTPL_ID"}, new Object[]{GENCMPTPL_ID});
        } else {
            genCmpTpl = new HashMap<String, Object>();
            genCmpTpl.put("GENCMPTPL_TYPE", "1");
        }
        request.setAttribute("genCmpTpl", genCmpTpl);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }

}
