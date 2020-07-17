package com.housoo.platform.core.util;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Xmemcached工具类做些泛型类型转换, 屏蔽Checked Exception之类的杂事
 *
 * @author gf
 */
public class PlatXMemcachedUtil {
    private static Logger logger = LoggerFactory.getLogger(PlatXMemcachedUtil.class);

    private MemcachedClient xMemcachedClient;

    /**
     * Get方法, 转换结果类型,失败时屏蔽异常只返回null.
     */
    public <T> T get(String key) {
        try {
            return (T) xMemcachedClient.get(key);
        } catch (Exception e) {
            handleException(e, key);
            return null;
        }
    }

    /**
     * Get方法,同时更新过期时间， 转换结果类型,失败时屏蔽异常只返回null.
     *
     * @param exp 过期秒数
     */
    public <T> T get(String key, int exp) {
        try {
            return (T) xMemcachedClient.getAndTouch(key, exp);
        } catch (Exception e) {
            handleException(e, key);
            return null;
        }
    }

    /**
     * 功能描述：判断key是否存在
     *
     * @param key
     * @return
     * @author zhangzg
     * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
     * @since 2014年8月8日
     */
    public boolean keyIsExist(String key) {
        try {
            if (null == xMemcachedClient.get(key))
                return false;
            return true;
        } catch (TimeoutException e) {
            return false;
        } catch (InterruptedException e) {
            return false;
        } catch (MemcachedException e) {
            return false;
        }
    }

    /**
     * GetBulk方法, 转换结果类型, 失败时屏蔽异常只返回null.
     */
    public <T> Map<String, T> getBulk(Collection<String> keys) {
        try {
            return (Map<String, T>) xMemcachedClient.get(keys);
        } catch (Exception e) {
            handleException(e, StringUtils.join(keys, ","));
            return null;
        }
    }

    /**
     * Set方法, 不等待操作返回结果, 失败抛出RuntimeException..
     */
    public void asyncSet(String key, int expiredTime, Object value) {
        try {
            xMemcachedClient.setWithNoReply(key, expiredTime, value);
        } catch (Exception e) {
            handleException(e, key);
        }
    }

    /**
     * Set方法,等待操作返回结果,失败抛出RuntimeException..
     */
    public boolean set(String key, int expiredTime, Object value) {
        try {
            return xMemcachedClient.set(key, expiredTime, value);
        } catch (Exception e) {
            throw handleException(e, key);
        }
    }

    /**
     * Delete方法, 失败抛出RuntimeException.
     */
    public boolean delete(String key) {
        try {
            return xMemcachedClient.delete(key);
        } catch (Exception e) {
            throw handleException(e, key);
        }
    }

    /**
     * Incr方法, 失败抛出RuntimeException.
     */
    public long incr(String key, int by, long defaultValue) {
        try {
            return xMemcachedClient.incr(key, by, defaultValue);
        } catch (Exception e) {
            throw handleException(e, key);
        }
    }

    /**
     * Decr方法, 失败RuntimeException.
     */
    public long decr(String key, int by, long defaultValue) {
        try {
            return xMemcachedClient.decr(key, by, defaultValue);
        } catch (Exception e) {
            throw handleException(e, key);
        }
    }

    private RuntimeException handleException(Exception e, String key) {
        logger.warn("xmemcached client receive an exception with key:" + key, e);
        return new RuntimeException(e);
    }

    public MemcachedClient getMemcachedClient() {
        return xMemcachedClient;
    }

    @Autowired(required = false)
    public void setMemcachedClient(MemcachedClient xMemcachedClient) {
        this.xMemcachedClient = xMemcachedClient;
        if (xMemcachedClient != null) {
            this.xMemcachedClient.setOpTimeout(5000L);
            this.xMemcachedClient.setOptimizeGet(false);
        }
    }

    public void destroy() throws Exception {
        if (xMemcachedClient != null) {
            xMemcachedClient.shutdown();
        }
    }
}
