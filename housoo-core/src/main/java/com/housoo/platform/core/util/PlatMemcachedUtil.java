package com.housoo.platform.core.util;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Date;

import com.danga.MemCached.MemCachedClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Memcached工具类
 *
 * @author gf
 */
public class PlatMemcachedUtil {
    private static Logger logger = LoggerFactory.getLogger(PlatMemcachedUtil.class);

    /**
     * cachedClient
     */
    private static MemCachedClient cachedClient;

    static {
        if (cachedClient == null) {
            cachedClient = new MemCachedClient("memcachedPool");
        }
    }

    /**
     * 构造函数
     */
    private PlatMemcachedUtil() {
    }

    /**
     * 向缓存添加新的键值对。如果键已经存在，则之前的值将被替换.
     *
     * @param key   键
     * @param value 值
     * @return boolean
     */
    public static boolean set(String key, Object value) {
        return setExp(key, value, null);
    }

    /**
     * 向缓存添加新的键值对。如果键已经存在，则之前的值将被替换.
     *
     * @param key    键
     * @param value  值
     * @param expire 过期时间 New Date(1000*10)：十秒后过期
     * @return boolean
     */
    public static boolean set(String key, Object value, Date expire) {
        return setExp(key, value, expire);
    }

    /**
     * 向缓存添加新的键值对。如果键已经存在，则之前的值将被替换.
     *
     * @param key    键
     * @param value  值
     * @param expire 过期时间 New Date(1000*10)：十秒后过期
     * @return boolean
     */
    private static boolean setExp(String key, Object value, Date expire) {
        boolean flag = false;
        try {
            flag = cachedClient.set(key, value, expire);
        } catch (Exception e) {
            MemcachedLog.writeLog("Memcached set方法报错，key值：" + key + "\r\n" + exceptionWrite(e));
        }
        return flag;
    }

    /**
     * 仅当缓存中不存在键时，add 命令才会向缓存中添加一个键值对.
     *
     * @param key   键
     * @param value 值
     * @return boolean
     */
    public static boolean add(String key, Object value) {
        return addExp(key, value, null);
    }

    /**
     * 仅当缓存中不存在键时，add 命令才会向缓存中添加一个键值对.
     *
     * @param key    键
     * @param value  值
     * @param expire 过期时间 New Date(1000*10)：十秒后过期
     * @return boolean
     */
    public static boolean add(String key, Object value, Date expire) {
        return addExp(key, value, expire);
    }

    /**
     * 仅当缓存中不存在键时，add 命令才会向缓存中添加一个键值对.
     *
     * @param key    键
     * @param value  值
     * @param expire 过期时间 New Date(1000*10)：十秒后过期
     * @return boolean
     */
    private static boolean addExp(String key, Object value, Date expire) {
        boolean flag = false;
        try {
            flag = cachedClient.add(key, value, expire);
        } catch (Exception e) {
            MemcachedLog.writeLog("Memcached add方法报错，key值：" + key + "\r\n" + exceptionWrite(e));
        }
        return flag;
    }

    /**
     * 仅当键已经存在时，replace 命令才会替换缓存中的键.
     *
     * @param key   键
     * @param value 值
     * @return boolean
     */
    public static boolean replace(String key, Object value) {
        return replaceExp(key, value, null);
    }

    /**
     * 仅当键已经存在时，replace 命令才会替换缓存中的键.
     *
     * @param key    键
     * @param value  值
     * @param expire 过期时间 New Date(1000*10)：十秒后过期
     * @return boolean
     */
    public static boolean replace(String key, Object value, Date expire) {
        return replaceExp(key, value, expire);
    }

