package com.housoo.platform.metadata.controller;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.util.*;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.metadata.service.DataResService;
import com.housoo.platform.metadata.service.DataSerService;
import com.housoo.platform.metadata.service.DatalogService;
import com.housoo.platform.metadata.service.QueryService;
import com.housoo.platform.core.service.SysLogService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 描述 数据服务信息业务相关Controller
 *
 * @author 高飞
 * @version 1.0
 * @created 2018-05-08 16:49:06
 */
@Controller
@RequestMapping("/metadata/DataSerController")
public class DataSerController extends BaseController {
    /**
     *
     */
    @Resource
    private DataSerService dataSerService;
    /**
     *
     */
    @Resource
    private DataResService dataResService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;
    /**
     *
     */
    @Resource
    private QueryService queryService;
    /**
     *
     */
    @Resource
    private DatalogService datalogService;

    /**
     * 删除数据服务信息数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        dataSerService.deleteRecords("PLAT_METADATA_DATASER", "DATASER_ID",
                selectColValues.split(","));
        sysLogService.saveBackLog("数据服务管理", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的数据服务信息", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改数据服务信息数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> dataSer = PlatBeanUtil.getMapFromRequest(request);
        String DATASER_ID = (String) dataSer.get("DATASER_ID");
        if (StringUtils.isEmpty(DATASER_ID)) {
            dataSer.put("DATASER_TIME", PlatDateTimeUtil.formatDate(new Date(),
                    "yyyy-MM-dd HH:mm:ss"));
        }
        dataSer = dataSerService.saveOrUpdate("PLAT_METADATA_DATASER", dataSer,
                SysConstants.ID_GENERATOR_UUID, null);
        // 获取资源ID
        String resId = request.getParameter("DATARES_ID");
        String serId = (String) dataSer.get("DATASER_ID");
        // 获取资源关联的目录信息
        Map<String, String> cataLogInfo = dataResService.getCatalogInfo(resId);
        String catalogIds = cataLogInfo.get("DATARES_CATALOGIDS");
        dataSerService.saveOrUpdateSerCatalog(serId, catalogIds);
        dataSer.put("success", true);
        this.printObjectJsonString(dataSer, response);
    }

    /**
     * 跳转到数据服务信息表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String DATASER_ID = request.getParameter("DATASER_ID");
        // 获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> dataSer = null;
        if (StringUtils.isNotEmpty(DATASER_ID)) {
            dataSer = this.dataSerService.getRecord("PLAT_METADATA_DATASER",
                    new String[]{"DATASER_ID"}, new Object[]{DATASER_ID});
        } else {
            dataSer = new HashMap<String, Object>();
        }
        request.setAttribute("dataSer", dataSer);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }

    /**
     * 跳转到选择器界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goSelect")
    public ModelAndView goSelect(HttpServletRequest request) {
        String DOCK_ID = request.getParameter("DOCK_ID");
        StringBuffer selectedRecordIds = new StringBuffer("");
        if (StringUtils.isNotEmpty(DOCK_ID)) {
            Map<String, Object> dockInfo = dataSerService.getRecord("PLAT_METADATA_DOCK",
                    new String[]{"DOCK_ID"}, new Object[]{DOCK_ID});
            String DOCK_SERIDS = (String) dockInfo.get("DOCK_SERIDS");
            selectedRecordIds.append(DOCK_SERIDS);
        }
        request.setAttribute("selectedRecordIds", selectedRecordIds.toString());
        return PlatUICompUtil.goDesignUI("dataser_select", request);
    }

    /**
     * 跳转到请求服务文档明细
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goDetail")
    public ModelAndView goDetail(HttpServletRequest request) {
        String DATASER_ID = request.getParameter("DATASER_ID");
        Map<String, Object> dataSer = this.dataSerService.getRecord("PLAT_METADATA_DATASER"
                , new String[]{"DATASER_ID"}, new Object[]{DATASER_ID});
        //获取请求服务的根据路径
        String reqserUrl = PlatAppUtil.getWebBasePath(request);
        //定义服务访问路径
        StringBuffer serverUrl = new StringBuffer(reqserUrl);
        serverUrl.append("metadata/DataSerController/getdata.do?servicecode=");
        serverUrl.append(dataSer.get("DATASER_CODE"));
        serverUrl.append("&grantcode=请求方授权码");
        //获取资源ID
        String DATARES_ID = (String) dataSer.get("DATARES_ID");
        //获取资源信息表
        Map<String, Object> dataRes = dataResService.getRecord("PLAT_METADATA_DATARES",
                new String[]{"DATARES_ID"}, new Object[]{DATARES_ID});
        List<Map> returnFieldList = new ArrayList<Map>();
        String DATARES_RETURNJSON = (String) dataRes.get("DATARES_RETURNJSON");
        if (StringUtils.isNotEmpty(DATARES_RETURNJSON)) {
            returnFieldList = JSON.parseArray(DATARES_RETURNJSON, Map.class);
        }
        dataSer.put("REQSER_URL", serverUrl.toString());
        List<Map<String, Object>> resqueryList = queryService.findByResId(DATARES_ID);
        request.setAttribute("dataSer", dataSer);
        request.setAttribute("returnFieldList", returnFieldList);
        request.setAttribute("resqueryList", resqueryList);
        return PlatUICompUtil.goDesignUI("reqserapidetail", request);
    }

    /**
     * 测试数据服务的数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "testData")
    public void testData(HttpServletRequest request,
                         HttpServletResponse response) {
        //获取请求过来的IP地址
        String requestIp = BrowserUtils.getIpAddr(request);
        //获取请求的服务编码
        String servicecode = request.getParameter("servicecode");
        //获取请求方的授权码
        String grantcode = "BFbhXSKZXvjii1+bMaL/Wg==";
        Map<String, Object> result = new HashMap<String, Object>();
        //开始记录请求日志
        Map<String, Object> datalog = new HashMap<String, Object>();
        String DATALOG_ERLOG = null;
        try {
            result = dataSerService.invokeService(request, true);
        } catch (Exception e) {
            DATALOG_ERLOG = ExceptionUtils.getStackTrace(e);
            result.put("success", false);
            result.put("invokeResultCode", DataSerService.CODE_OTHER);
            result.put("msg", "系统内部错误!");
            e.printStackTrace();
        }
        datalogService.saveDataLog(request, requestIp, servicecode, grantcode, result,
                datalog, DATALOG_ERLOG);
        this.printObjectJsonString(result, response);
    }


    /**
     * 调用请求服务
     *
     * @param request
     * @param response
     */
    @RequestMapping("/getdata")
    public void getdata(HttpServletRequest request,
                        HttpServletResponse response) {
        //获取请求过来的IP地址
        String requestIp = BrowserUtils.getIpAddr(request);
        //获取请求的服务编码
        String servicecode = request.getParameter("servicecode");
        //获取请求方的授权码
        String grantcode = request.getParameter("grantcode");
        Map<String, Object> result = new HashMap<String, Object>();
        //开始记录请求日志
        Map<String, Object> datalog = new HashMap<String, Object>();
        String DATALOG_ERLOG = null;
        try {
            result = dataSerService.invokeService(request, false);
        } catch (Exception e) {
            DATALOG_ERLOG = ExceptionUtils.getStackTrace(e);
            result.put("success", false);
            result.put("invokeResultCode", DataSerService.CODE_OTHER);
            e.printStackTrace();
        }
        datalogService.saveDataLog(request, requestIp, servicecode, grantcode, result,
                datalog, DATALOG_ERLOG);
        this.printObjectJsonString(result, response);
    }
}
