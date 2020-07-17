package com.housoo.platform.metadata.controller;

import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.metadata.service.DatalogService;
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
 * 描述 服务请求日志业务相关Controller
 *
 * @author 高飞
 * @version 1.0
 * @created 2018-05-10 17:03:49
 */
@Controller
@RequestMapping("/metadata/DatalogController")
public class DatalogController extends BaseController {
    /**
     *
     */
    @Resource
    private DatalogService datalogService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除服务请求日志数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        datalogService.deleteRecords("PLAT_METADATA_DATALOG", "DATALOG_ID", selectColValues.split(","));
        sysLogService.saveBackLog("数据服务管理", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的服务请求日志", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改服务请求日志数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> datalog = PlatBeanUtil.getMapFromRequest(request);
        String DATALOG_ID = (String) datalog.get("DATALOG_ID");
        datalog = datalogService.saveOrUpdate("PLAT_METADATA_DATALOG",
                datalog, SysConstants.ID_GENERATOR_UUID, null);
        //如果是保存树形结构表数据,请调用下面的接口,而注释掉上面的接口代码
        //datalog = datalogService.saveOrUpdateTreeData("PLAT_METADATA_DATALOG",
        //        datalog,AllConstants.IDGENERATOR_UUID,null);
        if (StringUtils.isNotEmpty(DATALOG_ID)) {
            sysLogService.saveBackLog("数据服务管理", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为[" + DATALOG_ID + "]服务请求日志", request);
        } else {
            DATALOG_ID = (String) datalog.get("DATALOG_ID");
            sysLogService.saveBackLog("数据服务管理", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为[" + DATALOG_ID + "]服务请求日志", request);
        }
        datalog.put("success", true);
        this.printObjectJsonString(datalog, response);
    }

    /**
     * 跳转到服务请求日志表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String DATALOG_ID = request.getParameter("DATALOG_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> datalog = null;
        if (StringUtils.isNotEmpty(DATALOG_ID)) {
            datalog = this.datalogService.getRecord("PLAT_METADATA_DATALOG"
                    , new String[]{"DATALOG_ID"}, new Object[]{DATALOG_ID});
        } else {
            datalog = new HashMap<String, Object>();
        }
        request.setAttribute("datalog", datalog);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
        //如果是跳转到树形表单录入界面,请开放以下代码,,而注释掉上面的代码
        /*String DATALOG_ID = request.getParameter("DATALOG_ID");
        String DATALOG_PARENTID = request.getParameter("DATALOG_PARENTID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String,Object> datalog = null;
        if(StringUtils.isNotEmpty(DATALOG_ID)){
            datalog = this.datalogService.getRecord("PLAT_METADATA_DATALOG"
                    ,new String[]{"DATALOG_ID"},new Object[]{DATALOG_ID});
            DATALOG_PARENTID = (String) datalog.get("Datalog_PARENTID");
        }
        Map<String,Object> parentDatalog = null;
        if(DATALOG_PARENTID.equals("0")){
            parentDatalog = new HashMap<String,Object>();
            parentDatalog.put("DATALOG_ID",DATALOG_PARENTID);
            parentDatalog.put("DATALOG_NAME","服务请求日志树");
        }else{
            parentDatalog = this.datalogService.getRecord("PLAT_METADATA_DATALOG",
                    new String[]{"DATALOG_ID"}, new Object[]{DATALOG_PARENTID});
        }
        if(datalog==null){
            datalog = new HashMap<String,Object>();
        }
        datalog.put("DATALOG_PARENTID",parentDatalog.get("DATALOG_ID"));
        datalog.put("DATALOG_PARENTNAME",parentDatalog.get("DATALOG_NAME"));
        request.setAttribute("datalog", datalog);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);*/
    }
}
