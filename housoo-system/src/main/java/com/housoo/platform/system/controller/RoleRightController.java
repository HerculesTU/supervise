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
import com.housoo.platform.core.service.RoleRightService;
import com.housoo.platform.core.service.SysLogService;

/**
 * 描述 角色权限中间表业务相关Controller
 *
 * @author housoo
 * @version 1.0
 * @created 2017-04-23 16:57:24
 */
@Controller
@RequestMapping("/system/RoleRightController")
public class RoleRightController extends BaseController {
    /**
     *
     */
    @Resource
    private RoleRightService roleRightService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除角色权限中间表数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        roleRightService.deleteRecords("PLAT_SYSTEM_ROLERIGHT", "RIGHT_ID", selectColValues.split(","));
        sysLogService.saveBackLog("角色管理", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的角色权限中间表", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改角色权限中间表数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> roleRight = PlatBeanUtil.getMapFromRequest(request);
        String RIGHT_ID = (String) roleRight.get("RIGHT_ID");
        roleRight = roleRightService.saveOrUpdate("PLAT_SYSTEM_ROLERIGHT",
                roleRight, SysConstants.ID_GENERATOR_UUID, null);
        //如果是保存树形结构表数据,请调用下面的接口,而注释掉上面的接口代码
        //roleRight = roleRightService.saveOrUpdateTreeData("PLAT_SYSTEM_ROLERIGHT",
        //        roleRight,SysConstants.ID_GENERATOR_UUID,null);
        if (StringUtils.isNotEmpty(RIGHT_ID)) {
            sysLogService.saveBackLog("角色管理", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为[" + RIGHT_ID + "]角色权限中间表", request);
        } else {
            RIGHT_ID = (String) roleRight.get("RIGHT_ID");
            sysLogService.saveBackLog("角色管理", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为[" + RIGHT_ID + "]角色权限中间表", request);
        }
        roleRight.put("success", true);
        this.printObjectJsonString(roleRight, response);
    }

    /**
     * 跳转到角色权限中间表表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String RIGHT_ID = request.getParameter("RIGHT_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> roleRight = null;
        if (StringUtils.isNotEmpty(RIGHT_ID)) {
            roleRight = this.roleRightService.getRecord("PLAT_SYSTEM_ROLERIGHT"
                    , new String[]{"RIGHT_ID"}, new Object[]{RIGHT_ID});
        } else {
            roleRight = new HashMap<String, Object>();
        }
        request.setAttribute("roleRight", roleRight);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }
}