    /**
     * 仅当键已经存在时，replace 命令才会替换缓存中的键.
     *
     * @param key    键
     * @param value  值
     * @param expire 过期时间 New Date(1000*10)：十秒后过期
     * @return boolean
     */
    private static boolean replaceExp(String key, Object value, Date expire) {
        boolean flag = false;
        try {
            flag = cachedClient.replace(key, value, expire);
        } catch (Exception e) {
            MemcachedLog.writeLog("Memcached replace方法报错，key值：" + key + "\r\n" + exceptionWrite(e));
        }
        return flag;
    }

    /**
     * get 命令用于检索与之前添加的键值对相关的值.
     *
     * @param key 键
     * @return boolean
     */
    public static Object get(String key) {
        Object obj = null;
        try {
            obj = cachedClient.get(key);
        } catch (Exception e) {
            MemcachedLog.writeLog("Memcached get方法报错，key值：" + key + "\r\n" + exceptionWrite(e));
        }
        return obj;
    }

    /**
     * 删除 memcached 中的任何现有值.
     *
     * @param key 键
     * @return boolean
     */
    public static boolean delete(String key) {
        return deleteExp(key, null);
    }

    /**
     * 删除 memcached 中的任何现有值.
     *
     * @param key    键
     * @param expire 过期时间 New Date(1000*10)：十秒后过期
     * @return boolean
     */
    public static boolean delete(String key, Date expire) {
        return deleteExp(key, expire);
    }

    /**
     * 删除 memcached 中的任何现有值.
     *
     * @param key    键
     * @param expire 过期时间 New Date(1000*10)：十秒后过期
     * @return boolean
     */
    @SuppressWarnings("deprecation")
    private static boolean deleteExp(String key, Date expire) {
        boolean flag = false;
        try {
            flag = cachedClient.delete(key, expire);
        } catch (Exception e) {
            MemcachedLog.writeLog("Memcached delete方法报错，key值：" + key + "\r\n" + exceptionWrite(e));
        }
        return flag;
    }

    /**
     * 清理缓存中的所有键/值对.
     *
     * @return boolean
     */
    public static boolean flashAll() {
        boolean flag = false;
        try {
            flag = cachedClient.flushAll();
        } catch (Exception e) {
            MemcachedLog.writeLog("Memcached flashAll方法报错\r\n" + exceptionWrite(e));
        }
        return flag;
    }

    /**
     * 返回异常栈信息，String类型
     *
     * @param e Exception
     * @return boolean
     */
    private static String exceptionWrite(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    /**
     * @author gf
     * @ClassName: MemcachedLog
     * @Description: Memcached日志记录
     */
    private static class MemcachedLog {
        private final static String MEMCACHED_LOG = "D:\\memcached.log";
        private final static String LINUX_MEMCACHED_LOG = "/usr/local/logs/memcached.log";
        private static FileWriter fileWriter;
        private static BufferedWriter logWrite;
        // 获取PID，可以找到对应的JVM进程
        private final static RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        private final static String PID = runtime.getName();

        /**
         * 初始化写入流
         */
        static {
            try {
                String osName = System.getProperty("os.name");
                if (osName.indexOf("Windows") == -1) {
                    fileWriter = new FileWriter(MEMCACHED_LOG, true);
                } else {
                    fileWriter = new FileWriter(LINUX_MEMCACHED_LOG, true);
                }
                logWrite = new BufferedWriter(fileWriter);
            } catch (IOException e) {
                logger.error("memcached 日志初始化失败", e);
                closeLogStream();
            }
        }

        /**
         * 写入日志信息
         *
         * @param content 日志内容
         */
        public static void writeLog(String content) {
            try {
                logWrite.write("[" + PID + "] " + "- [" + PlatDateTimeUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss") + "]\r\n"
                        + content);
                logWrite.newLine();
                logWrite.flush();
            } catch (IOException e) {
                logger.error("memcached 写入日志信息失败", e);
            }
        }

        /**
         * 关闭流
         */
        private static void closeLogStream() {
            try {
                fileWriter.close();
                logWrite.close();
            } catch (IOException e) {
                logger.error("memcached 日志对象关闭失败", e);
            }
        }
    }
}
