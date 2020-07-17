package com.housoo.platform.common.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.housoo.platform.core.model.PagingBean;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.util.*;
import com.housoo.platform.common.service.CommonService;
import com.housoo.platform.core.service.SysLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 描述
 *
 * @author 高飞
 * @created 2017年1月8日 上午11:57:08
 */
@Controller
@RequestMapping("/common/baseController")
public class BaseController {
    /**
     *
     */
    @Resource
    private CommonService commonService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 返回通用树形json
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "tree")
    public void tree(HttpServletRequest request,
                     HttpServletResponse response) {
        String treeJson = commonService.getTreeJson(request);
        response.setContentType("application/json");
        response.setHeader("Cache-Control", "no-store");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
        } catch (IOException e) {
            PlatLogUtil.printStackTrace(e);
        }
        pw.write(treeJson);
        pw.flush();
        pw.close();
    }

    /**
     * 打印JSON字符串
     *
     * @param targetJson
     * @param response
     */
    protected void printJsonString(String targetJson, HttpServletResponse response) {
        response.setContentType("application/json");
        response.setHeader("Cache-Control", "no-store");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
        } catch (IOException e) {
            PlatLogUtil.printStackTrace(e);
        }
        pw.write(targetJson);
        pw.flush();
        pw.close();
    }

    /**
     * 打印对象的json字符串到客户端
     *
     * @param obj
     * @param response
     */
    protected void printObjectJsonString(Object obj, HttpServletResponse response) {
        String json = JSON.toJSONString(obj, SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullNumberAsZero
                , SerializerFeature.WriteNullStringAsEmpty
        );
        response.setContentType("application/json");
        response.setHeader("Cache-Control", "no-store");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
        } catch (IOException e) {
            PlatLogUtil.printStackTrace(e);
        }
        pw.write(json);
        pw.flush();
        pw.close();
    }

    /**
     * 打印列表的JSON到客户端
     *
     * @param pb 分页对象
     * @param list 列表数据
     * @param response
     */
    protected void printListJsonString(PagingBean pb, List list, HttpServletResponse response) {
        Map<String, Object> listResult = new HashMap<String, Object>();
        if (pb != null && pb.getTotalPage() != null) {
            listResult.put("total", pb.getTotalPage());
            listResult.put("page", pb.getCurrentPage());
            listResult.put("records", pb.getTotalItems());
        } else {
            listResult.put("total", 1);
            listResult.put("page", 1);
            listResult.put("records", list.size());
        }
        listResult.put("rows", list);
        String json = JSON.toJSONString(listResult, SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullNumberAsZero
                , SerializerFeature.WriteNullStringAsEmpty
        );
        response.setContentType("application/json");
        response.setHeader("Cache-Control", "no-store");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
        } catch (IOException e) {
            PlatLogUtil.printStackTrace(e);
        }
        pw.write(json);
        pw.flush();
        pw.close();
    }

    /**
     * 验证字段的唯一性
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "checkUnique")
    public void checkUnique(HttpServletRequest request,
                            HttpServletResponse response) {
        String VALID_TABLENAME = request.getParameter("VALID_TABLENAME");
        String VALID_FIELDLABLE = request.getParameter("VALID_FIELDLABLE");
        String VALID_FIELDNAME = request.getParameter("VALID_FIELDNAME");
        String VALID_FIELDVALUE = request.getParameter(VALID_FIELDNAME);
        String RECORD_ID = request.getParameter("RECORD_ID");
        Map<String, String> result = new HashMap<String, String>();
        boolean isExists = commonService.isExistsRecord(VALID_TABLENAME,
                VALID_FIELDNAME, VALID_FIELDVALUE, RECORD_ID);
        if (isExists) {
            result.put("error", "该" + VALID_FIELDLABLE + "已经存在,请重新输入!");
        }
        this.printObjectJsonString(result, response);
    }

    /**
     * 缓存参数
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "cacheParams")
    public void cacheParams(HttpServletRequest request,
                            HttpServletResponse response) {
        Map<String, Object> params = PlatBeanUtil.getMapFromRequest(request);
        PlatAppUtil.setSessionCache("cacheParams", params);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 更新树形排序字段
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "updateTreeSn")
    public void updateTreeSn(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();

        // 获取被拖动节点ID
        String dragTreeNodeId = request.getParameter("dragTreeNodeId");
        // 获取被拖动节点最新level
        int dragTreeNodeNewLevel = Integer.parseInt(request.getParameter("dragTreeNodeNewLevel"));
        // 获取目标节点ID
        String targetNodeId = request.getParameter("targetNodeId");
        // 获取目标节点level
        int targetNodeLevel = Integer.parseInt(request.getParameter("targetNodeLevel"));
        String moveType = request.getParameter("moveType");
        String tableName = request.getParameter("tableName");
        if (dragTreeNodeNewLevel == 0) {
            result.put("success", false);
            result.put("msg", "不能将该节点拖曳成根节点!");
        } else {
            result.put("success", true);
            commonService.updateTreeSn(tableName, dragTreeNodeId,
                    dragTreeNodeNewLevel, targetNodeId, targetNodeLevel, moveType);
        }
        this.printObjectJsonString(result, response);
    }

    /**
     * 重新加载下拉框
     *
     * @param request
     * @param response
     */
    @RequestMapping("/reloadSelect")
    public void reloadSelect(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> paramMap = PlatBeanUtil.getMapFromRequest(request);
        String value = (String) paramMap.get("value");
        String multiselect = (String) paramMap.get("multiselect");
        Set<String> valueSet = new HashSet<String>();
        if (StringUtils.isNotEmpty(value)) {
            valueSet = new HashSet<String>(Arrays.asList(value.split(",")));
        }
        List<Map<String, Object>> list = PlatUICompUtil.getCompDataList(paramMap);
        List<String> options = PlatUICompUtil.getSelectOptions(list, valueSet, paramMap);
        StringBuffer resultHtml = new StringBuffer("");
        if (StringUtils.isEmpty(multiselect)) {
            resultHtml.append("<option disabled selected value></option>");
        }
        for (String option : options) {
            resultHtml.append(option);
        }
        Map<String, String> result = new HashMap<String, String>();
        result.put("resultHtml", resultHtml.toString());
        this.printObjectJsonString(result, response);
    }

    /**
     * 重新加载单选框
     *
     * @param request
     * @param response
     */
    @RequestMapping("/reloadRadio")
    public void reloadRadio(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> paramMap = PlatBeanUtil.getMapFromRequest(request);
        String value = (String) paramMap.get("value");
        Set<String> valueSet = new HashSet<String>();
        if (StringUtils.isNotEmpty(value)) {
            valueSet = new HashSet<String>(Arrays.asList(value.split(",")));
        }
        List<Map<String, Object>> list = PlatUICompUtil.getCompDataList(paramMap);
        List<String> options = PlatUICompUtil.getRadioOptions(list, valueSet, paramMap);
        StringBuffer resultHtml = new StringBuffer("");
        for (String option : options) {
            resultHtml.append(option);
        }
        Map<String, String> result = new HashMap<String, String>();
        result.put("resultHtml", resultHtml.toString());
        this.printObjectJsonString(result, response);
    }

    /**
     * 重新加载复选框
     *
     * @param request
     * @param response
     */
    @RequestMapping("/reloadCheckbox")
    public void reloadCheckbox(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> paramMap = PlatBeanUtil.getMapFromRequest(request);
        String value = (String) paramMap.get("value");
        Set<String> valueSet = new HashSet<String>();
        if (StringUtils.isNotEmpty(value)) {
            valueSet = new HashSet<String>(Arrays.asList(value.split(",")));
        }
        List<Map<String, Object>> list = PlatUICompUtil.getCompDataList(paramMap);
        List<String> options = PlatUICompUtil.getCheckBoxOptions(list, valueSet, paramMap);
        StringBuffer resultHtml = new StringBuffer("");
        for (String option : options) {
            resultHtml.append(option);
        }
        Map<String, String> result = new HashMap<String, String>();
        result.put("resultHtml", resultHtml.toString());
        this.printObjectJsonString(result, response);
    }


    /**
     * 自动补全
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "autoComplete")
    public void autoComplete(HttpServletRequest request,
                             HttpServletResponse response) {
        //如果自动补全的类型为2,那么获取到key之后进行判断过滤
        //String keyword = request.getParameter("keyword");
        SqlFilter sqlFilter = new SqlFilter(request);
        List<Map<String, Object>> list = this.commonService.findAutoCompleteDatas(sqlFilter);
        String json = JSON.toJSONString(list);
        response.setContentType("application/json");
        response.setHeader("Cache-Control", "no-store");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
        } catch (IOException e) {
            PlatLogUtil.printStackTrace(e);
        }
        pw.write(json.toLowerCase());
        pw.flush();
        pw.close();
    }

    /**
     * 添加可编辑表格行
     *
     * @param request
     * @param response
     */
    @RequestMapping("/loadEditTableRow")
    public ModelAndView loadEditTableRow(HttpServletRequest request, HttpServletResponse response) {
        String TABLE_ID = request.getParameter("tableId");
        String INTERFACLE = request.getParameter("dataInterface");
        String tr_tplpath = request.getParameter("tr_tplpath");
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
                    PlatLogUtil.printStackTrace(e);
                }
            }
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> map = list.get(i);
                    map.put("NAME_INDEX", UUIDGenerator.getUUID());
                    map.put("TR_INDEX", i + 1);
                }
            }
        } else {
            Map<String, Object> data = PlatBeanUtil.getMapFromRequest(request);
            data.put("NAME_INDEX", UUIDGenerator.getUUID());
            list.add(data);
        }
        request.setAttribute("TABLE_ID", TABLE_ID);
        request.setAttribute("EDIT_DATAS", list);
        request.setAttribute("POST_PARAMS", POST_PARAMS);
        return new ModelAndView(tr_tplpath);
    }

    /**
     * 重新加载操作列表组
     *
     * @param request
     * @param response
     */
    @RequestMapping("/reloadOperListGroup")
    public void reloadOperListGroup(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> paramMap = PlatBeanUtil.getMapFromRequest(request);
        List<Map<String, Object>> list = PlatUICompUtil.getCompDataList(paramMap);
        String tplPath = PlatAppUtil.getAppAbsolutePath() + "webpages/common/plattagtpl/operlistgroupitem_tag.jsp";
        String tplString = PlatFileUtil.readFileString(tplPath);
        paramMap.put("groupList", list);
        String htmlContent = PlatStringUtil.getFreeMarkResult(tplString, paramMap);
        Map<String, String> result = new HashMap<String, String>();
        result.put("resultHtml", htmlContent);
        this.printObjectJsonString(result, response);
    }

    /**
     * 加载自动补全
     *
     * @param request
     * @param response
     */
    @RequestMapping("/loadAutoComplete")
    public void loadAutoComplete(HttpServletRequest request, HttpServletResponse response) {
        //如果自动补全的类型为2,那么获取到key之后进行判断过滤
        //String keyword = request.getParameter("keyword");
        SqlFilter sqlFilter = new SqlFilter(request);
        List<Map<String, Object>> list = this.commonService.findAutoCompleteDatas(sqlFilter);
        String json = JSON.toJSONString(list);
        response.setContentType("application/json");
        response.setHeader("Cache-Control", "no-store");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
        } catch (IOException e) {
            PlatLogUtil.printStackTrace(e);
        }
        pw.write(json.toLowerCase());
        pw.flush();
        pw.close();
    }

    /**
     * 通用分页请求处理
     *
     * @param request
     * @param response
     */
    @RequestMapping("/loadPage")
    public ModelAndView loadPage(HttpServletRequest request, HttpServletResponse response) {
        SqlFilter sqlFilter = new SqlFilter(request);
        String dataJavaInter = request.getParameter("dataJavaInter");
        String beanId = dataJavaInter.split("[.]")[0];
        String method = dataJavaInter.split("[.]")[1];
        Object serviceBean = PlatAppUtil.getBean(beanId);
        if (serviceBean != null) {
            Method invokeMethod;
            String pagepath = null;
            try {
                invokeMethod = serviceBean.getClass().getDeclaredMethod(method,
                        new Class[]{SqlFilter.class});
                pagepath = (String) invokeMethod.invoke(serviceBean,
                        new Object[]{sqlFilter});
            } catch (Exception e) {
                PlatLogUtil.printStackTrace(e);
            }
            return new ModelAndView(pagepath);
        } else {
            return null;
        }
    }

    /**
     * 通用单表存储请求
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdateSingle")
    public void saveOrUpdateSingle(HttpServletRequest request,
                                   HttpServletResponse response) {
        Map<String, Object> entity = PlatBeanUtil.getMapFromRequest(request);
        String tableName = request.getParameter("tableName");
        String busDesc = request.getParameter("busDesc");
        String pkName = commonService.findPrimaryKeyNames(tableName).get(0);
        String pkValue = (String) entity.get(pkName);
        entity = commonService.saveOrUpdate(tableName,
                entity, SysConstants.ID_GENERATOR_UUID, null);
        if (StringUtils.isNotEmpty(pkValue)) {
            sysLogService.saveBackLog(busDesc, SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为[" + pkValue + "]" + busDesc, request);
        } else {
            pkValue = (String) entity.get(pkName);
            sysLogService.saveBackLog(busDesc, SysLogService.OPER_TYPE_ADD,
                    "新增了ID为[" + pkValue + "]" + busDesc, request);
        }
        entity.put("success", true);
        this.printObjectJsonString(entity, response);
    }

    /**
     * 通用单表存储请求
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdateTree")
    public void saveOrUpdateTree(HttpServletRequest request,
                                 HttpServletResponse response) {
        Map<String, Object> entity = PlatBeanUtil.getMapFromRequest(request);
        String tableName = request.getParameter("tableName");
        String busDesc = request.getParameter("busDesc");
        String pkName = commonService.findPrimaryKeyNames(tableName).get(0);
        String pkValue = (String) entity.get(pkName);
        entity = commonService.saveOrUpdateTreeData(tableName,
                entity, SysConstants.ID_GENERATOR_UUID, null);
        if (StringUtils.isNotEmpty(pkValue)) {
            sysLogService.saveBackLog(busDesc, SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为[" + pkValue + "]" + busDesc, request);
        } else {
            pkValue = (String) entity.get(pkName);
            sysLogService.saveBackLog(busDesc, SysLogService.OPER_TYPE_ADD,
                    "新增了ID为[" + pkValue + "]" + busDesc, request);
        }
        entity.put("success", true);
        this.printObjectJsonString(entity, response);
    }

    /**
     * 跳转到单表信息表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goSingleTableForm")
    public ModelAndView goSingleTableForm(HttpServletRequest request) {
        String tableName = request.getParameter("tableName");
        String entityName = request.getParameter("entityName");
        String pkName = commonService.findPrimaryKeyNames(tableName).get(0);
        String pkValue = request.getParameter(pkName);
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> entity = null;
        if (StringUtils.isNotEmpty(pkValue)) {
            entity = this.commonService.getRecord(tableName
                    , new String[]{pkName}, new Object[]{pkValue});
        } else {
            entity = new HashMap<String, Object>();
        }
        request.setAttribute(entityName, entity);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }

    /**
     * 跳转到属性表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goTreeTableForm")
    public ModelAndView goTreeTableForm(HttpServletRequest request) {
        String tableName = request.getParameter("tableName");
        String entityName = request.getParameter("entityName");
        String busDesc = request.getParameter("busDesc");
        String pkName = commonService.findPrimaryKeyNames(tableName).get(0);
        String pkValue = request.getParameter(pkName);
        String parentIdName = pkName.substring(0, pkName.lastIndexOf("_")) + "_PARENTID";
        String parentIdValue = request.getParameter(parentIdName);
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> entity = null;
        if (StringUtils.isNotEmpty(pkValue)) {
            entity = this.commonService.getRecord(tableName
                    , new String[]{pkName}, new Object[]{pkValue});
            parentIdValue = (String) entity.get(parentIdName);
        }
        Map<String, Object> parentEntity = null;
        if ("0".equals(parentIdValue)) {
            parentEntity = new HashMap<String, Object>();
            parentEntity.put(pkName, parentIdValue);
            parentEntity.put(pkName.substring(0, pkName.lastIndexOf("_")) + "_NAME", busDesc + "树");
        } else {
            parentEntity = this.commonService.getRecord(tableName,
                    new String[]{pkName}, new Object[]{parentIdValue});
        }
        if (entity == null) {
            entity = new HashMap<String, Object>();
        }
        entity.put(pkName.substring(0, pkName.lastIndexOf("_")) + "_PARENTID",
                parentEntity.get(pkName));
        entity.put(pkName.substring(0, pkName.lastIndexOf("_")) + "_PARENTNAME",
                parentEntity.get(pkName.substring(0, pkName.lastIndexOf("_")) + "_NAME"));
        request.setAttribute(entityName, entity);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }

    /**
     * 通用删除单表数据按钮
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String tableName = request.getParameter("tableName");
        String busDesc = request.getParameter("busDesc");
        String pkName = commonService.findPrimaryKeyNames(tableName).get(0);
        String selectColValues = request.getParameter("selectColValues");
        commonService.deleteRecords(tableName, pkName, selectColValues.split(","));
        sysLogService.saveBackLog(busDesc, SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的" + busDesc, request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 通用删除树形表数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "delTreeNode")
    public void delTreeNode(HttpServletRequest request,
                            HttpServletResponse response) {
        String tableName = request.getParameter("tableName");
        String busDesc = request.getParameter("busDesc");
        String treeNodeId = request.getParameter("treeNodeId");
        commonService.deleteTreeDataCascadeChild(treeNodeId, tableName);
        sysLogService.saveBackLog(busDesc, SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + treeNodeId + "]的" + busDesc, request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }
}
