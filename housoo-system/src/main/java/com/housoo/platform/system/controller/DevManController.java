package com.housoo.platform.system.controller;

import java.util.ArrayList;
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
import com.housoo.platform.core.util.PlatStringUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.service.AppEmailService;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.core.service.DevManService;

/**
 * 描述 开发者信息业务相关Controller
 *
 * @author housoo
 * @version 1.0
 * @created 2018-04-19 14:37:38
 */
@Controller
@RequestMapping("/system/DevManController")
public class DevManController extends BaseController {
    /**
     *
     */
    @Resource
    private DevManService devManService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;
    /**
     *
     */
    @Resource
    private AppEmailService appEmailService;

    /**
     * 删除开发者信息数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        devManService.deleteRecords("PLAT_SYSTEM_DEVMAN", "DEVMAN_ID", selectColValues.split(","));
        sysLogService.saveBackLog("自动化部署管理", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的开发者信息", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改开发者信息数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> devMan = PlatBeanUtil.getMapFromRequest(request);
        String DEVMAN_ID = (String) devMan.get("DEVMAN_ID");
        if (StringUtils.isEmpty(DEVMAN_ID)) {
            String DEVMAN_EMAIL = (String) devMan.get("DEVMAN_EMAIL");
            //产生随机的密码
            int number = PlatStringUtil.getRandomIntNumber(1, 999999);
            String pass = PlatStringUtil.getFormatNumber(6, String.valueOf(number));
            appEmailService.sendSimpleMail("您的开发者相关信息,请查收", pass, new String[]{DEVMAN_EMAIL});
            String encodePass = PlatStringUtil.getMD5Encode(pass, null, 1);
            devMan.put("DEVMAN_PASS", encodePass);
            devMan.put("DEVMAN_TIME", PlatDateTimeUtil.formatDate(new Date(),
                    "yyyy-MM-dd HH:mm:ss"));
        }
        devMan = devManService.saveOrUpdate("PLAT_SYSTEM_DEVMAN",
                devMan, SysConstants.ID_GENERATOR_UUID, null);
        devMan.put("success", true);
        this.printObjectJsonString(devMan, response);
    }

    /**
     * 跳转到开发者信息表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String DEVMAN_ID = request.getParameter("DEVMAN_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> devMan = null;
        if (StringUtils.isNotEmpty(DEVMAN_ID)) {
            devMan = this.devManService.getRecord("PLAT_SYSTEM_DEVMAN"
                    , new String[]{"DEVMAN_ID"}, new Object[]{DEVMAN_ID});
        } else {
            devMan = new HashMap<String, Object>();
        }
        request.setAttribute("devMan", devMan);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }

    /**
     * 新增或者修改开发者信息数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "updatePass")
    public void updatePass(HttpServletRequest request,
                           HttpServletResponse response) {
        String DEVMAN_ID = request.getParameter("DEVMAN_ID");
        String DEVMAN_PASS = request.getParameter("DEVMAN_PASS");
        Map<String, Object> devMan = this.devManService.getRecord("PLAT_SYSTEM_DEVMAN"
                , new String[]{"DEVMAN_ID"}, new Object[]{DEVMAN_ID});
        String oldEncodePass = (String) devMan.get("DEVMAN_PASS");
        //进行加密
        String encodePass = PlatStringUtil.getMD5Encode(DEVMAN_PASS, null, 1);
        Map<String, Object> result = new HashMap<String, Object>();
        if (oldEncodePass.equals(encodePass)) {
            String newPass = request.getParameter("DEVMAN_NEWPASS");
            String rePass = request.getParameter("DEVMAN_REPASS");
            if (newPass.equals(rePass)) {
                String newEncodePass = PlatStringUtil.getMD5Encode(newPass, null, 1);
                devMan.put("DEVMAN_PASS", newEncodePass);
                devMan = devManService.saveOrUpdate("PLAT_SYSTEM_DEVMAN",
                        devMan, SysConstants.ID_GENERATOR_UUID, null);
                result.put("success", true);
            } else {
                result.put("success", false);
                result.put("msg", "两次密码输入不一致!");
            }
        } else {
            result.put("success", false);
            result.put("msg", "旧密码输入错误!");
        }
        this.printObjectJsonString(result, response);
    }

}
