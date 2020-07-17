package com.housoo.platform.core.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.util.PlatLogUtil;
import com.housoo.platform.core.dao.DbConnDao;
import com.housoo.platform.core.service.DbConnService;
import org.apache.commons.dbutils.DbUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

/**
 * 描述 数据源信息业务相关service实现类
 *
 * @author 高飞
 * @version 1.0
 * @created 2018-03-30 15:06:38
 */
@Service("dbConnService")
public class DbConnServiceImpl extends BaseServiceImpl implements DbConnService {

    /**
     * 所引入的dao
     */
    @Resource
    private DbConnDao dao;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 判断是否是有效的连接
     *
     * @param dbConn
     * @return
     */
    @Override
    public boolean isValidDb(Map<String, Object> dbConn) {
        String dbUrl = (String) dbConn.get("DBCONN_URL");
        String username = (String) dbConn.get("DBCONN_USERNAME");
        String password = (String) dbConn.get("DBCONN_PASS");
        String classDriver = (String) dbConn.get("DBCONN_CLASS");
        Connection conn = null;
        boolean isValid = false;
        try {
            Class.forName(classDriver);
            conn = DriverManager.getConnection(dbUrl, username, password);
            isValid = conn.isValid(10);
        } catch (SQLException e) {
            PlatLogUtil.printStackTrace(e);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    DbUtils.close(conn);
                }
            } catch (SQLException e) {
                PlatLogUtil.printStackTrace(e);
            }
        }
        return isValid;
    }

    /**
     * 获取数据库类型
     *
     * @param dbConnCode
     * @return
     */
    @Override
    public String getDbType(String dbConnCode) {
        return dao.getDbType(dbConnCode);
    }

}
