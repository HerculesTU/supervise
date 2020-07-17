package com.housoo.platform.core.util;

import com.housoo.platform.core.service.DesignService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述 UI控件工具类
 *
 * @author housoo
 * @created 2017年2月5日 下午1:38:41
 */
public class PlatUICompUtil {

    // 正则表达式定义
    private static Pattern STRING_PATTERN = Pattern.compile("\\[(.*?)]");

    /**
     * 获取自定义控件的数据源list
     *
     * @param paramMap
     * @return
     */
    public static List<Map<String, Object>> getCompDataList(Map<String, Object> paramMap) {
        //获取静态数据源
        String static_values = (String) paramMap.get("static_values");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (StringUtils.isNotEmpty(static_values)) {
            String[] labelValueArray = static_values.split(",");
            for (String labelValue : labelValueArray) {
                Map<String, Object> data = new HashMap<String, Object>();
                String[] optionArray = labelValue.split(":");
                String optionlabel = optionArray[0];
                String optionvalue = "";
                if (optionArray.length > 1) {
                    optionvalue = optionArray[1];
                }
                data.put("LABEL", optionlabel);
                data.put("VALUE", optionvalue);
                list.add(data);
            }
        } else {
            //获取动态数据源
            String dyna_interface = (String) paramMap.get("dyna_interface");
            String dyna_param = (String) paramMap.get("dyna_param");
            String beanId = dyna_interface.split("[.]")[0];
            String method = dyna_interface.split("[.]")[1];
            Object serviceBean = PlatAppUtil.getBean(beanId);
            if (serviceBean != null) {
                Method invokeMethod;
                try {
                    invokeMethod = serviceBean.getClass().getDeclaredMethod(method,
                            new Class[]{String.class});
                    list = (List<Map<String, Object>>) invokeMethod.invoke(serviceBean,
                            new Object[]{dyna_param});
                } catch (Exception e) {
                    PlatLogUtil.printStackTrace(e);
                }
            }
        }
        if (list == null) {
            list = new ArrayList<Map<String, Object>>();
        }
        return list;
    }

