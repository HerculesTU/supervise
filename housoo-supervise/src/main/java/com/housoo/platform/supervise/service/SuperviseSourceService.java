/*
 * Copyright (c) 2000 www.cnnetchina.com All rights reserved.18888
 * ZW PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.housoo.platform.supervise.service;

import com.housoo.platform.core.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * 描述 督办来源业务相关service
 *
 * @author cjr
 * @version 1.0
 * @created 2020-04-13 11:14:38
 */
public interface SuperviseSourceService extends BaseService {

    /**
     * 获取可选的下拉督办来源数据
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> findForSelect(String params);

}
