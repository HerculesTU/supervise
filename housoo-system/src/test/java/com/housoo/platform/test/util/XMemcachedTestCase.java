package com.housoo.platform.test.util;

import com.housoo.platform.core.util.PlatLogUtil;
import com.housoo.platform.test.BaseTestCase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import net.rubyeye.xmemcached.MemcachedClient;

public class XMemcachedTestCase  extends BaseTestCase {
    private ApplicationContext app;
    private MemcachedClient xMemcachedClient;
    @Before
    public void init() {
        app = new ClassPathXmlApplicationContext("app-context.xml");
        xMemcachedClient = (MemcachedClient) app.getBean("xMemcachedClient");
    }
    @Test
    public void test() {
        try {
            // 设置/获取
            xMemcachedClient.set("zlex", 36000, "set/get");
            PlatLogUtil.println(xMemcachedClient.get("zlex"));
            // 替换
            xMemcachedClient.replace("zlex", 36000, "replace");
            PlatLogUtil.println(xMemcachedClient.get("zlex"));
            // 移除
            xMemcachedClient.delete("zlex");
            PlatLogUtil.println(xMemcachedClient.get("zlex"));
        } catch (Exception e) {
            PlatLogUtil.printStackTrace(e);
        }
    }
}
