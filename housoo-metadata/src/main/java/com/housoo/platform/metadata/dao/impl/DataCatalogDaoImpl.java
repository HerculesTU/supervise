package com.housoo.platform.metadata.dao.impl;

import com.housoo.platform.core.dao.impl.BaseDaoImpl;
import com.housoo.platform.metadata.dao.DataCatalogDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述数据目录业务相关dao实现类
 *
 * @author 高飞
 * @version 1.0
 * @created 2018-05-07 10:53:23
 */
@Repository
public class DataCatalogDaoImpl extends BaseDaoImpl implements DataCatalogDao {

    /**
     * 根据资源ID获取目录id集合
     *
     * @param resId
     * @return
     */
    @Override
    public List<String> findCatalogIds(String resId) {
        StringBuffer sql = new StringBuffer("SELECT T.CATALOG_ID");
        sql.append(" FROM PLAT_METADATA_CATARES T WHERE T.DATARES_ID=? ");
        return this.getJdbcTemplate().queryForList(sql.toString(),
                new Object[]{resId}, String.class);
    }
}
