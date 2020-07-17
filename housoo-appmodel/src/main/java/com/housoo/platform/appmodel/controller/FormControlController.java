package com.housoo.platform.appmodel.controller;

import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatFileUtil;
import com.housoo.platform.core.service.FieldConfigService;
import com.housoo.platform.core.service.FormControlService;
import com.housoo.platform.core.service.UiCompService;
import com.housoo.platform.common.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述 FormControl业务相关Controller
 *
 * @author housoo
 * @version 1.0
 * @created 2017-02-21 17:26:03
 */
@Controller
@RequestMapping("/appmodel/FormControlController")
public class FormControlController extends BaseController {
    /**
     *
     */
    @Resource
    private FormControlService formControlService;
    /**
     *
     */
    @Resource
    private UiCompService uiCompService;
    /**
     *
     */
    @Resource
    private FieldConfigService fieldConfigService;

    /**
     * 跳转到组件配置界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String DESIGN_ID = request.getParameter("DESIGN_ID");
        String FORMCONTROL_ID = request.getParameter("FORMCONTROL_ID");
        String PARENT_ID = request.getParameter("PARENT_ID");
        String PARENT_COMPID = request.getParameter("PARENT_COMPID");
        Map<String, Object> formControl = null;
        if (StringUtils.isNotEmpty(FORMCONTROL_ID)) {
            formControl = formControlService.getRecord("PLAT_APPMODEL_FORMCONTROL"
                    , new String[]{"FORMCONTROL_ID"}, new Object[]{FORMCONTROL_ID});
        } else {
            formControl = new HashMap<String, Object>();
            formControl.put("FORMCONTROL_DESIGN_ID", DESIGN_ID);
            formControl.put("FORMCONTROL_PARENTID", PARENT_ID);
            formControl.put("FORMCONTROL_PARENTCOMPID", PARENT_COMPID);
        }
        List<Map<String, Object>> uicompList = uiCompService.findByCompTypeCode(null);
        request.setAttribute("uicompList", uicompList);
        request.setAttribute("formControl", formControl);
        return new ModelAndView("background/appmodel/formcontrol/control_form");
    }

    /**
     * 跳转到下一步骤配置界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "getNextConfigView")
    public ModelAndView getNextConfigView(HttpServletRequest request) {
        String FORMCONTROL_COMPCODE = request.getParameter("FORMCONTROL_COMPCODE");
        String VIEW_TYPE = request.getParameter("VIEW_TYPE");
        String FORMCONTROL_ID = request.getParameter("FORMCONTROL_ID");
        //获取应用的绝对路径
        String appPath = PlatAppUtil.getAppAbsolutePath();
        //定义文件路径
        StringBuffer jspPath = new StringBuffer(appPath);
        jspPath.append("webpages/common/compdesign/");
        jspPath.append(FORMCONTROL_COMPCODE);
        if ("base".equals(VIEW_TYPE)) {
            jspPath.append("/base_config.jsp");
        } else {
            jspPath.append("/attach_config.jsp");
        }
        File jspFile = new File(jspPath.toString());
        if (jspFile.exists()) {
            Map<String, Object> formControl = formControlService.
                    getRecord("PLAT_APPMODEL_FORMCONTROL", new String[]{"FORMCONTROL_ID"}, new Object[]{FORMCONTROL_ID});
            Map<String, Object> fieldMap = fieldConfigService.getFieldMapInfo(FORMCONTROL_ID);
            request.setAttribute("fieldInfo", fieldMap);
            request.setAttribute("formControl", formControl);
            String viewPath = jspPath.substring(jspPath.indexOf("webpages/") + 9, jspPath.lastIndexOf(".jsp"));
            return new ModelAndView(viewPath);
        } else {
            return new ModelAndView("common/compdesign/no_config");
        }
    }

    /**
     * 保存表单控件基本信息
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveControlInfo")
    public void saveControlInfo(HttpServletRequest request,
                                HttpServletResponse response) {
        String OLD_ID = request.getParameter("FORMCONTROL_ID");
        String OLD_COMPCODE = null;
        if (StringUtils.isNotEmpty(OLD_ID)) {
            Map<String, Object> oldInfo = formControlService.getRecord("PLAT_APPMODEL_FORMCONTROL",
                    new String[]{"FORMCONTROL_ID"}, new Object[]{OLD_ID});
            if (oldInfo != null) {
                OLD_COMPCODE = (String) oldInfo.get("FORMCONTROL_COMPCODE");
            }
        }
        Map<String, Object> controlInfo = PlatBeanUtil.getMapFromRequest(request);
        //进行表单控件基本信息保存
        controlInfo = formControlService.saveOrUpdateTreeData("PLAT_APPMODEL_FORMCONTROL", controlInfo,
                SysConstants.ID_GENERATOR_UUID, null);
        String FORMCONTROL_ID = (String) controlInfo.get("FORMCONTROL_ID");
        String FORMCONTROL_COMPCODE = (String) controlInfo.get("FORMCONTROL_COMPCODE");
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        String tplCode = formControlService.getTplCode(FORMCONTROL_ID);
        Map<String, Object> uiComp = uiCompService.getRecord("PLAT_APPMODEL_UICOMP",
                new String[]{"COMP_CODE"}, new Object[]{FORMCONTROL_COMPCODE});
        if (StringUtils.isNotEmpty(OLD_COMPCODE) && !OLD_COMPCODE.equals(FORMCONTROL_COMPCODE)) {
            tplCode = null;
        }
        if (!StringUtils.isNotEmpty(tplCode)) {
            StringBuffer templatePath = new StringBuffer(PlatAppUtil.getAppAbsolutePath());
            templatePath.append("webpages/common/compdesign/").append(FORMCONTROL_COMPCODE);
            templatePath.append("/template.jsp");
            tplCode = PlatFileUtil.readFileString(templatePath.toString());
        }
        result.put("JS_NAME", uiComp.get("COMP_BASEJS"));
        result.put("TPL_CODE", tplCode);
        result.put("FORMCONTROL_ID", controlInfo.get("FORMCONTROL_ID").toString());
        this.printObjectJsonString(result, response);
    }

    /**
     * 保存基本配置信息
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveBaseConfig")
    public void saveBaseConfig(HttpServletRequest request,
                               HttpServletResponse response) {
        Map<String, Object> baseConfigField = PlatBeanUtil.getMapFromRequest(request);
        baseConfigField.put("VIEW_TYPE", "attach");
        //保存基本配置信息
        this.fieldConfigService.saveFieldConfigAfterDel(baseConfigField, "base");
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        String FORMCONTROL_COMPCODE = (String) baseConfigField.get("FORMCONTROL_COMPCODE");
        Map<String, Object> uiComp = uiCompService.getRecord("PLAT_APPMODEL_UICOMP",
                new String[]{"COMP_CODE"}, new Object[]{FORMCONTROL_COMPCODE});
        result.put("JS_NAME", uiComp.get("COMP_ATTACHJS"));
        this.printObjectJsonString(result, response);
    }

    /**
     * 保存附加配置信息
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveAttachConfig")
    public void saveAttachConfig(HttpServletRequest request,
                                 HttpServletResponse response) {
        Map<String, Object> attachConfigField = PlatBeanUtil.getMapFromRequest(request);
        //保存附加配置信息
        this.fieldConfigService.saveFieldConfigAfterDel(attachConfigField, "attach");
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }


    /**
     * 跳转到外部资源选择器
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "goExternalSelector")
    public ModelAndView goExternalSelector(HttpServletRequest request,
                                           HttpServletResponse response) {
        return new ModelAndView("background/appmodel/formcontrol/external_selector");
    }


    /**
     * 返回外部资源选择数据源
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "externalResTree")
    public void externalResTree(HttpServletRequest request,
                                HttpServletResponse response) {
        String needCheckIds = request.getParameter("needCheckIds");
        Map<String, Object> root = formControlService.getExternalResTreeData(needCheckIds);
        this.printObjectJsonString(root, response);
    }

    /**
     * 保存设计完整信息
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveControl")
    public void saveControl(HttpServletRequest request,
                            HttpServletResponse response) {
        Map<String, Object> formControl = PlatBeanUtil.getMapFromRequest(request);
        formControlService.saveFormControl(formControl);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 删除表单控件数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "delRecord")
    public void delRecord(HttpServletRequest request,
                          HttpServletResponse response) {
        String formControlId = request.getParameter("treeNodeId");
        String childctrolIds = request.getParameter("childctrolIds");
        formControlService.deleteCascadeFieldConfig(formControlId, childctrolIds);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 跳转到例子代码界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goExpCodeView")
    public ModelAndView goExpCodeView(HttpServletRequest request) {
        String expcodePath = request.getParameter("expcodePath");
        String allowedit = request.getParameter("allowedit");
        String keyName = request.getParameter("keyName");
        String appPath = PlatAppUtil.getAppAbsolutePath();
        if (StringUtils.isNotEmpty(expcodePath)) {
            StringBuffer filePath = new StringBuffer(appPath);
            filePath.append("webpages/common/compdesign/");
            filePath.append(expcodePath);
            String expCode = PlatFileUtil.readFileString(filePath.toString());
            request.setAttribute("expCode", expCode);
        }
        request.setAttribute("allowedit", allowedit);
        request.setAttribute("keyName", keyName);
        return new ModelAndView("background/appmodel/formcontrol/exp_codeview");
    }

    /**
     * 更新树形排序字段
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "updateTreeSn")
    public void updateTreeSn(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        // 获取被拖动节点ID
        String dragTreeNodeId = request.getParameter("dragTreeNodeId");
        // 获取被拖动节点最新level
        int dragTreeNodeNewLevel = Integer.parseInt(request.getParameter("dragTreeNodeNewLevel"));
        // 获取目标节点ID
        String targetNodeId = request.getParameter("targetNodeId");
        // 获取目标节点level
        int targetNodeLevel = Integer.parseInt(request.getParameter("targetNodeLevel"));
        //获取目标节点的组件ID
        String targetPlatComId = request.getParameter("targetPlatComId");
        String designId = request.getParameter("designId");
        String moveType = request.getParameter("moveType");
        formControlService.updateFormControlSn(designId, dragTreeNodeId,
                dragTreeNodeNewLevel, targetNodeId, targetNodeLevel, targetPlatComId, moveType);
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }
}
