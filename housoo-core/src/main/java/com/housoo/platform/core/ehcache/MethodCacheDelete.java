package com.housoo.platform.core.ehcache;

import com.housoo.platform.core.service.SysEhcacheService;
import net.sf.ehcache.Cache;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 描述
 *
 * @author housoo
 * @created 2017年5月3日 下午3:02:33
 */
public class MethodCacheDelete implements AfterReturningAdvice, InitializingBean {

    /**
     * log4j声明
     */
    private static Log log = LogFactory.getLog(MethodCacheDelete.class);

    /**
     * 引入Service
     */
    @Resource
    private SysEhcacheService sysEhcacheService;
    /**
     * 定义cache
     */
    private Cache cache;

    /**
     *
     */
    public MethodCacheDelete() {
        super();
    }

    /**
     * @param cache
     */
    public void setCache(Cache cache) {
        this.cache = cache;
    }

    /**
     *
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(cache, "Need a cache,please use setCache() to create");
    }

    /**
     *
     */
    @Override
    public void afterReturning(Object arg0, Method arg1, Object[] arg2, Object arg3) throws Throwable {
        List list = cache.getKeys();
        String targetName = arg3.getClass().getName();// 类名
        String methodName = arg1.getName();// 方法名
        String classname = targetName + "." + methodName;
        List<Map<String, Object>> refreshList = sysEhcacheService.findRefreshList(classname);
        Set<String> keySet = new HashSet<String>();
        if (refreshList != null && refreshList.size() > 0) {
            for (int i = 0; i < refreshList.size(); i++) {
                String cacheKey = (String) refreshList.get(i).get("EHCACHE_CLASS_NAME");
                for (int j = 0; j < list.size(); j++) {
                    if (list.get(j).toString().indexOf(cacheKey) > -1) {
                        keySet.add(list.get(j).toString());
                    }
                }
            }
        }
        if (keySet != null && keySet.size() > 0) {
            for (String str : keySet) {
                cache.remove(str);
            }
        }
    }

}
