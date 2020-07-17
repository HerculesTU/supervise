package com.housoo.platform.core.util;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.json.XML;

import java.util.Map;

/**
 * @author <p>
 * 2017年9月6日
 */
public class PlatXmlUtil {

    /**
     * 将XML转换成MAP
     *
     * @param xml
     * @return
     */
    public static Map<String, Object> xmlToMap(String xml) {
        return XML.toJSONObject(xml).toMap();
    }

    /**
     * 将XML转换成JSONString
     *
     * @param xml
     * @return
     */
    public static String xmlToJsonString(String xml) {
        // 将xml转为json
        return XML.toJSONObject(xml).toString();
    }

    /**
     * 将字符串转换成document对象
     *
     * @param s
     * @return
     * @author
     */
    public static Document stringToDocument(String s) {
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(s);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return doc;
    }
}
