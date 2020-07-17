/*
 * Copyright (c) 2005, 2014, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.service.impl.BaseServiceImpl;
import com.housoo.platform.workflow.dao.CommonOpinionDao;
import com.housoo.platform.workflow.service.CommonOpinionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 描述 常用意见业务相关service实现类
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-31 09:04:45
 */
@Service("commonOpinionService")
public class CommonOpinionServiceImpl extends BaseServiceImpl implements CommonOpinionService {

    /**
     * 所引入的dao
     */
    @Resource
    private CommonOpinionDao dao;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

}
