package com.housoo.platform.appmodel.controller;

import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.core.service.TableColService;
import com.housoo.platform.common.controller.BaseController;
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
 * 描述 表格列业务相关Controller
 *
 * @author housoo
 * @version 1.0
 * @created 2017-03-30 15:16:45
 */
@Controller
@RequestMapping("/appmodel/TableColController")
public class TableColController extends BaseController {
    /**
     *
     */
    @Resource
    private TableColService tableColService;

    /**
     * 删除表格列数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        tableColService.deleteRecords("PLAT_APPMODEL_TABLECOL", "TABLECOL_ID", selectColValues.split(","));
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改表格列数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> tableCol = PlatBeanUtil.getMapFromRequest(request);
        String TABLECOL_ID = (String) tableCol.get("TABLECOL_ID");
        String TABLECOL_FORMCONTROLID = (String) tableCol.get("TABLECOL_FORMCONTROLID");
        if (StringUtils.isEmpty(TABLECOL_ID)) {
            int TABLECOL_ORERSN = tableColService.getNextSn(TABLECOL_FORMCONTROLID);
            tableCol.put("TABLECOL_ORERSN", TABLECOL_ORERSN);
        }
        tableCol = tableColService.saveOrUpdate("PLAT_APPMODEL_TABLECOL",
                tableCol, SysConstants.ID_GENERATOR_UUID, null);
        tableCol.put("success", true);
        this.printObjectJsonString(tableCol, response);
    }

    /**
     * 跳转到表格列表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String TABLECOL_ID = request.getParameter("TABLECOL_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        String FORMCONTROL_ID = request.getParameter("FORMCONTROL_ID");
        Map<String, Object> tableCol = null;
        if (StringUtils.isNotEmpty(TABLECOL_ID)) {
            tableCol = this.tableColService.getRecord("PLAT_APPMODEL_TABLECOL"
                    , new String[]{"TABLECOL_ID"}, new Object[]{TABLECOL_ID});
        } else {
            tableCol = new HashMap<String, Object>();
            tableCol.put("TABLECOL_FORMCONTROLID", FORMCONTROL_ID);
        }
        request.setAttribute("tableCol", tableCol);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }
}
