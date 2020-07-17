/*
 * Copyright (c) 2000 www.cnnetchina.com All rights reserved.18888
 * ZW PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.housoo.platform.supervise.service;

import com.housoo.platform.core.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * 描述 督办环节配置业务相关service
 *
 * @author cjr
 * @version 1.0
 * @created 2020-04-13 18:20:59
 */
public interface SuperviseApproveService extends BaseService {
    /**
     * 获取所有的督办环节
     *
     * @return
     */
    List<Map<String, Object>> findAll();
}
