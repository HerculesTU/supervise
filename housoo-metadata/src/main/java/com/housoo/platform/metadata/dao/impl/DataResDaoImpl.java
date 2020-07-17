package com.housoo.platform.metadata.dao.impl;

import com.housoo.platform.core.dao.impl.BaseDaoImpl;
import com.housoo.platform.metadata.dao.DataResDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述数据资源信息业务相关dao实现类
 *
 * @author 高飞
 * @version 1.0
 * @created 2018-05-07 17:35:52
 */
@Repository
public class DataResDaoImpl extends BaseDaoImpl implements DataResDao {

    /**
     * 根据资源ID获取目录ID列表
     *
     * @param resId
     * @return
     */
    @Override
    public List<String> findCataLogIds(String resId) {
        StringBuffer sql = new StringBuffer("SELECT T.CATALOG_ID");
        sql.append(" FROM PLAT_METADATA_CATARES T WHERE T.DATARES_ID=?");
        List<String> idList = this.getJdbcTemplate().queryForList(sql.toString(),
                new Object[]{resId}, String.class);
        return idList;
    }
}
