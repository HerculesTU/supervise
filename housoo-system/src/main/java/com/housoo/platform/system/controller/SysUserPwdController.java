package com.housoo.platform.system.controller;

import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.core.service.SysUserPwdService;
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
import java.util.Map;

/**
 * 描述 系统用户密码业务相关Controller
 *
 * @author gf
 * @version 1.0
 * @created 2019-11-22
 */
@Controller
@RequestMapping("/system/sysUserPwdController")
public class SysUserPwdController extends BaseController {
    /**
     * SysUserPwdService
     */
    @Resource
    private SysUserPwdService sysUserPwdService;

    /**
     * 系统日志Service
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除系统用户密码数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request, HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        sysUserPwdService.deleteRecords("PLAT_SYSTEM_USER_PWD", "USER_ID", selectColValues.split(","));
        sysLogService.saveBackLog("业务管理", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的系统用户密码", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改系统用户密码数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> sysUserPwd = PlatBeanUtil.getMapFromRequest(request);
        String USER_ID = (String) sysUserPwd.get("USER_ID");
        sysUserPwd = sysUserPwdService.saveOrUpdate("PLAT_SYSTEM_USER_PWD",
                sysUserPwd, SysConstants.ID_GENERATOR_UUID, null);
        //如果是保存树形结构表数据,请调用下面的接口,而注释掉上面的接口代码
        //sysUserPwd = SysUserPwdService.saveOrUpdateTreeData("PLAT_SYSTEM_USER_PWD",
        //sysUserPwd,SysConstants.ID_GENERATOR_UUID,null);
        if (StringUtils.isNotEmpty(USER_ID)) {
            sysLogService.saveBackLog("业务管理", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为[" + USER_ID + "]系统用户密码", request);
        } else {
            USER_ID = (String) sysUserPwd.get("USER_ID");
            sysLogService.saveBackLog("业务管理", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为[" + USER_ID + "]系统用户密码", request);
        }
        sysUserPwd.put("success", true);
        this.printObjectJsonString(sysUserPwd, response);
    }

    /**
     * 跳转到系统用户密码表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String USER_ID = request.getParameter("USER_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> sysUserPwd = null;
        if (StringUtils.isNotEmpty(USER_ID)) {
            sysUserPwd = this.sysUserPwdService.getRecord("PLAT_SYSTEM_USER_PWD"
                    , new String[]{"USER_ID"}, new Object[]{USER_ID});
        } else {
            sysUserPwd = new HashMap
                    <String, Object>();
        }
        request.setAttribute("sysUserPwd", sysUserPwd);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
        //如果是跳转到树形表单录入界面,请开放以下代码,,而注释掉上面的代码
        /*
        String USER_ID = request.getParameter("USER_ID");
        String SYSUSERPWD_PARENTID = request.getParameter("SYSUSERPWD_PARENTID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map <String,Object> sysUserPwd = null;
        if(StringUtils.isNotEmpty(USER_ID)){
            sysUserPwd = this.SysUserPwdService.getRecord("PLAT_SYSTEM_USER_PWD"
            ,new String[]{"USER_ID"},new Object[]{USER_ID});
            SYSUSERPWD_PARENTID = (String) sysUserPwd.get("sysUserPwd_PARENTID");
        }
        Map <String,Object> parentsysUserPwd = null;
        if(SYSUSERPWD_PARENTID.equals("0")){
            parentsysUserPwd = new HashMap
            <String,Object>();
            parentsysUserPwd.put("USER_ID",SYSUSERPWD_PARENTID);
            parentsysUserPwd.put("SYSUSERPWD_NAME","系统用户密码树");
            }else{
            parentsysUserPwd = this.SysUserPwdService.getRecord("PLAT_SYSTEM_USER_PWD",
            new String[]{"USER_ID"}, new Object[]{SYSUSERPWD_PARENTID});
        }
        if(sysUserPwd==null){
            sysUserPwd = new HashMap
            <String,Object>();
        }
        sysUserPwd.put("SYSUSERPWD_PARENTID",parentsysUserPwd.get("USER_ID"));
        sysUserPwd.put("SYSUSERPWD_PARENTNAME",parentsysUserPwd.get("SYSUSERPWD_NAME"));
        request.setAttribute("sysUserPwd", sysUserPwd);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
        */
    }
}
