/*
 * Copyright (c) 2000 www.cnnetchina.com All rights reserved.18888
 * ZW PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.housoo.platform.supervise.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.service.impl.BaseServiceImpl;
import com.housoo.platform.supervise.dao.OpinionDao;
import com.housoo.platform.supervise.service.OpinionService;

/**
 * 描述 常用意见配置业务相关service实现类
 *
 * @author zxl
 * @version 1.0
 * @created 2020-05-09 16:23:23
 */
@Service("opinionService")
public class OpinionServiceImpl extends BaseServiceImpl implements OpinionService {

    /**
     * 所引入的dao
     */
    @Resource
    private OpinionDao dao;

    /**
     * 获取dao
     */
    @Override
    protected BaseDao getDao() {
        return dao;
    }
  
}
