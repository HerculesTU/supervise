package com.housoo.platform.system.controller;

import com.housoo.platform.core.util.CommonExcelView;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatDateTimeUtil;
import com.housoo.platform.core.util.PlatLogUtil;
import com.housoo.platform.core.util.PlatPropUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.SysLogBkService;
import com.housoo.platform.core.service.SysLogService;

import java.util.Calendar;
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

/**
 * 描述 系统日志备份业务相关Controller
 *
 * @author 高飞
 * @version 1.0
 * @created 2018-05-10 16:24:38
 */
@Controller
@RequestMapping({"/system/SysLogBkController"})
public class SysLogBkController extends BaseController {
    /**
     *
     */
    @Resource
    private SysLogBkService sysLogBkService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     *
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request, HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        this.sysLogBkService.deleteRecords("PLAT_SYSTEM_SYSLOG_BK", "LOG_ID", selectColValues.split(","));
        this.sysLogService.saveBackLog("系统日志管理", 5,
                "删除了ID为[" + selectColValues + "]的系统日志备份表", request);
        Map result = new HashMap();
        result.put("success", Boolean.valueOf(true));
        printObjectJsonString(result, response);
    }

    /**
     *
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request, HttpServletResponse response) {
        Map sysLogBk = PlatBeanUtil.getMapFromRequest(request);
        String LOG_ID = (String) sysLogBk.get("LOG_ID");
        sysLogBk = this.sysLogBkService.saveOrUpdate("PLAT_SYSTEM_SYSLOG_BK",
                sysLogBk, 1, null);

        if (StringUtils.isNotEmpty(LOG_ID)) {
            this.sysLogService.saveBackLog("系统日志管理", 4,
                    "修改了ID为[" + LOG_ID + "]系统日志备份表", request);
        } else {
            LOG_ID = (String) sysLogBk.get("LOG_ID");
            this.sysLogService.saveBackLog("系统日志管理", 3,
                    "新增了ID为[" + LOG_ID + "]系统日志备份表", request);
        }
        sysLogBk.put("success", Boolean.valueOf(true));
        printObjectJsonString(sysLogBk, response);
    }

    /**
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String LOG_ID = request.getParameter("LOG_ID");

        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map sysLogBk = null;
        if (StringUtils.isNotEmpty(LOG_ID)) {
            sysLogBk = this.sysLogBkService.getRecord("PLAT_SYSTEM_SYSLOG_BK",
                    new String[]{"LOG_ID"}, new Object[]{LOG_ID});
        } else {
            sysLogBk = new HashMap();
        }
        request.setAttribute("sysLogBk", sysLogBk);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }

    /**
     *
     */
    @RequestMapping(params = "copyLogDataForm")
    public ModelAndView copyLogDataForm(HttpServletRequest request) {
        String type = request.getParameter("type");

        Date preDate = PlatDateTimeUtil.getNextTime(new Date(), Calendar.MONTH, -6);
        String maxDay = PlatDateTimeUtil.formatDate(preDate, "yyyy-MM-dd");
        request.setAttribute("MAX_TIME", maxDay);
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        request.setAttribute("type", type);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }

    /**
     *
     */
    @RequestMapping(params = "copyLog")
    public void copyLog(HttpServletRequest request, HttpServletResponse response) {
        Map sysLogBk = PlatBeanUtil.getMapFromRequest(request);
        this.sysLogBkService.copyLog(sysLogBk);
        Map result = new HashMap();
        result.put("success", Boolean.valueOf(true));
        printObjectJsonString(result, response);
    }

    /**
     *
     */
    @RequestMapping(params = "exportExcel")
    public ModelAndView exportExcel(Map<String, Object> model,
                                    HttpServletRequest request, HttpServletResponse response) {
        Map sysLogBk = PlatBeanUtil.getMapFromRequest(request);
        String excelFileName = "";
        String type = (String) sysLogBk.get("TYPE");
        if ("1".equals(type)) {
            excelFileName = "普通用户操作日志";
        } else if ("2".equals(type)) {
            excelFileName = "系统管理员操作日志";
        } else if ("3".equals(type)) {
            excelFileName = "安全保密管理员操作日志";
        } else if ("4".equals(type)) {
            excelFileName = "安全审计员操作日志";
        }
        List list = this.sysLogBkService.findExportData(sysLogBk);
        String[] titles = {"日志ID", "操作模块", "操作类型", "操作人账号",
                "操作人姓名", "操作时间", "浏览器", "访问IP地址", "日志内容"};
        String[] values = {"LOG_ID", "OPER_MODULENAME", "OPER_TYPE", "OPER_USERACCOUNT",
                "OPER_USERNAME", "OPER_TIME", "BROWSER", "IP_ADDRESS", "LOG_CONTENT"};
        model.put("excelFileName", excelFileName);
        model.put("list", list);
        model.put("values", values);
        model.put("titles", titles);
        model.put("singleHeader", "true");
        CommonExcelView viewExcel = new CommonExcelView();
        return new ModelAndView(viewExcel, model);
    }

    /**
     * @param request
     * @param response
     */
    @RequestMapping(params = "backLogData")
    public void backLogData(HttpServletRequest request, HttpServletResponse response) {
        Map sysLogBk = PlatBeanUtil.getMapFromRequest(request);
        Map result = new HashMap();
        try {
            this.sysLogBkService.backLogData(sysLogBk);
            result.put("success", true);
        } catch (Exception e) {
            PlatLogUtil.printStackTrace(e);
            result.put("success", false);
        }
        printObjectJsonString(result, response);
    }

    /**
     * @param request
     * @return
     */
    @RequestMapping(params = "goBkInfo")
    public ModelAndView goBkInfo(HttpServletRequest request) {
        String BKINFO_INFO = request.getParameter("BKINFO_INFO");

        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map bkinfo = null;
        if (StringUtils.isNotEmpty(BKINFO_INFO)) {
            bkinfo = this.sysLogBkService.getRecord("PLAT_SYSTEM_SYSLOG_BKINFO",
                    new String[]{"BKINFO_INFO"}, new Object[]{BKINFO_INFO});
        } else {
            bkinfo = new HashMap();
        }
        request.setAttribute("bkinfo", bkinfo);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }

    /**
     * @param request
     * @param response
     */
    @RequestMapping(params = "impExcelDatas")
    public void impExcelDatas(HttpServletRequest request,
                              HttpServletResponse response) {
        String dbfilepath = request.getParameter("dbfilepath");
        String type = request.getParameter("type");
        //获取文件存储路径
        String attachFilePath = PlatPropUtil.getPropertyValue("config.properties"
                , "attachFilePath");
        String excelFilePath = attachFilePath + dbfilepath;
        Map<String, Object> result = null;
        try {
            result = sysLogBkService.impExcelDatas(excelFilePath, type);
        } catch (Exception e) {
            result.put("success", false);
            result.put("msg", "服务器错误,导入失败!");
            PlatLogUtil.printStackTrace(e);
        }
        this.printObjectJsonString(result, response);
    }
}