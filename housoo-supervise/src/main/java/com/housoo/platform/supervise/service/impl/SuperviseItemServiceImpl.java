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
import com.housoo.platform.supervise.dao.SuperviseItemDao;
import com.housoo.platform.supervise.service.SuperviseItemService;

/**
 * 描述 督办事项分类业务相关service实现类
 *
 * @author zxl
 * @version 1.0
 * @created 2020-04-13 14:45:11
 */
@Service("superviseItemService")
public class SuperviseItemServiceImpl extends BaseServiceImpl implements SuperviseItemService {

    /**
     * 所引入的dao
     */
    @Resource
    private SuperviseItemDao dao;

    /**
     * 获取dao
     */
    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 获取所有督办事项分类
     *
     * @param queryParamsJson
     * @return
     */
    @Override
    public List<Map<String, Object>> getAllSuperviseItem(String queryParamsJson) {
        StringBuffer sql = new StringBuffer("SELECT O.RECORD_ID AS VALUE,O.ITEM_NAME AS LABEL ");
        sql.append(" FROM tb_supervise_item O");
        return dao.findBySql(sql.toString(), null, null);
    }
}
