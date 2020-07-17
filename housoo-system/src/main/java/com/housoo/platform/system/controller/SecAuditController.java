package com.housoo.platform.system.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.util.PlatDateTimeUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.RoleRightService;
import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.core.service.SecAuditService;

/**
 * 描述 安全审核记录业务相关Controller
 *
 * @author housoo
 * @version 1.0
 * @created 2018-05-13 16:40:58
 */
@Controller
@RequestMapping("/system/SecAuditController")
public class SecAuditController extends BaseController {
    /**
     *
     */
    @Resource
    private SecAuditService secAuditService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;
    /**
     *
     */
    @Resource
    private RoleRightService roleRightService;

    /**
     * 删除安全审核记录数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        secAuditService.deleteRecords("PLAT_SYSTEM_SECAUDIT", "SECAUDIT_ID", selectColValues.split(","));
        sysLogService.saveBackLog("安全审核管理", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的安全审核记录", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改安全审核记录数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> secAudit = PlatBeanUtil.getMapFromRequest(request);
        secAudit.remove("POSTPARAM_JSON");
        Map<String, Object> auditer = PlatAppUtil.getBackPlatLoginUser();
        String SECAUDIT_AUID = (String) auditer.get("SYSUSER_ID");
        String SECAUDIT_AUTIME = PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
        secAudit.put("SECAUDIT_AUID", SECAUDIT_AUID);
        secAudit.put("SECAUDIT_AUTIME", SECAUDIT_AUTIME);
        secAudit = secAuditService.saveOrUpdate("PLAT_SYSTEM_SECAUDIT",
                secAudit, SysConstants.ID_GENERATOR_UUID, null);
        String SECAUDIT_ID = (String) secAudit.get("SECAUDIT_ID");
        Map<String, Object> secInfo = secAuditService.getSecAuditInfo(SECAUDIT_ID);
        StringBuffer logContent = new StringBuffer("");
        logContent.append("对【").append(secInfo.get("SYSUSER_NAME"));
        logContent.append("】对【").append(secInfo.get("OBJTYPE")).append("】进行");
        logContent.append(secInfo.get("BUSTYPE")).append("操作做出");
        String SECAUDIT_STATUS = secAudit.get("SECAUDIT_STATUS").toString();
        if ("1".equals(SECAUDIT_STATUS)) {
            logContent.append("【审核通过】的决定");
        } else {
            logContent.append("【审核不通过】的决定");
        }
        sysLogService.saveBackLog("安全审核管理", 9,
                logContent.toString(), request);
        secAudit.put("success", true);
        this.printObjectJsonString(secAudit, response);
    }

    /**
     * 跳转到安全审核记录表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String SECAUDIT_ID = request.getParameter("SECAUDIT_ID");
        Map<String, Object> secAudit = this.secAuditService.getRecord("PLAT_SYSTEM_SECAUDIT"
                , new String[]{"SECAUDIT_ID"}, new Object[]{SECAUDIT_ID});
        String UI_DESIGNCODE = null;
        int SECAUDIT_BUSTYPE = Integer.parseInt(secAudit.get("SECAUDIT_BUSTYPE").toString());
        if (SECAUDIT_BUSTYPE == SecAuditService.BUSTYPE_DEL) {
            UI_DESIGNCODE = "secaudit_delform";
        } else if (SECAUDIT_BUSTYPE == SecAuditService.BUSTYPE_ADD) {
            UI_DESIGNCODE = "secaudit_addform";
        } else if (SECAUDIT_BUSTYPE == SecAuditService.BUSTYPE_EDIT) {
            UI_DESIGNCODE = "secaudit_editform";
        } else if (SECAUDIT_BUSTYPE == SecAuditService.BUSTYPE_GRANT) {
            this.setGrantResRecordValues(request, secAudit);
            UI_DESIGNCODE = "secaudit_grantform";
        } else if (SECAUDIT_BUSTYPE == SecAuditService.BUSTYPE_GRANTUSER) {
            UI_DESIGNCODE = "secaudit_grantuserform";
        }
        request.setAttribute("secAudit", secAudit);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }

    /**
     * 设置授权资源回填记录
     *
     * @param request
     * @param secAudit
     */
    private void setGrantResRecordValues(HttpServletRequest request, Map<String, Object> secAudit) {
        String SECAUDIT_OBJID = (String) secAudit.get("SECAUDIT_OBJID");
        //获取对象类型
        int objType = Integer.parseInt(secAudit.get("SECAUDIT_OBJTYPE").toString());
        String tableName = null;
        switch (objType) {
            case SecAuditService.BUSOBJ_ROLE:
                tableName = "PLAT_SYSTEM_ROLE";
                break;
            case SecAuditService.BUSOBJ_USER:
                tableName = "PLAT_SYSTEM_SYSUSER";
                break;
            case SecAuditService.BUSOBJ_USERGROUP:
                tableName = "PLAT_SYSTEM_USERGROUP";
                break;
            default:
                break;
        }
        String POSTPARAM_JSON = (String) secAudit.get("POSTPARAM_JSON");
        Map postParam = JSON.parseObject(POSTPARAM_JSON, Map.class);
        String typedefIds = (String) postParam.get("typedefIds");
        String resIds = (String) postParam.get("resIds");
        String groupIds = (String) postParam.get("groupIds");
        StringBuffer rightRecordIds = new StringBuffer("");
        if (StringUtils.isNotEmpty(typedefIds)) {
            rightRecordIds.append(typedefIds).append(",");
        }
        if (StringUtils.isNotEmpty(resIds)) {
            rightRecordIds.append(resIds).append(",");
        }
        if (StringUtils.isNotEmpty(groupIds)) {
            rightRecordIds.append(groupIds).append(",");
        }
        if (StringUtils.isNotEmpty(rightRecordIds) && rightRecordIds.length() > 1) {
            rightRecordIds = rightRecordIds.deleteCharAt(rightRecordIds.length() - 1);
        }
        request.setAttribute("selectedRecordIds", rightRecordIds);
        request.setAttribute("ROLE_ID", SECAUDIT_OBJID);
        request.setAttribute("tableName", tableName);
    }

