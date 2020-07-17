package com.housoo.platform.core.dynadatasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.housoo.platform.core.util.PlatDbUtil;
import com.housoo.platform.core.util.PlatLogUtil;
import com.housoo.platform.core.util.PlatPropUtil;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author housoo
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    /**
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        // 从自定义的位置获取数据源标识  
        return DynamicDataSourceHolder.getDatasource();
    }

    /**
     *
     */
    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("select * from PLAT_APPMODEL_DBCONN t");
            sql.append(" order by t.DBCONN_TIME DESC");
            Properties properties = PlatPropUtil.readProperties("config.properties");
            String dbUrl = properties.getProperty("jdbc.url");
            String username = properties.getProperty("jdbc.username");
            String password = properties.getProperty("jdbc.password");
            Connection conn = PlatDbUtil.getConnect(dbUrl, username, password);
            List<Map<String, Object>> list = PlatDbUtil.findBySql(conn,
                    sql.toString(), null);
            for (Map<String, Object> data : list) {
                String DBCONN_CLASS = (String) data.get("DBCONN_CLASS");
                String DBCONN_URL = (String) data.get("DBCONN_URL");
                String DBCONN_USERNAME = (String) data.get("DBCONN_USERNAME");
                String DBCONN_PASS = (String) data.get("DBCONN_PASS");
                String DBCONN_CODE = (String) data.get("DBCONN_CODE");
                DruidDataSource dataSource = new DruidDataSource();
                dataSource.setDriverClassName(DBCONN_CLASS);
                dataSource.setUrl(DBCONN_URL);
                dataSource.setName(DBCONN_CODE);
                dataSource.setUsername(DBCONN_USERNAME);
                dataSource.setPassword(DBCONN_PASS);
                targetDataSources.put(DBCONN_CODE, dataSource);
            }
        } catch (Exception e) {
            PlatLogUtil.printStackTrace(e);
        }
        super.setTargetDataSources(targetDataSources);
    }

}
