package com.housoo.platform.appmodel.controller;

import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatLogUtil;
import com.housoo.platform.core.util.PlatPropUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.core.service.ExcelImpService;
import com.housoo.platform.core.service.FileAttachService;
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
import java.util.List;
import java.util.Map;

/**
 * 描述 excel导入配置业务相关Controller
 *
 * @author housoo
 * @version 1.0
 * @created 2017-04-30 15:20:47
 */
@Controller
@RequestMapping("/appmodel/ExcelImpController")
public class ExcelImpController extends BaseController {
    /**
     *
     */
    @Resource
    private ExcelImpService excelImpService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;
    /**
     *
     */
    @Resource
    private FileAttachService fileAttachService;

    /**
     * 删除excel导入配置数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        excelImpService.deleteRecords("PLAT_APPMODEL_EXCELIMP", "EXCELIMP_ID", selectColValues.split(","));
        sysLogService.saveBackLog("Excel导入配置", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的excel导入配置", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改excel导入配置数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> excelImp = PlatBeanUtil.getMapFromRequest(request);
        excelImpService.saveCascadeFileAttach(excelImp);
        excelImp.put("success", true);
        this.printObjectJsonString(excelImp, response);
    }


    /**
     * 保存列配置信息
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveColConf")
    public void saveColConf(HttpServletRequest request,
                            HttpServletResponse response) {
        Map<String, Object> params = PlatBeanUtil.getMapFromRequest(request);
        String EXCELIMP_ID = (String) params.get("EXCELIMP_ID");
        String EXCELIMP_COLUMNJSON = (String) params.get("EXCELIMP_COLUMNJSON");
        excelImpService.updateColumnJson(EXCELIMP_ID, EXCELIMP_COLUMNJSON);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 跳转到excel导入配置表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String EXCELIMP_ID = request.getParameter("EXCELIMP_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> excelImp = null;
        if (StringUtils.isNotEmpty(EXCELIMP_ID)) {
            excelImp = this.excelImpService.getRecord("PLAT_APPMODEL_EXCELIMP"
                    , new String[]{"EXCELIMP_ID"}, new Object[]{EXCELIMP_ID});
        } else {
            excelImp = new HashMap<String, Object>();
            excelImp.put("EXCELIMP_INTERFACE", "excelImpService.importExcelDatas");
        }
        request.setAttribute("excelImp", excelImp);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }

    /**
     * 跳转到excel列配置表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goColForm")
    public ModelAndView goColForm(HttpServletRequest request) {
        String EXCELIMP_ID = request.getParameter("EXCELIMP_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        request.setAttribute("EXCELIMP_ID", EXCELIMP_ID);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }

    /**
     * 导入excel的数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "impExcelDatas")
    public void impExcelDatas(HttpServletRequest request,
                              HttpServletResponse response) {
        String dbfilepath = request.getParameter("dbfilepath");
        String excelimpcode = request.getParameter("excelimpcode");
        //获取文件存储路径
        String attachFilePath = PlatPropUtil.getPropertyValue("config.properties"
                , "attachFilePath");
        String excelFilePath = attachFilePath + dbfilepath;
        Map<String, Object> result = null;
        try {
            result = excelImpService.impExcelDatas(excelFilePath, excelimpcode);
        } catch (Exception e) {
            result.put("success", false);
            result.put("msg", "服务器错误,导入失败!");
            PlatLogUtil.printStackTrace(e);
        }
        this.printObjectJsonString(result, response);
    }

    /**
     * 获取配置的Excel模版路径和名称
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "getExcelTpl")
    public void getExcelTpl(HttpServletRequest request,
                            HttpServletResponse response) {
        String excelimpcode = request.getParameter("excelimpcode");
        Map<String, Object> excelImp = this.excelImpService.getRecord("PLAT_APPMODEL_EXCELIMP"
                , new String[]{"EXCELIMP_CODE"}, new Object[]{excelimpcode});
        String EXCELIMP_ID = (String) excelImp.get("EXCELIMP_ID");
        List<Map<String, Object>> fileList = fileAttachService.findList("PLAT_APPMODEL_EXCELIMP"
                , EXCELIMP_ID, null);
        Map<String, Object> file = fileList.get(0);
        String FILE_PATH = (String) file.get("FILE_PATH");
        String FILE_NAME = (String) file.get("FILE_NAME");
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("dbfilepath", FILE_PATH);
        result.put("fileName", FILE_NAME);
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }
}
