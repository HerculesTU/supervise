package com.housoo.platform.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;

import java.util.List;
import java.util.Map;

/**
 * @author
 */
public class PlatJsonUtil {

    /**
     * 在一个JSON字符串中查询单条有效数据
     *
     * @param keyName        字段的名称
     * @param keyValue       字段值
     * @param jsonDataSource JSON数据源
     * @return
     */
    public static Map getUniqueData(String keyName, String keyValue, String jsonDataSource) {
        StringBuffer pathExp = new StringBuffer("$[?(@.");
        pathExp.append(keyName).append("='").append(keyValue).append("')]");
        List<Map> list = JSON.parseArray(JSONPath.eval(JSON.parseArray(jsonDataSource), pathExp.toString())
                .toString(), Map.class);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 根据路径获取值
     *
     * @param path
     * @param jsonString
     * @return
     */
    public static String getFieldByPath(String path, String jsonString) {
        JSONObject jsonObject = JSON.parseObject(jsonString);
        String[] fieldPathArray = path.split("/");
        JSONObject targetObj = jsonObject;
        String resultString = null;
        for (int i = 0; i < fieldPathArray.length; i++) {
            String fPath = fieldPathArray[i];
            if (i != fieldPathArray.length - 1) {
                targetObj = targetObj.getJSONObject(fPath);
            } else {
                resultString = targetObj.get(fPath).toString();
            }
        }
        return resultString;
    }
}
