/*
 * Copyright (c) 2005, 2014, STOOGES Technology Co.,Ltd. All rights reserved.
 * STOOGES PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.demo.dao.impl;

import com.housoo.platform.core.dao.impl.BaseDaoImpl;
import com.housoo.platform.core.util.PlatStringUtil;
import com.housoo.platform.demo.dao.ProductDao;
import org.springframework.stereotype.Repository;

/**
 * 描述产品信息业务相关dao实现类
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-07-10 13:48:25
 */
@Repository
public class ProductDaoImpl extends BaseDaoImpl implements ProductDao {

    /**
     * @param productIds
     */
    @Override
    public void updateIsShow(String productIds, String isShow) {
        StringBuffer sql = new StringBuffer("UPDATE PLAT_DEMO_PRODUCT");
        sql.append(" SET PRODUCT_ISSHOW=? ");
        sql.append("WHERE PRODUCT_ID IN ").append(PlatStringUtil.getSqlInCondition(productIds));
        this.getJdbcTemplate().update(sql.toString(), new Object[]{isShow});
    }

    @Override
    public void testList() {
        String sql = "select U.NAME as TABLE_NAME,cast(sys.extended_properties.value as varchar) AS COMMENTS from sysobjects U LEFT JOIN sys.extended_properties  on U.id=sys.extended_properties.major_id  WHERE U.TYPE='U' and sys.extended_properties.minor_id='0' ";
        System.out.println(this.getJdbcTemplate().queryForList(sql).size());
    }
}
