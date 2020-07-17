package com.housoo.platform.chatonline.controller;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatLogUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.chatonline.service.UserGroupService;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.SysLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述 用户分组业务相关Controller
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-07-13 10:59:23
 */
@Controller
@RequestMapping("/chatonline/UserGroupController")
public class UserGroupController extends BaseController {
    /**
     *
     */
    @Resource
    private UserGroupService userGroupService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除用户分组数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        userGroupService.deleteRecords("PLAT_CHATONLINE_USERGROUP", "USERGROUP_ID", selectColValues.split(","));
        sysLogService.saveBackLog("用户组管理", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的用户分组", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改用户分组数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> userGroup = PlatBeanUtil.getMapFromRequest(request);
        /*userGroup = userGroupService.saveOrUpdate("PLAT_CHATONLINE_USERGROUP",
                userGroup,AllConstants.IDGENERATOR_UUID,null);*/
        userGroup = userGroupService.saveOrUpdateGroup(userGroup);
        userGroup.put("success", true);
        this.printObjectJsonString(userGroup, response);
    }

    /**
     * 跳转到用户分组表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String USERGROUP_ID = request.getParameter("USERGROUP_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> userGroup = null;
        if (StringUtils.isNotEmpty(USERGROUP_ID)) {
            userGroup = this.userGroupService.getRecord("PLAT_CHATONLINE_USERGROUP"
                    , new String[]{"USERGROUP_ID"}, new Object[]{USERGROUP_ID});
        } else {
            userGroup = new HashMap<String, Object>();
        }
        request.setAttribute("usergroup", userGroup);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }

    /**
     * 描述
     *
     * @param request
     * @return
     * @created 2017年7月16日 下午3:37:10
     */
    @RequestMapping(params = "showUserGroupPerson")
    public ModelAndView showUserGroupPerson(HttpServletRequest request) {
        String id = request.getParameter("id");
        String type = request.getParameter("type");
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> createMap = this.userGroupService.getCreateMap(id);
        String cid = createMap.get("id").toString();
        Map<String, Object> userMap = PlatAppUtil.getBackPlatLoginUser();
        String systemuserId = userMap.get("SYSUSER_ID").toString();
        if ("edit".equals(type) && systemuserId.equals(cid)) {
            request.setAttribute("IS_ADMIN", true);
        }
        StringBuffer personId = new StringBuffer("");
        personId.append(createMap.get("id") + ",");
        map.put("createMap", createMap);
        List<Map<String, Object>> list = this.userGroupService.findGroupMemberListMap(id);
        map.put("list", list);
        for (int i = 0; i < list.size(); i++) {
            personId.append(list.get(i).get("id") + ",");
        }
        request.setAttribute("map", map);
        request.setAttribute("id", id);
        request.setAttribute("type", type);
        request.setAttribute("personId", personId.toString());
        return new ModelAndView("background/chatonline/showUserGroupPerson");
    }

    /**
     * 描述
     *
     * @param request
     * @param response
     * @created 2017年7月28日 上午11:12:44
     */
    @Override
    @RequestMapping(params = "tree")
    public void tree(HttpServletRequest request,
                     HttpServletResponse response) {
        String groupId = request.getParameter("groupId");
        String treeJson = userGroupService.getTreeJsonByGroupId(groupId);
        response.setContentType("application/json");
        response.setHeader("Cache-Control", "no-store");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
        } catch (IOException e) {
            PlatLogUtil.printStackTrace(e);
        }
        pw.write(treeJson);
        pw.flush();
        pw.close();
    }

    /**
     * 描述
     *
     * @param request
     * @param response
     * @created 2017年7月28日 下午3:03:23
     */
    @Override
    @RequestMapping(params = "autoComplete")
    public void autoComplete(HttpServletRequest request,
                             HttpServletResponse response) {
        SqlFilter sqlFilter = new SqlFilter(request);
        List<Map<String, Object>> list = userGroupService.findAutoComplete(sqlFilter);
        String json = JSON.toJSONString(list);
        this.printJsonString(json.toLowerCase(), response);
    }

    /**
     * 新增或者修改用户分组数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "assignmentGroup")
    public void assignmentGroup(HttpServletRequest request,
                                HttpServletResponse response) {
        Map<String, Object> userGroup = PlatBeanUtil.getMapFromRequest(request);
        userGroup = userGroupService.assignmentGroup(userGroup);
        userGroup.put("success", true);
        this.printObjectJsonString(userGroup, response);
    }
}
