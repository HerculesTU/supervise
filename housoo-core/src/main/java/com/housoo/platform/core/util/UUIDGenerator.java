package com.housoo.platform.core.util;

import java.net.InetAddress;

/**
 * 描述 UUID生成器
 *
 * @author
 * @version 1.0
 * @created 2016年2月21日 下午5:16:47
 */
public class UUIDGenerator {
    /**
     * IP
     */
    private static final int IP;
    /**
     * JVM
     */
    private static final int JVM = (int) (System.currentTimeMillis() >>> 8);
    /**
     * sep
     */
    private final static String SEP = "";
    /**
     * counter
     */
    private static short counter = (short) 0;

    static {
        int ipadd;
        try {
            ipadd = iptoInt(InetAddress.getLocalHost().getAddress());
        } catch (Exception e) {
            ipadd = 0;
        }
        IP = ipadd;
    }

    /**
     * 描述
     *
     * @author
     * @created 2014年9月6日 上午8:51:05
     */
    public UUIDGenerator() {
    }

    /**
     * 描述
     *
     * @param bytes
     * @return
     * @author
     * @created 2014年9月6日 上午8:50:47
     */
    public static int iptoInt(byte[] bytes) {
        int result = 0;
        for (int i = 0; i < 4; i++) {
            result = (result << 8) - Byte.MIN_VALUE + (int) bytes[i];
        }
        return result;
    }

    /**
     * Unique across JVMs on this machine (unless they load this class in the
     * same quater second - very unlikely)
     */
    protected static int getJVM() {
        return JVM;
    }

    /**
     * Unique in a millisecond for this JVM instance (unless there are >
     * Short.MAX_VALUE instances created in a millisecond)
     */
    protected static short getCount() {
        synchronized (UUIDGenerator.class) {
            if (counter < 0) {
                counter = 0;
            }
            return counter++;
        }
    }

    /**
     * Unique in a local network
     */
    protected static int getIP() {
        return IP;
    }

    /**
     * Unique down to millisecond
     */
    protected static short getHiTime() {
        return (short) (System.currentTimeMillis() >>> 32);
    }

    /**
     * 描述
     *
     * @return
     * @author
     * @created 2014年9月6日 上午8:51:15
     */
    protected static int getLoTime() {
        return (int) System.currentTimeMillis();
    }

    /**
     * 描述
     *
     * @param intval
     * @return
     * @author
     * @created 2014年9月6日 上午8:51:24
     */
    protected static String format(int intval) {
        String formatted = Integer.toHexString(intval);
        StringBuffer buf = new StringBuffer("00000000");
        buf.replace(8 - formatted.length(), 8, formatted);
        return buf.toString();
    }

    /**
     * 描述
     *
     * @param shortval
     * @return
     * @author
     * @created 2014年9月6日 上午8:51:28
     */
    protected static String format(short shortval) {
        String formatted = Integer.toHexString(shortval);
        StringBuffer buf = new StringBuffer("0000");
        buf.replace(4 - formatted.length(), 4, formatted);
        return buf.toString();
    }

    /**
     * 描述
     *
     * @return
     * @author
     * @created 2014年9月6日 上午8:51:32
     */
    public static String getUUID() {
        return new StringBuffer(36).append(format(getIP())).append(SEP)
                .append(format(getJVM())).append(SEP)
                .append(format(getHiTime())).append(SEP)
                .append(format(getLoTime())).append(SEP)
                .append(format(getCount())).toString();
    }
}
