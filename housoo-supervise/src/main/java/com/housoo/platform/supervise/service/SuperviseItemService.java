/*
 * Copyright (c) 2000 www.cnnetchina.com All rights reserved.18888
 * ZW PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.housoo.platform.supervise.service;

import com.housoo.platform.core.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * 描述 督办事项分类业务相关service
 *
 * @author zxl
 * @version 1.0
 * @created 2020-04-13 14:45:11
 */
public interface SuperviseItemService extends BaseService {

    /**
     * 获取所有督办事项分类
     */
    List<Map<String, Object>> getAllSuperviseItem(String queryParamsJson);
    
}
