package com.housoo.platform.test.demo;

import javax.annotation.Resource;

import org.junit.Test;

import com.housoo.platform.test.BaseTestCase;
import com.housoo.platform.demo.service.LeaveInfoService;

/**
 * @author 胡裕
 *
 * 
 */
public class LeaveInfoServiceTestCase extends BaseTestCase {
    /**
     * 
     */
    @Resource
    private LeaveInfoService leaveInfoService;
    
    /**
     * 
     */
    @Test
    public void testDbChange(){
        leaveInfoService.testNativeDb();
        leaveInfoService.testMysql();
    }
    
    /**
     * 
     */
    @Test
    public void testDbChange2(){
        leaveInfoService.testOracle();
        leaveInfoService.testNativeDb();
    }
}
