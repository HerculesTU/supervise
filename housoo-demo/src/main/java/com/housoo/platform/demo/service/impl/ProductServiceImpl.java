/*
 * Copyright (c) 2005, 2014, STOOGES Technology Co.,Ltd. All rights reserved.
 * STOOGES PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.demo.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.service.impl.BaseServiceImpl;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatDateTimeUtil;
import com.housoo.platform.demo.dao.ProductDao;
import com.housoo.platform.demo.service.ProductService;
import com.housoo.platform.metadata.service.DataSerService;
import com.housoo.platform.core.service.DicTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 描述 产品信息业务相关service实现类
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-07-10 13:48:25
 */
@Service("productService")
public class ProductServiceImpl extends BaseServiceImpl implements ProductService {

    /**
     * 所引入的dao
     */
    @Resource
    private ProductDao dao;
    /**
     *
     */
    @Resource
    private DicTypeService dicTypeService;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> findList(String params) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("VALUE", "1");
        map1.put("LABEL", "测试");
        list.add(map1);
        return list;
    }

    /**
     * @param productIds
     */
    @Override
    public void updateIsShow(String productIds, String isShow) {
        dao.updateIsShow(productIds, isShow);
    }

    /**
     * 测试调用接口
     *
     * @param request
     * @param postParams
     * @return
     */
    @Override
    public Map<String, Object> testInvokeServer(HttpServletRequest request,
                                                Map<String, Object> postParams) {
        System.out.println("调用了该接口..." + PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        String treeJson = dicTypeService.getTreeJson(request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        result.put("treeJson", treeJson);
        return result;
    }

    /**
     * 保存数据,面向请求服务测试
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> saveInfoForDataSer(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> product = PlatBeanUtil.getMapFromRequest(request);
        this.saveOrUpdate("PLAT_DEMO_PRODUCT", product, SysConstants.ID_GENERATOR_UUID, null);
        result.put("success", true);
        result.put("invokeResultCode", DataSerService.CODE_SUCCESS);
        result.put("PRODUCT_ID", product.get("PRODUCT_ID"));
        return result;
    }

}
