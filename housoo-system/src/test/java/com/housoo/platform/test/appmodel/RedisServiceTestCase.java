package com.housoo.platform.test.appmodel;

import javax.annotation.Resource;

import org.junit.Test;

import com.housoo.platform.test.BaseTestCase;
import com.housoo.platform.core.service.RedisService;

/**
 * @author 胡裕
 *
 * 
 */
public class RedisServiceTestCase extends BaseTestCase {
    /**
     * 
     */
    @Resource
    private RedisService redisService;
    
    /**
     * 
     */
    @Test
    public void getKeySetByKeyPattern(){
        System.out.println(redisService.getKeySetByKeyPattern("001_").toString());
    }
    
    /**
     * 
     */
    @Test
    public void deleteByLikeKeyName(){
        redisService.deleteByLikeKeyName("001_");
    }
}
