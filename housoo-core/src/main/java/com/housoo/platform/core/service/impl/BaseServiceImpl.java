package com.housoo.platform.core.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.model.PagingBean;
import com.housoo.platform.core.model.TableColumn;
import com.housoo.platform.core.model.TableInfo;
import com.housoo.platform.core.service.BaseService;
import com.housoo.platform.core.util.PlatBeanUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 描述
 *
 * @author housoo
 * @created 2017年1月8日 下午12:17:51
 */
public abstract class BaseServiceImpl implements BaseService {
    /**
     * 获取dao对象
     *
     * @return
     */
    protected abstract BaseDao getDao();

    /**
     * 描述 根据sql语句和查询参数获取单条数据库信息
     *
     * @param sql
     * @param params
     * @return
     * @例子: Map<String   ,   Object> map = dao.getBySql("SELECT * FROM T_SYSTEM_USER U WHERE U.USERID=?",new Object[]{"1"});
     * @created 2016年2月20日 上午9:32:12
     */
    @Override
    public Map<String, Object> getBySql(String sql, Object[] params) {
        return getDao().getBySql(sql, params);
    }

    /**
     * 描述 根据表名、列名、列的值获取数据库单条记录
     *
     * @param tableName
     * @param colNames
     * @param colValues
     * @return
     * @例子 Map<String   ,   Object> map = user2Service.getRecord("JBPM6_TASK",
     * new String[]{"TASK_ID"},new Object[]{"402881c352ee10e20152ee391cea001b"});
     * @created 2016年2月20日 上午11:55:59
     */
    @Override
    public Map<String, Object> getRecord(String tableName, String[] colNames, Object[] colValues) {
        return getDao().getRecord(tableName, colNames, colValues);
    }

    /**
     * 描述 根据数据库表名称获取主键的名称
     *
     * @param tableName
     * @return
     * @created 2016年2月20日 下午1:04:29
     */
    @Override
    public List<String> findPrimaryKeyNames(String tableName) {
        return getDao().findPrimaryKeyNames(tableName);
    }

    /**
     * 描述 新增或者修改单表记录,单表必须存在主键,如果表记录不存在则进行新增操作,存在进行修改操作
     *
     * @param tableName:表名称
     * @param colValues:列的MAP
     * @param idGenerator                                主键生成策略,可传值说明
     *                                                   1:使用UUID 2:自增长(面向MYSQL数据库) 3:分配模式 4:序列(面向ORACLE数据库)
     * @param seqName:只针对oracle的序列存储,为序列名称,并且主键生成方式为序列模式
     * @return
     * @created 2016年2月21日 下午6:14:52
     */
    @Override
    public Map<String, Object> saveOrUpdate(String tableName, Map<String, Object>
            colValues, int idGenerator, String seqName) {
        return getDao().saveOrUpdate(tableName, colValues, idGenerator, seqName);
    }

    /**
     * 描述 判断数据库中是否存在记录
     *
     * @param tableName
     * @param colValues
     * @return
     * @created 2016年2月20日 下午1:53:42
     */
    @Override
    public boolean isExists(String tableName, Map<String, Object> colValues) {
        return getDao().isExists(tableName, colValues);
    }

    /**
     * 描述 根据表名称获取列的名称列表
     *
     * @param tableName
     * @return
     * @created 2016年2月21日 下午4:37:23
     */
    @Override
    public List<String> findColumnName(String tableName) {
        return getDao().findColumnName(tableName);
    }

    /**
     * 描述 删除单表数据库记录信息
     *
     * @param tableName:表名称
     * @param colNames:列名数组
     * @param colValues:列值
     * @created 2016年2月22日 上午9:15:04
     */
    @Override
    public void deleteRecord(String tableName, String[] colNames, Object[] colValues) {
        getDao().deleteRecord(tableName, colNames, colValues);
    }

    /**
     * 描述 根据sql语句获取记录列表
     *
     * @param sql:sql语句
     * @param colValues:查询的值参数
     * @param pb:分页对象,如果不传,那么不进行分页
     * @return
     * @created 2016年2月22日 上午9:37:35
     */
    @Override
    public List<Map<String, Object>> findBySql(String sql, Object[] colValues, PagingBean pb) {
        return getDao().findBySql(sql, colValues, pb);
    }

