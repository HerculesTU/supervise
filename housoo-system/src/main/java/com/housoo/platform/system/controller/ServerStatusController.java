package com.housoo.platform.system.controller;

import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatSystemInfoUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.core.service.ServerStatusService;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.DictionaryService;
import com.housoo.platform.core.service.SysLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述 服务器硬件监控信息业务相关Controller
 *
 * @author housoo
 * @version 1.0
 * @created 2018-03-20 16:00:36
 */
@Controller
@RequestMapping("/appmodel/ServerStatusController")
public class ServerStatusController extends BaseController {
    /**
     *
     */
    @Resource
    private ServerStatusService serverStatusService;
    /**
     *
     */
    @Resource
    private DictionaryService dictionaryService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除服务器硬件监控信息数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        serverStatusService.deleteRecords("PLAT_APPMODEL_COMMONCODE", "COMMONCODE_ID", selectColValues.split(","));
        sysLogService.saveBackLog("应用建模", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的服务器硬件监控信息", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改服务器硬件监控信息数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> serverStatus = PlatBeanUtil.getMapFromRequest(request);
        String COMMONCODE_ID = (String) serverStatus.get("COMMONCODE_ID");
        serverStatus = serverStatusService.saveOrUpdate("PLAT_APPMODEL_COMMONCODE",
                serverStatus, SysConstants.ID_GENERATOR_UUID, null);
        if (StringUtils.isNotEmpty(COMMONCODE_ID)) {
            sysLogService.saveBackLog("应用建模", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为[" + COMMONCODE_ID + "]服务器硬件监控信息", request);
        } else {
            COMMONCODE_ID = (String) serverStatus.get("COMMONCODE_ID");
            sysLogService.saveBackLog("应用建模", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为[" + COMMONCODE_ID + "]服务器硬件监控信息", request);
        }
        serverStatus.put("success", true);
        this.printObjectJsonString(serverStatus, response);
    }

    /**
     * 跳转到服务器硬件监控信息表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String COMMONCODE_ID = request.getParameter("COMMONCODE_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> serverStatus = null;
        if (StringUtils.isNotEmpty(COMMONCODE_ID)) {
            serverStatus = this.serverStatusService.getRecord("PLAT_APPMODEL_COMMONCODE"
                    , new String[]{"COMMONCODE_ID"}, new Object[]{COMMONCODE_ID});
        } else {
            serverStatus = new HashMap<String, Object>();
        }
        request.setAttribute("serverStatus", serverStatus);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }


    /**
     * @param request
     * @return
     */
    @RequestMapping(params = "goIndex")
    public ModelAndView goIndex(HttpServletRequest request) {
        Map<String, Object> osInfo = PlatSystemInfoUtil.getOSInfo();
        List<Map<String, Object>> cpuInfoList = PlatSystemInfoUtil.getCpuTotal();
        Map<String, Object> physicalMemory = PlatSystemInfoUtil.getPhysicalMemory();
        Map<String, Object> jvmInfo = PlatSystemInfoUtil.getJVMInfo();
        List<Map<String, Object>> fileInfoList = PlatSystemInfoUtil.getFileInfo();
        DecimalFormat df = new DecimalFormat("#0.00");
        if (fileInfoList != null) {
            float allTotal = 0;
            float allUsed = 0;
            for (int i = 0; i < fileInfoList.size(); i++) {
                float usageTotal = Float.parseFloat(fileInfoList.get(i).get("usageTotal").toString());
                float usageUsed = Float.parseFloat(fileInfoList.get(i).get("usageUsed").toString());
                allTotal += usageTotal;
                allUsed += usageUsed;
            }
            if (allTotal != 0) {
                String usePercent = df.format((allUsed * 100) / allTotal);
                request.setAttribute("usePercent", usePercent);
            }

        }
        request.setAttribute("osInfo", osInfo);
        request.setAttribute("cpuInfo", cpuInfoList.get(0));
        request.setAttribute("cpuSize", PlatSystemInfoUtil.getCpuCount());
        request.setAttribute("physicalMemory", physicalMemory);
        request.setAttribute("jvmInfo", jvmInfo);
        request.setAttribute("fileInfoList", fileInfoList);
        return new ModelAndView("background/appmodel/serverstatus/index");
    }

    /**
     * @param request
     * @param response
     */
    @RequestMapping("/getIntervalInfo")
    public void getIntervalInfo(HttpServletRequest request,
                                HttpServletResponse response) {
        String nodes = request.getParameter("DEPLOYLOG_NODES");
        Map<String, Object> result = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(nodes)) {
            result = this.serverStatusService.postGetIntervalInfo(nodes);
        } else {
            Map<String, Object> cpuPerc = PlatSystemInfoUtil.getEasyCpuPerc();
            result.put("cpuPercCombined", cpuPerc.get("cpuPercCombined").toString().replace("%", ""));
            Map<String, Object> jvmInfo = PlatSystemInfoUtil.getJVMInfo();
            result.put("jvmUsedMemoryPre", jvmInfo.get("usedMemoryPre"));
            Map<String, Object> menoryMap = PlatSystemInfoUtil.getPhysicalMemory();
            result.put("UsedMemoryPre", menoryMap.get("UsedMemoryPre"));
            result.put("success", true);

        }
        this.printObjectJsonString(result, response);
    }

    /**
     * @param request
     * @param response
     */
    @RequestMapping("/getServerStatusInfo")
    public void getServerStatusInfo(HttpServletRequest request,
                                    HttpServletResponse response) {
        String nodes = request.getParameter("DEPLOYLOG_NODES");
        Map<String, Object> result = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(nodes)) {
            result = this.serverStatusService.postGetServerStatusInfo(nodes);
        } else {
            result = this.serverStatusService.getServerStatusInfo();
            result.put("success", true);
        }
        this.printObjectJsonString(result, response);
    }

    /**
     * @param request
     * @return
     */
    @RequestMapping(params = "goPortalIndex")
    public ModelAndView goPortalIndex(HttpServletRequest request) {
        List<Map<String, Object>> fileInfoList = PlatSystemInfoUtil.getFileInfo();
        DecimalFormat df = new DecimalFormat("#0.00");
        if (fileInfoList != null) {
            float allTotal = 0;
            float allUsed = 0;
            for (int i = 0; i < fileInfoList.size(); i++) {
                float usageTotal = Float.parseFloat(fileInfoList.get(i).get("usageTotal").toString());
                float usageUsed = Float.parseFloat(fileInfoList.get(i).get("usageUsed").toString());
                allTotal += usageTotal;
                allUsed += usageUsed;
            }
            if (allTotal != 0) {
                String usePercent = df.format((allUsed * 100) / allTotal);
                request.setAttribute("usePercent", usePercent);
            }

        }
        String dicVal = dictionaryService.getDictionaryValue("warnLineNumber", "阈值");
        double dicIntV = 50;
        if (StringUtils.isNotEmpty(dicVal)) {
            dicIntV = Double.parseDouble(dicVal);
        }
        request.setAttribute("warnLineNumber", df.format(dicIntV / 100));
        return new ModelAndView("background/appmodel/serverstatus/portalIndex");
    }
}
