package com.housoo.platform.core.service;

import com.housoo.platform.core.model.SqlFilter;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 描述 缓存配置业务相关service
 *
 * @author housoo
 * @version 1.0
 * @created 2017-05-03 16:14:24
 */
public interface SysEhcacheService extends BaseService {
    /**
     * 获取可编辑表格数据
     *
     * @param filter
     * @return
     */
    public List<Map> findTableColumnByFilter(SqlFilter filter);

    /**
     * @param statue
     * @return
     */
    public List<Map<String, Object>> findByStatue(String statue);

    /**
     * @param string
     * @return
     */
    public Set<String> findDelListByStatue(String string);

    /**
     * @param classname
     * @return
     */
    public List<Map<String, Object>> findRefreshList(String classname);

    /**
     * @param selectColValues
     */
    public void manualReloadEhcache(String selectColValues);

    /**
     * @param selectColValues
     * @param statue
     */
    public void updateEhcacheStatue(String selectColValues, String statue);

}
