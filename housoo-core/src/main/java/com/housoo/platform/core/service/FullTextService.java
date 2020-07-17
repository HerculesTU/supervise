package com.housoo.platform.core.service;

import java.util.Map;

/**
 * 描述 全文检索业务相关service
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-07-01 10:15:45
 */
public interface FullTextService extends BaseService {
    /**
     * 操作类型：新增或者修改
     */
    public static final String OPERTYPE_SAVEORUPDATE = "1";
    /**
     * 操作类型:删除
     */
    public static final String OPERTYPE_DEL = "3";

    /**
     * 同步索引
     *
     * @param fullText
     * @param operType 操作类型1新增或者修改 3删除
     */
    public void synchIndex(Map<String, Object> fullText, String operType);

    /**
     * 级联删除索引
     *
     * @param FULLTEXT_IDS
     */
    public void deleteCascadeIndex(String[] FULLTEXT_IDS);

    /**
     * 重建索引
     */
    public void rebuildIndex();
}
