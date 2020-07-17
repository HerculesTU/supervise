package com.housoo.platform.system.controller;

import com.housoo.platform.core.util.*;
import com.housoo.platform.core.service.SysEhcacheService;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.SysLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述 缓存配置业务相关Controller
 *
 * @author housoo
 * @version 1.0
 * @created 2017-05-03 16:14:24
 */
@Controller
@RequestMapping("/system/SysEhcacheController")
public class SysEhcacheController extends BaseController {
    /**
     *
     */
    @Resource
    private SysEhcacheService sysEhcacheService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除缓存配置数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        sysEhcacheService.deleteRecords("PLAT_SYSTEM_EHCACHE", "EHCACHE_ID", selectColValues.split(","));
        sysLogService.saveBackLog("缓存配置管理", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的缓存配置", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改缓存配置数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> sysEhcache = PlatBeanUtil.getMapFromRequest(request);
        String EHCACHE_ID = (String) sysEhcache.get("EHCACHE_ID");
        if (StringUtils.isEmpty(EHCACHE_ID)) {
            sysEhcache.put("EHCACHE_CREATE_TIME", PlatDateTimeUtil.formatDate(new Date(),
                    "yyyy-MM-dd HH:mm:ss"));
            sysEhcache.put("EHCACHE_STATUE", "1");
        }
        sysEhcache = sysEhcacheService.saveOrUpdate("PLAT_SYSTEM_EHCACHE",
                sysEhcache, SysConstants.ID_GENERATOR_UUID, null);
        if (StringUtils.isNotEmpty(EHCACHE_ID)) {
            sysLogService.saveBackLog("缓存配置管理", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为[" + EHCACHE_ID + "]缓存配置", request);
        } else {
            EHCACHE_ID = (String) sysEhcache.get("EHCACHE_ID");
            sysLogService.saveBackLog("缓存配置管理", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为[" + EHCACHE_ID + "]缓存配置", request);
        }
        sysEhcache.put("success", true);
        this.printObjectJsonString(sysEhcache, response);
    }

    /**
     * 跳转到缓存配置表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String EHCACHE_ID = request.getParameter("EHCACHE_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> sysEhcache = null;
        if (StringUtils.isNotEmpty(EHCACHE_ID)) {
            sysEhcache = this.sysEhcacheService.getRecord("PLAT_SYSTEM_EHCACHE"
                    , new String[]{"EHCACHE_ID"}, new Object[]{EHCACHE_ID});
        } else {
            sysEhcache = new HashMap<String, Object>();
        }
        request.setAttribute("sysEhcache", sysEhcache);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }

    /**
     * 删除指定缓存
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "manualReloadEhcache")
    public void manualReloadEhcache(HttpServletRequest request,
                                    HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        sysEhcacheService.manualReloadEhcache(selectColValues);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 删除所有的缓存
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "manualAllEhcache")
    @ResponseBody
    public void manualAllEhcache(HttpServletRequest request,
                                 HttpServletResponse response) {
        PlatEhcacheUtil.moveAllEhcache();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * @param request
     * @param response
     */
    @RequestMapping(params = "enable")
    public void enable(HttpServletRequest request,
                       HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        sysEhcacheService.updateEhcacheStatue(selectColValues, "1");
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * @param request
     * @param response
     */
    @RequestMapping(params = "disable")
    public void disable(HttpServletRequest request,
                        HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        sysEhcacheService.updateEhcacheStatue(selectColValues, "0");
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }
}
