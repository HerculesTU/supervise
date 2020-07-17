package com.housoo.platform.test.util;

import com.housoo.platform.core.util.PlatLogUtil;
import com.housoo.platform.core.util.PlatMemcachedUtil;
import com.housoo.platform.test.BaseTestCase;
import net.rubyeye.xmemcached.MemcachedClient;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

public class MemcachedTestCase extends BaseTestCase {
    private ApplicationContext app;
    private MemcachedClient memcachedClient;
    @Before
    public void init() {
        app = new ClassPathXmlApplicationContext("app-context.xml");
        memcachedClient = (MemcachedClient) app.getBean("memcachedPool");
    }
    @Test
    public void test(){
        System.out.println(PlatMemcachedUtil.set("aa", "bb", new Date(1000 * 60)));
        Object obj = PlatMemcachedUtil.get("aa");
        System.out.println("***************************");
        System.out.println(obj.toString());
    }

}
