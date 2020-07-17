package com.housoo.platform.metadata.controller;

import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatDateTimeUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.metadata.service.QueryService;
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
 * 描述 输入参数业务相关Controller
 *
 * @author housoo
 * @version 1.0
 * @created 2018-05-08 11:11:34
 */
@Controller
@RequestMapping("/metadata/QueryController")
public class QueryController extends BaseController {
    /**
     *
     */
    @Resource
    private QueryService queryService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除输入参数数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        queryService.deleteRecords("PLAT_METADATA_QUERY", "QUERY_ID",
                selectColValues.split(","));
        sysLogService.saveBackLog("数据资源管理", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的输入参数", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改输入参数数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> query = PlatBeanUtil.getMapFromRequest(request);
        String QUERY_ID = (String) query.get("QUERY_ID");
        if (StringUtils.isEmpty(QUERY_ID)) {
            query.put("QUERY_TIME", PlatDateTimeUtil.formatDate(new Date(),
                    "yyyy-MM-dd HH:mm:ss"));
            String resId = request.getParameter("DATARES_ID");
            String cn = request.getParameter("QUERY_CN");
            String en = request.getParameter("QUERY_EN");
            boolean isExists = queryService.isExistsQuery(resId, cn, en);
            if (isExists) {
                query.put("success", false);
                query.put("msg", "参数中文和英文都不能重复创建!");
                this.printObjectJsonString(query, response);
                return;
            }
        }
        query = queryService.saveOrUpdate("PLAT_METADATA_QUERY", query,
                SysConstants.ID_GENERATOR_UUID, null);
        query.put("success", true);
        this.printObjectJsonString(query, response);
    }

    /**
     * 跳转到输入参数表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String QUERY_ID = request.getParameter("QUERY_ID");
        String resId = request.getParameter("resId");
        // 获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> query = null;
        if (StringUtils.isNotEmpty(QUERY_ID)) {
            query = this.queryService.getRecord("PLAT_METADATA_QUERY",
                    new String[]{"QUERY_ID"}, new Object[]{QUERY_ID});
        } else {
            query = new HashMap<String, Object>();
            query.put("DATARES_ID", resId);
        }
        request.setAttribute("query", query);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }
}
