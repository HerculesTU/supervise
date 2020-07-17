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
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatFileUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.SysLogService;

/**
 * 描述 系统日志业务相关Controller
 *
 * @author housoo
 * @version 1.0
 * @created 2017-04-17 16:24:38
 */
@Controller
@RequestMapping("/system/SysLogController")
public class SysLogController extends BaseController {
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 读取系统Log4j日志
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "getLogContent")
    public void getLogContent(HttpServletRequest request,
                              HttpServletResponse response) {
        String startRowNum = request.getParameter("startRowNum");
        String folderPath = PlatAppUtil.getAppAbsolutePath();
        String logFilePath = folderPath + "/WEB-INF/logs/daily.log";
        String logContent = PlatFileUtil.readFileString(logFilePath,
                Integer.parseInt(startRowNum));
        int endRowNum = PlatFileUtil.getTextFileTotalLine(logFilePath);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("logContent", logContent);
        result.put("endRowNum", endRowNum);
        this.printObjectJsonString(result, response);
    }


    /**
     * 新增或者修改系统日志数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> sysLog = PlatBeanUtil.getMapFromRequest(request);
        sysLog = sysLogService.saveOrUpdate("PLAT_SYSTEM_LOG",
                sysLog, SysConstants.ID_GENERATOR_UUID, null);
        //如果是保存树形结构表数据,请调用下面的接口,而注释掉上面的接口代码
        //sysLog = sysLogService.saveOrUpdateTreeData("PLAT_SYSTEM_LOG",
        //        sysLog,SysConstants.ID_GENERATOR_UUID,null);
        sysLog.put("success", true);
        this.printObjectJsonString(sysLog, response);
    }

    /**
     * 跳转到系统日志表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String LOG_ID = request.getParameter("LOG_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> sysLog = null;
        if (StringUtils.isNotEmpty(LOG_ID)) {
            sysLog = this.sysLogService.getRecord("PLAT_SYSTEM_LOG"
                    , new String[]{"LOG_ID"}, new Object[]{LOG_ID});
        } else {
            sysLog = new HashMap<String, Object>();
        }
        request.setAttribute("sysLog", sysLog);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }

    /**
     * 跳转到系统日志详情界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goDetail")
    public ModelAndView goDetail(HttpServletRequest request) {
        String LOG_ID = request.getParameter("LOG_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = "";
        Map<String, Object> sysLog = this.sysLogService.getRecord("PLAT_SYSTEM_SYSLOG"
                , new String[]{"LOG_ID"}, new Object[]{LOG_ID});
        int OPER_TYPE = Integer.parseInt(sysLog.get("OPER_TYPE").toString());
        if (OPER_TYPE == SysLogService.OPER_TYPE_ADD) {
            UI_DESIGNCODE = "syslog_addform";
        } else if (OPER_TYPE == SysLogService.OPER_TYPE_EDIT) {
            UI_DESIGNCODE = "syslog_editform";
        } else if (OPER_TYPE == SysLogService.OPER_TYPE_DEL) {
            UI_DESIGNCODE = "syslog_delform";
        }
        request.setAttribute("sysLog", sysLog);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }
}
