package com.housoo.platform.core.model;

import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatLogUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 描述 SQL过滤器
 *
 * @author housoo
 * @created 2017年1月25日 下午1:10:55
 */
public class SqlFilter {
    /**
     * 过滤类型查询参数
     */
    public static final int FILTER_TYPE_QUERY = 1;
    /**
     * 过滤类型:排序参数
     */
    public static final int FILTER_TYPE_ORDER = 2;
    /**
     * 过滤类型:分组参数
     */
    public static final int FILTER_TYPE_GROUP = 3;
    /**
     * 属性null
     */
    private HttpServletRequest request = null;

    /**
     * 分页工具类
     */
    private PagingBean pagingBean = null;
    /**
     * 查询参数
     */
    private Map<String, Object> queryParams = new HashMap<String, Object>();
    /**
     * 排序参数
     */
    private Map<String, String> orderParams = new LinkedHashMap<String, String>();
    /**
     * 分组排序参数
     */
    private Map<String, String> groupParams = new LinkedHashMap<String, String>();


    /**
     * 通用查询对象
     * 1、参数以Q_打头的将会纳入到查询条件中
     * 2、参数以O_打头的将会作为排序条件
     * 3、参数以G_打头的将会作为分组条件
     *
     * @param sqlParams
     */
    public SqlFilter(Map<String, Object> sqlParams) {
        constructFilter(sqlParams);
    }

    /**
     * 通用查询对象
     * 1、参数以Q_打头的将会纳入到查询条件中
     * 2、参数以O_打头的将会作为排序条件
     * 3、参数以G_打头的将会作为分组条件
     *
     * @param request
     */
    public SqlFilter(HttpServletRequest request) {
        this.request = request;
        Map<String, Object> sqlParams = PlatBeanUtil.getMapFromRequest(request);
        this.constructFilter(sqlParams);
    }

    /**
     * 添加查询参数并且重构sql语句
     *
     * @param paramName
     * @param paramValue
     * @param sql
     * @param params
     */
    public static void addQueryParam(String paramName, String paramValue, StringBuffer sql, List<Object> params) {
        // 获取到字段的名称
        String fieldName = paramName.substring(paramName.indexOf("_") + 1,
                paramName.lastIndexOf("_"));
        // 获取到字段的查询条件
        String condition = paramName.substring(paramName.lastIndexOf("_") + 1,
                paramName.length());
        String[] legalOperType = new String[]{"EQ", "NEQ", "LT", "GT", "LE", "GE",
                "LIKE", "LLIKE", "RLIKE", "IN",
                "NULL", "NOTNULL", "APPEND", "NOTIN"};
        Set<String> legalOperSet = new HashSet<String>(Arrays.asList(legalOperType));
        if ("EQ".equals(condition)) {
            condition = "=";
        } else if ("NEQ".equals(condition)) {
            condition = "!=";
        } else if ("LT".equals(condition)) {
            condition = "<";
        } else if ("GT".equals(condition)) {
            condition = ">";
        } else if ("LE".equals(condition)) {
            condition = "<=";
        } else if ("GE".equals(condition)) {
            condition = ">=";
        } else if (!legalOperSet.contains(condition)) {
            condition = null;
        }
        String dbType = PlatAppUtil.getDbType();
        if (StringUtils.isNotEmpty(condition)) {
            sql.append(" and ").append(fieldName);
            if ("LIKE".equals(condition)) {
                if ("ORACLE".equals(dbType) || "SQLSERVER".equals(dbType)) {
                    if (paramValue.contains("_")) {
                        sql.append(" LIKE ? ESCAPE '\\' ");
                        paramValue = paramValue.replace("_", "\\_");
                    } else {
                        sql.append(" LIKE ? ");
                    }
                } else if ("MYSQL".equals(dbType)) {
                    sql.append(" LIKE ? ");
                }
                params.add("%" + paramValue + "%");
            } else if ("LLIKE".equals(condition)) {
                if ("ORACLE".equals(dbType) || "SQLSERVER".equals(dbType)) {
                    if (paramValue.contains("_")) {
                        sql.append(" LIKE ? ESCAPE '\\' ");
                        paramValue = paramValue.replace("_", "\\_");
                    } else {
                        sql.append(" LIKE ? ");
                    }
                } else if ("MYSQL".equals(dbType)) {
                    sql.append(" LIKE ? ");
                }
                params.add("%" + paramValue);
            } else if ("RLIKE".equals(condition)) {
                if ("ORACLE".equals(dbType) || "SQLSERVER".equals(dbType)) {
                    if (paramValue.contains("_")) {
                        sql.append(" LIKE ? ESCAPE '\\' ");
                        paramValue = paramValue.replace("_", "\\_");
                    } else {
                        sql.append(" LIKE ? ");
                    }
                } else if ("MYSQL".equals(dbType)) {
                    sql.append(" LIKE ? ");
                }
                params.add(paramValue + "%");
            } else if ("IN".equals(condition)) {
                sql.append(" in (");
                String[] newValues = paramValue.split(",");
                for (String newValue : newValues) {
                    sql.append("?,");
                    params.add(newValue);
                }
                sql.deleteCharAt(sql.length() - 1);
                sql.append(") ");
            } else if ("NOTIN".equals(condition)) {
                sql.append(" not in (");
                String[] newValues = paramValue.split(",");
                for (String newValue : newValues) {
                    sql.append("?,");
                    params.add(newValue);
                }
                sql.deleteCharAt(sql.length() - 1);
                sql.append(") ");
            } else if ("NULL".equals(condition)) {
                sql.append(" is null ");
            } else if ("NOTNULL".equals(condition)) {
                sql.append(" is not null ");
            } else if ("APPEND".equals(condition)) {
                sql.append(" ").append(paramValue).append(" ");
            } else {
                sql.append(" ").append(condition).append("?").append(" ");
                params.add(paramValue);
            }
        }

    }

