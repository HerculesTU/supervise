package com.housoo.platform.appmodel.controller;

import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.core.service.InvokeLogService;
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
 * 描述 服务调用日志业务相关Controller
 *
 * @author housoo
 * @version 1.0
 * @created 2017-08-03 18:41:18
 */
@Controller
@RequestMapping("/dataset/InvokeLogController")
public class InvokeLogController extends BaseController {
    /**
     *
     */
    @Resource
    private InvokeLogService invokeLogService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除服务调用日志数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        invokeLogService.deleteRecords("MZT_DATASET_INVOKELOG", "INVOKELOG_ID", selectColValues.split(","));
        sysLogService.saveBackLog("APP服务调用日志", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的服务调用日志", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改服务调用日志数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> invokelog = PlatBeanUtil.getMapFromRequest(request);
        String INVOKELOG_ID = (String) invokelog.get("INVOKELOG_ID");
        invokelog = invokeLogService.saveOrUpdate("MZT_DATASET_INVOKELOG",
                invokelog, SysConstants.ID_GENERATOR_UUID, null);
        //如果是保存树形结构表数据,请调用下面的接口,而注释掉上面的接口代码
        //invokelog = invokelogService.saveOrUpdateTreeData("MZT_DATASET_INVOKELOG",
        //        invokelog,AllConstants.IDGENERATOR_UUID,null);
        if (StringUtils.isNotEmpty(INVOKELOG_ID)) {
            sysLogService.saveBackLog("APP服务调用日志", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为[" + INVOKELOG_ID + "]服务调用日志", request);
        } else {
            INVOKELOG_ID = (String) invokelog.get("INVOKELOG_ID");
            sysLogService.saveBackLog("APP服务调用日志", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为[" + INVOKELOG_ID + "]服务调用日志", request);
        }
        invokelog.put("success", true);
        this.printObjectJsonString(invokelog, response);
    }

    /**
     * 跳转到服务调用日志表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String INVOKELOG_ID = request.getParameter("INVOKELOG_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> invokelog = null;
        if (StringUtils.isNotEmpty(INVOKELOG_ID)) {
            invokelog = this.invokeLogService.getRecord("MZT_DATASET_INVOKELOG"
                    , new String[]{"INVOKELOG_ID"}, new Object[]{INVOKELOG_ID});
        } else {
            invokelog = new HashMap<String, Object>();
        }
        request.setAttribute("invokelog", invokelog);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }
}
