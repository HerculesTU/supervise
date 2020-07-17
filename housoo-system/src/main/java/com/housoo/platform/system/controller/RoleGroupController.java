package com.housoo.platform.system.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatDbUtil;
import com.housoo.platform.core.util.PlatStringUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.RoleGroupService;
import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.core.service.SysUserService;

/**
 * 描述 角色组业务相关Controller
 *
 * @author housoo
 * @version 1.0
 * @created 2017-04-15 15:20:40
 */
@Controller
@RequestMapping("/system/RoleGroupController")
public class RoleGroupController extends BaseController {
    /**
     *
     */
    @Resource
    private RoleGroupService roleGroupService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除角色组数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String groupId = request.getParameter("groupId");
        String sql = PlatDbUtil.getDiskSqlContent("system/rolegroup/002", null);
        List<List<String>> groupList = roleGroupService.findListBySql(sql + PlatStringUtil.
                getSqlInCondition(groupId), null, null);
        roleGroupService.deleteGroupCascadeRole(groupId);
        sysLogService.saveBackLog("角色管理", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + groupId + "]的角色组信息", request, null, "角色组ID,角色组名称,角色描述", groupList);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改角色组数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> roleGroup = PlatBeanUtil.getMapFromRequest(request);
        String GROUP_ID = (String) roleGroup.get("GROUP_ID");
        //获取前端传递过来的字段变更JSON
        String formfieldModifyArrayJson = request.getParameter("formfieldModifyArrayJson");
        //解析成修改字段数组
        List<Map> formfieldModifyArray = JSON.parseArray(formfieldModifyArrayJson, Map.class);
        roleGroup = roleGroupService.saveOrUpdate("PLAT_SYSTEM_ROLEGROUP",
                roleGroup, SysConstants.ID_GENERATOR_UUID, null);
        if (StringUtils.isNotEmpty(GROUP_ID)) {
            sysLogService.saveBackLog("角色管理", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为[" + GROUP_ID + "]的角色组信息", request, formfieldModifyArray, null, null);
        } else {
            GROUP_ID = (String) roleGroup.get("GROUP_ID");
            //获取当前用户所拥有的角色
            Map<String, Object> sysUser = PlatAppUtil.getBackPlatLoginUser();
            String ROLECODES = (String) sysUser.get("ROLECODES");
            String[] roleCodes = ROLECODES.split(",");
            for (String roleCode : roleCodes) {
                Map<String, Object> roleInfo = this.roleGroupService.getRecord("PLAT_SYSTEM_ROLE"
                        , new String[]{"ROLE_CODE"}, new Object[]{roleCode});
                String ROLE_ID = (String) roleInfo.get("ROLE_ID");
                Map<String, Object> roleRight = new HashMap<String, Object>();
                roleRight.put("ROLE_ID", ROLE_ID);
                roleRight.put("RE_RECORDID", GROUP_ID);
                roleRight.put("RE_TABLENAME", "PLAT_SYSTEM_ROLEGROUP");
                roleRight.put("ROLE_TABLE", "PLAT_SYSTEM_ROLE");
                roleGroupService.saveOrUpdate("PLAT_SYSTEM_ROLERIGHT", roleRight,
                        SysConstants.ID_GENERATOR_UUID, null);
            }
            /*String sysUserId = (String) sysUser.get("SYSUSER_ID");
             //获取被授权的角色组IDS集合
            Set<String> groupIdSet = roleGroupService.getUserGrantGroupIds(sysUserId);
            if(groupIdSet!=null&&groupIdSet.size()>0){
                sysUser.put(SysUserService.GROUPIDSET_KEY, groupIdSet);
                sysUser.put(SysUserService.GROUPIDS_KEY,PlatStringUtil.getSetStringSplit(groupIdSet));
            }*/
            roleGroup.put("msg", "新增角色组成功,该角色组下新增的角色,需要对当前登录用户进行数据授权才可见!");
            sysLogService.saveBackLog("角色管理", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为[" + GROUP_ID + "]的角色组信息", request, formfieldModifyArray, null, null);
        }
        roleGroup.put("success", true);
        this.printObjectJsonString(roleGroup, response);
    }

    /**
     * 跳转到角色组表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String GROUP_ID = request.getParameter("GROUP_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> roleGroup = null;
        if (StringUtils.isNotEmpty(GROUP_ID)) {
            roleGroup = this.roleGroupService.getRecord("PLAT_SYSTEM_ROLEGROUP"
                    , new String[]{"GROUP_ID"}, new Object[]{GROUP_ID});
        } else {
            roleGroup = new HashMap<String, Object>();
        }
        request.setAttribute("roleGroup", roleGroup);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }

    /**
     * 获取树形的角色组和角色数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "groupAndRoles")
    public void groupAndRoles(HttpServletRequest request,
                              HttpServletResponse response) {
        Map<String, Object> params = PlatBeanUtil.getMapFromRequest(request);
        String departJson = roleGroupService.getGroupAndRoleJson(params);
        this.printJsonString(departJson, response);
    }

    /**
     * 获取树形的角色组数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "groupTreeData")
    public void groupTreeData(HttpServletRequest request,
                              HttpServletResponse response) {
        Map<String, Object> params = PlatBeanUtil.getMapFromRequest(request);
        String departJson = roleGroupService.getGroupTreeJson(params);
        this.printJsonString(departJson, response);
    }

    /**
     * 自动补全角色组
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "autoGroup")
    public void autoGroup(HttpServletRequest request,
                          HttpServletResponse response) {
        //如果自动补全的类型为2,那么获取到key之后进行判断过滤
        //String keyword = request.getParameter("keyword");
        SqlFilter sqlFilter = new SqlFilter(request);
        List<Map<String, Object>> list = roleGroupService.findAutoGroup(sqlFilter);
        String json = JSON.toJSONString(list);
        this.printJsonString(json.toLowerCase(), response);
    }

    /**
     * 自动补全角色组和角色
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "autoGroupAndRole")
    public void autoGroupAndRole(HttpServletRequest request,
                                 HttpServletResponse response) {
        //如果自动补全的类型为2,那么获取到key之后进行判断过滤
        //String keyword = request.getParameter("keyword");
        SqlFilter sqlFilter = new SqlFilter(request);
        List<Map<String, Object>> list = roleGroupService.findAutoGroupRole(sqlFilter);
        String json = JSON.toJSONString(list);
        this.printJsonString(json.toLowerCase(), response);
    }
}
