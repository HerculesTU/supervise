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
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.core.service.GlobalConfigService;

/**
 * 描述 全局配置业务相关Controller
 *
 * @author housoo
 * @version 1.0
 * @created 2018-05-09 13:52:03
 */
@Controller
@RequestMapping("/system/GlobalConfigController")
public class GlobalConfigController extends BaseController {
    /**
     *
     */
    @Resource
    private GlobalConfigService globalConfigService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除全局配置数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        globalConfigService.deleteRecords("PLAT_SYSTEM_GLOBALCONFIG", "CONFIG_ID", selectColValues.split(","));
        sysLogService.saveBackLog("全局配置", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的全局配置", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改全局配置数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> globalConfig = PlatBeanUtil.getMapFromRequest(request);
        String CONFIG_ID = (String) globalConfig.get("CONFIG_ID");
        globalConfig = globalConfigService.saveOrUpdate("PLAT_SYSTEM_GLOBALCONFIG",
                globalConfig, SysConstants.ID_GENERATOR_UUID, null);
        //如果是保存树形结构表数据,请调用下面的接口,而注释掉上面的接口代码
        //globalConfig = globalConfigService.saveOrUpdateTreeData("PLAT_SYSTEM_GLOBALCONFIG",
        //        globalConfig,SysConstants.ID_GENERATOR_UUID,null);
        if (StringUtils.isNotEmpty(CONFIG_ID)) {
            sysLogService.saveBackLog("全局配置", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为[" + CONFIG_ID + "]全局配置", request);
        } else {
            CONFIG_ID = (String) globalConfig.get("CONFIG_ID");
            sysLogService.saveBackLog("全局配置", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为[" + CONFIG_ID + "]全局配置", request);
        }
        globalConfig.put("success", true);
        this.printObjectJsonString(globalConfig, response);
    }

    /**
     * 跳转到全局配置表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String CONFIG_ID = request.getParameter("CONFIG_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> globalConfig = null;
        if (StringUtils.isNotEmpty(CONFIG_ID)) {
            globalConfig = this.globalConfigService.getRecord("PLAT_SYSTEM_GLOBALCONFIG"
                    , new String[]{"CONFIG_ID"}, new Object[]{CONFIG_ID});
        } else {
            globalConfig = this.globalConfigService.getFirstConfigMap();

        }
        request.setAttribute("globalConfig", globalConfig);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }
}
