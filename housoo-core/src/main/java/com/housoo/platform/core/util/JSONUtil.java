package com.housoo.platform.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.*;

/**
 * Created by cjr on 2018/11/28.
 */
public class JSONUtil {

    public static boolean compare(Object o1, Object o2) {
        if (o1 instanceof Map && o2 instanceof Map) {
            return compareMap((Map) o1, (Map) o2);
        }
        if (o1 instanceof List && o2 instanceof List) {
            return compareList((List) o1, (List) o2);
        }
        return false;
    }

    private static boolean compareList(List list1, List list2) {
        if (list1.size() != list2.size()) {
            return false;
        }
        for (Object o : list1) {
            if (!list2.contains(o)) {
                return false;
            }
        }
        return true;
    }

    private static boolean compareMap(Map<String, Object> m1, Map<String, Object> m2) {
        Iterator<Map.Entry<String, Object>> iter1 = m1.entrySet().iterator();
        while (iter1.hasNext()) {
            Map.Entry<String, Object> entry1 = (Map.Entry<String, Object>) iter1.next();
            String m1value = entry1.getValue() == null ? "" : (String) entry1.getValue();
            String m2value = m2.get(entry1.getKey()) == null ? "" : (String) m2.get(entry1.getKey());
            if (!m1value.replace("\"", "").equals(m2value.replace("\"", ""))) {//若两个map中相同key对应的value不相等
                return false;
            }
        }
        return true;
    }

    private static boolean compareMap2(Map<String, Object> m1, Map<String, Object> m2) {
        Iterator<Map.Entry<String, Object>> iter1 = m1.entrySet().iterator();
        while (iter1.hasNext()) {
            Map.Entry<String, Object> entry1 = (Map.Entry<String, Object>) iter1.next();
            String m1value = entry1.getValue() == null ? "" : entry1.getValue().toString();
            String m2value = m2.get(entry1.getKey()) == null ? "" : (String) m2.get(entry1.getKey()).toString();
            if (!m1value.replace("\"", "").equals(m2value.replace("\"", ""))) {//若两个map中相同key对应的value不相等
                return false;
            }
        }
        return true;
    }

    public static List<Map<String, Object>> json2List(Object json) {
        JSONArray jsonArr = JSONArray.parseArray((String) json);
        List<Map<String, Object>> arrList = new ArrayList<Map<String, Object>>();
        if (jsonArr != null) {
            for (int i = 0; i < jsonArr.size(); ++i) {
                arrList.add(strJson2Map(jsonArr.getString(i)));
            }
        }
        return arrList;
    }

    public static Map<String, Object> strJson2Map(String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        Map<String, Object> resMap = new HashMap<String, Object>();
        Iterator<Map.Entry<String, Object>> it = jsonObject.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> param = (Map.Entry<String, Object>) it.next();
            if (param.getValue() instanceof JSONObject) {
                resMap.put(param.getKey(), strJson2Map(param.getValue().toString()));
            } else if (param.getValue() instanceof JSONArray) {
                resMap.put(param.getKey(), jsonToList(param.getValue()));
            } else {
                resMap.put(param.getKey(), JSONObject.toJSONString(param.getValue(), SerializerFeature.WriteClassName));
            }
        }
        return resMap;
    }

    /**
     * cjr 0312 新增
     *
     * @return
     */
    public static boolean compare2(Object o1, Object o2) {
        if (o1 instanceof Map && o2 instanceof Map) {
            return compareMap2((Map) o1, (Map) o2);
        }
        if (o1 instanceof List && o2 instanceof List) {
            return compareList((List) o1, (List) o2);
        }
        return false;
    }

    /**
     * cjr 0312 新增
     *
     * @param json
     * @return
     */
    public static List<Map<String, Object>> jsonToList(Object json) {
        JSONArray jsonArr = JSONArray.parseArray(JSON.toJSONString(json));
        List<Map<String, Object>> arrList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < jsonArr.size(); ++i) {
            arrList.add(jsonStr2Map(jsonArr.getString(i)));
        }
        return arrList;
    }

    /**
     * cjr 0312 新增
     *
     * @param json
     * @return
     */
    public static Map<String, Object> jsonStr2Map(String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        Map<String, Object> resMap = new HashMap<String, Object>();
        Iterator<Map.Entry<String, Object>> it = jsonObject.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> param = (Map.Entry<String, Object>) it.next();
            if (param.getValue() instanceof JSONObject) {
                resMap.put(param.getKey(), jsonStr2Map(param.getValue().toString()));
            } else if (param.getValue() instanceof JSONArray) {
                resMap.put(param.getKey(), jsonToList(param.getValue()));
            } else {
                resMap.put(param.getKey(), JSONObject.toJSONString(param.getValue(), SerializerFeature.WriteClassName));
            }
        }
        return resMap;
    }

    public static void main(String[] args) {

    }

}
