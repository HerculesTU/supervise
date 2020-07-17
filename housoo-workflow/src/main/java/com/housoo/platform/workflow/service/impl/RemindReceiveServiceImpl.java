/*
 * Copyright (c) 2005, 2014, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.service.impl.BaseServiceImpl;
import com.housoo.platform.workflow.dao.RemindReceiveDao;
import com.housoo.platform.workflow.service.RemindReceiveService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 描述 催办接收人信息业务相关service实现类
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-11-16 08:50:10
 */
@Service("remindReceiveService")
public class RemindReceiveServiceImpl extends BaseServiceImpl implements RemindReceiveService {

    /**
     * 所引入的dao
     */
    @Resource
    private RemindReceiveDao dao;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

}
