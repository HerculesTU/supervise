package com.housoo.platform.core.ehcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * 描述
 *
 * @author housoo
 * @created 2017年5月3日 下午2:43:43
 */
public class MethodCacheInterceptor implements MethodInterceptor, InitializingBean {
    /**
     * log4j声明
     */
    private static Log log = LogFactory.getLog(MethodCacheInterceptor.class);
    /**
     * 设置cache
     */
    private Cache cache;

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
        // TODO Auto-generated method stub
        Assert.notNull(cache, "Need a cache,please use setCache() to create");
    }

    /**
     *
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String targetName = invocation.getThis().getClass().getName();// 类名
        String methodName = invocation.getMethod().getName();// 方法名
        Object[] arguments = invocation.getArguments();// 参数
        Object result;
        String cacheKey = getCacheKey(targetName + "." + methodName, arguments);
        Element element = null;
        synchronized (this) {
            element = cache.get(cacheKey);
            if (element == null) {
                //PlatLogUtil.println(cacheKey + "  加入到缓存  ,缓存方法" +targetName+"."+methodName);
                result = invocation.proceed();
                element = new Element(cacheKey, (Serializable) result);
                cache.put(element);
            } else {
                //PlatLogUtil.println(cacheKey + " 使用到缓存  " + cache.getName());
            }
        }
        return element.getObjectValue();
    }

    /**
     * 获取拼接的cacheKey
     *
     * @param cacheKey
     * @param arguments
     * @return
     */
    private String getCacheKey(String cacheKey,
                               Object[] arguments) {
        StringBuffer sb = new StringBuffer();
        sb.append(cacheKey);
        if ((arguments != null) && (arguments.length != 0)) {
            for (int i = 0; i < arguments.length; i++) {
                if (arguments[i] instanceof Object[]) {
                    for (Object object : (Object[]) arguments[i]) {
                        sb.append(".").append(object);
                    }
                } else {
                    sb.append(".").append(arguments[i]);
                }
            }
        }
        return sb.toString();
    }
}
