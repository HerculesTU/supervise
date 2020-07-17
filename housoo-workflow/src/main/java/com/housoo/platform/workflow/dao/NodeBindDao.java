/*
 * Copyright (c) 2005, 2014, STOOGES Technology Co.,Ltd. All rights reserved.
 * STOOGES PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.dao;

import com.housoo.platform.core.dao.BaseDao;

import java.util.List;

/**
 * 描述 流程绑定业务相关dao
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-06 13:24:42
 */
public interface NodeBindDao extends BaseDao {

    /**
     * 获取绑定的节点信息KEY列表
     *
     * @param defId
     * @param flowVersion
     * @param busRecordId
     * @param bindType
     * @return
     */
    public List<String> findBindNodeKeyList(String defId,
                                            int flowVersion, String busRecordId, int bindType);

    /**
     * 获取环节任务性质
     *
     * @param defId
     * @param defVersion
     * @param nodeKey
     * @return
     */
    public String getTaskNature(String defId, String defVersion, String nodeKey);
}
