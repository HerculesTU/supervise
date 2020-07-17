package com.housoo.platform.core.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.dao.DicTypeDao;
import com.housoo.platform.core.service.DicTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述
 *
 * @author 高飞
 * @created 2017年1月31日 上午9:25:30
 */
@Service("dicTypeService")
public class DicTypeServiceImpl extends BaseServiceImpl implements
        DicTypeService {

    /**
     * 所引入的dao
     */
    @Resource
    private DicTypeDao dao;

    /**
     *
     */
    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 删除字典类别,并且级联更新字典的类别编码
     *
     * @param dicTypeId
     */
    @Override
    public void deleteDicTypeCascade(String dicTypeId) {
        StringBuffer sql = new StringBuffer("UPDATE PLAT_SYSTEM_DICTIONARY SET DIC_DICTYPE_CODE=?");
        sql.append(" WHERE DIC_DICTYPE_CODE IN (");
        sql.append("SELECT T.DICTYPE_CODE FROM PLAT_SYSTEM_DICTYPE T ");
        sql.append("WHERE T.DICTYPE_PATH LIKE ? )");
        dao.executeSql(sql.toString(), new Object[]{"", "%." + dicTypeId + ".%"});
        sql = new StringBuffer("DELETE FROM PLAT_SYSTEM_DICTYPE ");
        sql.append("WHERE DICTYPE_PATH LIKE ? ");
        dao.executeSql(sql.toString(), new Object[]{"%." + dicTypeId + ".%"});
    }

    /**
     * 初始化子行政区划
     */
    private void initChildDivision(String newParentId, String oldParentId) {
        StringBuffer sql = new StringBuffer("select * FROM T_MSJW_SYSTEM_DICTYPE D");
        sql.append(" WHERE D.PARENT_ID=? ORDER BY D.TREE_SN ASC");
        List<Map<String, Object>> children = dao.findBySql(sql.toString(),
                new Object[]{oldParentId}, null);
        for (Map<String, Object> child : children) {
            Map<String, Object> newType = new HashMap<String, Object>();
            newType.put("DICTYPE_NAME", child.get("TYPE_NAME"));
            newType.put("DICTYPE_CODE", child.get("TYPE_CODE"));
            newType.put("DICTYPE_PARENTID", newParentId);
            newType = this.saveOrUpdateTreeData("PLAT_SYSTEM_DICTYPE", newType,
                    SysConstants.ID_GENERATOR_UUID, null);
            String newTypeId = (String) newType.get("DICTYPE_ID");
            String oldTypeId = (String) child.get("TYPE_ID");
            this.initChildDivision(newTypeId, oldTypeId);
        }
    }

    /**
     * 根据父亲类别编码获取下一级类别数据列表
     *
     * @param parentTypeCode
     * @return
     */
    @Override
    public List<Map<String, Object>> findChildren(String parentTypeCode) {
        if (StringUtils.isNotEmpty(parentTypeCode)) {
            StringBuffer sql = new StringBuffer("SELECT D.DICTYPE_CODE AS VALUE,D.DICTYPE_NAME AS LABEL");
            sql.append(" FROM PLAT_SYSTEM_DICTYPE D WHERE D.DICTYPE_PARENTID=(");
            sql.append("SELECT T.DICTYPE_ID from PLAT_SYSTEM_DICTYPE T WHERE T.DICTYPE_CODE=?)");
            sql.append(" ORDER BY D.DICTYPE_TREESN ASC");
            List<Map<String, Object>> list = dao.findBySql(sql.toString(), new Object[]{parentTypeCode}, null);
            return list;
        } else {
            return null;
        }
    }

    /**
     * 初始化全国行政区划数据
     */
    @Override
    public void initAdminDivision() {
        StringBuffer sql = new StringBuffer("select * FROM T_MSJW_SYSTEM_DICTYPE D");
        sql.append(" WHERE D.PARENT_ID=? ORDER BY D.TREE_SN ASC");
        List<Map<String, Object>> topList = dao.findBySql(sql.toString(),
                new Object[]{"4028b8c654ff22fb0154ff2414cc0002"}, null);
        String newParentId = "402848a55b411950015b4119ec490001";
        for (Map<String, Object> top : topList) {
            Map<String, Object> newTop = new HashMap<String, Object>();
            newTop.put("DICTYPE_NAME", top.get("TYPE_NAME"));
            newTop.put("DICTYPE_CODE", top.get("TYPE_CODE"));
            newTop.put("DICTYPE_PARENTID", newParentId);
            newTop = this.saveOrUpdateTreeData("PLAT_SYSTEM_DICTYPE", newTop,
                    SysConstants.ID_GENERATOR_UUID, null);
            String newTopId = (String) newTop.get("DICTYPE_ID");
            String oldTypeId = (String) top.get("TYPE_ID");
            this.initChildDivision(newTopId, oldTypeId);
        }
    }
}
