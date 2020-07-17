/*
 * Copyright (c) 2005, 2014, STOOGES Technology Co.,Ltd. All rights reserved.
 * STOOGES PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.demo.service;

import com.housoo.platform.core.service.BaseService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 描述 产品信息业务相关service
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-07-10 13:48:25
 */
public interface ProductService extends BaseService {
    /**
     * @param params
     * @return
     */
    public List<Map<String, Object>> findList(String params);

    /**
     * @param productIds
     */
    public void updateIsShow(String productIds, String isShow);

    /**
     * 测试调用接口
     *
     * @param request
     * @param postParams
     * @return
     */
    public Map<String, Object> testInvokeServer(HttpServletRequest request,
                                                Map<String, Object> postParams);

    /**
     * 保存数据,面向请求服务测试
     *
     * @param request
     * @return
     */
    public Map<String, Object> saveInfoForDataSer(HttpServletRequest request);
}
