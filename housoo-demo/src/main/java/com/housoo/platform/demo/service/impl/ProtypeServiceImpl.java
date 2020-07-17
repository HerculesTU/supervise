/*
 * Copyright (c) 2005, 2014, STOOGES Technology Co.,Ltd. All rights reserved.
 * STOOGES PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.demo.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.service.impl.BaseServiceImpl;
import com.housoo.platform.demo.dao.ProtypeDao;
import com.housoo.platform.demo.service.ProtypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 描述 产品类别业务相关service实现类
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-07-11 16:28:06
 */
@Service("protypeService")
public class ProtypeServiceImpl extends BaseServiceImpl implements ProtypeService {

    /**
     * 所引入的dao
     */
    @Resource
    private ProtypeDao dao;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

}
