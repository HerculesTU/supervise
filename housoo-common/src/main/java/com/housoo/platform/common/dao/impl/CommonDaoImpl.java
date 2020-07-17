package com.housoo.platform.common.dao.impl;

import com.housoo.platform.core.dao.impl.BaseDaoImpl;
import com.housoo.platform.common.dao.CommonDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述
 *
 * @author 高飞
 * @created 2017年2月1日 上午10:23:14
 */
@Repository
public class CommonDaoImpl extends BaseDaoImpl implements CommonDao {

    /**
     * 获取记录的数量
     *
     * @param validTableName
     * @param validFieldName
     * @param validFieldValue
     * @return
     */
    @Override
    public int getRecordCount(String validTableName, String validFieldName,
                              String validFieldValue, String RECORD_ID) {
        StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM ");
        sql.append(validTableName).append(" WHERE ");
        sql.append(validFieldName).append("=? ");
        List<Object> params = new ArrayList<Object>();
        params.add(validFieldValue);
        if (StringUtils.isNotEmpty(RECORD_ID)) {
            //获取表名称
            String pkName = this.findPrimaryKeyNames(validTableName).get(0);
            sql.append(" AND ").append(pkName).append("!=? ");
            params.add(RECORD_ID);
        }
        int count = this.getIntBySql(sql.toString(), params.toArray());
        return count;
    }
}
