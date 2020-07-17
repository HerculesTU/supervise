package com.housoo.platform.core.dao;

import java.util.List;

/**
 * 描述 FormControl业务相关dao
 *
 * @author housoo
 * @version 1.0
 * @created 2017-02-21 17:26:03
 */
public interface FormControlDao extends BaseDao {
    /**
     * 根据设计ID获取控件ID集合
     *
     * @param designId
     * @return
     */
    public List<String> findControlIds(String designId);

    /**
     * 根据表单控件ID获取模版代码
     *
     * @param formControlId
     * @return
     */
    public String getTplCode(String formControlId);
}
