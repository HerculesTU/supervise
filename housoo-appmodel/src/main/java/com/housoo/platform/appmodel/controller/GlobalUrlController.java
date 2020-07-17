package com.housoo.platform.appmodel.controller;

import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.core.service.GlobalUrlService;
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
 * 描述 全局URL业务相关Controller
 *
 * @author housoo
 * @version 1.0
 * @created 2017-04-23 09:22:54
 */
@Controller
@RequestMapping("/system/GlobalUrlController")
public class GlobalUrlController extends BaseController {
    /**
     *
     */
    @Resource
    private GlobalUrlService globalUrlService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除全局URL数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        globalUrlService.deleteRecords("PLAT_SYSTEM_GLOBALURL", "URL_ID", selectColValues.split(","));
        sysLogService.saveBackLog("全局URL管理", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的全局URL", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改全局URL数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> globalUrl = PlatBeanUtil.getMapFromRequest(request);
        String URL_ID = (String) globalUrl.get("URL_ID");
        globalUrl = globalUrlService.saveOrUpdate("PLAT_SYSTEM_GLOBALURL",
                globalUrl, SysConstants.ID_GENERATOR_UUID, null);
        if (StringUtils.isNotEmpty(URL_ID)) {
            sysLogService.saveBackLog("全局URL管理", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为[" + URL_ID + "]全局URL", request);
        } else {
            URL_ID = (String) globalUrl.get("URL_ID");
            sysLogService.saveBackLog("全局URL管理", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为[" + URL_ID + "]全局URL", request);
        }
        globalUrl.put("success", true);
        this.printObjectJsonString(globalUrl, response);
    }

    /**
     * 跳转到全局URL表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String URL_ID = request.getParameter("URL_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> globalUrl = null;
        if (StringUtils.isNotEmpty(URL_ID)) {
            globalUrl = this.globalUrlService.getRecord("PLAT_SYSTEM_GLOBALURL"
                    , new String[]{"URL_ID"}, new Object[]{URL_ID});
        } else {
            globalUrl = new HashMap<String, Object>();
        }
        request.setAttribute("globalUrl", globalUrl);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }
}
