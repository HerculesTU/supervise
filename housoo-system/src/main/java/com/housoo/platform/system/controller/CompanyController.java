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
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.CompanyService;
import com.housoo.platform.core.service.SysLogService;

/**
 * 描述 单位业务相关Controller
 *
 * @author housoo
 * @version 1.0
 * @created 2017-04-09 09:43:59
 */
@Controller
@RequestMapping("/system/CompanyController")
public class CompanyController extends BaseController {
    /**
     *
     */
    @Resource
    private CompanyService companyService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;


    /**
     * 跳转到单位表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        //如果是跳转到树形表单录入界面,请开放以下代码,,而注释掉上面的代码
        String COMPANY_ID = request.getParameter("COMPANY_ID");
        String COMPANY_PARENTID = request.getParameter("COMPANY_PARENTID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> company = null;
        if (StringUtils.isNotEmpty(COMPANY_ID)) {
            company = this.companyService.getRecord("PLAT_SYSTEM_COMPANY"
                    , new String[]{"COMPANY_ID"}, new Object[]{COMPANY_ID});
            COMPANY_PARENTID = (String) company.get("Company_PARENTID");
        }
        Map<String, Object> parentCompany = null;
        if ("0".equals(COMPANY_PARENTID)) {
            parentCompany = new HashMap<String, Object>();
            parentCompany.put("COMPANY_ID", COMPANY_PARENTID);
            parentCompany.put("COMPANY_NAME", "单位树");
        } else {
            parentCompany = this.companyService.getRecord("PLAT_SYSTEM_COMPANY",
                    new String[]{"COMPANY_ID"}, new Object[]{COMPANY_PARENTID});
        }
        if (company == null) {
            company = new HashMap<String, Object>();
        }
        company.put("COMPANY_PARENTID", parentCompany.get("COMPANY_ID"));
        company.put("COMPANY_PARENTNAME", parentCompany.get("COMPANY_NAME"));
        request.setAttribute("company", company);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }
}
