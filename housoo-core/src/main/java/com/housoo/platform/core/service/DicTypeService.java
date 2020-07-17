package com.housoo.platform.core.service;

import java.util.List;
import java.util.Map;

/**
 * 描述
 *
 * @author 高飞
 * @created 2017年1月31日 上午9:25:01
 */
public interface DicTypeService extends BaseService {
    /**
     * 删除字典类别,并且级联更新字典的类别编码
     *
     * @param dicTypeId
     */
    public void deleteDicTypeCascade(String dicTypeId);

    /**
     * 初始化全国行政区划数据
     */
    public void initAdminDivision();

    /**
     * 根据父亲类别编码获取下一级类别数据列表
     *
     * @param parentTypeCode
     * @return
     */
    public List<Map<String, Object>> findChildren(String parentTypeCode);
}
