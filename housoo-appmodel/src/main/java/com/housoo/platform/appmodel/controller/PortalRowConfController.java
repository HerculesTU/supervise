package com.housoo.platform.appmodel.controller;

import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.core.service.PortalRowConfService;
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
 * 描述 行组件配置业务相关Controller
 *
 * @author housoo
 * @version 1.0
 * @created 2017-06-08 15:47:00
 */
@Controller
@RequestMapping("/appmodel/PortalRowConfController")
public class PortalRowConfController extends BaseController {
    /**
     *
     */
    @Resource
    private PortalRowConfService portalRowConfService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除行组件配置数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        portalRowConfService.deleteRecords("PLAT_APPMODEL_PORTALROWCONF", "CONF_ID", selectColValues.split(","));
        sysLogService.saveBackLog("门户管理", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的行组件配置", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改行组件配置数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> portalRowconf = PlatBeanUtil.getMapFromRequest(request);
        //获取主题ID
        String THEME_ID = (String) portalRowconf.get("THEME_ID");
        String CONF_COMPID = (String) portalRowconf.get("CONF_COMPID");
        String CONF_ID = (String) portalRowconf.get("CONF_ID");
        boolean isExists = portalRowConfService.isExists(THEME_ID, CONF_ID, CONF_COMPID);
        if (isExists) {
            portalRowconf.put("success", false);
            portalRowconf.put("msg", "当前主题中已经配置了该组件,不允许重复配置!");
        } else {
            portalRowconf = portalRowConfService.saveOrUpdate("PLAT_APPMODEL_PORTALROWCONF",
                    portalRowconf, SysConstants.ID_GENERATOR_UUID, null);
            portalRowconf.put("success", true);
        }
        this.printObjectJsonString(portalRowconf, response);
    }

    /**
     * 跳转到行组件配置表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String CONF_ID = request.getParameter("CONF_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        String THEME_ID = request.getParameter("THEME_ID");
        Map<String, Object> portalRowconf = null;
        if (StringUtils.isNotEmpty(CONF_ID)) {
            portalRowconf = this.portalRowConfService.getRecord("PLAT_APPMODEL_PORTALROWCONF"
                    , new String[]{"CONF_ID"}, new Object[]{CONF_ID});
        } else {
            portalRowconf = new HashMap<String, Object>();
        }
        portalRowconf.put("THEME_ID", THEME_ID);
        request.setAttribute("portalRowconf", portalRowconf);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }
}
