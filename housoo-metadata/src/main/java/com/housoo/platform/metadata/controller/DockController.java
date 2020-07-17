package com.housoo.platform.metadata.controller;

import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatDateTimeUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.metadata.service.DataSerService;
import com.housoo.platform.metadata.service.DockService;
import com.housoo.platform.core.service.SysLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述 服务对接申请业务相关Controller
 *
 * @author 高飞
 * @version 1.0
 * @created 2018-05-09 14:23:14
 */
@Controller
@RequestMapping("/metadata/DockController")
public class DockController extends BaseController {
    /**
     *
     */
    @Resource
    private DockService dockService;
    /**
     *
     */
    @Resource
    private DataSerService dataSerService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除服务对接申请数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        dockService.deleteRecords("PLAT_METADATA_DOCK", "DOCK_ID", selectColValues.split(","));
        sysLogService.saveBackLog("数据服务管理", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的服务对接申请", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改服务对接申请数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> dock = PlatBeanUtil.getMapFromRequest(request);
        String DOCK_ID = (String) dock.get("DOCK_ID");
        if (StringUtils.isEmpty(DOCK_ID)) {
            dock.put("DOCK_TIME", PlatDateTimeUtil.formatDate(new Date(),
                    "yyyy-MM-dd HH:mm:ss"));
            dock.put("DOCK_RESULT", 0);
        }
        dock = dockService.saveOrUpdate("PLAT_METADATA_DOCK",
                dock, SysConstants.ID_GENERATOR_UUID, null);
        dock.put("success", true);
        this.printObjectJsonString(dock, response);
    }

    /**
     * 跳转到服务对接申请表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String DOCK_ID = request.getParameter("DOCK_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> dock = null;
        if (StringUtils.isNotEmpty(DOCK_ID)) {
            dock = this.dockService.getRecord("PLAT_METADATA_DOCK"
                    , new String[]{"DOCK_ID"}, new Object[]{DOCK_ID});
            String DOCK_SERIDS = (String) dock.get("DOCK_SERIDS");
            Map<String, String> serInfo = dataSerService.getDataSerInfo(DOCK_SERIDS);
            dock.put("DOCK_SERIDS", serInfo.get("recordIds"));
            dock.put("DOCK_SERLABELS", serInfo.get("recordNames"));
        } else {
            dock = new HashMap<String, Object>();
        }
        request.setAttribute("dock", dock);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }
}
