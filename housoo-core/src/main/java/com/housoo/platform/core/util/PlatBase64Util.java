package com.housoo.platform.core.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.io.*;
import java.security.Key;
import java.security.SecureRandom;

/**
 * base64编码工具类
 */
public class PlatBase64Util {

    private Key key;
    public static String generateKeyStr = "china_shanxi_netchina_housoo_java";
    private static String encryptAlgorithm = "DES";

    public PlatBase64Util() {
        getKey(generateKeyStr);
    }

    /**
     * 根据参数生成KEY
     *
     * @param strKey 密钥字符串
     */
    public void getKey(String strKey) {
        try {
            KeyGenerator generator = KeyGenerator
                    .getInstance(encryptAlgorithm);
            generator.init(new SecureRandom(strKey.getBytes()));
            key = generator.generateKey();
            generator = null;
        } catch (Exception e) {
            PlatLogUtil.printStackTrace(e);
        }
    }

    /**
     * 获取加密字符串 输入String明文,输出String密文
     *
     * @param unencryptedStr String明文
     * @return encryptStr String密文
     */
    public static String getEncString(String unencryptedStr) {
        byte[] encryptByte = null;
        byte[] unencryptedByte = null;
        String encryptStr = "";
        BASE64Encoder base64Encoder = new BASE64Encoder();
        try {
            unencryptedByte = unencryptedStr.getBytes("UTF8");
            encryptStr = base64Encoder.encode(unencryptedByte);
        } catch (Exception e) {
            PlatLogUtil.printStackTrace(e);
        } finally {
            base64Encoder = null;
            unencryptedByte = null;
            encryptByte = null;
        }
        return encryptStr;
    }

    /**
     * 获取解密字符串 输入String密文,输出String明文
     *
     * @param encryptStr String密文
     * @return unencryptedStr String明文
     */
    public static String getDesString(String encryptStr) {
        BASE64Decoder base64Decoder = new BASE64Decoder();
        byte[] encryptByte = null;
        String unencryptedStr = "";
        try {
            encryptByte = base64Decoder.decodeBuffer(encryptStr);
            unencryptedStr = new String(encryptByte, "UTF8");
        } catch (Exception e) {
            PlatLogUtil.printStackTrace(e);
        } finally {
            base64Decoder = null;
            encryptByte = null;
        }
        return unencryptedStr;
    }

    /**
     * 编码
     *
     * @param @param bt
     * @return String 返回类型
     * @Description: (BASE64编码以byte[]为输入 ， 以String字符串为输出)
     */
    public static String encode(byte[] bt) {
        BASE64Encoder encoder = new BASE64Encoder();
        if (bt != null) {
            String bTemp = encoder.encode(bt);
            bTemp = bTemp.replaceAll("[\\s*\t\n\r]", "");
            return bTemp;
        }
        return "";
    }

    /**
     * 解码
     *
     * @param @param bt
     * @return byte[] 返回类型
     * @Description: (BASE64解码, 以byte[]数组为输入 ， byte[]为输出)
     */
    public static byte[] decode(byte[] bt) {
        if (bt != null) {
            try {
                return new BASE64Decoder().decodeBuffer(new ByteArrayInputStream(bt));
            } catch (IOException e) {
                PlatLogUtil.printStackTrace(e);
            }
        }
        return null;
    }

    /**
     * 为getEncString方法提供服务
     * 加密以byte[]明文输入,byte[]密文输出
     *
     * @param byteS byte[]明文
     * @return byte[]密文
     */
    private byte[] getEncCode(byte[] byteS) {
        byte[] byteFina = null;
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(encryptAlgorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byteFina = cipher.doFinal(byteS);
        } catch (Exception e) {
            PlatLogUtil.printStackTrace(e);
        } finally {
            cipher = null;
        }
        return byteFina;
    }

    /**
     * 为getDesString方法提供服务
     * 解密以byte[]密文输入,以byte[]明文输出
     *
     * @param byteD byte[]密文
     * @return byte[]明文
     */
    private byte[] getDesCode(byte[] byteD) {
        Cipher cipher;
        byte[] byteFina = null;
        try {
            cipher = Cipher.getInstance(encryptAlgorithm);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byteFina = cipher.doFinal(byteD);
        } catch (Exception e) {
            PlatLogUtil.printStackTrace(e);
        } finally {
            cipher = null;
        }
        return byteFina;
    }

    /**
     * 将字符串写入文件中
     *
     * @param str
     * @param path
     * @param @throws IOException
     * @return void
     * @Description: (将字符串写入指定文件)
     */
    public void writeStringTofile(String str, String path) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        File file = new File(path);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fw = new FileWriter(path, true);
            bw = new BufferedWriter(fw);
            bw.write(str);
            PlatLogUtil.println("字符串长度: " + str.length());
        } catch (IOException e) {
            PlatLogUtil.printStackTrace(e);
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException e) {
                PlatLogUtil.printStackTrace(e);
            }
        }
    }

    /**
     * 读取文件返回字节数组
     *
     * @param path
     * @return byte[]
     */
    public static byte[] readString(String path) {
        File file = new File(path);
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            //size为字串的长度，这里一次性读完
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            in.close();
            return buffer;
        } catch (IOException e) {
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    PlatLogUtil.printStackTrace(e);
                }
            }
        }
    }

    /**
     * 文件转base64编码
     *
     * @param file
     * @return String
     */
    public static String fileToBase64(File file) {
        String base64Str = null;
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            byte[] bytes = new byte[in.available()];
            in.read(bytes);
            base64Str = PlatBase64Util.encode(bytes);
        } catch (Exception e) {
            PlatLogUtil.printStackTrace(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    PlatLogUtil.printStackTrace(e);
                }
            }
        }
        return base64Str;
    }

    /**
     * main方法
     *
     * @param args
     */
    public static void main(String[] args) {
        File file = new File("C:\\31320800004_通知书2\\31320800004_通知书2_00.png");
        InputStream in = null;
        String s = null;
        try {
            in = new FileInputStream(file);
            byte[] bytes = new byte[in.available()];
            in.read(bytes);
            s = PlatBase64Util.encode(bytes);
        } catch (Exception e) {
            PlatLogUtil.printStackTrace(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    PlatLogUtil.printStackTrace(e);
                }
            }
        }
        PlatLogUtil.println(s);
    }

}
