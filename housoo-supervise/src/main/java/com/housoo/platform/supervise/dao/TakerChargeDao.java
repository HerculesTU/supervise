/*
 * Copyright (c) 2000 www.cnnetchina.com All rights reserved.18888
 * ZW PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.housoo.platform.supervise.dao;

import com.housoo.platform.core.dao.BaseDao;

import java.util.List;
import java.util.Map;

/**
 * 描述 承办人业务相关dao
 *
 * @author cjr
 * @version 1.0
 * @created 2020-04-14 15:30:11
 */
public interface TakerChargeDao extends BaseDao {

    /**
     * 根据督办事项Id获取承办部门任务信息
     *
     * @param superviseId
     * @return
     */
    List<Map<String, Object>> findTaskListBySuperviseId(String superviseId);

    /**
     * 根据督办事项Id获取承办部门督办事项节点信息
     *
     * @param superviseId
     * @return
     */
    List<Map<String, Object>> findTaskNodeListBySuperviseId(String superviseId);

}