    /**
     * 获取下拉框的options
     *
     * @param list
     * @param valueSet
     * @return
     */
    public static List<String> getSelectOptions(List<Map<String, Object>> list, Set<String> valueSet
            , Map<String, Object> paramMap) {
        List<String> options = new ArrayList<String>();
        String onlyselectleaf = (String) paramMap.get("onlyselectleaf");
        for (Map<String, Object> data : list) {
            StringBuffer sb = new StringBuffer("");
            //获取值
            String optionValue = (String) data.get("VALUE");
            String optionLable = (String) data.get("LABEL");
            sb.append("<option value=\"").append(optionValue).append("\" ");
            sb.append(" label=\"").append(optionLable).append("\" ");
            if (valueSet.contains(optionValue)) {
                sb.append(" selected=\"selected\" ");
            }
            if (StringUtils.isNotEmpty(onlyselectleaf) && "true".equals(onlyselectleaf)) {
                String ISLEAF = (String) data.get("ISLEAF");
                if (!(StringUtils.isNotEmpty(ISLEAF) && "true".equals(ISLEAF))) {
                    sb.append(" disabled=\"disabled\" ");
                }
            }
            Iterator it = data.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> e = (Map.Entry<String, Object>) it.next();
                if (!"VALUE".equals(e.getKey()) && !"LABEL".equals(e.getKey())) {
                    sb.append(e.getKey()).append("=\"").append(e.getValue())
                            .append("\" ");
                }
            }
            sb.append(">").append(optionLable).append("</option>");
            options.add(sb.toString());
        }
        return options;
    }

    /**
     * 获取下拉框标签的HTML代码
     *
     * @param paramMap
     * @return
     */
    public static String getSelectTagHtml(Map<String, Object> paramMap) {
        String tplPath = PlatAppUtil.getAppAbsolutePath() + "webpages/common/plattagtpl/select_tag.jsp";
        String tplString = PlatFileUtil.readFileString(tplPath);
        String value = (String) paramMap.get("value");
        Set<String> valueSet = new HashSet<String>();
        if (StringUtils.isNotEmpty(value)) {
            valueSet = new HashSet<String>(Arrays.asList(value.split(",")));
        }
        List<Map<String, Object>> list = getCompDataList(paramMap);
        if (list != null) {
            List<String> options = getSelectOptions(list, valueSet, paramMap);
            paramMap.put("optionlist", options);
        }
        paramMap = changeCompAuth("selectcomp", paramMap);
        String htmlContent = PlatStringUtil.getFreeMarkResult(tplString, paramMap);
        return htmlContent;
    }

    /**
     * 获取单选框可选值列表
     *
     * @param list
     * @param valueSet
     * @param paramMap
     * @return
     */
    public static List<String> getRadioOptions(List<Map<String, Object>> list,
                                               Set<String> valueSet, Map<String, Object> paramMap) {
        String name = (String) paramMap.get("name");
        String select_first = (String) paramMap.get("select_first");
        String allowblank = (String) paramMap.get("allowblank");
        String is_horizontal = (String) paramMap.get("is_horizontal");
        String auth_type = (String) paramMap.get("auth_type");
        String attach_props = (String) paramMap.get("attach_props");
        List<String> radios = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> data = list.get(i);
            //获取值
            String optionValue = (String) data.get("VALUE");
            String optionLable = (String) data.get("LABEL");
            String radioId = name + "_" + optionValue;
            StringBuffer sb = new StringBuffer("<div class=\"radio radio-success");
            if ("true".equals(is_horizontal)) {
                sb.append(" radio-inline\" >");
            } else {
                sb.append("\" >");
            }
            sb.append("<input type=\"radio\" name=\"");
            sb.append(name).append("\" id=\"").append(radioId);
            sb.append("\" ");
            sb.append(" value=\"").append(optionValue).append("\" ");
            sb.append(" label=\"").append(optionLable).append("\" ");
            if (StringUtils.isNotEmpty(attach_props)) {
                sb.append(attach_props).append(" ");
            }
            if (i == 0) {
                if ("false".equals(allowblank)) {
                    sb.append(" data-rule=\"checked\" datarule=\"checked\" ");
                }
                if (valueSet.size() == 0 && "true".equals(select_first)) {
                    sb.append(" checked=\"checked\" ");
                }
            }
            if (valueSet.contains(optionValue)) {
                sb.append(" checked=\"checked\" ");
            }
            Iterator it = data.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> e = (Map.Entry<String, Object>) it.next();
                if (!"VALUE".equals(e.getKey()) && !"LABEL".equals(e.getKey())) {
                    sb.append(e.getKey()).append("=\"").append(e.getValue())
                            .append("\" ");
                }
            }
            if ("readonly".equals(auth_type)) {
                sb.append(" disabled=\"disabled\" ");
            }
            sb.append(">");
            sb.append("<label for=\"").append(radioId).append("\" >");
            sb.append(optionLable).append("</label></div>");
            radios.add(sb.toString());
        }
        return radios;
    }

    /**
     * 获取复选框可选值列表
     *
     * @param list
     * @param valueSet
     * @param paramMap
     * @return
     */
    public static List<String> getCheckBoxOptions(List<Map<String, Object>> list,
                                                  Set<String> valueSet, Map<String, Object> paramMap) {
        String name = (String) paramMap.get("name");
        String allowblank = (String) paramMap.get("allowblank");
        String is_horizontal = (String) paramMap.get("is_horizontal");
        String auth_type = (String) paramMap.get("auth_type");
        String max = (String) paramMap.get("max");
        String min = (String) paramMap.get("min");
        List<String> checkboxs = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> data = list.get(i);
            //获取值
            String optionValue = (String) data.get("VALUE");
            String optionLable = (String) data.get("LABEL");
            String checkboxId = name + "_" + optionValue;
            StringBuffer sb = new StringBuffer("<div class=\"checkbox checkbox-success");
            if ("true".equals(is_horizontal)) {
                sb.append(" checkbox-inline\" >");
            } else {
                sb.append("\" >");
            }
            sb.append("<input type=\"checkbox\" name=\"");
            sb.append(name).append("\" id=\"").append(checkboxId);
            sb.append("\" ");
            sb.append(" value=\"").append(optionValue).append("\" ");
            sb.append(" label=\"").append(optionLable).append("\" ");
            if (i == 0) {
                StringBuffer datarule = new StringBuffer("");
                if ("false".equals(allowblank)) {
                    datarule.append("checked;");
                }
                if (StringUtils.isNotEmpty(min) || StringUtils.isNotEmpty(max)) {
                    datarule.append("checked[");
                    if (StringUtils.isNotEmpty(min)) {
                        datarule.append(min);
                    }
                    datarule.append("~");
                    if (StringUtils.isNotEmpty(max)) {
                        datarule.append(max);
                    }
                    datarule.append("]");
                }
                if (datarule.length() > 1) {
                    sb.append(" data-rule=\"").append(datarule).append("\" ");
                    sb.append(" datarule=\"").append(datarule).append("\" ");
                }
            }
            if (valueSet.contains(optionValue)) {
                sb.append(" checked=\"checked\" ");
            }
            Iterator it = data.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> e = (Map.Entry<String, Object>) it.next();
                if (!"VALUE".equals(e.getKey()) && !"LABEL".equals(e.getKey())) {
                    sb.append(e.getKey()).append("=\"").append(e.getValue())
                            .append("\" ");
                }
            }
            if ("readonly".equals(auth_type)) {
                sb.append(" disabled=\"disabled\" ");
            }
            sb.append(">");
            sb.append("<label for=\"").append(checkboxId).append("\" >");
            sb.append(optionLable).append("</label></div>");
            checkboxs.add(sb.toString());
        }
        return checkboxs;
    }

    /**
     * 获取复选框标签的HTML代码
     *
     * @param paramMap
     * @return
     */
    public static String getCheckBoxTagHtml(Map<String, Object> paramMap) {
        String tplPath = PlatAppUtil.getAppAbsolutePath() + "webpages/common/plattagtpl/checkbox_tag.jsp";
        String tplString = PlatFileUtil.readFileString(tplPath);
        String value = (String) paramMap.get("value");
        Set<String> valueSet = new HashSet<String>();
        if (StringUtils.isNotEmpty(value)) {
            valueSet = new HashSet<String>(Arrays.asList(value.split(",")));
        }
        List<Map<String, Object>> list = getCompDataList(paramMap);
        paramMap = changeCompAuth("checkboxcomp", paramMap);
        List<String> options = getCheckBoxOptions(list, valueSet, paramMap);
        paramMap.put("optionlist", options);
        String htmlContent = PlatStringUtil.getFreeMarkResult(tplString, paramMap);
        return htmlContent;
    }

    /**
     * 获取单选框标签的HTML代码
     *
     * @param paramMap
     * @return
     */
    public static String getRadioTagHtml(Map<String, Object> paramMap) {
        String tplPath = PlatAppUtil.getAppAbsolutePath() + "webpages/common/plattagtpl/radio_tag.jsp";
        String tplString = PlatFileUtil.readFileString(tplPath);
        String value = (String) paramMap.get("value");
        Set<String> valueSet = new HashSet<String>();
        if (StringUtils.isNotEmpty(value)) {
            valueSet = new HashSet<String>(Arrays.asList(value.split(",")));
        }
        List<Map<String, Object>> list = getCompDataList(paramMap);
        paramMap = changeCompAuth("radiocomp", paramMap);
        List<String> options = getRadioOptions(list, valueSet, paramMap);
        paramMap.put("optionlist", options);
        String htmlContent = PlatStringUtil.getFreeMarkResult(tplString, paramMap);
        return htmlContent;
    }

    /**
     * 改变控件的权限值
     *
     * @param compType
     */
    public static Map<String, Object> changeCompAuth(String compType, Map<String, Object> paramMap) {
        Map<String, String[]> changeCompAuthMap = (Map<String, String[]>) PlatAppUtil.getRequest().
                getAttribute(SysConstants.CHANGE_COMP_AUTH_MAP_KEY);
        if (changeCompAuthMap != null) {
            String name = (String) paramMap.get("name");
            if (changeCompAuthMap.get(name) != null) {
                String[] configs = changeCompAuthMap.get(name);
                String allowblank = configs[0];
                String auth_type = configs[1];
                paramMap.put("allowblank", allowblank);
                paramMap.put("auth_type", auth_type);
                String datarule = (String) paramMap.get("datarule");
                if ("true".equals(allowblank)) {
                    if (StringUtils.isNotEmpty(datarule)) {
                        datarule = datarule.replace("required;", "");
                    }
                } else {
                    if (StringUtils.isNotEmpty(datarule) && !datarule.contains("required;")) {
                        datarule += "required;";
                    } else if (StringUtils.isNotEmpty(datarule) && datarule.contains("required;")) {

                    } else {
                        //datarule = datarule.replace("required;","");
                        datarule = "required;";
                    }
                }
                paramMap.put("datarule", datarule);
            }
        }
        return paramMap;
    }

    /**
     * 获取星级评定标签代码
     *
     * @param paramMap
     * @return
     */
    public static String getRatingStarTagHtml(Map<String, Object> paramMap) {
        String tplPath = PlatAppUtil.getAppAbsolutePath() + "webpages/common/plattagtpl/ratingstar_tag.jsp";
        String tplString = PlatFileUtil.readFileString(tplPath);
        String htmlContent = PlatStringUtil.getFreeMarkResult(tplString, paramMap);
        return htmlContent;
    }

    /**
     * 获取输入框标签html代码
     *
     * @param paramMap
     * @return Map<String   ,   String   [   ]> changeCompAuthMap = new HashMap<String,String[]>();
     * changeCompAuthMap.put("DESIGN_NAME",new String[]{"true","hidden"});
     * request.setAttribute(SysConstants.CHANGE_COMP_AUTH_MAP_KEY, changeCompAuthMap);
     */
    public static String getInputTagHtml(Map<String, Object> paramMap) {
        String tplPath = PlatAppUtil.getAppAbsolutePath() + "webpages/common/plattagtpl/input_tag.jsp";
        String tplString = PlatFileUtil.readFileString(tplPath);
        paramMap = changeCompAuth("inputcomp", paramMap);
        String htmlContent = PlatStringUtil.getFreeMarkResult(tplString, paramMap);
        return htmlContent;
    }

    /**
     * 获取标签控件的HTML代码
     *
     * @param paramMap
     * @return
     */
    public static String getLabelTagHtml(Map<String, Object> paramMap) {
        String tplPath = PlatAppUtil.getAppAbsolutePath() + "webpages/common/plattagtpl/label_tag.jsp";
        String tplString = PlatFileUtil.readFileString(tplPath);
        paramMap = changeCompAuth("labelcomp", paramMap);
        String htmlContent = PlatStringUtil.getFreeMarkResult(tplString, paramMap);
        return htmlContent;
    }

    /**
     * 获取数字输入框标签代码
     *
     * @param paramMap
     * @return
     */
    public static String getNumberTagHtml(Map<String, Object> paramMap) {
        String tplPath = PlatAppUtil.getAppAbsolutePath() + "webpages/common/plattagtpl/number_tag.jsp";
        String tplString = PlatFileUtil.readFileString(tplPath);
        Map<String, String[]> changeCompAuthMap = (Map<String, String[]>) PlatAppUtil.getRequest().
                getAttribute(SysConstants.CHANGE_COMP_AUTH_MAP_KEY);
        if (changeCompAuthMap != null) {
            String name = (String) paramMap.get("name");
            if (changeCompAuthMap.get(name) != null) {
                String[] configs = changeCompAuthMap.get(name);
                String allowblank = configs[0];
                String auth_type = configs[1];
                paramMap.put("allowblank", allowblank);
                paramMap.put("auth_type", auth_type);
                String datarule = (String) paramMap.get("datarule");
                if ("true".equals(allowblank)) {
                    if (StringUtils.isNotEmpty(datarule)) {
                        datarule = datarule.replace("required;", "");
                    }
                } else {
                    if (StringUtils.isNotEmpty(datarule) && !datarule.contains("required;")) {
                        datarule += "required;";
                    } else {
                        datarule = "required;";
                    }
                }
                paramMap.put("datarule", datarule);
            }
        }
        String htmlContent = PlatStringUtil.getFreeMarkResult(tplString, paramMap);
        return htmlContent;
    }

    /**
     * 获取日期标签html代码
     *
     * @param paramMap
     * @return
     */
    public static String getDateTimeTagHtml(Map<String, Object> paramMap) {
        String tplPath = PlatAppUtil.getAppAbsolutePath() + "webpages/common/plattagtpl/datetime_tag.jsp";
        String tplString = PlatFileUtil.readFileString(tplPath);
        paramMap = changeCompAuth("datetime", paramMap);
        //获取格式
        String format = (String) paramMap.get("format");
        if (StringUtils.isNotEmpty(format)) {
            String defaultnow = (String) paramMap.get("defaultnow");
            if (StringUtils.isNotEmpty(defaultnow) && "1".equals(defaultnow)) {
                if (format.contains("hh")) {
                    format = format.replace("hh", "HH");
                }
                if (format.contains("DD")) {
                    format = format.replace("DD", "dd");
                }
                paramMap.put("value", PlatDateTimeUtil.formatDate(new Date(), format));
            }
        }
        String htmlContent = PlatStringUtil.getFreeMarkResult(tplString, paramMap);
        return htmlContent;
    }

    /**
     * 获取时间范围控件的HTML代码
     *
     * @param paramMap
     * @return
     */
    public static String getRangeTimeTagHtml(Map<String, Object> paramMap) {
        String tplPath = PlatAppUtil.getAppAbsolutePath() + "webpages/common/plattagtpl/rangetime_tag.jsp";
        String tplString = PlatFileUtil.readFileString(tplPath);
        String htmlContent = PlatStringUtil.getFreeMarkResult(tplString, paramMap);
        return htmlContent;
    }


    /**
     * 获取大文本框标签html代码
     *
     * @param paramMap
     * @return
     */
    public static String getTextareaTagHtml(Map<String, Object> paramMap) {
        String tplPath = PlatAppUtil.getAppAbsolutePath() + "webpages/common/plattagtpl/textarea_tag.jsp";
        String tplString = PlatFileUtil.readFileString(tplPath);
        String maxlength = (String) paramMap.get("maxlength");
        String minlength = (String) paramMap.get("minlength");
        String allowblank = (String) paramMap.get("allowblank");
        StringBuffer datarule = new StringBuffer();
        if ("false".equals(allowblank)) {
            datarule.append("required;");
        }
        if (StringUtils.isNotEmpty(minlength) || StringUtils.isNotEmpty(maxlength)) {
            datarule.append("length[");
            if (StringUtils.isNotEmpty(minlength)) {
                datarule.append(minlength);
            }
            datarule.append("~");
            if (StringUtils.isNotEmpty(maxlength)) {
                datarule.append(maxlength);
            }
            datarule.append("]");
        }
        if (datarule.length() > 1) {
            paramMap.put("datarule", datarule.toString());
        }
        String buttonconfigs = (String) paramMap.get("buttonconfigs");
        if (StringUtils.isNotEmpty(buttonconfigs)) {
            //Pattern p = Pattern.compile("\\[(.*?)]");
            Matcher m = STRING_PATTERN.matcher(buttonconfigs);
            List<Map<String, Object>> buttonList = new ArrayList<Map<String, Object>>();
            while (m.find()) {
                Map<String, Object> menu = new HashMap<String, Object>();
                String menuValue = m.group(1);
                menu.put("btnName", menuValue.split(",")[0]);
                menu.put("fnName", menuValue.split(",")[1]);
                buttonList.add(menu);
            }
            paramMap.put("buttonList", buttonList);
        }
        paramMap = changeCompAuth("textareacomp", paramMap);
        String htmlContent = PlatStringUtil.getFreeMarkResult(tplString, paramMap);
        return htmlContent;
    }

    /**
     * 获取属性面板标签html代码
     *
     * @param paramMap
     * @return
     */
    public static String getTreePanelTagHtml(Map<String, Object> paramMap) {
        String tplPath = PlatAppUtil.getAppAbsolutePath() + "webpages/common/plattagtpl/treepanel_tag.jsp";
        String tplString = PlatFileUtil.readFileString(tplPath);
        String right_menu = (String) paramMap.get("right_menu");
        if (StringUtils.isNotEmpty(right_menu)) {
            //Pattern p = Pattern.compile("\\[(.*?)]");
            Matcher m = STRING_PATTERN.matcher(right_menu);
            List<Map<String, Object>> rightmenus = new ArrayList<Map<String, Object>>();
            while (m.find()) {
                Map<String, Object> menu = new HashMap<String, Object>();
                String menuValue = m.group(1);
                menu.put("menuName", menuValue.split(",")[0]);
                menu.put("iconName", menuValue.split(",")[1]);
                menu.put("fnName", menuValue.split(",")[2]);
                if (menuValue.split(",").length > 3) {
                    menu.put("platreskey", menuValue.split(",")[3]);
                }
                rightmenus.add(menu);
            }
            paramMap.put("rightmenus", rightmenus);
        }

        String htmlContent = PlatStringUtil.getFreeMarkResult(tplString, paramMap);
        return htmlContent;
    }

    /**
     * 获取列表组标签HTML代码
     *
     * @param paramMap
     * @return
     */
    public static String getListGroupTagHtml(Map<String, Object> paramMap) {
        String tplPath = PlatAppUtil.getAppAbsolutePath() + "webpages/common/plattagtpl/listgroup_tag.jsp";
        String tplString = PlatFileUtil.readFileString(tplPath);
        List<Map<String, Object>> list = getCompDataList(paramMap);
        paramMap.put("groupList", list);
        String htmlContent = PlatStringUtil.getFreeMarkResult(tplString, paramMap);
        return htmlContent;
    }

    /**
     * 获取图片滑动标签html
     *
     * @param paramMap
     * @return
     */
    public static String getPicSlideTagHtml(Map<String, Object> paramMap) {
        String tplPath = PlatAppUtil.getAppAbsolutePath() + "webpages/common/plattagtpl/picslide_tag.jsp";
        String tplString = PlatFileUtil.readFileString(tplPath);
        List<Map<String, Object>> list = getCompDataList(paramMap);
        paramMap.put("dataList", list);
        String htmlContent = PlatStringUtil.getFreeMarkResult(tplString, paramMap);
        return htmlContent;
    }

    /**
     * 获取可操作列表组标签HTML代码
     *
     * @param paramMap
     * @return
     */
    public static String getOperListGroupTagHtml(Map<String, Object> paramMap) {
        String tplPath = PlatAppUtil.getAppAbsolutePath() + "webpages/common/plattagtpl/operlistgroup_tag.jsp";
        String tplString = PlatFileUtil.readFileString(tplPath);
        List<Map<String, Object>> list = getCompDataList(paramMap);
        paramMap.put("groupList", list);
        String htmlContent = PlatStringUtil.getFreeMarkResult(tplString, paramMap);
        return htmlContent;
    }

    /**
     * 获取可编辑表格html代码
     *
     * @param paramMap
     * @return
     */
    public static String getEditTableTagHtml(Map<String, Object> paramMap) {
        String tplPath = PlatAppUtil.getAppAbsolutePath() + "webpages/common/plattagtpl/edittable_tag.jsp";
        String tplString = PlatFileUtil.readFileString(tplPath);
        String col_style = (String) paramMap.get("col_style");
        if (StringUtils.isNotEmpty(col_style)) {
            //Pattern p = Pattern.compile("\\[(.*?)]");
            Matcher m = STRING_PATTERN.matcher(col_style);
            List<Map<String, Object>> cols = new ArrayList<Map<String, Object>>();
            while (m.find()) {
                Map<String, Object> col = new HashMap<String, Object>();
                String colValue = m.group(1);
                col.put("width", colValue.split(",")[0]);
                col.put("name", colValue.split(",")[1]);
                cols.add(col);
            }
            paramMap.put("cols", cols);
        }
        String htmlContent = PlatStringUtil.getFreeMarkResult(tplString, paramMap);
        return htmlContent;
    }

    /**
     * 获取网格条目配置的MAP
     *
     * @param itemConf
     * @return
     */
    public static Map<String, String> getGridItemConfMap(String itemConf) {
        //Pattern p = Pattern.compile("\\[(.*?)]");
        Matcher m = STRING_PATTERN.matcher(itemConf);
        Map<String, String> newCol = new HashMap<String, String>();
        while (m.find()) {
            String colValue = m.group(1);
            String newColValue = colValue.split(",")[1] + "," + colValue.split(",")[2];
            newCol.put(colValue.split(",")[0], newColValue);
        }
        return newCol;
    }

    /**
     * 获取网格项目的列表
     *
     * @param itemKey
     * @return
     */
    public static List<Map<String, Object>> getGridItemList(String itemKey, String iconfont,
                                                            Map<String, String> getGridItemConf, List<Map<String, Object>> list) {
        for (Map<String, Object> data : list) {
            data.put("ITEM_KEY", data.get(itemKey));
            data.put("ICONFONT", iconfont);
            List<Map<String, Object>> FIELD_DATAS = new ArrayList<Map<String, Object>>();
            StringBuffer dataRecord = new StringBuffer("");
            Iterator it = data.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
                String fieldName = entry.getKey();
                Object val = entry.getValue();
                if (getGridItemConf.keySet().contains(fieldName)) {
                    String itemConfValue = getGridItemConf.get(fieldName);
                    String ishide = itemConfValue.split(",")[0];
                    if ("-1".equals(ishide)) {
                        dataRecord.append(val).append(" ");
                    }
                    Map<String, Object> fieldData = new HashMap<String, Object>();
                    fieldData.put("VALUE", val);
                    fieldData.put("COLKEY", fieldName);
                    fieldData.put("ISHIDE", ishide);
                    fieldData.put("LABEL", itemConfValue.split(",")[1]);
                    FIELD_DATAS.add(fieldData);
                }
            }
            data.put("DATA_RECORD", dataRecord.toString());
            if (FIELD_DATAS.size() > 0) {
                data.put("FIELD_DATAS", FIELD_DATAS);
            }
        }
        return list;
    }

    /**
     * 获取网格控件html代码
     *
     * @param paramMap
     * @return
     */
    public static String getGridItemTagHtml(Map<String, Object> paramMap) {
        String tplPath = PlatAppUtil.getAppAbsolutePath() + "webpages/common/plattagtpl/griditem_tag.jsp";
        String tplString = PlatFileUtil.readFileString(tplPath);
        String col_style = (String) paramMap.get("col_style");
        if (StringUtils.isNotEmpty(col_style)) {
            //Pattern p = Pattern.compile("\\[(.*?)]");
            Matcher m = STRING_PATTERN.matcher(col_style);
            List<Map<String, Object>> cols = new ArrayList<Map<String, Object>>();
            while (m.find()) {
                Map<String, Object> col = new HashMap<String, Object>();
                String colValue = m.group(1);
                col.put("width", colValue.split(",")[0]);
                col.put("name", colValue.split(",")[1]);
                cols.add(col);
            }
            paramMap.put("cols", cols);
        }
        String htmlContent = PlatStringUtil.getFreeMarkResult(tplString, paramMap);
        return htmlContent;
    }

    /**
     * 获取弹出层标签的HTML代码
     *
     * @param paramMap
     * @return
     */
    public static String getWinSelectorTagHtml(Map<String, Object> paramMap) {
        String tplPath = PlatAppUtil.getAppAbsolutePath() + "webpages/common/plattagtpl/winselector_tag.jsp";
        String tplString = PlatFileUtil.readFileString(tplPath);
        paramMap = changeCompAuth("winselector", paramMap);
        String htmlContent = PlatStringUtil.getFreeMarkResult(tplString, paramMap);
        return htmlContent;
    }

    /**
     * 获取警告框的标签HTML
     *
     * @param paramMap
     * @return
     */
    public static String getAlertDivTagHtml(Map<String, Object> paramMap) {
        String tplPath = PlatAppUtil.getAppAbsolutePath() + "webpages/common/plattagtpl/alertdiv_tag.jsp";
        String tplString = PlatFileUtil.readFileString(tplPath);
        String htmlContent = PlatStringUtil.getFreeMarkResult(tplString, paramMap);
        return htmlContent;
    }

    /**
     * 构建树形下拉数据
     *
     * @param list
     * @return
     */
    public static List<Map<String, Object>> getTreeSelectDatas(List<Map<String, Object>> list) {
        for (Map<String, Object> map : list) {
            int TREE_LEVEL = Integer.parseInt(map.get("TREE_LEVEL").toString());
            StringBuffer preBlank = new StringBuffer("");
            int count = (TREE_LEVEL - 1) * 4;
            for (int i = 0; i < count; i++) {
                preBlank.append("&nbsp;");
            }
            String LABEL = (String) map.get("LABEL");
            map.put("LABEL", preBlank.append(LABEL).toString());
        }
        return list;
    }


    /**
     * 跳转到设计UI的生成界面
     *
     * @param desingCode
     * @param request
     * @return
     */
    public static ModelAndView goDesignUI(String desingCode, HttpServletRequest request) {
        DesignService designService = (DesignService) PlatAppUtil.getBean("designService");
        String appPath = PlatAppUtil.getAppAbsolutePath();
        String jspPath = designService.getGenPathByDesignIdOrCode(desingCode,
                desingCode + ".jsp", false, false);
        File jspFile = new File(jspPath);
        if (!jspFile.exists()) {
            Map<String, Object> design = designService.getRecord("PLAT_APPMODEL_DESIGN",
                    new String[]{"DESIGN_CODE"}, new Object[]{desingCode});
            String genJspCode = designService.saveGenJspCode(design, appPath);
            PlatFileUtil.writeDataToDisk(genJspCode, jspPath.toString(), null);
        }
        return new ModelAndView("background/genui/" + desingCode + "/" + desingCode);
    }
}
