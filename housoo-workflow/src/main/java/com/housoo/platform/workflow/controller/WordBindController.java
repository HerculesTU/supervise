package com.housoo.platform.workflow.controller;

import com.housoo.platform.core.util.*;
import com.housoo.platform.core.service.WordTplService;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.workflow.service.WordBindService;
import com.zhuozhengsoft.pageoffice.FileSaver;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述 流程绑定文书业务相关Controller
 *
 * @author housoo
 * @version 1.0
 * @created 2017-06-02 15:52:39
 */
@Controller
@RequestMapping("/workflow/WordBindController")
public class WordBindController extends BaseController {
    /**
     *
     */
    @Resource
    private WordBindService wordBindService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;
    /**
     *
     */
    @Resource
    private WordTplService wordTplService;

    /**
     * 删除流程绑定文书数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        wordBindService.deleteRecords("JBPM6_WORDBIND", "WORDBIND_ID", selectColValues.split(","));
        sysLogService.saveBackLog("流程定义管理", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的流程绑定文书", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改流程绑定文书数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> wordBind = PlatBeanUtil.getMapFromRequest(request);
        String WORDBIND_NODENAMES = (String) wordBind.get("WORDBIND_NODEKEYS_LABELS");
        wordBind.put("WORDBIND_NODENAMES", WORDBIND_NODENAMES);
        String WORDBIND_ID = (String) wordBind.get("WORDBIND_ID");
        wordBind = wordBindService.saveOrUpdate("JBPM6_WORDBIND",
                wordBind, SysConstants.ID_GENERATOR_UUID, null);
        if (StringUtils.isNotEmpty(WORDBIND_ID)) {
            sysLogService.saveBackLog("流程定义管理", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为[" + WORDBIND_ID + "]流程绑定文书", request);
        } else {
            WORDBIND_ID = (String) wordBind.get("WORDBIND_ID");
            sysLogService.saveBackLog("流程定义管理", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为[" + WORDBIND_ID + "]流程绑定文书", request);
        }
        wordBind.put("success", true);
        this.printObjectJsonString(wordBind, response);
    }

    /**
     * 跳转到流程绑定文书表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String WORDBIND_ID = request.getParameter("WORDBIND_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        String FLOWDEF_ID = request.getParameter("FLOWDEF_ID");
        String FLOWDEF_VERSION = request.getParameter("FLOWDEF_VERSION");
        Map<String, Object> wordBind = null;
        if (StringUtils.isNotEmpty(WORDBIND_ID)) {
            wordBind = this.wordBindService.getRecord("JBPM6_WORDBIND"
                    , new String[]{"WORDBIND_ID"}, new Object[]{WORDBIND_ID});
        } else {
            wordBind = new HashMap<String, Object>();
            wordBind.put("WORDBIND_FLOWDEFID", FLOWDEF_ID);
        }
        wordBind.put("FLOWDEF_VERSION", FLOWDEF_VERSION);
        request.setAttribute("wordBind", wordBind);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }

    /**
     * 跳转到界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "openHome")
    public ModelAndView openHome(HttpServletRequest request) {
        String docPath = request.getParameter("docPath");
        //String docPath = "doc/test.doc";
        String docReadOnly = request.getParameter("docReadOnly");
        //String docReadOnly = "false";
        String docPrint = request.getParameter("docPrint");
        String docSave = request.getParameter("docSave");
        //String docPrint = "true";
        //String docSave = "true";
        String callBackMethod = request.getParameter("callBackMethod");
        //String callBackMethod = "wordBindService.printFilePath";
        request.setAttribute("docPath", docPath);
        request.setAttribute("docReadOnly", docReadOnly);
        request.setAttribute("docPrint", docPrint);
        request.setAttribute("docSave", docSave);
        request.setAttribute("callBackMethod", callBackMethod);
        return new ModelAndView("background/workflow/printWord/openHome");
    }

    /**
     * @param request
     * @param response
     */
    @RequestMapping(params = "setParam")
    public void setParam(HttpServletRequest request,
                         HttpServletResponse response) {
        String docPath = request.getParameter("docPath");
        String docReadOnly = request.getParameter("docReadOnly");
        String docPrint = request.getParameter("docPrint");
        String docSave = request.getParameter("docSave");
        PlatAppUtil.getSession().setAttribute("docPath", docPath);
        PlatAppUtil.getSession().setAttribute("docReadOnly", docReadOnly);
        PlatAppUtil.getSession().setAttribute("docPrint", docPrint);
        PlatAppUtil.getSession().setAttribute("docSave", docSave);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * @param request
     * @return
     */
    @RequestMapping(params = "printView")
    public ModelAndView printView(HttpServletRequest request) {
        /*String docPath = (String)PlatAppUtil.getSession().getAttribute("docPath");
        String docReadOnly = (String)PlatAppUtil.getSession().getAttribute("docReadOnly");
        String docPrint = (String)PlatAppUtil.getSession().getAttribute("docPrint");
        String docSave = (String)PlatAppUtil.getSession().getAttribute("docSave");
        PlatAppUtil.getSession().setAttribute("docPath", null);
        PlatAppUtil.getSession().setAttribute("docReadOnly", null);
        PlatAppUtil.getSession().setAttribute("docPrint", null);
        PlatAppUtil.getSession().setAttribute("docSave", null);*/
        String docPath = request.getParameter("docPath");
        String docReadOnly = request.getParameter("docReadOnly");
        String docPrint = request.getParameter("docPrint");
        String docSave = request.getParameter("docSave");
        String callBackMethod = request.getParameter("callBackMethod");
        if (docPath != null) {
            request.setAttribute("docPath", docPath);
        }
        if (docReadOnly != null) {
            request.setAttribute("docReadOnly", docReadOnly);
        }
        if (docPrint != null) {
            request.setAttribute("docPrint", docPrint);
        }
        if (docSave != null) {
            request.setAttribute("docSave", docSave);
        }
        if (callBackMethod != null) {
            request.setAttribute("callBackMethod", callBackMethod);
        }
        return new ModelAndView("background/workflow/printWord/printView");
    }

    /**
     * 保存word文件
     *
     * @param request
     * @param response
     */
    @RequestMapping("/saveWordFile")
    public void saveWordFile(HttpServletRequest request, HttpServletResponse response) {
        String docPath = request.getParameter("docPath");
        String callBackMethod = request.getParameter("callBackMethod");
        FileSaver fs = new FileSaver(request, response);
        if (docPath != null) {
            fs.saveToFile(PlatPropUtil.getPropertyValue("config.properties", "attachFilePath") + docPath.toString());
            fs.setCustomSaveResult(docPath);
            String postParams = docPath;
            if (StringUtils.isNotEmpty(callBackMethod)) {
                String[] beanMethods = callBackMethod.split("[.]");
                String beanId = beanMethods[0];
                String method = beanMethods[1];
                Object serviceBean = PlatAppUtil.getBean(beanId);
                if (serviceBean != null) {
                    Method invokeMethod;
                    try {
                        invokeMethod = serviceBean.getClass().getDeclaredMethod(method,
                                new Class[]{String.class});
                        invokeMethod.invoke(serviceBean,
                                new Object[]{postParams});
                    } catch (Exception e) {
                        PlatLogUtil.printStackTrace(e);
                    }
                }
            }
        }
        fs.close();
    }

    /**
     * @param request
     * @return
     */
    @RequestMapping(params = "test")
    public ModelAndView test(HttpServletRequest request) {
        return new ModelAndView("background/workflow/printWord/test");
    }

    /**
     * @param request
     * @return
     */
    @RequestMapping(params = "onlineEdit")
    public ModelAndView onlineEdit(HttpServletRequest request) {
        String clientId = request.getParameter("clientId");
        String bindId = request.getParameter("bindId");
        String tplCode = request.getParameter("tplCode");
        String flowToken = request.getParameter("flowToken");
        Map<String, Object> params = (Map<String, Object>) PlatAppUtil.getSessionCache(flowToken);
        PlatAppUtil.getSession().setAttribute("clientId", clientId);
        PlatAppUtil.getSession().setAttribute("bindId", bindId);
        String docPath = wordTplService.genWordByTplCodeAndParams(tplCode, params);
        String docReadOnly = "false";
        String docPrint = "true";
        String docSave = "true";
        String callBackMethod = "wordBindService.saveWordAfterCallClient";
        request.setAttribute("docPath", docPath);
        request.setAttribute("docReadOnly", docReadOnly);
        request.setAttribute("docPrint", docPrint);
        request.setAttribute("docSave", docSave);
        request.setAttribute("callBackMethod", callBackMethod);
        return new ModelAndView("background/workflow/printWord/openHome");
    }
}
