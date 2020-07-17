package com.housoo.platform.system.controller;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jxls.transformer.XLSTransformer;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatDateTimeUtil;
import com.housoo.platform.core.util.PlatEhcacheUtil;
import com.housoo.platform.core.util.PlatLogUtil;
import com.housoo.platform.core.util.PlatOfficeUtil;
import com.housoo.platform.core.util.PlatStringUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.core.service.WorkdayService;

/**
 * 描述 工作日业务相关Controller
 *
 * @author housoo
 * @version 1.0
 * @created 2017-05-03 09:32:53
 */
@Controller
@RequestMapping("/system/WorkdayController")
public class WorkdayController extends BaseController {
    /**
     *
     */
    @Resource
    private WorkdayService workdayService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除工作日数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        workdayService.deleteRecords("PLAT_SYSTEM_WORKDAY", "WORKDAY_ID", selectColValues.split(","));
        sysLogService.saveBackLog("系统工作日管理", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的工作日", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改工作日数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> workday = PlatBeanUtil.getMapFromRequest(request);
        String WORKDAY_ID = (String) workday.get("WORKDAY_ID");
        workday = workdayService.saveOrUpdate("PLAT_SYSTEM_WORKDAY",
                workday, SysConstants.ID_GENERATOR_UUID, null);
        //如果是保存树形结构表数据,请调用下面的接口,而注释掉上面的接口代码
        //workday = workdayService.saveOrUpdateTreeData("PLAT_SYSTEM_WORKDAY",
        //        workday, SysConstants.ID_GENERATOR_UUID,null);
        if (StringUtils.isNotEmpty(WORKDAY_ID)) {
            sysLogService.saveBackLog("系统工作日管理", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为[" + WORKDAY_ID + "]工作日", request);
        } else {
            WORKDAY_ID = (String) workday.get("WORKDAY_ID");
            sysLogService.saveBackLog("系统工作日管理", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为[" + WORKDAY_ID + "]工作日", request);
        }
        workday.put("success", true);
        this.printObjectJsonString(workday, response);
    }

    /**
     * 跳转到工作日表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String WORKDAY_ID = request.getParameter("WORKDAY_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> workday = null;
        if (StringUtils.isNotEmpty(WORKDAY_ID)) {
            workday = this.workdayService.getRecord("PLAT_SYSTEM_WORKDAY"
                    , new String[]{"WORKDAY_ID"}, new Object[]{WORKDAY_ID});
        } else {
            workday = new HashMap<String, Object>();
        }
        request.setAttribute("workday", workday);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }

    /**
     * 跳转到界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goView")
    public ModelAndView goView(HttpServletRequest request) {
        return new ModelAndView("background/system/workday/view");
    }

    /**
     * 获取工作台数据
     *
     * @param request
     * @param response
     * @throws ParseException
     */
    @RequestMapping(params = "findData")
    public void findData(HttpServletRequest request, HttpServletResponse response) throws ParseException {
        SqlFilter filter = new SqlFilter(request);
        List<Map<String, Object>> list = workdayService.findDataBySqlFilter(filter);
        for (Map<String, Object> map : list) {
            if ("1".equals(map.get("WORKDAY_SETID").toString())) {
                map.put("title", "休息日");
                map.put("color", "#FF0000");
            } else if ("2".equals(map.get("WORKDAY_SETID").toString())) {
                map.put("title", "工作日");
            }
            map.put("start", (String) map.get("WORKDAY_DATE"));
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        String data = JSON.toJSONString(list);
        result.put("data", data);
        this.printObjectJsonString(result, response);
    }

    /**
     * @param request
     * @param response
     */
    @RequestMapping(params = "testAdd")
    public void testAdd(HttpServletRequest request,
                        HttpServletResponse response) {
        // String str = workdayService.testAdd(PlatDateTimeUtil.formatDate(new Date(), "YYYY-MM-DD"));
        String str = (String) PlatEhcacheUtil.getEhcache("aaa");
        if (str == null) {
            str = PlatDateTimeUtil.formatDate(new Date(), "YYYY-MM-dd HH:mm:ss:mss");
            PlatEhcacheUtil.addEhcache("aaa", str, 2, 2);
        }
        String str2 = (String) PlatEhcacheUtil.getEhcache("bbb");
        if (str2 == null) {
            str2 = PlatDateTimeUtil.formatDate(new Date(), "YYYY-MM-dd HH:mm:ss:mss");
            PlatEhcacheUtil.addEhcache("bbb", str2);
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        result.put("str", str);
        result.put("str2", str2);
        this.printObjectJsonString(result, response);
    }

    /**
     * @param request
     * @param response
     */
    @RequestMapping(params = "testDel")
    public void testDel(HttpServletRequest request,
                        HttpServletResponse response) {
        //workdayService.testDel();
        PlatEhcacheUtil.moveEhcacheByKey("aaa");
        PlatEhcacheUtil.moveEhcacheByKey("bbb");
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 跳转到统计界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goStatisticsView")
    public ModelAndView goStatisticsView(HttpServletRequest request) {
        return new ModelAndView("background/system/workday/goStatisticsView");
    }
}
