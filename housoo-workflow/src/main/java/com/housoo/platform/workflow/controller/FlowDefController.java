/*
 * Copyright (c) 2005, 2014, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.controller;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.util.*;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.core.model.JbpmFlowInfo;
import com.housoo.platform.workflow.service.*;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述 流程定义业务相关Controller
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-03 16:12:37
 */
@Controller
@RequestMapping("/workflow/FlowDefController")
public class FlowDefController extends BaseController {
    /**
     *
     */
    @Resource
    private FlowDefService flowDefService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;
    /**
     *
     */
    @Resource
    private FlowNodeService flowNodeService;
    /**
     *
     */
    @Resource
    private NodeBindService nodeBindService;
    /**
     *
     */
    @Resource
    private FlowFormService flowFormService;
    /**
     *
     */
    @Resource
    private FormFieldService formFieldService;
    /**
     *
     */
    @Resource
    private JbpmService jbpmService;
    /**
     *
     */
    @Resource
    private TableBindService tableBindService;
    /**
     *
     */
    @Resource
    private ExecutionService executionService;

    /**
     * 删除流程定义数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        int exeCount = executionService.getCountByDefIds(selectColValues);
        Map<String, Object> result = new HashMap<String, Object>();
        if (exeCount != 0) {
            result.put("success", false);
            result.put("msg", "所选流程定义存在流程实例数据,无法删除!");
        } else {
            flowDefService.deleteCascade(selectColValues.split(","));
            result.put("success", true);
        }
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改流程定义数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> flowDef = PlatBeanUtil.getMapFromRequest(request);
        flowDef = flowDefService.saveOrUpdateCascade(flowDef);
        flowDef.put("success", true);
        this.printObjectJsonString(flowDef, response);
    }

    /**
     * 列表页面跳转
     *
     * @return
     */
    @RequestMapping(params = "showDesignCode")
    public ModelAndView showDesignCode(HttpServletRequest request) {
        String operType = request.getParameter("operType");
        request.setAttribute("operType", operType);
        return PlatUICompUtil.goDesignUI("flowdesigncodeform", request);
    }

    /**
     * 跳转到流程定义表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String FLOWDEF_ID = request.getParameter("FLOWDEF_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> flowDef = null;
        if (StringUtils.isNotEmpty(FLOWDEF_ID)) {
            flowDef = this.flowDefService.getRecord("JBPM6_FLOWDEF"
                    , new String[]{"FLOWDEF_ID"}, new Object[]{FLOWDEF_ID});
        } else {
            flowDef = new HashMap<String, Object>();
        }
        request.setAttribute("flowDef", flowDef);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }

    /**
     * 跳转到流程在线设计界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goOnlineDesign")
    public ModelAndView goOnlineDesign(HttpServletRequest request) {
        String FLOWDEF_ID = request.getParameter("FLOWDEF_ID");
        if (StringUtils.isNotEmpty(FLOWDEF_ID)) {
            Map<String, Object> flowDef = this.flowDefService.getRecord("JBPM6_FLOWDEF"
                    , new String[]{"FLOWDEF_ID"}, new Object[]{FLOWDEF_ID});
            //获取定义的XML
            String defJson = (String) flowDef.get("FLOWDEF_JSON");
            flowDef.put("FLOWDEF_JSON", StringEscapeUtils.escapeHtml3(defJson));
            request.setAttribute("flowDef", flowDef);
        }
        return new ModelAndView("background/workflow/flowdef/onlinedesign");
    }

    /**
     * 跳转到环节表单绑定界面
     *
     * @return
     */
    @RequestMapping(params = "goDefConfig")
    public ModelAndView goDefConfig(HttpServletRequest request) {
        String FLOWDEF_ID = request.getParameter("FLOWDEF_ID");
        String FLOWDEF_VERSION = request.getParameter("FLOWDEF_VERSION");
        Map<String, Object> flowDef = flowDefService.getFlowDefInfo(FLOWDEF_ID,
                Integer.parseInt(FLOWDEF_VERSION));
        //获取定义的XML
        String defJson = (String) flowDef.get("FLOWDEF_JSON");
        request.setAttribute("FLOWDEF_JSON", StringEscapeUtils.escapeHtml3(defJson));
        request.setAttribute("FLOWDEF_ID", FLOWDEF_ID);
        request.setAttribute("FLOWDEF_VERSION", FLOWDEF_VERSION);
        request.setAttribute("TOTALLIMIT_DAYS", flowDef.get("TOTALLIMIT_DAYS"));
        request.setAttribute("TOTALLIMIT_TYPES", flowDef.get("TOTALLIMIT_TYPES"));
        return PlatUICompUtil.goDesignUI("flowdefconfig", request);
    }

