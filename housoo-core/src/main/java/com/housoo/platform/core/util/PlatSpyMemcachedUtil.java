package com.housoo.platform.core.util;

import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketAddress;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import net.spy.memcached.ConnectionObserver;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.transcoders.Transcoder;

/**
 * SpyMemcached工具类
 *
 * @author gf
 */
public class PlatSpyMemcachedUtil {
    /**
     * memcached客户单实例.
     */
    private MemcachedClient spyMemcachedClient;
    public static int DEFAULT_TIMEOUT = 5;
    public static TimeUnit DEFAULT_TIMEUNIT = TimeUnit.SECONDS;

    /**
     * 添加观察者
     *
     * @param obs obs
     */
    public void addObserver(ConnectionObserver obs) {
        spyMemcachedClient.addObserver(obs);
    }

    /**
     * 删除观察者
     *
     * @param obs obs
     */
    public void removeObserver(ConnectionObserver obs) {
        spyMemcachedClient.removeObserver(obs);
    }

    // ---- Basic Operation Start ----//

    /**
     * 加入缓存
     *
     * @param key    key
     * @param value  value
     * @param expire 过期时间
     * @return boolean
     */
    public boolean set(String key, Object value, int expire) {
        Future<Boolean> f = spyMemcachedClient.set(key, expire, value);
        return getBooleanValue(f);
    }

    /**
     * 从缓存中获取
     *
     * @param key key
     * @return Object
     */
    public Object get(String key) {
        return spyMemcachedClient.get(key);
    }

    /**
     * 从缓存中获取
     *
     * @param key key
     * @return Object
     */
    public Object asyncGet(String key) {
        Object obj = null;
        Future<Object> f = spyMemcachedClient.asyncGet(key);
        try {
            obj = f.get(DEFAULT_TIMEOUT, DEFAULT_TIMEUNIT);
        } catch (Exception e) {
            f.cancel(false);
        }
        return obj;
    }

    /**
     * 加入缓存
     *
     * @param key    key
     * @param value  value
     * @param expire 过期时间
     * @return boolean
     */
    public boolean add(String key, Object value, int expire) {
        Future<Boolean> f = spyMemcachedClient.add(key, expire, value);
        return getBooleanValue(f);
    }

    /**
     * 替换
     *
     * @param key    key
     * @param value  value
     * @param expire 过期时间
     * @return boolean
     */
    public boolean replace(String key, Object value, int expire) {
        Future<Boolean> f = spyMemcachedClient.replace(key, expire, value);
        return getBooleanValue(f);
    }

    /**
     * 从缓存中删除
     *
     * @param key key
     * @return boolean
     */
    public boolean delete(String key) {
        Future<Boolean> f = spyMemcachedClient.delete(key);
        return getBooleanValue(f);
    }

    /**
     * 刷新
     *
     * @return boolean
     */
    public boolean flush() {
        Future<Boolean> f = spyMemcachedClient.flush();
        return getBooleanValue(f);
    }

    /**
     * 从缓存中获取
     *
     * @param keys keys
     * @return Map<String ,   Object>
     */
    public Map<String, Object> getMulti(Collection<String> keys) {
        return spyMemcachedClient.getBulk(keys);
    }

    /**
     * 从缓存中获取
     *
     * @param keys keys
     * @return Map<String ,   Object>
     */
    public Map<String, Object> getMulti(String[] keys) {
        return spyMemcachedClient.getBulk(keys);
    }

    /**
     * 从缓存中获取
     *
     * @param keys keys
     * @return Map<String ,   Object>
     */
    public Map<String, Object> asyncGetMulti(Collection<String> keys) {
        Map<String, Object> map = null;
        Future<Map<String, Object>> f = spyMemcachedClient.asyncGetBulk(keys);
        try {
            map = f.get(DEFAULT_TIMEOUT, DEFAULT_TIMEUNIT);
        } catch (Exception e) {
            f.cancel(false);
        }
        return map;
    }

    /**
     * 从缓存中获取
     *
     * @param keys keys
     * @return Map<String ,   Object>
     */
    public Map<String, Object> asyncGetMulti(String[] keys) {
        Map<String, Object> map = null;
        Future<Map<String, Object>> f = spyMemcachedClient.asyncGetBulk(keys);
        try {
            map = f.get(DEFAULT_TIMEOUT, DEFAULT_TIMEUNIT);
        } catch (Exception e) {
            f.cancel(false);
        }
        return map;
    }
    // ---- Basic Operation End ----//

    // ---- increment & decrement Start ----//
    public long increment(String key, int by, long defaultValue, int expire) {
        return spyMemcachedClient.incr(key, by, defaultValue, expire);
    }

    public long increment(String key, int by) {
        return spyMemcachedClient.incr(key, by);
    }

    public long decrement(String key, int by, long defaultValue, int expire) {
        return spyMemcachedClient.decr(key, by, defaultValue, expire);
    }

    public long decrement(String key, int by) {
        return spyMemcachedClient.decr(key, by);
    }

    public long asyncIncrement(String key, int by) {
        Future<Long> f = spyMemcachedClient.asyncIncr(key, by);
        return getLongValue(f);
    }

    public long asyncDecrement(String key, int by) {
        Future<Long> f = spyMemcachedClient.asyncDecr(key, by);
        return getLongValue(f);
    }
    // ---- increment & decrement End ----//

    public void printStats() throws IOException {
        printStats(null);
    }

    public void printStats(OutputStream stream) throws IOException {
        Map<SocketAddress, Map<String, String>> statMap = spyMemcachedClient.getStats();
        if (stream == null) {
            stream = System.out;
        }
        StringBuffer buf = new StringBuffer();
        Set<SocketAddress> addrSet = statMap.keySet();
        Iterator<SocketAddress> iter = addrSet.iterator();
        while (iter.hasNext()) {
            SocketAddress addr = iter.next();
            buf.append(addr.toString() + "/n");
            Map<String, String> stat = statMap.get(addr);
            Set<String> keys = stat.keySet();
            Iterator<String> keyIter = keys.iterator();
            while (keyIter.hasNext()) {
                String key = keyIter.next();
                String value = stat.get(key);
                buf.append("  key=" + key + ";value=" + value + "/n");
            }
            buf.append("/n");
        }
        stream.write(buf.toString().getBytes());
        stream.flush();
    }

    public Transcoder getTranscoder() {
        return spyMemcachedClient.getTranscoder();
    }

    private long getLongValue(Future<Long> f) {
        try {
            Long l = f.get(DEFAULT_TIMEOUT, DEFAULT_TIMEUNIT);
            return l.longValue();
        } catch (Exception e) {
            f.cancel(false);
        }
        return -1;
    }

    private boolean getBooleanValue(Future<Boolean> f) {
        try {
            Boolean bool = f.get(DEFAULT_TIMEOUT, DEFAULT_TIMEUNIT);
            return bool.booleanValue();
        } catch (Exception e) {
            f.cancel(false);
            return false;
        }
    }

    public MemcachedClient getMemcachedClient() {
        return spyMemcachedClient;
    }

    public void setMemcachedClient(MemcachedClient spyMemcachedClient) {
        this.spyMemcachedClient = spyMemcachedClient;
    }
}
