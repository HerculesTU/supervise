package com.housoo.platform.supervise.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.service.impl.BaseServiceImpl;
import com.housoo.platform.supervise.dao.SuperviseClazzDao;
import com.housoo.platform.supervise.service.SuperviseClazzService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 描述 督办类型业务相关service实现类
 *
 * @author zxl
 * @version 1.0
 * @created 2020-04-13 11:28:30
 */
@Service("superviseClazzService")
public class SuperviseClazzServiceImpl extends BaseServiceImpl implements SuperviseClazzService {

    /**
     * 所引入的dao
     */
    @Resource
    private SuperviseClazzDao dao;

    /**
     * 获取dao
     */
    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 获取所有督办类型
     */
    @Override
    public List<Map<String, Object>> getAllSuperviseClazz(String queryParamsJson) {
        StringBuffer sql = new StringBuffer("SELECT O.RECORD_ID AS VALUE,O.CLAZZ_NAME AS LABEL ");
        sql.append(" FROM tb_supervise_clazz O ORDER BY O.CLAZZ_VALUE ASC ");
        return dao.findBySql(sql.toString(), null, null);
    }
}
