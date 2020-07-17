package com.housoo.platform.test.util;

import com.housoo.platform.core.util.PlatSpyMemcachedUtil;
import com.housoo.platform.test.BaseTestCase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpyMemcachedTestCase extends BaseTestCase {
    private ApplicationContext app;
    private PlatSpyMemcachedUtil platSpyMemcachedUtil;
    @Before
    public void init() {
        app = new ClassPathXmlApplicationContext("app-context.xml");
        platSpyMemcachedUtil = (PlatSpyMemcachedUtil) app.getBean("platSpyMemcachedUtil");
    }
    @Test
    public void test() {
        try {
            System.out.println("set："+platSpyMemcachedUtil.set("SpyMemcached", "test", 9000));
            System.out.println("get："+platSpyMemcachedUtil.get("SpyMemcached"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
