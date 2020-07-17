package com.housoo.platform.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.dao.DicAttachDao;
import com.housoo.platform.core.service.DicAttachService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 描述 字典附加属性业务相关service实现类
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-04-22 09:56:24
 */
@Service("dicAttachService")
public class DicAttachServiceImpl extends BaseServiceImpl implements DicAttachService {

    /**
     * 所引入的dao
     */
    @Resource
    private DicAttachDao dao;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 获取可编辑表格的数据
     *
     * @param filter
     * @return
     */
    @Override
    public List<Map<String, Object>> findEditTableDatas(SqlFilter filter) {
        String DIC_ID = filter.getRequest().getParameter("DICATTACH_DICID");
        StringBuffer sql = new StringBuffer("SELECT * FROM PLAT_SYSTEM_DICATTACH D");
        sql.append(" WHERE D.DICATTACH_DICID=? ORDER BY D.DICATTACH_CREATETIME ASC");
        return dao.findBySql(sql.toString(), new Object[]{DIC_ID}, null);
    }

    /**
     * 根据字典ID获取附加属性列表
     *
     * @param dicId
     * @return
     */
    @Override
    public List<Map<String, Object>> findByDicId(String dicId) {
        StringBuffer sql = new StringBuffer("SELECT * FROM PLAT_SYSTEM_DICATTACH D");
        sql.append(" WHERE D.DICATTACH_DICID=? ORDER BY D.DICATTACH_CREATETIME ASC");
        return dao.findBySql(sql.toString(), new Object[]{dicId}, null);
    }

    /**
     * 保存字典的附加属性配置
     *
     * @param dicId
     * @param attachJson
     */
    @Override
    public void saveDicAttachs(String dicId, String attachJson) {
        //先清除之前配置的记录
        StringBuffer sql = new StringBuffer("DELETE FROM PLAT_SYSTEM_DICATTACH ");
        sql.append("WHERE DICATTACH_DICID=? ");
        dao.executeSql(sql.toString(), new Object[]{dicId});
        if (StringUtils.isNotEmpty(attachJson)) {
            List<Map> attachList = JSON.parseArray(attachJson, Map.class);
            for (Map attach : attachList) {
                attach.put("DICATTACH_DICID", dicId);
                dao.saveOrUpdate("PLAT_SYSTEM_DICATTACH", attach,
                        SysConstants.ID_GENERATOR_UUID, null);
            }
        }
    }

    /**
     * 设置字典附加属性的值
     *
     * @param dicInfo
     * @return
     */
    @Override
    public Map<String, Object> setDicAttachValues(Map<String, Object> dicInfo) {
        String dicId = (String) dicInfo.get("DIC_ID");
        List<Map<String, Object>> list = this.findByDicId(dicId);
        for (Map<String, Object> map : list) {
            String DICATTACH_KEY = (String) map.get("DICATTACH_KEY");
            String DICATTACH_VALUE = (String) map.get("DICATTACH_VALUE");
            dicInfo.put(DICATTACH_KEY, DICATTACH_VALUE);
        }
        return dicInfo;
    }

}
