package com.housoo.platform.metadata.controller;

import com.housoo.platform.core.util.*;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.metadata.service.DataCatalogService;
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
import java.util.List;
import java.util.Map;

/**
 * 描述 数据目录业务相关Controller
 *
 * @author 高飞
 * @version 1.0
 * @created 2018-05-07 10:53:23
 */
@Controller
@RequestMapping("/metadata/DataCatalogController")
public class DataCatalogController extends BaseController {
    /**
     *
     */
    @Resource
    private DataCatalogService dataCatalogService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除数据目录数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String catalogId = request.getParameter("treeNodeId");
        dataCatalogService.deleteCascadeChild(catalogId);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改数据目录数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> dataCatalog = PlatBeanUtil
                .getMapFromRequest(request);
        String CATALOG_ID = (String) dataCatalog.get("CATALOG_ID");
        if (StringUtils.isEmpty(CATALOG_ID)) {
            dataCatalog.put("CATALOG_CREATETIME", PlatDateTimeUtil.formatDate(
                    new Date(), "yyyy-MM-dd HH:mm:ss"));
            Map<String, Object> loginUser = PlatAppUtil.getBackPlatLoginUser();
            String SYSUSER_NAME = (String) loginUser.get("SYSUSER_NAME");
            dataCatalog.put("CATALOG_CREATOR", SYSUSER_NAME);
        }
        // 如果是保存树形结构表数据,请调用下面的接口,而注释掉上面的接口代码
        dataCatalog = dataCatalogService.saveOrUpdateTreeData(
                "PLAT_METADATA_CATALOG", dataCatalog,
                SysConstants.ID_GENERATOR_UUID, null);
        dataCatalog.put("success", true);
        this.printObjectJsonString(dataCatalog, response);
    }

    /**
     * 跳转到数据目录表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        // 如果是跳转到树形表单录入界面,请开放以下代码,,而注释掉上面的代码
        String CATALOG_ID = request.getParameter("CATALOG_ID");
        String DATACATALOG_PARENTID = request
                .getParameter("DATACATALOG_PARENTID");
        // 获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> dataCatalog = null;
        if (StringUtils.isNotEmpty(CATALOG_ID)) {
            dataCatalog = this.dataCatalogService.getRecord(
                    "PLAT_METADATA_CATALOG", new String[]{"CATALOG_ID"},
                    new Object[]{CATALOG_ID});
            DATACATALOG_PARENTID = (String) dataCatalog.get("CATALOG_PARENTID");
        }
        Map<String, Object> parentDataCatalog = null;
        if ("0".equals(DATACATALOG_PARENTID)) {
            parentDataCatalog = new HashMap<String, Object>();
            parentDataCatalog.put("CATALOG_ID", DATACATALOG_PARENTID);
            parentDataCatalog.put("CATALOG_NAME", "数据目录树");
        } else {
            parentDataCatalog = this.dataCatalogService.getRecord(
                    "PLAT_METADATA_CATALOG", new String[]{"CATALOG_ID"},
                    new Object[]{DATACATALOG_PARENTID});
        }
        if (dataCatalog == null) {
            dataCatalog = new HashMap<String, Object>();
        }
        dataCatalog
                .put("CATALOG_PARENTID", parentDataCatalog.get("CATALOG_ID"));
        dataCatalog.put("CATALOG_PARENTNAME",
                parentDataCatalog.get("CATALOG_NAME"));
        request.setAttribute("dataCatalog", dataCatalog);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }

    /**
     * 跳转到目录选择界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goSelect")
    public ModelAndView goSelect(HttpServletRequest request) {
        String resId = request.getParameter("DATARES_ID");
        List<String> cataLogIds = dataCatalogService.findCatalogIds(resId);
        StringBuffer selectedRecordIds = new StringBuffer("");
        for (int i = 0; i < cataLogIds.size(); i++) {
            if (i > 0) {
                selectedRecordIds.append(",");
            }
            selectedRecordIds.append(cataLogIds.get(i));
        }
        request.setAttribute("selectedRecordIds", selectedRecordIds.toString());
        return PlatUICompUtil.goDesignUI("catalog_select", request);
    }
}
