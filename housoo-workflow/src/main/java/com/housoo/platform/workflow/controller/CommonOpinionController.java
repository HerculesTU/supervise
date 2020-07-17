package com.housoo.platform.workflow.controller;

import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.workflow.service.CommonOpinionService;
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
 * 描述 常用意见业务相关Controller
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-31 09:04:45
 */
@Controller
@RequestMapping("/workflow/CommonOpinionController")
public class CommonOpinionController extends BaseController {
    /**
     *
     */
    @Resource
    private CommonOpinionService commonOpinionService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除常用意见数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        commonOpinionService.deleteRecords("JBPM6_COMMONOPINION", "OPINION_ID", selectColValues.split(","));
        sysLogService.saveBackLog("我的待办", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的常用意见", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 跳转到常用意见选择列表
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goSelector")
    public ModelAndView goSelector(HttpServletRequest request) {
        return PlatUICompUtil.goDesignUI("commonopinionlist", request);
    }

    /**
     * 新增或者修改常用意见数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> commonOpinion = PlatBeanUtil.getMapFromRequest(request);
        Map<String, Object> backLoginUser = PlatAppUtil.getBackPlatLoginUser();
        String SYSUSER_ID = (String) backLoginUser.get("SYSUSER_ID");
        commonOpinion.put("OPINION_CREATORID", SYSUSER_ID);
        commonOpinion = commonOpinionService.saveOrUpdate("JBPM6_COMMONOPINION",
                commonOpinion, SysConstants.ID_GENERATOR_UUID, null);
        commonOpinion.put("success", true);
        this.printObjectJsonString(commonOpinion, response);
    }

    /**
     * 跳转到常用意见表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String OPINION_ID = request.getParameter("OPINION_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> commonOpinion = null;
        if (StringUtils.isNotEmpty(OPINION_ID)) {
            commonOpinion = this.commonOpinionService.getRecord("JBPM6_COMMONOPINION"
                    , new String[]{"OPINION_ID"}, new Object[]{OPINION_ID});
        } else {
            commonOpinion = new HashMap<String, Object>();
        }
        request.setAttribute("commonOpinion", commonOpinion);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }
}