    /**
     * 更新流程总时限信息
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "updateTimeLimit")
    public void updateTimeLimit(HttpServletRequest request,
                                HttpServletResponse response) {
        String TOTALLIMIT_TYPES = request.getParameter("TOTALLIMIT_TYPES");
        String TOTALLIMIT_DAYS = request.getParameter("TOTALLIMIT_DAYS");
        String FLOWDEF_ID = request.getParameter("FLOWDEF_ID");
        if (StringUtils.isNotEmpty(FLOWDEF_ID)) {
            flowDefService.updateTimeLimit(FLOWDEF_ID, TOTALLIMIT_DAYS, TOTALLIMIT_TYPES);
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 跳转到发起流程界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goStart")
    public ModelAndView goStart(HttpServletRequest request) {
        //==========结束构建流程对象信息============================
        String FLOWDEF_ID = request.getParameter("FLOWDEF_ID");
        String SERITEM_ID = request.getParameter("SERITEM_ID");
        JbpmFlowInfo jbpmFlowInfo = jbpmService.getJbpmFlowInfo(null, FLOWDEF_ID, "false", null);
        jbpmFlowInfo.setJbpmExeId("-1");
        jbpmFlowInfo.setJbpmSerItemId(SERITEM_ID);
        return jbpmService.getFlowDesignUI(request, jbpmFlowInfo);
    }

    /**
     * 克隆流程定义信息
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "cloneFlowDefInfo")
    public void cloneFlowDefInfo(HttpServletRequest request,
                                 HttpServletResponse response) {
        String FLOWDEF_ID = request.getParameter("FLOWDEF_ID");
        String FLOWDEF_CODE = request.getParameter("FLOWDEF_CODE");
        String FLOWDEF_NAME = request.getParameter("FLOWDEF_NAME");
        flowDefService.cloneFlowDef(FLOWDEF_ID, FLOWDEF_CODE, FLOWDEF_NAME);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 跳转到克隆界面
     *
     * @return
     */
    @RequestMapping(params = "goClone")
    public ModelAndView goClone(HttpServletRequest request) {
        String FLOWDEF_ID = request.getParameter("FLOWDEF_ID");
        String FLOWDEF_NAME = request.getParameter("FLOWDEF_NAME");
        Map<String, Object> flowDef = new HashMap<String, Object>();
        flowDef.put("FLOWDEF_ID", FLOWDEF_ID);
        flowDef.put("FLOWDEF_OLDNAME", FLOWDEF_NAME);
        request.setAttribute("flowDef", flowDef);
        return PlatUICompUtil.goDesignUI("cloneflowdef", request);
    }

    /**
     * 跳转到版本管理界面
     *
     * @return
     */
    @RequestMapping(params = "goVersion")
    public ModelAndView goVersion(HttpServletRequest request) {
        String FLOWDEF_ID = request.getParameter("FLOWDEF_ID");
        request.setAttribute("FLOWDEF_ID", FLOWDEF_ID);
        return PlatUICompUtil.goDesignUI("histdeploy", request);
    }

    /**
     * 跳转到流程数据查询界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goFlowDataQuery")
    public ModelAndView goFlowDataQuery(HttpServletRequest request) {
        String FLOWDEF_ID = request.getParameter("FLOWDEF_ID");
        String designCode = tableBindService.getTableBindDesignCode(FLOWDEF_ID, "4");
        request.setAttribute("FLOWDEF_ID", FLOWDEF_ID);
        if (StringUtils.isNotEmpty(designCode)) {
            return PlatUICompUtil.goDesignUI(designCode, request);
        } else {
            return PlatUICompUtil.goDesignUI("defaultflowdata", request);
        }

    }

    /**
     * 导入流程定义配置
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "impConfig")
    public void impConfig(HttpServletRequest request,
                          HttpServletResponse response) {
        String dbfilepath = request.getParameter("dbfilepath");
        //获取文件存储路径
        String attachFilePath = PlatPropUtil.getPropertyValue("config.properties"
                , "attachFilePath");
        String jsonFilePath = attachFilePath + dbfilepath;
        //获取文件内容
        String jsonContent = PlatFileUtil.readFileString(jsonFilePath, "UTF-8");
        flowDefService.importDefConfig(jsonContent);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        result.put("msg", "导入成功!");
        this.printObjectJsonString(result, response);
    }

    /**
     * 导出配置
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("/exportConfig")
    public void exportConfig(HttpServletRequest request, HttpServletResponse response) {
        String defIds = request.getParameter("defIds");
        Map<String, Object> result = this.flowDefService.getExportDefInfo(defIds);
        String json = JSON.toJSONString(result);
        try {
            String downloadFileName = PlatDateTimeUtil.formatDate(new Date(), "yyyyMMdd") + "defconfig.json";
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=" + downloadFileName);
            //用于记录以完成的下载的数据量，单位是byte
            InputStream inputStream = new ByteArrayInputStream(json.getBytes());
            //激活下载操作
            OutputStream os = response.getOutputStream();
            //循环写入输出流
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }
            // 这里主要关闭。
            os.close();
            inputStream.close();
        } catch (UnsupportedEncodingException e) {
            PlatLogUtil.printStackTrace(e);
        } catch (FileNotFoundException e) {
            PlatLogUtil.printStackTrace(e);
        } catch (IOException e) {
            PlatLogUtil.printStackTrace(e);
        }
    }


}
