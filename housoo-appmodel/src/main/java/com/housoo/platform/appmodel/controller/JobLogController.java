package com.housoo.platform.appmodel.controller;

import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.core.service.JobLogService;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.SysLogService;
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
 * 描述 定时器日志业务相关Controller
 *
 * @author housoo
 * @version 1.0
 * @created 2017-04-29 16:05:22
 */
@Controller
@RequestMapping("/system/JobLogController")
public class JobLogController extends BaseController {
    /**
     *
     */
    @Resource
    private JobLogService jobLogService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除定时器日志数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        jobLogService.deleteRecords("PLAT_SYSTEM_JOBLOG", "JOBLOG_ID", selectColValues.split(","));
        sysLogService.saveBackLog("定时器管理", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的定时器日志", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改定时器日志数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> jobLog = PlatBeanUtil.getMapFromRequest(request);
        String JOBLOG_ID = (String) jobLog.get("JOBLOG_ID");
        jobLog = jobLogService.saveOrUpdate("PLAT_SYSTEM_JOBLOG",
                jobLog, SysConstants.ID_GENERATOR_UUID, null);
        if (StringUtils.isNotEmpty(JOBLOG_ID)) {
            sysLogService.saveBackLog("定时器管理", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为[" + JOBLOG_ID + "]定时器日志", request);
        } else {
            JOBLOG_ID = (String) jobLog.get("JOBLOG_ID");
            sysLogService.saveBackLog("定时器管理", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为[" + JOBLOG_ID + "]定时器日志", request);
        }
        jobLog.put("success", true);
        this.printObjectJsonString(jobLog, response);
    }

    /**
     * 跳转到定时器日志表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String JOBLOG_ID = request.getParameter("JOBLOG_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> jobLog = null;
        if (StringUtils.isNotEmpty(JOBLOG_ID)) {
            jobLog = this.jobLogService.getRecord("PLAT_SYSTEM_JOBLOG"
                    , new String[]{"JOBLOG_ID"}, new Object[]{JOBLOG_ID});
        } else {
            jobLog = new HashMap<String, Object>();
        }
        request.setAttribute("jobLog", jobLog);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }
}
