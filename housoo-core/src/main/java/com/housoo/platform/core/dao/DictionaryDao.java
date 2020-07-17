package com.housoo.platform.core.dao;

/**
 * 描述
 *
 * @author housoo
 * @created 2017年2月1日 下午6:01:32
 */
public interface DictionaryDao extends BaseDao {
    /**
     * 根据类别编码获取最大排序
     *
     * @param typeCode
     * @return
     */
    public int getMaxSn(String typeCode);

    /**
     * 判断一个字典值是否存在
     *
     * @param dicId
     * @param typeCode
     * @param dicValue
     * @return
     */
    public boolean isExistsDic(String dicId, String typeCode, String dicValue);

    /**
     * 描述 更新排序字段
     *
     * @param dicIds
     * @author housoo
     * @created 2014年10月3日 下午12:54:23
     */
    public void updateSn(String[] dicIds);

    /**
     * 获取字典的值
     *
     * @param typeCode
     * @param dicName
     * @return
     */
    public String getDictionaryValue(String typeCode, String dicName);
}