    /**
     * 添加过滤参数到filter
     *
     * @param paramName  参数名称
     * @param paramValue 参数值
     * @param type       过滤类型 1:查询 2:排序 3:分组
     */
    public void addFilter(String paramName, String paramValue, int type) {
        String[] fieldInfo = paramName.split("[_]");
        try {
            if (fieldInfo != null && fieldInfo.length > 0) {
                switch (type) {
                    case SqlFilter.FILTER_TYPE_QUERY:
                        queryParams.put(paramName, paramValue);
                        break;
                    case SqlFilter.FILTER_TYPE_ORDER:
                        orderParams.put(paramName, paramValue);
                        break;
                    case SqlFilter.FILTER_TYPE_GROUP:
                        groupParams.put(paramName, paramValue);
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            PlatLogUtil.printStackTrace(e);
        }
    }

    /**
     * 构造过滤器
     *
     * @param sqlParams
     */
    private void constructFilter(Map<String, Object> sqlParams) {
        Iterator it = sqlParams.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
            String fieldName = entry.getKey();
            Object val = entry.getValue();
            if (val != null && StringUtils.isNotEmpty(val.toString())) {
                if (fieldName.startsWith("Q_")) {
                    //获取最后一个操作符
                    this.addFilter(fieldName, val.toString().trim(), 1);
                } else if (fieldName.startsWith("O_")) {
                    this.addFilter(fieldName, val.toString().trim(), 2);
                } else if (fieldName.startsWith("G_")) {
                    this.addFilter(fieldName, val.toString().trim(), 3);
                }
            }
        }
        // 取得分页的信息
        Integer start = 0;
        Integer limit = PagingBean.DEFAULT_PAGE_SIZE;
        // 获取当前页
        int currentPage = 1;
        String s_start = (String) sqlParams.get("start");
        String s_limit = (String) sqlParams.get("limit");
        if (StringUtils.isNotEmpty((String) sqlParams.get("page"))) {
            // 获取当前页
            currentPage = Integer.parseInt((String) sqlParams.get("page"));
            // 计算开始记录
            // 获取页数限制
            int rows = Integer.parseInt((String) sqlParams.get("rows"));
            int startRecord = (currentPage - 1) * rows;
            s_start = String.valueOf(startRecord);
        }
        if (sqlParams.get("rows") != null) {
            s_limit = (String) sqlParams.get("rows");
        }
        if (sqlParams.get("limit") != null) {
            s_limit = sqlParams.get("limit").toString();
        }
        if (StringUtils.isNotEmpty(s_start)) {
            start = Integer.valueOf(s_start);
        }
        if (StringUtils.isNotEmpty(s_limit)) {
            limit = Integer.valueOf(s_limit);
        }
        this.pagingBean = new PagingBean(start, limit);
        this.pagingBean.setCurrentPage(currentPage);
    }

    /**
     * @return the pagingBean
     */
    public PagingBean getPagingBean() {
        return pagingBean;
    }

    /**
     * @param pagingBean the pagingBean to set
     */
    public void setPagingBean(PagingBean pagingBean) {
        this.pagingBean = pagingBean;
    }

    /**
     * @return the queryParams
     */
    public Map<String, Object> getQueryParams() {
        return queryParams;
    }

    /**
     * @param queryParams the queryParams to set
     */
    public void setQueryParams(Map<String, Object> queryParams) {
        this.queryParams = queryParams;
    }

    /**
     * @return the orderParams
     */
    public Map<String, String> getOrderParams() {
        return orderParams;
    }

    /**
     * @param orderParams the orderParams to set
     */
    public void setOrderParams(Map<String, String> orderParams) {
        this.orderParams = orderParams;
    }

    /**
     * @return the groupParams
     */
    public Map<String, String> getGroupParams() {
        return groupParams;
    }

    /**
     * @param groupParams the groupParams to set
     */
    public void setGroupParams(Map<String, String> groupParams) {
        this.groupParams = groupParams;
    }

    /**
     * @return the request
     */
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * @param request the request to set
     */
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

}
