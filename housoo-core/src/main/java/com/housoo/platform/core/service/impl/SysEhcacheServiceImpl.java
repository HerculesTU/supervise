package com.housoo.platform.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.util.*;
import com.housoo.platform.core.dao.SysEhcacheDao;
import com.housoo.platform.core.service.SysEhcacheService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * 描述 缓存配置业务相关service实现类
 *
 * @author housoo
 * @version 1.0
 * @created 2017-05-03 16:14:24
 */
@Service("sysEhcacheService")
public class SysEhcacheServiceImpl extends BaseServiceImpl implements SysEhcacheService {

    /**
     * 所引入的dao
     */
    @Resource
    private SysEhcacheDao dao;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     *
     */
    @Override
    public List<Map> findTableColumnByFilter(SqlFilter sqlFilter) {
        String EHCACHE_ID = sqlFilter.getRequest().getParameter("Q_T.EHCACHE_ID_EQ");
        List<Map> list = new ArrayList<Map>();
        if (StringUtils.isNotEmpty(EHCACHE_ID)) {
            Map<String, Object> sysEhcache = dao.getRecord("PLAT_SYSTEM_EHCACHE"
                    , new String[]{"EHCACHE_ID"}, new Object[]{EHCACHE_ID});
            String EHCACHE_DEL_CLASS_NAME = (String) sysEhcache.get("EHCACHE_DEL_CLASS_NAME");
            if (StringUtils.isNotEmpty(EHCACHE_DEL_CLASS_NAME)) {
                list = JSON.parseArray(EHCACHE_DEL_CLASS_NAME, Map.class);
            }
        }
        return list;
    }

    /**
     * 获取启用的缓存列表数据
     */
    @Override
    public List<Map<String, Object>> findByStatue(String statue) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT T.EHCACHE_CLASS_NAME FROM PLAT_SYSTEM_EHCACHE T ");
        sql.append(" WHERE T.EHCACHE_STATUE=? ");
        return dao.findBySql(sql.toString(), new Object[]{statue}, null);
    }

    /**
     * 获取所有刷新的方法数据
     */
    @Override
    public Set<String> findDelListByStatue(String statue) {
        Set<String> result = new HashSet<String>();
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT T.EHCACHE_DEL_CLASS_NAME FROM PLAT_SYSTEM_EHCACHE T ");
        sql.append(" WHERE T.EHCACHE_STATUE=? ");
        List<Map<String, Object>> list = null;
        if (dao == null) {
            try {
                Properties properties = PlatPropUtil.readProperties("config.properties");
                String dbUrl = properties.getProperty("jdbc.url");
                String username = properties.getProperty("jdbc.username");
                String password = properties.getProperty("jdbc.password");
                Connection conn = PlatDbUtil.getConnect(dbUrl, username, password);
                list = PlatDbUtil.findBySql(conn, sql.toString(), new Object[]{statue});
            } catch (SQLException e) {
                PlatLogUtil.printStackTrace(e);
            }
        } else {
            list = this.findBySql(sql.toString(), new Object[]{statue}, null);
        }
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String EHCACHE_DEL_CLASS_NAME = (String) list.get(i).get("EHCACHE_DEL_CLASS_NAME");
                if (StringUtils.isNotEmpty(EHCACHE_DEL_CLASS_NAME)) {
                    List<Map> delList = JSON.parseArray(EHCACHE_DEL_CLASS_NAME, Map.class);
                    for (int j = 0; j < delList.size(); j++) {
                        result.add((String) delList.get(i).get("DEL_CLASS_NAME"));
                    }
                }
            }
        }
        return result;
    }

    /**
     *
     */
    @Override
    public List<Map<String, Object>> findRefreshList(String classname) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT T.EHCACHE_CLASS_NAME FROM PLAT_SYSTEM_EHCACHE T ");
        sql.append(" WHERE T.EHCACHE_STATUE=?  AND T.EHCACHE_DEL_CLASS_NAME LIKE ? ");
        return dao.findBySql(sql.toString(), new Object[]{"1", "%" + classname + "%"}, null);
    }

    /**
     *
     */
    @Override
    public void manualReloadEhcache(String selectColValues) {
        String[] ehcacheId = selectColValues.split(",");
        for (int m = 0; m < ehcacheId.length; m++) {
            Map<String, Object> sysEhcache = this.getRecord("PLAT_SYSTEM_EHCACHE", new String[]{"EHCACHE_ID"},
                    new Object[]{ehcacheId[m]});
            String key = (String) sysEhcache.get("EHCACHE_CLASS_NAME");
            PlatEhcacheUtil.moveEhcacheByStartKey(key);
        }

    }

    /**
     * 更新状态
     */
    @Override
    public void updateEhcacheStatue(String selectColValues, String statue) {
        StringBuffer sql = new StringBuffer("");
        sql.append(" update PLAT_SYSTEM_EHCACHE set EHCACHE_STATUE=? ");
        sql.append(" where EHCACHE_ID in ").append(PlatStringUtil.getSqlInCondition(selectColValues));
        dao.executeSql(sql.toString(), new Object[]{statue});
    }


}
