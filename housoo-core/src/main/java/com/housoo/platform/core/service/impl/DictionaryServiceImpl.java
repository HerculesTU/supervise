package com.housoo.platform.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatDbUtil;
import com.housoo.platform.core.util.PlatStringUtil;
import com.housoo.platform.core.dao.DictionaryDao;
import com.housoo.platform.core.service.DicAttachService;
import com.housoo.platform.core.service.DictionaryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述
 *
 * @author 高飞
 * @created 2017年2月1日 下午6:02:34
 */
@Service("dictionaryService")
public class DictionaryServiceImpl extends BaseServiceImpl implements
        DictionaryService {

    /**
     * 所引入的dao
     */
    @Resource
    private DictionaryDao dao;
    /**
     *
     */
    @Resource
    private DicAttachService dicAttachService;

    /**
     *
     */
    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 根据sqlFilter获取到列表数据
     *
     * @param filter
     * @param fieldInfo
     * @return
     */
    @Override
    public List<Map<String, Object>> findByList(SqlFilter filter, Map<String, Object> fieldInfo) {
        String dicTypeCode = filter.getRequest().getParameter("Q_T.DIC_DICTYPE_CODE_EQ");
        if (StringUtils.isNotEmpty(dicTypeCode)) {
            filter.addFilter("O_T.DIC_SN", "DESC", SqlFilter.FILTER_TYPE_ORDER);
        } else {
            filter.addFilter("O_T.DIC_CREATETIME", "DESC", SqlFilter.FILTER_TYPE_ORDER);
        }
        List<Object> params = new ArrayList<Object>();
        StringBuffer sql = new StringBuffer("select D.DICTYPE_NAME,T.DIC_ID,T.DIC_NAME");
        sql.append(",T.DIC_VALUE,T.DIC_SN,T.DIC_DICTYPE_CODE FROM ");
        sql.append("PLAT_SYSTEM_DICTIONARY T LEFT JOIN PLAT_SYSTEM_DICTYPE D ");
        sql.append("ON T.DIC_DICTYPE_CODE=D.DICTYPE_CODE");
        String exeSql = dao.getQuerySql(filter, sql.toString(), params);
        List<Map<String, Object>> list = dao.findBySql(exeSql,
                params.toArray(), filter.getPagingBean());
        return list;
    }

    /**
     * 根据类别编码获取最大排序
     *
     * @param typeCode
     * @return
     */
    @Override
    public int getMaxSn(String typeCode) {
        return dao.getMaxSn(typeCode);
    }

    /**
     * 判断一个字典值是否存在
     *
     * @param dicId
     * @param typeCode
     * @param dicValue
     * @return
     */
    @Override
    public boolean isExistsDic(String dicId, String typeCode, String dicValue) {
        return dao.isExistsDic(dicId, typeCode, dicValue);
    }

    /**
     * 描述 更新排序字段
     *
     * @param dicIds
     * @author housoo
     * @created 2014年10月3日 下午12:54:23
     */
    @Override
    public void updateSn(String[] dicIds) {
        dao.updateSn(dicIds);
    }

    /**
     * 描述 根据查询参数JSON获取数据列表
     *
     * @param queryParamsJson
     * @return
     * @created 2016年3月27日 上午11:16:25
     */
    @Override
    public List<Map<String, Object>> findList(String queryParamsJson) {
        Map<String, Object> queryParams = JSON.parseObject(queryParamsJson, Map.class);
        //获取字典类别编码
        String typeCode = (String) queryParams.get("TYPE_CODE");
        //需要除去字典编码
        String notDicCode = (String) queryParams.get("NOT_DIC_CODE");
        //获取排序方式
        String orderType = (String) queryParams.get("ORDER_TYPE");
        StringBuffer sql = new StringBuffer("SELECT T.DIC_VALUE AS VALUE,T.DIC_NAME AS LABEL ");
        sql.append(" FROM PLAT_SYSTEM_DICTIONARY T");
        sql.append(" WHERE T.DIC_DICTYPE_CODE=? ");
        if (StringUtils.isNotEmpty(notDicCode)) {
            sql.append(" AND T.DIC_VALUE NOT IN ").append(PlatStringUtil.getValueArray(notDicCode));
        }
        sql.append(" ORDER BY T.DIC_SN ").append(orderType);
        return dao.findBySql(sql.toString(), new Object[]{typeCode}, null);
    }

    /**
     * 获取UI类别列表数据
     *
     * @param queryParamJson
     * @return
     */
    @Override
    public List<Map<String, Object>> findUiTypeList(String queryParamJson) {
        StringBuffer sql = new StringBuffer("SELECT T.DIC_VALUE AS VALUE,T.DIC_NAME AS LABEL ");
        sql.append(" FROM PLAT_SYSTEM_DICTIONARY T");
        sql.append(" WHERE T.DIC_DICTYPE_CODE=? ");
        sql.append(" ORDER BY T.DIC_SN ").append("ASC");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(), new Object[]{"CONTROL_TYPE"}, null);
        for (Map<String, Object> map : list) {
            map.put("GROUP_FONT", "fa fa-asterisk");
        }
        Map<String, Object> allType = new HashMap<String, Object>();
        allType.put("GROUP_FONT", "fa fa-home");
        allType.put("LABEL", "全部类别");
        allType.put("VALUE", "0");
        list.add(0, allType);
        return list;
    }

    /**
     * 获取资源库列表数据
     *
     * @param queryParamJson
     * @return
     */
    @Override
    public List<Map<String, Object>> findResLibaryList(String queryParamJson) {
        StringBuffer sql = new StringBuffer("SELECT T.DIC_VALUE AS VALUE,T.DIC_NAME AS LABEL ");
        sql.append(" FROM PLAT_SYSTEM_DICTIONARY T");
        sql.append(" WHERE T.DIC_DICTYPE_CODE=? ");
        sql.append(" ORDER BY T.DIC_SN ").append("ASC");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(), new Object[]{"ResLibraryType"}, null);
        for (Map<String, Object> map : list) {
            map.put("GROUP_FONT", "fa fa-asterisk");
        }
        Map<String, Object> allType = new HashMap<String, Object>();
        allType.put("GROUP_FONT", "fa fa-home");
        allType.put("LABEL", "全部类型");
        allType.put("VALUE", "0");
        list.add(0, allType);
        return list;
    }

    /**
     * 初始化全国街道数据
     */
    @Override
    public void initCountryStree() {
        StringBuffer findTypeListSql = new StringBuffer("select T.DICTYPE_CODE from PLAT_SYSTEM_DICTYPE T");
        findTypeListSql.append(" WHERE T.DICTYPE_PATH  LIKE "
                + "'0.402848a55b411950015b411950260000.402848a55b411950015b4119ec490001.%'");
        findTypeListSql.append(" ORDER BY T.DICTYPE_CREATETIME ASC");
        List<Map<String, Object>> typeList = dao.findBySql(findTypeListSql.toString(),
                null, null);
        StringBuffer sql = new StringBuffer("SELECT * FROM T_MSJW_SYSTEM_DICTIONARY");
        sql.append(" T WHERE T.TYPE_CODE=? ORDER BY T.DIC_SN ASC");
        for (Map<String, Object> type : typeList) {
            String DICTYPE_CODE = (String) type.get("DICTYPE_CODE");
            List<Map<String, Object>> dicList = dao.findBySql(sql.toString(),
                    new Object[]{DICTYPE_CODE}, null);
            for (Map<String, Object> dic : dicList) {
                Map<String, Object> newDic = new HashMap<String, Object>();
                newDic.put("DIC_NAME", dic.get("DIC_NAME"));
                newDic.put("DIC_VALUE", dic.get("DIC_CODE"));
                newDic.put("DIC_SN", dic.get("DIC_SN"));
                newDic.put("DIC_DICTYPE_CODE", DICTYPE_CODE);
                dao.saveOrUpdate("PLAT_SYSTEM_DICTIONARY", newDic,
                        SysConstants.ID_GENERATOR_UUID, null);
            }
        }
    }

    /**
     * 获取字典数据根据类别编码和排序方式
     *
     * @param typeCode
     * @param orderType
     * @return
     */
    @Override
    public List<Map<String, Object>> findList(String typeCode, String orderType) {
        StringBuffer sql = new StringBuffer("SELECT * FROM ");
        sql.append("PLAT_SYSTEM_DICTIONARY T WHERE T.DIC_DICTYPE_CODE=? ");
        sql.append(" ORDER BY T.DIC_SN ").append(orderType);
        List<Map<String, Object>> list = dao.findBySql(sql.toString(),
                new Object[]{typeCode}, null);
        if (list != null) {
            for (Map<String, Object> map : list) {
                dicAttachService.setDicAttachValues(map);
            }
        }
        return list;
    }

    /**
     * 获取客户端验证规则
     *
     * @return
     */
    @Override
    public Map<String, String[]> getJsValidRules() {
        List<Map<String, Object>> ruleList = this.findList("VALID_RULE", "ASC");
        Map<String, String[]> map = new HashMap<String, String[]>();
        for (Map<String, Object> rule : ruleList) {
            String DIC_VALUE = (String) rule.get("DIC_VALUE");
            String ruleKey = DIC_VALUE.replace(";", "");
            String DIC_NAME = (String) rule.get("DIC_NAME");
            String RULE = (String) rule.get("RULE");
            map.put(ruleKey, new String[]{RULE, DIC_NAME});
        }
        return map;
    }

    /**
     * 根据类别和名称获取值
     */
    @Override
    public Map<String, Object> getDicValue(String dicTypeCode, String dicName) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT T.DIC_VALUE FROM PLAT_SYSTEM_DICTIONARY T ");
        sql.append(" WHERE T.DIC_DICTYPE_CODE=? AND T.DIC_NAME=? ");
        Map<String, Object> dictionMap = dao.getBySql(sql.toString(), new Object[]{dicTypeCode,
                dicName});
        return dictionMap;
    }

    /**
     * 获取字典的附加值
     *
     * @param dicValue
     * @param dicTypeCode
     * @param attachKey
     * @return
     */
    @Override
    public String getAttachValue(String dicValue, String dicTypeCode, String attachKey) {
        Map<String, Object> dic = dao.getRecord("PLAT_SYSTEM_DICTIONARY",
                new String[]{"DIC_VALUE", "DIC_DICTYPE_CODE"}, new Object[]{dicValue, dicTypeCode});
        String dicId = (String) dic.get("DIC_ID");
        Map<String, Object> attach = dao.getRecord("PLAT_SYSTEM_DICATTACH",
                new String[]{"DICATTACH_KEY", "DICATTACH_DICID"}, new Object[]{attachKey,
                        dicId});
        String DICATTACH_VALUE = (String) attach.get("DICATTACH_VALUE");
        return DICATTACH_VALUE;
    }

    /**
     * 获取字典的值
     *
     * @param typeCode
     * @param dicName
     * @return
     */
    @Override
    public String getDictionaryValue(String typeCode, String dicName) {
        return dao.getDictionaryValue(typeCode, dicName);
    }

    /**
     * 获取行政区划列表数据
     *
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> findAdminDivision(String params) {
        if (StringUtils.isNotEmpty(params) && params.split(",").length == 2) {
            String code = params.split(",")[0];
            String type = params.split(",")[1];
            if (StringUtils.isNotEmpty(code)) {
                if ("1".equals(type)) {
                    String sql = PlatDbUtil.getDiskSqlContent("system/dictionary/001", null);
                    return this.findBySql(sql, new Object[]{"%0000"}, null);
                } else if ("2".equals(type)) {
                    String sql = PlatDbUtil.getDiskSqlContent("system/dictionary/002", null);
                    //截取前面两位
                    String preCode = code.substring(0, 2);
                    Map<String, Object> province = this.getRecord("PLAT_SYSTEM_DICTIONARY",
                            new String[]{"DIC_VALUE", "DIC_DICTYPE_CODE"}, new Object[]{code, "ADMIN_DIVISION"});
                    String dicName = (String) province.get("DIC_NAME");
                    List<Map<String, Object>> list = this.findBySql(sql, new Object[]{"%00", preCode + "%", code}, null);
                    for (Map<String, Object> map : list) {
                        String LABEL = (String) map.get("LABEL");
                        if (LABEL.contains(dicName)) {
                            LABEL = LABEL.replace(dicName, "");
                        }
                        map.put("LABEL", LABEL);
                    }
                    return list;
                } else if ("3".equals(type)) {
                    String sql = PlatDbUtil.getDiskSqlContent("system/dictionary/003", null);
                    //截取前面两位
                    String preCode = code.substring(0, 4);
                    Map<String, Object> city = this.getRecord("PLAT_SYSTEM_DICTIONARY",
                            new String[]{"DIC_VALUE", "DIC_DICTYPE_CODE"}, new Object[]{code, "ADMIN_DIVISION"});
                    if (city != null) {
                        String dicName = (String) city.get("DIC_NAME");
                        List<Map<String, Object>> list = this.findBySql(sql, new Object[]{"%00", preCode + "%"}, null);
                        for (Map<String, Object> map : list) {
                            String LABEL = (String) map.get("LABEL");
                            if (LABEL.contains(dicName)) {
                                LABEL = LABEL.replace(dicName, "");
                            }
                            map.put("LABEL", LABEL);
                        }
                        return list;
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