    /**
     * 跳转到审批表单
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goAuditForm")
    public ModelAndView goAuditForm(HttpServletRequest request) {
        String SECAUDIT_ID = request.getParameter("SECAUDIT_ID");
        Map<String, Object> secAudit = this.secAuditService.getRecord("PLAT_SYSTEM_SECAUDIT"
                , new String[]{"SECAUDIT_ID"}, new Object[]{SECAUDIT_ID});
        String UI_DESIGNCODE = null;
        int SECAUDIT_BUSTYPE = Integer.parseInt(secAudit.get("SECAUDIT_BUSTYPE").toString());
        if (SECAUDIT_BUSTYPE == SecAuditService.BUSTYPE_DEL) {
            UI_DESIGNCODE = "secaudit_delformsp";
        } else if (SECAUDIT_BUSTYPE == SecAuditService.BUSTYPE_ADD) {
            UI_DESIGNCODE = "secaudit_addformsp";
        } else if (SECAUDIT_BUSTYPE == SecAuditService.BUSTYPE_EDIT) {
            UI_DESIGNCODE = "secaudit_editformsp";
        } else if (SECAUDIT_BUSTYPE == SecAuditService.BUSTYPE_GRANT) {
            this.setGrantResRecordValues(request, secAudit);
            UI_DESIGNCODE = "secaudit_grantformsp";
        } else if (SECAUDIT_BUSTYPE == SecAuditService.BUSTYPE_GRANTUSER) {
            UI_DESIGNCODE = "secaudit_grantuserformsp";
        }
        String POSTPARAM_JSON = (String) secAudit.get("POSTPARAM_JSON");
        secAudit.put("POSTPARAM_JSON", StringEscapeUtils.escapeHtml3(POSTPARAM_JSON));
        request.setAttribute("secAudit", secAudit);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }
}
