package com.housoo.platform.appmodel.controller;

import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.core.service.CommonCodeService;
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
 * 描述 代码块业务相关Controller
 *
 * @author housoo
 * @version 1.0
 * @created 2017-05-14 09:15:53
 */
@Controller
@RequestMapping("/appmodel/CommonCodeController")
public class CommonCodeController extends BaseController {
    /**
     *
     */
    @Resource
    private CommonCodeService commonCodeService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除代码块数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        commonCodeService.deleteRecords("PLAT_APPMODEL_COMMONCODE", "COMMONCODE_ID", selectColValues.split(","));
        sysLogService.saveBackLog("常用代码块", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的代码块", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改代码块数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> commonCode = PlatBeanUtil.getMapFromRequest(request);
        commonCode = commonCodeService.saveOrUpdate("PLAT_APPMODEL_COMMONCODE",
                commonCode, SysConstants.ID_GENERATOR_UUID, null);
        commonCode.put("success", true);
        this.printObjectJsonString(commonCode, response);
    }

    /**
     * 跳转到代码块表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String COMMONCODE_ID = request.getParameter("COMMONCODE_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> commonCode = null;
        if (StringUtils.isNotEmpty(COMMONCODE_ID)) {
            commonCode = this.commonCodeService.getRecord("PLAT_APPMODEL_COMMONCODE"
                    , new String[]{"COMMONCODE_ID"}, new Object[]{COMMONCODE_ID});
        } else {
            commonCode = new HashMap<String, Object>();
        }
        request.setAttribute("commonCode", commonCode);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }

    /**
     * 获取代码
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "getCode")
    public void getCode(HttpServletRequest request,
                        HttpServletResponse response) {
        String COMMONCODE_ID = request.getParameter("COMMONCODE_ID");
        Map<String, Object> commonCode = this.commonCodeService.getRecord("PLAT_APPMODEL_COMMONCODE"
                , new String[]{"COMMONCODE_ID"}, new Object[]{COMMONCODE_ID});
        String COMMONCODE_CONTENT = (String) commonCode.get("COMMONCODE_CONTENT");
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        result.put("COMMONCODE_CONTENT", COMMONCODE_CONTENT);
        this.printObjectJsonString(result, response);
    }
}
