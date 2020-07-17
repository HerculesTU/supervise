package com.housoo.platform.core.service;

import com.housoo.platform.core.model.SqlFilter;

import java.util.List;
import java.util.Map;

/**
 * 描述 字典附加属性业务相关service
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-04-22 09:56:24
 */
public interface DicAttachService extends BaseService {
    /**
     * 获取可编辑表格的数据
     *
     * @param filter
     * @return
     */
    public List<Map<String, Object>> findEditTableDatas(SqlFilter filter);

    /**
     * 保存字典的附加属性配置
     *
     * @param dicId
     * @param attachJson
     */
    public void saveDicAttachs(String dicId, String attachJson);

    /**
     * 设置字典附加属性的值
     *
     * @param dicInfo
     * @return
     */
    public Map<String, Object> setDicAttachValues(Map<String, Object> dicInfo);

    /**
     * 根据字典ID获取附加属性列表
     *
     * @param dicId
     * @return
     */
    public List<Map<String, Object>> findByDicId(String dicId);
}
