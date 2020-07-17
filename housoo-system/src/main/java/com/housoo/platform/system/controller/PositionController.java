package com.housoo.platform.system.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import com.housoo.platform.core.util.PlatDateTimeUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.core.service.PositionService;

/**
 * 描述 岗位业务相关Controller
 *
 * @author housoo
 * @version 1.0
 * @created 2018-05-12 15:52:01
 */
@Controller
@RequestMapping("/system/PositionController")
public class PositionController extends BaseController {
    /**
     *
     */
    @Resource
    private PositionService positionService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;


    /**
     * 跳转到岗位表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String POSITION_ID = request.getParameter("POSITION_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> position = null;
        if (StringUtils.isNotEmpty(POSITION_ID)) {
            position = this.positionService.getRecord("PLAT_SYSTEM_POSITION"
                    , new String[]{"POSITION_ID"}, new Object[]{POSITION_ID});
        } else {
            position = new HashMap<String, Object>();
        }
        request.setAttribute("position", position);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }

    /**
     * 跳转到系统用户选择界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goUserGrant")
    public ModelAndView goUserGrant(HttpServletRequest request) {
        String POSITION_ID = request.getParameter("POSITION_ID");
        String selectedRecordIds = positionService.getTableFieldValues("PLAT_SYSTEM_SYSUSERPOS",
                "SYSUSER_ID", new String[]{"POSITION_ID"}, new Object[]{POSITION_ID}, ",");
        if (StringUtils.isEmpty(selectedRecordIds)) {
            selectedRecordIds = "";
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
        String POSITION_ID = request.getParameter("POSITION_ID");
        String checkUserIds = request.getParameter("checkUserIds");
        positionService.saveUsers(POSITION_ID, checkUserIds.split(","));
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 跳转到选择器界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goSelect")
    public ModelAndView goSelect(HttpServletRequest request) {
        String USER_ID = request.getParameter("USER_ID");
        StringBuffer selectedRecordIds = new StringBuffer("");
        if (StringUtils.isNotEmpty(USER_ID)) {
            String recordIds = positionService.getTableFieldValues("PLAT_SYSTEM_SYSUSERPOS",
                    "POSITION_ID", new String[]{"SYSUSER_ID"},
                    new Object[]{USER_ID}, ",");
            if (StringUtils.isNotEmpty(recordIds)) {
                selectedRecordIds.append(recordIds);
            }
        }
        request.setAttribute("selectedRecordIds", selectedRecordIds.toString());
        return PlatUICompUtil.goDesignUI("position_select", request);
    }
}
