package com.housoo.platform.appmodel.controller;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.util.*;
import com.housoo.platform.core.service.CommonUIService;
import com.housoo.platform.core.service.FieldConfigService;
import com.housoo.platform.core.service.QueryConditionService;
import com.housoo.platform.common.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.*;
import java.util.List;

/**
 * 描述 公共UI控制器
 *
 * @author housoo
 * @created 2017年3月17日 下午4:23:57
 */
@Controller
@RequestMapping("/appmodel/CommonUIController")
public class CommonUIController extends BaseController {
    /**
     *
     */
    @Resource
    private CommonUIService commonUIService;
    /**
     *
     */
    @Resource
    private FieldConfigService fieldConfigService;
    /**
     *
     */
    @Resource
    private QueryConditionService queryConditionService;

    /**
     * 获取 JqGrid按钮编码
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "getJqgridBtnCode")
    public void getJqgridBtnCode(HttpServletRequest request,
                                 HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        String FORMCONTROL_ID = request.getParameter("FORMCONTROL_ID");
        String GENCMPTPL_ID = request.getParameter("GENCMPTPL_ID");
        String tplCode = commonUIService.getJqgridBtnTplCode(FORMCONTROL_ID, GENCMPTPL_ID);
        result.put("success", true);
        result.put("tplCode", tplCode);
        this.printObjectJsonString(result, response);
    }

    /**
     * 获取 JqGrid树形表格的数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "jqtreetabledata")
    public void jqtreetabledata(HttpServletRequest request,
                                HttpServletResponse response) {
        String FORMCONTROL_ID = request.getParameter("FORMCONTROL_ID");
        Map<String, Object> fieldInfo = fieldConfigService.getFieldMapInfo(FORMCONTROL_ID);
        String tableName = (String) fieldInfo.get("TREE_TABLENAME");
        String entityName = tableName.substring(tableName.lastIndexOf("_") + 1, tableName.length());
        //获取父亲ID名称
        String PARENT_ID_NAME = entityName + "_PARENTID_EQ";
        String LOAD_ROOTID = (String) fieldInfo.get("LOAD_ROOTID");
        String ID_ANDNAME = (String) fieldInfo.get("ID_ANDNAME");
        String ROOT_NAME = (String) fieldInfo.get("ROOT_NAME");
        StringBuffer paramsConfig = new StringBuffer("[TABLE_NAME:");
        paramsConfig.append(tableName).append("]");
        paramsConfig.append("[TREE_IDANDNAMECOL:").append(ID_ANDNAME).append("]");
        paramsConfig.append("[TREE_QUERYFIELDS:").append(fieldInfo.get("TARGET_COLS")).append("]");
        StringBuffer filterConfig = new StringBuffer("[FILTERS:");
        filterConfig.append(PARENT_ID_NAME).append("|").append(LOAD_ROOTID);
        List<Map<String, Object>> querys = queryConditionService.findByFormControlId(FORMCONTROL_ID);
        if (querys != null && querys.size() > 0) {
            filterConfig.append(",");
            for (int i = 0; i < querys.size(); i++) {
                Map<String, Object> query = querys.get(i);
                String QUERYCONDITION_CTRLNAME = (String) query.get("QUERYCONDITION_CTRLNAME");
                String QUERYCONDITION_CONTROLVALUE = (String) query.get("QUERYCONDITION_CONTROLVALUE");
                if (StringUtils.isNotEmpty(request.getParameter(QUERYCONDITION_CTRLNAME))) {
                    QUERYCONDITION_CONTROLVALUE = request.getParameter(QUERYCONDITION_CTRLNAME);
                }
                if (i > 0) {
                    filterConfig.append(",");
                }
                filterConfig.append(QUERYCONDITION_CTRLNAME).
                        append("|").append(QUERYCONDITION_CONTROLVALUE);
            }
        }
        filterConfig.append("]");
        paramsConfig.append(filterConfig);
        List<Map<String, Object>> list = commonUIService.findGenTreeSelectorDatas(paramsConfig.toString());
        if (list == null || list.size() == 0) {
            list = new ArrayList<Map<String, Object>>();
        }
        Map<String, Object> rootMap = new HashMap<String, Object>();
        rootMap.put("expanded", true);
        if (list.size() == 0) {
            rootMap.put("isLeaf", true);
        } else {
            rootMap.put("isLeaf", false);
        }
        rootMap.put("level", 0);
        rootMap.put("parent", "-1");
        rootMap.put(ID_ANDNAME.split(",")[0], "0");
        rootMap.put(ID_ANDNAME.split(",")[1], ROOT_NAME);
        list.add(0, rootMap);
        this.printListJsonString(null, list, response);
        try {

        } catch (Exception e) {
            PlatLogUtil.printStackTrace(e);
        }
    }

    /**
     * 获取 表格的数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "tabledata")
    public void tabledata(HttpServletRequest request,
                          HttpServletResponse response) {
        SqlFilter filter = new SqlFilter(request);
        //添加客户端排序条件
        String columnOrderName = request.getParameter("sidx");
        if (StringUtils.isNotEmpty(columnOrderName)) {
            String sord = request.getParameter("sord");
            filter.addFilter(columnOrderName, sord.toUpperCase(), SqlFilter.FILTER_TYPE_ORDER);
        }
        String FORMCONTROL_ID = request.getParameter("FORMCONTROL_ID");
        //获取数据源类型
        String DATA_TYPE = fieldConfigService.getFieldValue(FORMCONTROL_ID, "DATA_TYPE");
        //获取是否分页
        String IS_PAGE = fieldConfigService.getFieldValue(FORMCONTROL_ID, "IS_PAGE");
        List<Map<String, Object>> list = getJqtableList(filter, FORMCONTROL_ID, DATA_TYPE, IS_PAGE);
        if ("1".equals(IS_PAGE)) {
            if (list == null) {
                list = new ArrayList<Map<String, Object>>();
            }
            if (filter.getPagingBean() != null) {
                this.printListJsonString(filter.getPagingBean(), list, response);
            } else {
                this.printListJsonString(null, list, response);
            }
        } else {
            if (list == null) {
                list = new ArrayList<Map<String, Object>>();
            }
            this.printListJsonString(null, list, response);
        }

    }

    /**
     * 获取echart图形报表数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "echartdata")
    public void echartdata(HttpServletRequest request,
                           HttpServletResponse response) {
        SqlFilter filter = new SqlFilter(request);
        String FORMCONTROL_ID = request.getParameter("FORMCONTROL_ID");
        Map<String, Object> fieldInfo = fieldConfigService.getFieldMapInfo(FORMCONTROL_ID);
        String JAVA_INTERFACE = (String) fieldInfo.get("JAVAINTER");
        String beanId = JAVA_INTERFACE.split("[.]")[0];
        String method = JAVA_INTERFACE.split("[.]")[1];
        Object serviceBean = PlatAppUtil.getBean(beanId);
        Map<String, Object> result = new HashMap<String, Object>();
        if (serviceBean != null) {
            Method invokeMethod;
            try {
                invokeMethod = serviceBean.getClass().getDeclaredMethod(method,
                        new Class[]{SqlFilter.class, Map.class});
                result = (Map<String, Object>) invokeMethod.invoke(serviceBean,
                        new Object[]{filter, fieldInfo});
            } catch (Exception e) {
                PlatLogUtil.printStackTrace(e);
            }
        }
        this.printObjectJsonString(result, response);
    }

    /**
     * 获取Jqtable的数据列表
     *
     * @param filter
     * @param FORMCONTROL_ID
     * @param DATA_TYPE
     * @param IS_PAGE
     * @return
     */
    private List<Map<String, Object>> getJqtableList(SqlFilter filter,
                                                     String FORMCONTROL_ID, String DATA_TYPE, String IS_PAGE) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if ("1".equals(DATA_TYPE)) {
            list = commonUIService.findTableDatas(filter, IS_PAGE, FORMCONTROL_ID);
        } else if ("2".equals(DATA_TYPE)) {
            Map<String, Object> fieldInfo = fieldConfigService.getFieldMapInfo(FORMCONTROL_ID);
            String JAVA_INTERFACE = (String) fieldInfo.get("JAVA_INTERCODE");
            String beanId = JAVA_INTERFACE.split("[.]")[0];
            String method = JAVA_INTERFACE.split("[.]")[1];
            Object serviceBean = PlatAppUtil.getBean(beanId);
            if (serviceBean != null) {
                Method invokeMethod;
                try {
                    invokeMethod = serviceBean.getClass().getDeclaredMethod(method,
                            new Class[]{SqlFilter.class, Map.class});
                    list = (List<Map<String, Object>>) invokeMethod.invoke(serviceBean,
                            new Object[]{filter, fieldInfo});
                } catch (Exception e) {
                    PlatLogUtil.printStackTrace(e);
                }
            }
        }
        return list;
    }

    /**
     * 跳转到图片剪裁工具界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "piccutView")
    public ModelAndView piccutView(HttpServletRequest request) {
        String imageUrl = request.getParameter("imageUrl");
        request.setAttribute("imageUrl", imageUrl);
        return new ModelAndView("common/compui/picturecutview");
    }

    /**
     * 构建图片信息
     *
     * @param imgUrl
     * @return
     */
    public Map<String, String> getImageFile(String imgUrl) {
        //获取附件的根路径
        String realPath = PlatPropUtil.getPropertyValue("config.properties", "attachFilePath");
        //获取请求路径
        String attachFileUrl = PlatPropUtil.getPropertyValue("config.properties", "attachFileUrl");
        //获取源目录文件夹
        List<Integer> indexList = PlatStringUtil.getAllIndexes("/",
                imgUrl, 0, null);
        //获取请求路径的文件夹
        List<Integer> attachFileUrlIndexList = PlatStringUtil.getAllIndexes("/",
                attachFileUrl, 0, null);
        String sourceFolderPath = imgUrl.substring(indexList.get(attachFileUrlIndexList.size() - 1) + 1,
                imgUrl.lastIndexOf("/") + 1);
        String sourceFileName = imgUrl.substring(imgUrl.lastIndexOf("/") + 1, imgUrl.length());
        String type = PlatFileUtil.getFileExt(imgUrl);
        String destPath = sourceFolderPath + UUID.randomUUID() + "." + type;
        String tempFile = realPath + sourceFolderPath + UUID.randomUUID() + "." + type;
        Map<String, String> imgInfo = new HashMap<String, String>();
        imgInfo.put("realPath", realPath);
        imgInfo.put("attachFileUrl", attachFileUrl);
        imgInfo.put("sourceFolderPath", sourceFolderPath);
        imgInfo.put("type", type);
        imgInfo.put("destPath", destPath);
        imgInfo.put("tempFile", tempFile);
        imgInfo.put("sourceFileName", sourceFileName);
        return imgInfo;
    }

    /**
     * 保存裁剪图片
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveCutImg")
    public void saveCutImg(HttpServletRequest request,
                           HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        String imgUrl = request.getParameter("imgUrl");
        Map<String, String> imgInfo = this.getImageFile(imgUrl);
        String realPath = imgInfo.get("realPath");
        String sourceFolderPath = imgInfo.get("sourceFolderPath");
        String sourceFileName = imgInfo.get("sourceFileName");
        String tempFile = imgInfo.get("tempFile");
        String destPath = imgInfo.get("destPath");
        String attachFileUrl = imgInfo.get("attachFileUrl");
        String type = imgInfo.get("type");
        String imgW = request.getParameter("imgW");
        String imgH = request.getParameter("imgH");
        String scaledX = request.getParameter("scaledX");
        String scaledY = request.getParameter("scaledY");
        String scaledW = request.getParameter("scaledW");
        String scaledH = request.getParameter("scaledH");

        try {
            PlatImageUtil.scaleImageByType(realPath + sourceFolderPath + sourceFileName,
                    tempFile, Integer.parseInt(imgH), Integer.parseInt(imgW), true, type);
            PlatImageUtil.cutRectangleImage(tempFile, realPath + destPath,
                    Integer.parseInt(scaledX), Integer.parseInt(scaledY),
                    Integer.parseInt(scaledW), Integer.parseInt(scaledH), type.toUpperCase());
            result.put("success", true);
            result.put("path", attachFileUrl + destPath);
            result.put("dbfilepath", destPath);
        } catch (Exception e) {
            PlatLogUtil.printStackTrace(e);
            result.put("success", false);
            result.put("msg", "裁剪保存失败!");
        }
        this.printObjectJsonString(result, response);
    }

    /**
     * 保存旋转图片
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveRotImg")
    public void saveRotImg(HttpServletRequest request,
                           HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        String imgUrl = request.getParameter("imgUrl");
        Map<String, String> imgInfo = this.getImageFile(imgUrl);
        String realPath = imgInfo.get("realPath");
        String sourceFolderPath = imgInfo.get("sourceFolderPath");
        String sourceFileName = imgInfo.get("sourceFileName");
        String destPath = imgInfo.get("destPath");
        String attachFileUrl = imgInfo.get("attachFileUrl");
        String type = imgInfo.get("type");
        String rotNum = request.getParameter("rotNum");
        try {
            PlatImageUtil.rotateImg(realPath + sourceFolderPath + sourceFileName,
                    realPath + destPath, Integer.parseInt(rotNum), type.toUpperCase());
            result.put("success", true);
            result.put("path", attachFileUrl + destPath);
            result.put("dbfilepath", destPath);
        } catch (Exception e) {
            PlatLogUtil.printStackTrace(e);
            result.put("success", false);
            result.put("msg", "裁剪保存失败!");
        }
        this.printObjectJsonString(result, response);
    }

    /**
     * 保存文字
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveTextImg")
    public void saveTextImg(HttpServletRequest request,
                            HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        String imgUrl = request.getParameter("imgUrl");
        Map<String, String> imgInfo = this.getImageFile(imgUrl);
        String realPath = imgInfo.get("realPath");
        String sourceFolderPath = imgInfo.get("sourceFolderPath");
        String sourceFileName = imgInfo.get("sourceFileName");
        String destPath = imgInfo.get("destPath");
        String attachFileUrl = imgInfo.get("attachFileUrl");
        String type = imgInfo.get("type");
        String text = request.getParameter("text");
        String fontName = request.getParameter("fontFamily");
        String fontStyleStr = request.getParameter("fontstyle");
        String fontWeight = request.getParameter("fontWeight");
        String textdecoration = request.getParameter("textdecoration");
        boolean isUnderLine = false;
        if ("underline".equals(textdecoration)) {
            isUnderLine = true;
        }
        int fontStyle = getFontStyle(fontStyleStr, fontWeight, textdecoration);
        String colorStr = request.getParameter("color");
        Color color = new Color(Integer.parseInt(colorStr.substring(1), 16));
        int fontSize = Integer.parseInt(request.getParameter("fontSize"));
        int x = (int) Double.parseDouble(request.getParameter("left"));
        int y = (int) Double.parseDouble(request.getParameter("top"));
        float alpha = Float.parseFloat(request.getParameter("opicty"));
        try {
            PlatImageUtil.watermarkText(text, realPath + sourceFolderPath + sourceFileName, realPath + destPath, fontName,
                    fontStyle, color, fontSize, x - 3, y, alpha, type.toUpperCase(), isUnderLine);
            result.put("success", true);
            result.put("path", attachFileUrl + destPath);
            result.put("dbfilepath", destPath);
        } catch (Exception e) {
            PlatLogUtil.printStackTrace(e);
            result.put("success", false);
            result.put("msg", "裁剪保存失败!");
        }
        this.printObjectJsonString(result, response);
    }

    /**
     * 获取字体类型
     *
     * @param fontStyleStr
     * @param fontWeight
     * @param textdecoration
     * @return
     */
    private int getFontStyle(String fontStyleStr, String fontWeight,
                             String textdecoration) {
        int z = Font.PLAIN;
        if ("italic".equals(fontStyleStr)) {
            z += Font.ITALIC;
        }
        if ("bold".equals(fontWeight)) {
            z += Font.BOLD;
        }
        return z;
    }

    /**
     * 保存水印文件
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveWaterImage")
    public void saveWaterImage(HttpServletRequest request,
                               HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        int x = Integer.parseInt(request.getParameter("x"));
        int y = Integer.parseInt(request.getParameter("y"));
        float alpha = Float.parseFloat(request.getParameter("alpha"));
        String imgUrl = request.getParameter("imgUrl");
        Map<String, String> imgInfo = this.getImageFile(imgUrl);
        String realPath = imgInfo.get("realPath");
        String sourceFolderPath = imgInfo.get("sourceFolderPath");
        String sourceFileName = imgInfo.get("sourceFileName");
        String destPath = imgInfo.get("destPath");
        String attachFileUrl = imgInfo.get("attachFileUrl");
        String type = imgInfo.get("type");
        String waterImgUrl = request.getParameter("waterImgUrl");
        Map<String, String> waterImageInfo = this.getImageFile(waterImgUrl);
        String waterRealPath = waterImageInfo.get("realPath");
        String waterSourceFolderPath = waterImageInfo.get("sourceFolderPath");
        String waterSourceFileName = waterImageInfo.get("sourceFileName");
        try {
            PlatImageUtil.watermarkImage(waterRealPath + waterSourceFolderPath + waterSourceFileName,
                    realPath + sourceFolderPath + sourceFileName, realPath + destPath,
                    x - 3, y, alpha, type.toUpperCase());
            result.put("success", true);
            result.put("path", attachFileUrl + destPath);
            result.put("dbfilepath", destPath);
        } catch (Exception e) {
            PlatLogUtil.printStackTrace(e);
            result.put("success", false);
            result.put("msg", "水印保存失败!");
        }
        this.printObjectJsonString(result, response);
    }

    /**
     * 加载网格项目
     *
     * @param request
     * @param response
     */
    @RequestMapping("/loadGridItem")
    public ModelAndView loadGridItem(HttpServletRequest request, HttpServletResponse response) {
        String INTERFACLE = request.getParameter("dyna_interface");
        String itemtplpath = request.getParameter("itemtplpath");
        Map<String, Object> POST_PARAMS = PlatBeanUtil.getMapFromRequest(request);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (StringUtils.isNotEmpty(INTERFACLE)) {
            String beanId = INTERFACLE.split("[.]")[0];
            String method = INTERFACLE.split("[.]")[1];
            SqlFilter sqlFilter = new SqlFilter(request);
            Object serviceBean = PlatAppUtil.getBean(beanId);
            if (serviceBean != null) {
                Method invokeMethod;
                try {
                    invokeMethod = serviceBean.getClass().getDeclaredMethod(method,
                            new Class[]{SqlFilter.class});
                    list = (List<Map<String, Object>>) invokeMethod.invoke(serviceBean,
                            new Object[]{sqlFilter});
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    PlatLogUtil.printStackTrace(e);
                }
            }
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> map = list.get(i);
                    map.put("TR_INDEX", i + 1);
                }
            }
        } else {
            Map<String, Object> data = PlatBeanUtil.getMapFromRequest(request);
            list.add(data);
        }
        request.setAttribute("EDIT_DATAS", list);
        request.setAttribute("POST_PARAMS", POST_PARAMS);
        return new ModelAndView(itemtplpath);
    }

    public String encodeFilename(HttpServletRequest request,
                                 String fileName) throws Exception {
        String agent = request.getHeader("USER-AGENT");
        // Firefox
        if (null != agent && -1 != agent.toLowerCase().indexOf("firefox")) {
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        } else {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        }
        return fileName;
    }

    /**
     * 通用导出Excel数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "exportExcel")
    public ModelAndView exportExcel(Map<String, Object> model, HttpServletRequest request,
                                    HttpServletResponse response) {
        SqlFilter filter = new SqlFilter(request);
        String excelFileName = request.getParameter("excelFileName");
        String FORMCONTROL_ID = request.getParameter("formControlId");
        //获取数据源类型
        String DATA_TYPE = fieldConfigService.getFieldValue(FORMCONTROL_ID, "DATA_TYPE");
        //获取配置的列数据
        String COLUMN_JSON = fieldConfigService.getFieldValue(FORMCONTROL_ID, "COLUMN_JSON");
        List<String> columnKeys = new ArrayList<String>();
        List<String> columnNames = new ArrayList<String>();
        List<Map> columnList = JSON.parseArray(COLUMN_JSON, Map.class);
        for (Map column : columnList) {
            String FIELD_NAME = (String) column.get("FIELD_NAME");
            String FIELD_COMMENT = (String) column.get("FIELD_COMMENT");
            String FIELD_ISHIDE = (String) column.get("FIELD_ISHIDE");
            if ("-1".equals(FIELD_ISHIDE)) {
                columnKeys.add(FIELD_NAME);
                columnNames.add(FIELD_COMMENT);
            }
        }
        List<Map<String, Object>> list = getJqtableList(filter, FORMCONTROL_ID, DATA_TYPE, "-1");
        model.put("excelFileName", excelFileName);
        model.put("list", list);
        model.put("values", columnKeys.toArray(new String[columnKeys.size()]));
        model.put("titles", columnNames.toArray(new String[columnNames.size()]));
        model.put("singleHeader", "true");
        CommonExcelView viewExcel = new CommonExcelView();
        return new ModelAndView(viewExcel, model);
    }

    /**
     * 加载已选记录表格
     *
     * @param request
     * @param response
     */
    @RequestMapping("/loadSelectedTable")
    public ModelAndView loadSelectedTable(HttpServletRequest request, HttpServletResponse response) {
        String INTERFACLE = request.getParameter("dynainterface");
        String tableid = request.getParameter("tableid");
        String checkvaluecol = request.getParameter("checkvaluecol");
        String checklabelcol = request.getParameter("checklabelcol");
        String showcolcodes = request.getParameter("showcolcodes");
        String currentrecordIds = request.getParameter("currentrecordIds");
        Set<String> currentrecordIdSet = new HashSet<String>();
        if (StringUtils.isNotEmpty(currentrecordIds)) {
            currentrecordIdSet = new HashSet<String>(Arrays.asList(currentrecordIds.split(",")));
        }
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (StringUtils.isNotEmpty(INTERFACLE)) {
            String beanId = INTERFACLE.split("[.]")[0];
            String method = INTERFACLE.split("[.]")[1];
            SqlFilter sqlFilter = new SqlFilter(request);
            Object serviceBean = PlatAppUtil.getBean(beanId);
            if (serviceBean != null) {
                Method invokeMethod;
                try {
                    invokeMethod = serviceBean.getClass().getDeclaredMethod(method,
                            new Class[]{SqlFilter.class});
                    list = (List<Map<String, Object>>) invokeMethod.invoke(serviceBean,
                            new Object[]{sqlFilter});
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    PlatLogUtil.printStackTrace(e);
                }
                if (list != null && list.size() > 0) {
                    String[] showcolcodeArray = showcolcodes.split(",");
                    for (Map<String, Object> data : list) {
                        String idValue = (String) data.get(checkvaluecol);
                        if (!currentrecordIdSet.contains(idValue)) {
                            StringBuffer trContent = new StringBuffer("<tr id=\"");
                            trContent.append(data.get(checkvaluecol)).append("\" >");
                            trContent.append("<td><input type=\"checkbox\" name=\"");
                            trContent.append(tableid).append("_CHECKBOX\" value=\"").append(data.get(checkvaluecol));
                            trContent.append("\" label =\"").append(data.get(checklabelcol)).append("\"  /></td>");
                            for (String colCode : showcolcodeArray) {
                                trContent.append("<td>");
                                trContent.append(data.get(colCode)).append("</td>");
                            }
                            trContent.append("</tr>");
                            data.put("trcontent", trContent.toString());
                        }
                    }
                }
            }
        } else {
            Map<String, Object> data = PlatBeanUtil.getMapFromRequest(request);
            list.add(data);
        }
        request.setAttribute("selectedList", list);
        request.setAttribute("tableid", tableid);
        return new ModelAndView("common/plattagtpl/selectedtable_tr");
    }
}
