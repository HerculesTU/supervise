package com.housoo.platform.system.controller;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.service.CompanyService;
import com.housoo.platform.core.service.DepartService;
import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.core.service.SysUserService;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.core.util.SysConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述 部门业务相关Controller
 *
 * @author housoo
 * @version 1.0
 * @created 2017-04-09 09:43:59
 */
@Controller
@RequestMapping("/system/DepartController")
public class DepartController extends BaseController {
    /**
     *
     */
    @Resource
    private DepartService departService;
    /**
     *
     */
    @Resource
    private CompanyService companyService;
    /**
     *
     */
    @Resource
    private SysUserService sysUserService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 自动补全
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "autoDepartAndUser")
    public void autoDepartAndUser(HttpServletRequest request,
                                  HttpServletResponse response) {
        //如果自动补全的类型为2,那么获取到key之后进行判断过滤
        //String keyword = request.getParameter("keyword");
        SqlFilter sqlFilter = new SqlFilter(request);
        List<Map<String, Object>> list = this.departService.findAutoDepartUser(sqlFilter);
        String json = JSON.toJSONString(list);
        this.printJsonString(json.toLowerCase(), response);
    }

    /**
     * 获取树形的部门和用户数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "departAndUsers")
    public void departAndUsers(HttpServletRequest request,
                               HttpServletResponse response) {
        Map<String, Object> params = PlatBeanUtil.getMapFromRequest(request);
        String flowToken = request.getParameter("flowToken");
        if (StringUtils.isNotEmpty(flowToken)) {
            Map<String, Object> flowVars = (Map<String, Object>) PlatAppUtil.getSessionCache(flowToken);
            params.putAll(flowVars);
        }
        String departJson = departService.getDepartAndUserJson(params);
        this.printJsonString(departJson, response);
    }

    /**
     * 核对部门编码是否存在
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "checkDepcode")
    public void checkDepcode(HttpServletRequest request,
                             HttpServletResponse response) {
        String DEPART_COMPANYID = request.getParameter("DEPART_COMPANYID");
        String DEPART_CODE = request.getParameter("DEPART_CODE");
        Map<String, String> result = new HashMap<String, String>();
        boolean isExists = true;
        if (StringUtils.isNotEmpty(DEPART_CODE)) {
            isExists = departService.isExistsDepart(DEPART_COMPANYID, DEPART_CODE);
        } else {
            isExists = false;
        }
        if (isExists) {
            result.put("error", "该部门编码已经存在!请重新输入!");
        }
        this.printObjectJsonString(result, response);
    }


    /**
     * 新增或者修改部门数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> depart = PlatBeanUtil.getMapFromRequest(request);
        String DEPART_ID = (String) depart.get("DEPART_ID");
        //如果是保存树形结构表数据,请调用下面的接口,而注释掉上面的接口代码
        depart = departService.saveOrUpdateTreeData("PLAT_SYSTEM_DEPART",
                depart, SysConstants.ID_GENERATOR_UUID, null);
        if (StringUtils.isNotEmpty(DEPART_ID)) {
            sysLogService.saveBackLog("单位部门管理", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为[" + DEPART_ID + "]的部门信息", request);
        } else {
            DEPART_ID = (String) depart.get("COMPANY_ID");
            sysLogService.saveBackLog("单位部门管理", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为[" + DEPART_ID + "]的部门信息", request);
        }
        depart.put("success", true);
        this.printObjectJsonString(depart, response);
    }

    /**
     * 跳转到部门表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        //如果是跳转到树形表单录入界面,请开放以下代码,,而注释掉上面的代码
        String DEPART_ID = request.getParameter("DEPART_ID");
        String DEPART_PARENTID = request.getParameter("DEPART_PARENTID");
        //获取单位ID
        String DEPART_COMPANYID = request.getParameter("DEPART_COMPANYID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> company = null;
        Map<String, Object> depart = null;
        if (StringUtils.isNotEmpty(DEPART_ID)) {
            depart = this.departService.getRecord("PLAT_SYSTEM_DEPART"
                    , new String[]{"DEPART_ID"}, new Object[]{DEPART_ID});
            DEPART_PARENTID = (String) depart.get("DEPART_PARENTID");
            company = companyService.getRecord("PLAT_SYSTEM_COMPANY",
                    new String[]{"COMPANY_ID"}, new Object[]{depart.get("DEPART_COMPANYID")});
        } else {
            company = companyService.getRecord("PLAT_SYSTEM_COMPANY",
                    new String[]{"COMPANY_ID"}, new Object[]{DEPART_COMPANYID});
        }
        Map<String, Object> parentDepart = null;
        if ("0".equals(DEPART_PARENTID)) {
            parentDepart = new HashMap<String, Object>();
            parentDepart.put("DEPART_ID", DEPART_PARENTID);
            parentDepart.put("DEPART_NAME", "部门树");
        } else {
            parentDepart = this.departService.getRecord("PLAT_SYSTEM_DEPART",
                    new String[]{"DEPART_ID"}, new Object[]{DEPART_PARENTID});
        }
        if (depart == null) {
            depart = new HashMap<String, Object>();
        }
        depart.put("DEPART_PARENTID", parentDepart.get("DEPART_ID"));
        depart.put("DEPART_PARENTNAME", parentDepart.get("DEPART_NAME"));
        depart.put("DEPART_COMPANYID", company.get("COMPANY_ID"));
        depart.put("DEPART_COMPANYNAME", company.get("COMPANY_NAME"));
        request.setAttribute("depart", depart);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }


    /**
     * @param request
     * @return
     */
    @RequestMapping(params = "goUserGrant")
    public ModelAndView goUserGrant(HttpServletRequest request) {
        String DEPART_ID = request.getParameter("DEPART_ID");
        Map<String, Object> depart = this.departService.getRecord("PLAT_SYSTEM_DEPART"
                , new String[]{"DEPART_ID"}, new Object[]{DEPART_ID});
        Map<String, Object> company = companyService.getRecord("PLAT_SYSTEM_COMPANY",
                new String[]{"COMPANY_ID"}, new Object[]{depart.get("DEPART_COMPANYID")});
        List<String> userIds = sysUserService.findDepartUserIds(DEPART_ID);
        StringBuffer selectedRecordIds = new StringBuffer("");
        for (int i = 0; i < userIds.size(); i++) {
            if (i > 0) {
                selectedRecordIds.append(",");
            }
            selectedRecordIds.append(userIds.get(i));
        }
        request.setAttribute("selectedRecordIds", selectedRecordIds.toString());
        request.setAttribute("DEPART_COMPANYID", depart.get("DEPART_COMPANYID"));
        return PlatUICompUtil.goDesignUI("userselectorbydep", request);
    }

    /**
     * @param request
     * @param response
     */
    @RequestMapping(params = "grantUsers")
    public void grantUsers(HttpServletRequest request,
                           HttpServletResponse response) {
        String DEPART_ID = request.getParameter("DEPART_ID");
        String checkUserIds = request.getParameter("checkUserIds");
        departService.saveUsers(DEPART_ID, checkUserIds);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 获取部门下的用户和无部门的用户数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "departAndUsersAndAll")
    public void departAndUsersAndAll(HttpServletRequest request,
                                     HttpServletResponse response) {
        Map<String, Object> params = PlatBeanUtil.getMapFromRequest(request);
        String flowToken = request.getParameter("flowToken");
        if (StringUtils.isNotEmpty(flowToken)) {
            Map<String, Object> flowVars = (Map<String, Object>) PlatAppUtil.getSessionCache(flowToken);
            params.putAll(flowVars);
        }
        String departJson = departService.getDepartAndUserAndAllJson(params);
        this.printJsonString(departJson, response);
    }

    /**
     * 跳转到分配部门主任页面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goDepartDirectorGrant")
    public ModelAndView goDepartDirectorGrant(HttpServletRequest request) {
        String DEPART_ID = request.getParameter("DEPART_ID");
        List<String> userIds = departService.findDirectorListByDepartId(DEPART_ID);
        StringBuffer selectedRecordIds = new StringBuffer("");
        for (int i = 0; i < userIds.size(); i++) {
            if (i > 0) {
                selectedRecordIds.append(",");
            }
            selectedRecordIds.append(userIds.get(i));
        }
        request.setAttribute("selectedRecordIds", selectedRecordIds.toString());
        request.setAttribute("DEPART_ID", DEPART_ID);
        request.setAttribute("DEPART_PARENTID", request.getParameter("DEPART_PARENTID"));
        return PlatUICompUtil.goDesignUI("deptDirectorSelector", request);
    }

    /**
     * 分配部门主任
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "grantDepartDirector")
    public void grantUserDepart(HttpServletRequest request, HttpServletResponse response) {
        String DEPART_ID = request.getParameter("DEPART_ID");
        String checkUserIds = request.getParameter("checkUserIds");
        String checkDeptNames = request.getParameter("checkDeptNames");
        departService.saveDepartDirector(DEPART_ID, checkUserIds, checkDeptNames);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 跳转到分配部门承办人页面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goDepartTakerGrant")
    public ModelAndView goDepartTakerGrant(HttpServletRequest request) {
        String DEPART_ID = request.getParameter("DEPART_ID");
        List<String> userIds = departService.findTakerListByDepartId(DEPART_ID);
        StringBuffer selectedRecordIds = new StringBuffer("");
        for (int i = 0; i < userIds.size(); i++) {
            if (i > 0) {
                selectedRecordIds.append(",");
            }
            selectedRecordIds.append(userIds.get(i));
        }
        request.setAttribute("selectedRecordIds", selectedRecordIds.toString());
        request.setAttribute("DEPART_ID", DEPART_ID);
        request.setAttribute("DEPART_PARENTID", request.getParameter("DEPART_PARENTID"));
        return PlatUICompUtil.goDesignUI("deptTakerSelector", request);
    }

    /**
     * 分配部门承办人
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "grantDepartTaker")
    public void grantDepartTaker(HttpServletRequest request, HttpServletResponse response) {
        String DEPART_ID = request.getParameter("DEPART_ID");
        String checkUserIds = request.getParameter("checkUserIds");
        String checkDeptNames = request.getParameter("checkDeptNames");
        departService.saveDepartTaker(DEPART_ID, checkUserIds, checkDeptNames);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }
}
