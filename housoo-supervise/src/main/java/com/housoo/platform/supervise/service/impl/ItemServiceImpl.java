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
import com.housoo.platform.supervise.dao.ItemDao;
import com.housoo.platform.supervise.service.ItemService;

/**
 * 描述 督办事项分类业务相关service实现类
 *
 * @author tudeodng
 * @version 1.0
 * @created 2020-05-06 15:42:46
 */
@Service("itemService")
public class ItemServiceImpl extends BaseServiceImpl implements ItemService {

    /**
     * 所引入的dao
     */
    @Resource
    private ItemDao dao;

    /**
     * 获取dao
     */
    @Override
    protected BaseDao getDao() {
        return dao;
    }
  
}
