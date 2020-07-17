package com.housoo.platform.system.controller;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.DictionaryService;
import com.housoo.platform.core.service.RoleRightService;
import com.housoo.platform.core.service.RoleService;
import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.core.service.SysUserService;

/**
 * 描述 角色业务相关Controller
 *
 * @author housoo
 * @version 1.0
 * @created 2017-04-15 15:20:40
 */
@Controller
@RequestMapping("/system/RoleController")
public class RoleController extends BaseController {
    /**
     *
     */
    @Resource
    private RoleService roleService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;
    /**
     *
     */
    @Resource
    private SysUserService sysUserService;
    /**
     *
     */
    @Resource
    private RoleRightService roleRightService;
    /**
     *
     */
    @Resource
    private DictionaryService dictionaryService;

    /**
     * 自动补全
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "autoRoleAndUser")
    public void autoRoleAndUser(HttpServletRequest request,
                                HttpServletResponse response) {
        //如果自动补全的类型为2,那么获取到key之后进行判断过滤
        //String keyword = request.getParameter("keyword");
        SqlFilter sqlFilter = new SqlFilter(request);
        List<Map<String, Object>> list = roleService.findAutoRoleUser(sqlFilter);
        String json = JSON.toJSONString(list);
        this.printJsonString(json.toLowerCase(), response);
    }


    /**
     * 跳转到角色表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String ROLE_ID = request.getParameter("ROLE_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        String roleGroupId = request.getParameter("roleGroupId");
        Map<String, Object> role = null;
        if (StringUtils.isNotEmpty(ROLE_ID)) {
            role = this.roleService.getRecord("PLAT_SYSTEM_ROLE"
                    , new String[]{"ROLE_ID"}, new Object[]{ROLE_ID});
        } else {
            role = new HashMap<String, Object>();
            if (StringUtils.isNotEmpty(roleGroupId)) {
                role.put("ROLE_GROUPID", roleGroupId);
            }
        }
        request.setAttribute("role", role);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }

    /**
     * 获取树形的角色和用户数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "roleAndUsers")
    public void roleAndUsers(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> params = PlatBeanUtil.getMapFromRequest(request);
        String departJson = roleService.getRoleAndUserJson(params);
        this.printJsonString(departJson, response);
    }

    /**
     * 跳转到系统用户选择界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goUserGrant")
    public ModelAndView goUserGrant(HttpServletRequest request) {
        String ROLE_ID = request.getParameter("ROLE_ID");
        List<String> userIds = sysUserService.findUserIds(ROLE_ID);
        StringBuffer selectedRecordIds = new StringBuffer("");
        for (int i = 0; i < userIds.size(); i++) {
            if (i > 0) {
                selectedRecordIds.append(",");
            }
            selectedRecordIds.append(userIds.get(i));
        }
        request.setAttribute("selectedRecordIds", selectedRecordIds.toString());
        return PlatUICompUtil.goDesignUI("userselector", request);
    }

    /**
     * 分配用户
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "grantUsers")
    public void grantUsers(HttpServletRequest request,
                           HttpServletResponse response) {
        String ROLE_ID = request.getParameter("ROLE_ID");
        String checkUserIds = request.getParameter("checkUserIds");
        roleService.saveUsers(ROLE_ID, Arrays.asList(checkUserIds.split(",")));
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 跳转到权限授权界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goRightGrant")
    public ModelAndView goRightGrant(HttpServletRequest request) {
        String ROLE_ID = request.getParameter("ROLE_ID");
        String tableName = request.getParameter("tableName");
        String uiCode = request.getParameter("uiCode");
        if (StringUtils.isEmpty(uiCode)) {
            uiCode = "rolegrantform";
        }
        List<String> rightRecordIds = roleRightService.getRightRecordIds(ROLE_ID, tableName);
        StringBuffer selectedRecordIds = new StringBuffer("");
        for (int i = 0; i < rightRecordIds.size(); i++) {
            if (i > 0) {
                selectedRecordIds.append(",");
            }
            selectedRecordIds.append(rightRecordIds.get(i));
        }
        request.setAttribute("selectedRecordIds", selectedRecordIds.toString());
        request.setAttribute("ROLE_ID", ROLE_ID);
        request.setAttribute("tableName", tableName);
        return PlatUICompUtil.goDesignUI(uiCode, request);
    }

    /**
     * @param request
     * @param response
     */
    @RequestMapping(params = "updateSn")
    public void updateSn(HttpServletRequest request,
                         HttpServletResponse response) {
        String roleIds = request.getParameter("roleIds");
        roleService.updateSn(roleIds.split(","));
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }
}