    /**
     * 描述 保存树形结构表数据
     *
     * @param tableName
     * @param colValues
     * @param idGenerator
     * @param seqName
     * @return
     * @created 2016年2月27日 上午11:32:44
     */
    @Override
    public Map<String, Object> saveOrUpdateTreeData(String tableName,
                                                    Map<String, Object> colValues, int idGenerator, String seqName) {
        //获取私有主键名称
        String primaryKeyName = this.findPrimaryKeyNames(tableName).get(0);
        String entityName = tableName.substring(tableName.lastIndexOf("_") + 1, tableName.length());
        //获取父亲ID名称
        String PARENT_ID_NAME = entityName + "_PARENTID";
        String PARENT_IDVALUE = colValues.get(PARENT_ID_NAME).toString();
        //获取树形等级的命名
        String TREE_LEVEL_NAME = entityName + "_LEVEL";
        // 获取旧的主键值
        String pkValue = null;
        if (colValues.get(primaryKeyName) != null) {
            pkValue = colValues.get(primaryKeyName).toString();
        }
        //定义旧的父亲ID
        String OLD_PARENTID = null;
        if (StringUtils.isNotEmpty(pkValue)) {
            Map<String, Object> oldCoValues = getDao().getRecord(tableName,
                    new String[]{primaryKeyName}, new Object[]{pkValue});
            if (oldCoValues != null) {
                OLD_PARENTID = (String) oldCoValues.get(PARENT_ID_NAME);
            }
        }
        colValues = getDao().saveOrUpdateTreeData(tableName, colValues, idGenerator, seqName);
        if (StringUtils.isNotEmpty(pkValue) && StringUtils.isNotEmpty(OLD_PARENTID)) {
            if (!OLD_PARENTID.equals(PARENT_IDVALUE)) {
                String dragTreeNodeId = pkValue;
                int targetNodeLevel = 0;
                Map<String, Object> newParent = null;
                if (!"0".equals(PARENT_IDVALUE)) {
                    newParent = this.getRecord(tableName,
                            new String[]{primaryKeyName}, new Object[]{PARENT_IDVALUE});
                    targetNodeLevel = Integer.parseInt(newParent.get(TREE_LEVEL_NAME).toString());
                }
                int dragTreeNodeNewLevel = targetNodeLevel + 1;
                this.updateTreeSn(tableName, dragTreeNodeId,
                        dragTreeNodeNewLevel, PARENT_IDVALUE, targetNodeLevel, "inner");

            }
        }
        return colValues;
    }

    /**
     * 描述 获取树形结构表的最大排序值
     *
     * @param tableName
     * @return
     * @created 2016年2月27日 上午11:35:41
     */
    @Override
    public int getMaxTreeSortSn(String tableName, String treeSnName) {
        return getDao().getMaxTreeSortSn(tableName, treeSnName);
    }

    /**
     * 获取数据库表信息列表
     *
     * @return
     */
    @Override
    public List<TableInfo> findDbTables() {
        return getDao().findDbTables();
    }

    /**
     * 获取树形的json数据
     *
     * @param request
     * @return
     */
    @Override
    public String getTreeJson(HttpServletRequest request) {
        Map<String, Object> params = PlatBeanUtil.getMapFromRequest(request);
        return getDao().getTreeJson(params);
    }

    /**
     * 更新树形排序字段
     *
     * @param tableName
     * @param dragTreeNodeId
     * @param dragTreeNodeNewLevel
     * @param targetNodeId
     * @param targetNodeLevel
     */
    @Override
    public void updateTreeSn(String tableName, String dragTreeNodeId, int dragTreeNodeNewLevel, String targetNodeId,
                             int targetNodeLevel, String moveType) {
        getDao().updateTreeSn(tableName, dragTreeNodeId,
                dragTreeNodeNewLevel, targetNodeId, targetNodeLevel, moveType);
    }

    /**
     * 根据表名称和字段名称删除记录
     *
     * @param tableName
     * @param fieldName
     * @param colValues
     */
    @Override
    public void deleteRecords(String tableName, String fieldName, String[] colValues) {
        getDao().deleteRecords(tableName, fieldName, colValues);
    }

    /**
     * 根据表名称和字段名称获取注释
     *
     * @param tableName
     * @param colName
     * @return
     */
    @Override
    public String getColComment(String tableName, String colName) {
        return getDao().getColComment(tableName, colName);
    }

    /**
     * 获取树形的数据
     *
     * @param params
     * @return
     */
    @Override
    public Object getTreeData(Map<String, Object> params) {
        return getDao().getTreeData(params);
    }

    /**
     * 根据表名称获取数据库列的集合列表
     *
     * @param tableName
     * @return
     */
    @Override
    public List<TableColumn> findTableColumnByTableName(String tableName) {
        return getDao().findTableColumnByTableName(tableName);
    }

    /**
     * 获取表字段记录,按拼接返回结果
     *
     * @param tableName        表名
     * @param targetFieldName  目标返回字段
     * @param queryFieldNames  查询参数字段名
     * @param queryFieldValues 查询参数字段值
     * @param splitSymbol      分割符
     * @return
     */
    @Override
    public String getTableFieldValues(String tableName, String targetFieldName,
                                      String[] queryFieldNames, Object[] queryFieldValues, String splitSymbol) {
        return getDao().getTableFieldValues(tableName, targetFieldName, queryFieldNames,
                queryFieldValues, splitSymbol);
    }

    /**
     * 获取非键值对的结果列表
     *
     * @param sql
     * @param colValues
     * @param pb
     * @return
     */
    @Override
    public List<List<String>> findListBySql(String sql, Object[] colValues, PagingBean pb) {
        return getDao().findListBySql(sql, colValues, pb);
    }
}
