package com.housoo.platform.appmodel.controller;

import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatDateTimeUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.core.service.DbConnService;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.SysLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述 数据源信息业务相关Controller
 *
 * @author housoo
 * @version 1.0
 * @created 2018-03-30 15:06:38
 */
@Controller
@RequestMapping("/appmodel/DbConnController")
public class DbConnController extends BaseController {
    /**
     *
     */
    @Resource
    private DbConnService dbConnService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除数据源信息数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        dbConnService.deleteRecords("PLAT_APPMODEL_DBCONN", "DBCONN_ID", selectColValues.split(","));
        sysLogService.saveBackLog("元数据管理", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的数据源信息", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改数据源信息数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> dbConn = PlatBeanUtil.getMapFromRequest(request);
        String DBCONN_ID = (String) dbConn.get("DBCONN_ID");
        if (StringUtils.isEmpty(DBCONN_ID)) {
            dbConn.put("DBCONN_TIME", PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        }
        dbConn = dbConnService.saveOrUpdate("PLAT_APPMODEL_DBCONN",
                dbConn, SysConstants.ID_GENERATOR_UUID, null);
        dbConn.put("success", true);
        this.printObjectJsonString(dbConn, response);
    }

    /**
     * 跳转到数据源信息表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String DBCONN_ID = request.getParameter("DBCONN_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> dbConn = null;
        if (StringUtils.isNotEmpty(DBCONN_ID)) {
            dbConn = this.dbConnService.getRecord("PLAT_APPMODEL_DBCONN"
                    , new String[]{"DBCONN_ID"}, new Object[]{DBCONN_ID});
        } else {
            dbConn = new HashMap<String, Object>();
        }
        request.setAttribute("dbConn", dbConn);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }

    /**
     * 验证数据源有效性
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "isValid")
    public void isValid(HttpServletRequest request,
                        HttpServletResponse response) {
        Map<String, Object> dbConn = PlatBeanUtil.getMapFromRequest(request);
        boolean isValidDb = dbConnService.isValidDb(dbConn);
        dbConn.put("success", isValidDb);
        this.printObjectJsonString(dbConn, response);
    }
}
