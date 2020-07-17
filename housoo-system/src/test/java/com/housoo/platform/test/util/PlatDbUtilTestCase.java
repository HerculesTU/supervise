package com.housoo.platform.test.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.housoo.platform.core.util.PlatDbUtil;

/**
 * @author 胡裕
 *
 * 
 */
public class PlatDbUtilTestCase {
    /**
     * @param args
     */
    public static void main(String[] args) {
        StringBuffer sql = new StringBuffer("SELECT * FROM PLAT_SYSTEM_SYSLOG T WHERE ${T.BROWSER LIKE '%[浏览器名称]%'}");
        sql.append(" AND ${T.OPER_TYPE=[操作类型]} ");
        List<Object> conditionParams = new ArrayList<Object>();
        Map<String,Object> queryParams = new HashMap<String,Object>();
        queryParams.put("浏览器名称", "fireFox");
        PlatDbUtil.transSqlContent(sql.toString(), queryParams, conditionParams);
    }

}
