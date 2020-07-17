package com.housoo.platform.system.controller;

import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.service.ScheduleService;
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
 * 描述 定时任务业务相关Controller
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-04-27 16:37:05
 */
@Controller
@RequestMapping("/system/ScheduleController")
public class ScheduleController extends BaseController {
    /**
     *
     */
    @Resource
    private ScheduleService scheduleService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除定时任务数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "updateStatus")
    public void updateStatus(HttpServletRequest request,
                             HttpServletResponse response) {
        String scheduleIds = request.getParameter("selectColValues");
        String status = request.getParameter("status");
        scheduleService.updateStatus(scheduleIds, Integer.parseInt(status));
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 删除定时任务数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        scheduleService.deleteCascadeJob(selectColValues);
        sysLogService.saveBackLog("定时器管理", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的定时任务", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改定时任务数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> schedule = PlatBeanUtil.getMapFromRequest(request);
        String SCHEDULE_ID = (String) schedule.get("SCHEDULE_ID");
        if (StringUtils.isEmpty(SCHEDULE_ID)) {
            String SCHEDULE_CODE = (String) schedule.get("SCHEDULE_CODE");
            boolean isExists = scheduleService.isExistsSchedule(SCHEDULE_CODE);
            if (isExists) {
                schedule.put("success", false);
                schedule.put("msg", "该定时器编码已经存在!");
                this.printObjectJsonString(schedule, response);
                return;
            }
        }
        schedule = scheduleService.saveOrUpdateCascadeJob(schedule);
        if (StringUtils.isNotEmpty(SCHEDULE_ID)) {
            sysLogService.saveBackLog("定时器管理", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为[" + SCHEDULE_ID + "]定时任务", request);
        } else {
            SCHEDULE_ID = (String) schedule.get("SCHEDULE_ID");
            sysLogService.saveBackLog("定时器管理", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为[" + SCHEDULE_ID + "]定时任务", request);
        }
        schedule.put("success", true);
        this.printObjectJsonString(schedule, response);
    }

    /**
     * 跳转到定时任务表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String SCHEDULE_ID = request.getParameter("SCHEDULE_ID");
        Map<String, Object> schedule = null;
        if (StringUtils.isNotEmpty(SCHEDULE_ID)) {
            schedule = this.scheduleService.getRecord("PLAT_SYSTEM_SCHEDULE"
                    , new String[]{"SCHEDULE_ID"}, new Object[]{SCHEDULE_ID});
        } else {
            schedule = new HashMap<String, Object>();
            schedule.put("SCHEDULE_CRON", "* * * * * ?");
        }
        request.setAttribute("schedule", schedule);
        return new ModelAndView("background/system/schedule/scheduleForm");
    }

}
