package com.housoo.platform.core.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.dao.SysUserPwdDao;
import com.housoo.platform.core.service.SysUserPwdService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述 系统用户密码业务相关service实现类
 *
 * @author gf
 * @version 1.0
 * @created 2019-11-22
 */
@Service("sysUserPwdService")
public class SysUserPwdServiceImpl extends BaseServiceImpl implements SysUserPwdService {

    /**
     * 所引入的dao
     */
    @Resource
    private SysUserPwdDao dao;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 根据用户ID获取用户密码信息
     *
     * @param userId
     * @return
     */
    public Map<String, Object> getUserPwdInfo(String userId) {
        Map<String, Object> userPwd = new HashMap<>();
        StringBuffer sql = new StringBuffer(" SELECT USER_ID,PASSWORD,UPDATE_TIME FROM PLAT_SYSTEM_USER_PWD ");
        sql.append(" WHERE USER_ID = ? ");
        userPwd = this.getBySql(sql.toString(), new Object[]{userId});
        return userPwd;
    }

}
