package com.housoo.platform.test.system;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;

import com.housoo.platform.test.BaseTestCase;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.service.CompanyService;

/**
 * @author 胡裕
 *
 * 
 */
public class CompanyServiceTestCase extends BaseTestCase {
    /**
     * 
     */
    @Resource
    private CompanyService companyService;
    
    /**
     * 
     */
    @Test
    public void testSave(){
        Map<String,Object> company = new HashMap<String,Object>();
        company.put("COMPANY_ID","1");
        company.put("COMPANY_NAME","测试");
        company.put("COMPANY_PARENTID","402848a55b556eb9015b55968e8e002c");
        companyService.saveOrUpdateTreeData("PLAT_SYSTEM_COMPANY",company,
                SysConstants.ID_GENERATOR_ASSIGNED, null);
    }
}
