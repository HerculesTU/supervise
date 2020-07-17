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
import com.housoo.platform.supervise.dao.TaskDao;
import com.housoo.platform.supervise.service.TaskService;

/**
 * 描述 督办任务业务相关service实现类
 *
 * @author tudeodng
 * @version 1.0
 * @created 2020-05-07 10:00:03
 */
@Service("taskService")
public class TaskServiceImpl extends BaseServiceImpl implements TaskService {

    /**
     * 所引入的dao
     */
    @Resource
    private TaskDao dao;

    /**
     * 获取dao
     */
    @Override
    protected BaseDao getDao() {
        return dao;
    }
  
}
