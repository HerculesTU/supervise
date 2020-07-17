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
import com.housoo.platform.supervise.dao.SysuserDao;
import com.housoo.platform.supervise.service.SysuserService;

/**
 * 描述 系统用户业务相关service实现类
 *
 * @author tudeodng
 * @version 1.0
 * @created 2020-05-07 08:46:27
 */
@Service("sysuserService")
public class SysuserServiceImpl extends BaseServiceImpl implements SysuserService {

    /**
     * 所引入的dao
     */
    @Resource
    private SysuserDao dao;

    /**
     * 获取dao
     */
    @Override
    protected BaseDao getDao() {
        return dao;
    }
  
}
