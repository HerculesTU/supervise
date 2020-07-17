package com.housoo.platform.appmodel.controller;

import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.core.service.EmailLogService;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.SysLogService;
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
 * 描述 邮件发送日志业务相关Controller
 *
 * @author housoo
 * @version 1.0
 * @created 2017-12-20 15:45:42
 */
@Controller
@RequestMapping("/appmodel/EmailLogController")
public class EmailLogController extends BaseController {
    /**
     *
     */
    @Resource
    private EmailLogService emailLogService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除邮件发送日志数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        emailLogService.deleteRecords("PLAT_APPMODEL_EMAILLOG", "EMAILLOG_ID", selectColValues.split(","));
        sysLogService.saveBackLog("邮件管理", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的邮件发送日志", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改邮件发送日志数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> emailLog = PlatBeanUtil.getMapFromRequest(request);
        String EMAILLOG_ID = (String) emailLog.get("EMAILLOG_ID");
        emailLog = emailLogService.saveOrUpdate("PLAT_APPMODEL_EMAILLOG",
                emailLog, SysConstants.ID_GENERATOR_UUID, null);
        //如果是保存树形结构表数据,请调用下面的接口,而注释掉上面的接口代码
        //emailLog = emailLogService.saveOrUpdateTreeData("PLAT_APPMODEL_EMAILLOG",
        //        emailLog,SysConstants.ID_GENERATOR_UUID,null);
        if (StringUtils.isNotEmpty(EMAILLOG_ID)) {
            sysLogService.saveBackLog("邮件管理", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为[" + EMAILLOG_ID + "]邮件发送日志", request);
        } else {
            EMAILLOG_ID = (String) emailLog.get("EMAILLOG_ID");
            sysLogService.saveBackLog("邮件管理", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为[" + EMAILLOG_ID + "]邮件发送日志", request);
        }
        emailLog.put("success", true);
        this.printObjectJsonString(emailLog, response);
    }

    /**
     * 跳转到邮件发送日志表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String EMAILLOG_ID = request.getParameter("EMAILLOG_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> emailLog = null;
        if (StringUtils.isNotEmpty(EMAILLOG_ID)) {
            emailLog = this.emailLogService.getRecord("PLAT_APPMODEL_EMAILLOG"
                    , new String[]{"EMAILLOG_ID"}, new Object[]{EMAILLOG_ID});
        } else {
            emailLog = new HashMap<String, Object>();
        }
        request.setAttribute("emailLog", emailLog);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
        //如果是跳转到树形表单录入界面,请开放以下代码,,而注释掉上面的代码
        /*String EMAILLOG_ID = request.getParameter("EMAILLOG_ID");
        String EMAILLOG_PARENTID = request.getParameter("EMAILLOG_PARENTID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String,Object> emailLog = null;
        if(StringUtils.isNotEmpty(EMAILLOG_ID)){
            emailLog = this.emailLogService.getRecord("PLAT_APPMODEL_EMAILLOG"
                    ,new String[]{"EMAILLOG_ID"},new Object[]{EMAILLOG_ID});
            EMAILLOG_PARENTID = (String) emailLog.get("EmailLog_PARENTID");
        }
        Map<String,Object> parentEmailLog = null;
        if(EMAILLOG_PARENTID.equals("0")){
            parentEmailLog = new HashMap<String,Object>();
            parentEmailLog.put("EMAILLOG_ID",EMAILLOG_PARENTID);
            parentEmailLog.put("EMAILLOG_NAME","邮件发送日志树");
        }else{
            parentEmailLog = this.emailLogService.getRecord("PLAT_APPMODEL_EMAILLOG",
                    new String[]{"EMAILLOG_ID"}, new Object[]{EMAILLOG_PARENTID});
        }
        if(emailLog==null){
            emailLog = new HashMap<String,Object>();
        }
        emailLog.put("EMAILLOG_PARENTID",parentEmailLog.get("EMAILLOG_ID"));
        emailLog.put("EMAILLOG_PARENTNAME",parentEmailLog.get("EMAILLOG_NAME"));
        request.setAttribute("emailLog", emailLog);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);*/
    }
}
