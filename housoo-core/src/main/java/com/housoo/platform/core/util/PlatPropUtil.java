package com.housoo.platform.core.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 描述 封装读取配置文件的工具类
 *
 * @author
 * @created 2017年1月5日 下午4:10:42
 */
public class PlatPropUtil {
    /**
     * 获取配置文件对象
     *
     * @param propFileName:文件名称,例如(config.properties)
     * @return
     */
    public static Properties readProperties(String propFileName) {
        Properties p = new Properties();
        InputStreamReader inputStream = null;
        try {
            inputStream = new InputStreamReader(PlatPropUtil.class.getClassLoader().
                    getResourceAsStream(propFileName), "UTF-8");
            p.load(inputStream);
        } catch (IOException e) {
            PlatLogUtil.printStackTrace(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    PlatLogUtil.printStackTrace(e);
                }
            }
        }
        return p;
    }

    /**
     * 读取配置文件的值
     *
     * @param propFileName:文件名称,例如(config.properties)
     * @param key:KEY
     * @return
     */
    public static String getPropertyValue(String propFileName, String key) {
        Properties pro = readProperties(propFileName);
        return (String) pro.get(key);
    }
}
