package com.housoo.platform.core.dao.impl;

import com.housoo.platform.core.dao.FormControlDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述 FormControl业务相关dao实现类
 *
 * @author housoo
 * @version 1.0
 * @created 2017-02-21 17:26:03
 */
@Repository
public class FormControlDaoImpl extends BaseDaoImpl implements FormControlDao {

    /**
     * 根据设计ID获取控件ID集合
     *
     * @param designId
     * @return
     */
    @Override
    public List<String> findControlIds(String designId) {
        StringBuffer sql = new StringBuffer("select T.FORMCONTROL_ID ");
        sql.append("FROM PLAT_APPMODEL_FORMCONTROL T WHERE T.FORMCONTROL_DESIGN_ID=?");
        List<String> list = this.getJdbcTemplate().queryForList(sql.toString(),
                new Object[]{designId}, String.class);
        return list;
    }

    /**
     * 根据表单控件ID获取模版代码
     *
     * @param formControlId
     * @return
     */
    @Override
    public String getTplCode(String formControlId) {
        StringBuffer sql = new StringBuffer("SELECT T.FORMCONTROL_TPLCODE FROM ");
        sql.append("PLAT_APPMODEL_FORMCONTROL T WHERE T.FORMCONTROL_ID=?");
        String tplCode = this.getJdbcTemplate().queryForObject(sql.toString(),
                new Object[]{formControlId}, String.class);
        return tplCode;
    }
}
