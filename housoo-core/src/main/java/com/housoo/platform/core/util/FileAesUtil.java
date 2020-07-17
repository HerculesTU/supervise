package com.housoo.platform.core.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.GeneralSecurityException;

/**
 * Created by gf on 2018/9/17
 */
public class FileAesUtil {

    /**
     * AES加密
     *
     * @param data       待加密的数据
     * @param rawKeyData 加密密匙
     * @return
     * @throws GeneralSecurityException
     */
    public static byte[] aesEncrypt(byte[] data, byte[] rawKeyData)
            throws GeneralSecurityException {
        // 处理密钥
        SecretKeySpec key = new SecretKeySpec(rawKeyData, "AES");
        // 加密
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    /**
     * AES解密
     *
     * @param data       待解密的数据
     * @param rawKeyData 加密密匙
     * @return
     * @throws GeneralSecurityException
     */
    public static byte[] aesDecrypt(byte[] data, byte[] rawKeyData)
            throws GeneralSecurityException {
        // 处理密钥
        SecretKeySpec key = new SecretKeySpec(rawKeyData, "AES");
        // 解密
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    /**
     * 加密文件
     *
     * @param key        加密密鈅
     * @param sourceFile 源文件
     * @param destFile   加密后的文件
     * @throws Exception
     */
    public static void decryptFile(String key, String sourceFile, String destFile) throws Exception {
        // AES算法要求密鈅128位(16字节)
        byte[] rawKeyData = key.getBytes("UTF-8");
        // 读取文件内容
        File file = new File(sourceFile);
        byte[] source = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(source, 0, (int) file.length());
        fis.close();
        // 加密
        byte[] enc = aesEncrypt(source, rawKeyData);
        // 输出到文件
        FileOutputStream fos = new FileOutputStream(new File(destFile));
        fos.write(enc, 0, enc.length);
        fos.close();
    }

    /**
     * 解密文件
     *
     * @param key        解密密鈅
     * @param sourceFile 源文件
     * @param destFile   解密后的文件
     * @throws Exception
     */
    public static void encryptFile(String key, String sourceFile, String destFile) throws Exception {
        // AES算法要求密鈅128位(16字节)
        byte[] rawKeyData = key.getBytes("UTF-8");
        // 读取文件内容(为了简单忽略错误处理）
        File file = new File(sourceFile);
        byte[] data = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(data, 0, (int) file.length());
        fis.close();
        // 解密
        byte[] dec = aesDecrypt(data, rawKeyData);
        File newFile = new File(destFile);
        if (!newFile.getParentFile().exists()) {
            newFile.getParentFile().mkdirs();
            newFile.createNewFile();
        }
        // 输出到文件
        FileOutputStream fos = new FileOutputStream(newFile);
        fos.write(dec, 0, dec.length);
        fos.close();
    }

    public static void main(String[] args) throws Exception {
        FileAesUtil fileAesUtil = new FileAesUtil();
        encryptFile("2121400001812101", "D:\\2121400001812101_NWCXFK_202411_90f7ab1dc67ee39c7ddb4779076d2756.NJWF",
                "D:\\2121400001812101_NWCXFK.zip");
        //fileAesUtil.decryptFile("2121400001809301", "C:\\Users\\Administrator\\Desktop\\2121400001809301_08_8ebc7c8c04cca4fbeef639e1a81283ba.zip", "C:\\Users\\Administrator\\Desktop\\2121400001809301_08.jwf");
    }
}
