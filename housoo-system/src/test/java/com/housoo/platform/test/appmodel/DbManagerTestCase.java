package com.housoo.platform.test.appmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.model.TableColumn;
import com.housoo.platform.core.model.TableInfo;
import com.housoo.platform.test.BaseTestCase;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.util.PlatDbUtil;
import com.housoo.platform.core.service.DbManagerService;

/**
 * @author 胡裕
 *
 * 
 */
public class DbManagerTestCase extends BaseTestCase {
    /**
     * 
     */
    @Resource
    private DbManagerService dbManagerService;
    
    /**
     * 
     */
    @Test
    public void saveMySqlTableDatas(){
        List<TableInfo> tableList = dbManagerService.findDbTables();
        Map<String,List<Map<String,Object>>> dataMap = new HashMap<String,List<Map<String,Object>>>();
        for(TableInfo tableInfo:tableList){
            String tableName = tableInfo.getTableName();
            StringBuffer sql = new StringBuffer("SELECT * FROM ");
            sql.append(tableName);
            List<Map<String,Object>> list = dbManagerService.findBySql(sql.toString(), null, null);
            dataMap.put(tableName, list);
        }
        Iterator it = dataMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Entry) it.next();
            String tableName = (String) entry.getKey();
            Object val = entry.getValue();
            dbManagerService.saveMySqlTableDatas((List<Map<String, Object>>) val, tableName);
        }
    }
    
    /**
     * 
     */
    @Test
    public void saveSqlServerTableDatas(){
        List<TableInfo> tableList = dbManagerService.findDbTables();
        Map<String,List<Map<String,Object>>> dataMap = new HashMap<String,List<Map<String,Object>>>();
        for(TableInfo tableInfo:tableList){
            String tableName = tableInfo.getTableName();
            StringBuffer sql = new StringBuffer("SELECT * FROM ");
            sql.append(tableName);
            List<Map<String,Object>> list = dbManagerService.findBySql(sql.toString(), null, null);
            dataMap.put(tableName, list);
        }
        Iterator it = dataMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Entry) it.next();
            String tableName = (String) entry.getKey();
            Object val = entry.getValue();
            dbManagerService.saveSqlServerTableDatas((List<Map<String, Object>>) val, tableName);
        }
    }
    
    /**
     * 
     */
    @Test
    public void createMySqlTable(){
        List<TableInfo> tableList = dbManagerService.findDbTables();
        List<String> allSqlList = new ArrayList<String>();
        for(TableInfo tableInfo:tableList){
            List<TableColumn> columnList= dbManagerService.findTableColumnByTableName(tableInfo.getTableName());
            List<String> sqlList = dbManagerService.getMysqlCreateTableAllSql(tableInfo.getTableName(), 
                    tableInfo.getTableComments(), columnList);
            allSqlList.addAll(sqlList);
            
        }
        dbManagerService.createMySqlTable(allSqlList);
    }
    
    /**
     * 
     */
    @Test
    public void createSqlServerTable(){
        List<TableInfo> tableList = dbManagerService.findDbTables();
        List<String> allSqlList = new ArrayList<String>();
        for(TableInfo tableInfo:tableList){
            List<TableColumn> columnList= dbManagerService.findTableColumnByTableName(tableInfo.getTableName());
            List<String> sqlList = dbManagerService.getSqlServerCreateTableAllSql(tableInfo.getTableName(), 
                    tableInfo.getTableComments(), columnList);
            allSqlList.addAll(sqlList);
            
        }
        dbManagerService.createSqlServerTable(allSqlList);
    }
    
    /**
     * 将oracle转换成mysql
     */
    @Test
    public void changeOracleToMySQL(){
        StringBuffer sql = new StringBuffer("SELECT cu.table_name,cu.constraint_name,cu.column_name");
        sql.append(" FROM user_constraints AU LEFT JOIN user_cons_columns CU ");
        sql.append("ON cu.constraint_name=au.constraint_name ");
        sql.append("AND AU.table_name=CU.table_name WHERE  AU.constraint_type='U'");
        sql.append(" ORDER BY CU.table_name ASC");
        List<Map<String,Object>> list = this.dbManagerService.findBySql(sql.toString(), null, null);
        for(Map<String,Object> map:list){
            String table_name = (String) map.get("TABLE_NAME");
            String constrainName = (String) map.get("CONSTRAINT_NAME");
            String column_name = (String) map.get("COLUMN_NAME");
            StringBuffer alterSql = new StringBuffer("alter table ");
            alterSql.append(table_name).append(" add UNIQUE ");
            alterSql.append(constrainName).append("(").append(column_name).append(");");
            System.out.println(alterSql.toString());
        }
        //第二步: 执行批量修改字段类型SQL
        //SELECT CONCAT('alter table ',T.TABLE_NAME,' modify column ',T.COLUMN_NAME,' timestamp;') FROM 
        // information_schema.COLUMNS T WHERE T.TABLE_SCHEMA='STOOGES' AND T.DATA_TYPE='datetime';
        //第三步: 执行批量修改timestamp更新时不进行更新
        //SELECT CONCAT('alter table ',T.TABLE_NAME,' CHANGE ',T.COLUMN_NAME,' ',T.COLUMN_NAME,' TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;') FROM 
        //information_schema.COLUMNS T WHERE T.TABLE_SCHEMA='STOOGES' AND T.DATA_TYPE='timestamp';
        //第四步: 执行批量修改decimal为int字段语句
        //SELECT CONCAT('alter table ',T.TABLE_NAME,' modify column ',T.COLUMN_NAME,' int;') FROM 
        //information_schema.COLUMNS T WHERE T.TABLE_SCHEMA='STOOGES' AND T.DATA_TYPE='decimal';
        //第五步: 执行修改text字段为varchar,以下存放在oracle中执行
        //SELECT 'alter table '||COL.TABLE_NAME||' modify column '||COL.COLUMN_NAME ||' varchar('||COL.DATA_LENGTH||');' 
        //FROM user_tab_columns col left join user_col_comments cm 
        //on col.COLUMN_NAME=cm.column_name AND COL.TABLE_NAME=CM.table_name where 
        //  COL.DATA_TYPE='VARCHAR2' AND COL.DATA_LENGTH>=256
        ///ORDER BY col.COLUMN_ID ASC;
        //第六步: 更新缺省值
        //SELECT COL.COLUMN_NAME,COL.DATA_DEFAULT,COL.TABLE_NAME
        //FROM user_tab_columns col left join user_col_comments cm
        // on col.COLUMN_NAME=cm.column_name AND COL.TABLE_NAME=CM.table_name
        // WHERE COL.DATA_DEFAULT IS NOT NULL AND COL.TABLE_NAME='JBPM6_TASK'
        // and COL.DATA_TYPE IN ('VARCHAR2','NUMBER')
        // ORDER BY COL.TABLE_NAME ASC;

    }
}
