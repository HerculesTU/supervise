package com.housoo.platform.appmodel.controller;

import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.service.TableButtonService;
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
 * 描述 表格按钮业务相关Controller
 *
 * @author housoo
 * @version 1.0
 * @created 2017-03-17 10:25:49
 */
@Controller
@RequestMapping("/appmodel/TableButtonController")
public class TableButtonController extends BaseController {
    /**
     *
     */
    @Resource
    private TableButtonService tableButtonService;

    /**
     * 新增或者修改表格按钮
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> tableButton = PlatBeanUtil.getMapFromRequest(request);
        String TABLEBUTTON_ID = (String) tableButton.get("TABLEBUTTON_ID");
        if (StringUtils.isEmpty(TABLEBUTTON_ID)) {
            String formControlId = (String) tableButton.get("TABLEBUTTON_FORMCONTROLID");
            int nextSn = tableButtonService.getNextSn(formControlId);
            tableButton.put("TABLEBUTTON_SN", nextSn);
        }
        tableButton = tableButtonService.saveOrUpdate("PLAT_APPMODEL_TABLEBUTTON",
                tableButton, SysConstants.ID_GENERATOR_UUID, null);
        tableButton.put("success", true);
        this.printObjectJsonString(tableButton, response);
    }

    /**
     * 跳转到操作按钮界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String TABLEBUTTON_ID = request.getParameter("TABLEBUTTON_ID");
        String FORMCONTROL_ID = request.getParameter("FORMCONTROL_ID");
        Map<String, Object> tableButton = null;
        if (StringUtils.isNotEmpty(TABLEBUTTON_ID)) {
            tableButton = tableButtonService.getRecord("PLAT_APPMODEL_TABLEBUTTON",
                    new String[]{"TABLEBUTTON_ID"}, new Object[]{TABLEBUTTON_ID});
        } else {
            tableButton = new HashMap<String, Object>();
            tableButton.put("TABLEBUTTON_FORMCONTROLID", FORMCONTROL_ID);
            tableButton.put("TABLEBUTTON_UPLOADED", "false");
            tableButton.put("TABLEBUTTON_IMPFILETYPES", "xls,xlsx");
            tableButton.put("TABLEBUTTON_IMPFILESIZE", "10485760");
            tableButton.put("TABLEBUTTON_IMPURL", "appmodel/ExcelImpController.do?impExcelDatas&excelimpcode=");
        }
        request.setAttribute("tableButton", tableButton);
        return new ModelAndView("background/appmodel/tablebutton/tablebutton_form");
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
        filter.addFilter("O_T.TABLEBUTTON_SN", "ASC", SqlFilter.FILTER_TYPE_ORDER);
        List<Map<String, Object>> list = tableButtonService.findBySqlFilter(filter);
        this.printListJsonString(null, list, response);
    }

    /**
     * 删除按钮配置信息
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        tableButtonService.deleteRecords("PLAT_APPMODEL_TABLEBUTTON"
                , "TABLEBUTTON_ID", selectColValues.split(","));
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }
}
