/*
 * Copyright (c) 2000 www.cnnetchina.com All rights reserved.18888
 * ZW PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.housoo.platform.demo.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.service.impl.BaseServiceImpl;
import com.housoo.platform.demo.dao.ProjectDao;
import com.housoo.platform.demo.service.ProjectService;

/**
 * 描述 项目信息业务相关service实现类
 *
 * @author gf
 * @version 1.0
 * @created 2020-03-11 16:34:21
 */
@Service("projectService")
public class ProjectServiceImpl extends BaseServiceImpl implements ProjectService {

    /**
     * 所引入的dao
     */
    @Resource
    private ProjectDao dao;

    /**
     * 获取dao
     */
    @Override
    protected BaseDao getDao() {
        return dao;
    }
  
}
