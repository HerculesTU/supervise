package com.housoo.platform.core.service;

import com.housoo.platform.core.model.SqlFilter;
import redis.clients.jedis.Tuple;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 描述 Redis相关操作service
 *
 * @author housoo
 * @version 1.0
 * @created 2017-08-27 09:59:41
 */
public interface RedisService {

    /**
     * 设置值
     *
     * @param key
     * @param value
     * @return
     */
    public String set(final String key, final String value);

    /**
     * 获取值
     *
     * @param key
     * @return
     */
    public String get(final String key);

    /**
     * 删除
     *
     * @param key
     * @return
     */
    public Long del(final String key);

    /**
     * 根据KEY删除缓存
     *
     * @param keys
     */
    public void deleteByKeys(String[] keys);

    /**
     * 设置生存时间
     *
     * @param key
     * @return
     */
    public Long expire(final String key, final Integer seconds);

    /**
     * 设置值以及生存时间，单位为秒
     *
     * @param key
     * @param value
     * @param seconds
     * @return
     */
    public String set(final String key, final String value, final Integer seconds);

    /**
     * 获取Key列表
     *
     * @param keyPattern
     * @return
     */
    public List<Map<String, Object>> findKeyList(final String keyPattern);

    /**
     * 根据filter和配置信息获取KEY列表
     *
     * @param filter
     * @param fieldInfo
     * @return
     */
    public List<Map<String, Object>> findKeyList(SqlFilter filter, Map<String, Object> fieldInfo);

    /**
     * 设置KEY的失效时间
     *
     * @param keyNames
     * @param timelimit
     */
    public void expireKeys(final String keyNames, final int timelimit);

    /**
     * 设置KEY的具体失效截止时间
     *
     * @param keyNames
     * @param timestamp 时间戳
     */
    public void expireAtTimeStamp(final String keyNames, final Long timestamp);

    /**
     * 持久化KEY
     *
     * @param keyNames
     */
    public void persist(final String[] keyNames);

    /**
     * 获取KEY的基本信息
     *
     * @param keyName
     * @return
     */
    public Map<String, Object> getKeyInfo(final String keyName);

    /**
     * 根据filter获取KEY的值
     *
     * @param filter
     * @return
     */
    public List<Map> findValuesByFilter(SqlFilter filter);

    /**
     * 获取set的值
     *
     * @param keyName
     * @return
     */
    public Set<String> getSetValue(final String keyName);

    /**
     * 设置SET对象值
     *
     * @param keyName
     * @param setValue
     * @return
     */
    public Long setSetValue(final String keyName, final Set<String> setValue);

    /**
     * 获取zset的值
     *
     * @param keyName
     * @return
     */
    public Set<Tuple> getZSetValue(final String keyName);

    /**
     * 设置zSet的值
     *
     * @param keyName
     * @param zsetValue
     * @return
     */
    public Long setZSetValue(final String keyName, final Map<String, Double> zsetValue);

    /**
     * 获取hash的值
     *
     * @param keyName
     * @return
     */
    public Map<String, String> getHashValue(final String keyName);

    /**
     * 设置hash的值
     *
     * @param keyName
     * @param hashValue
     * @return
     */
    public String setHashValue(final String keyName, final Map<String, String> hashValue);

    /**
     * 获取list的值
     *
     * @param keyName
     * @return
     */
    public List<String> getListValue(final String keyName);

    /**
     * 设置list的值
     *
     * @param keyName
     * @param values
     * @return
     */
    public Long setListValue(final String keyName, final List<String> values);

    /**
     * 更新非string类型的值
     *
     * @param keyName
     * @param keyType
     * @param valueJson
     */
    public void updateNoStringValue(String keyName, String keyType, String valueJson);

    /**
     * 根据模糊匹配出KEY的集合
     *
     * @param keyName
     * @return
     */
    public Set<String> getKeySetByKeyPattern(String keyName);

    /**
     * 根据模糊匹配删除KEY
     *
     * @param keyName
     */
    public void deleteByLikeKeyName(String keyName);
}
