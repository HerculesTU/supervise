package com.housoo.platform.core.util;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 描述 缓存工具类
 *
 * @author housoo
 * @created 2017年5月4日 上午10:20:11
 */
public class PlatEhcacheUtil {
    /**
     * 定义cache
     */
    private static Cache cache;

    /**
     * 根据缓存key删除缓存(多个key用英文逗号隔开)
     */
    public static void moveEhcacheByKey(String keys) {
        if (StringUtils.isNotEmpty(keys)) {
            String[] keyArr = keys.split(",");
            for (int i = 0; i < keyArr.length; i++) {
                cache.remove(keyArr[i]);
            }
        }
    }

    /**
     * 根据删除以某些key打头的缓存(多个key用英文逗号隔开)
     */
    public static void moveEhcacheByStartKey(String keys) {
        List list = cache.getKeys();
        if (StringUtils.isNotEmpty(keys)) {
            String[] keyArr = keys.split(",");
            for (int i = 0; i < keyArr.length; i++) {
                String cacheKey = keyArr[i];
                for (int j = 0; j < list.size(); j++) {
                    if (list.get(j).toString().indexOf(cacheKey) > -1) {
                        cache.remove(list.get(j).toString());
                    }
                }
            }
        }
    }

    /**
     * 删除所有的缓存
     */
    public static void moveAllEhcache() {
        cache.removeAll();
    }

    /**
     * 添加缓存
     *
     * @param key
     * @param value
     */
    public static void addEhcache(String key, Object value) {
        Element element = new Element(key, (Serializable) value);
        cache.put(element);
    }

    /**
     * 根据key获取缓存
     */
    public static Object getEhcache(String key) {
        Element element = null;
        element = cache.get(key);
        if (element != null) {
            return element.getObjectValue();
        } else {
            return null;
        }

    }

    /**
     * 添加缓存
     *
     * @param key
     * @param value
     * @param timeToIdleSeconds
     * @param timeToLiveSeconds
     */
    public static void addEhcache(String key, Object value, int timeToIdleSeconds, int timeToLiveSeconds) {
        Element element = new Element(key, (Serializable) value, false, timeToIdleSeconds, timeToLiveSeconds);
        cache.put(element);
    }

    /**
     * 设置缓存
     *
     * @param cache
     */
    public void setCache(Cache cache) {
        PlatEhcacheUtil.cache = cache;
    }
}
