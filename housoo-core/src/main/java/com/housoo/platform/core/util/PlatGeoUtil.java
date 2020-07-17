package com.housoo.platform.core.util;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 地理位置工具类
 */
public class PlatGeoUtil {
    /**
     * 根据IP地址获取地理位置信息
     *
     * @param ipAddress
     * @return country:国家 province:省份 city:地市
     */
    public static Map<String, String> getGeoLocByIp(String ipAddress) {
        Map<String, String> info = new HashMap<String, String>();
        try {
            String url = "http://ip.taobao.com/service/getIpInfo.php";
            String param = "ip=" + ipAddress;
            String result = PlatHttpUtil.httpGetRequest(url, param, "UTF-8");
            Map<String, Object> map = JSON.parseObject(result, Map.class);
            Map<String, Object> data = (Map<String, Object>) map.get("data");

            info.put("country", data.get("country").toString());
            info.put("province", data.get("region").toString());
            info.put("city", data.get("city").toString());
        } catch (Exception e) {
            PlatLogUtil.doNothing(e);
        }
        return info;
    }
}
