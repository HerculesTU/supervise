/*
 * Copyright (c) 2005, 2014, STOOGES Technology Co.,Ltd. All rights reserved.
 * STOOGES PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.demo.dao;

import com.housoo.platform.core.dao.BaseDao;

/**
 * 描述 产品信息业务相关dao
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-07-10 13:48:25
 */
public interface ProductDao extends BaseDao {
    /**
     * @param productIds
     */
    public void updateIsShow(String productIds, String isShow);

    public void testList();

}
