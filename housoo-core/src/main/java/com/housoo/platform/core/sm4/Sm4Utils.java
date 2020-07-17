package com.housoo.platform.core.sm4;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author housoo
 *
 * 2017年8月1日
 */
public class Sm4Utils {
    /**
     * log4J声明
     */
    private static Log log = LogFactory.getLog(Sm4Utils.class);
    /**
     * secretKey
     */
    private String secretKey = "";
    /**
     * iv
     */
    private String iv = "";
    /**
     * hexString
     */
    private boolean hexString = false;

    private static Pattern STRING_PATTERN = Pattern.compile("\\s*|\t|\r|\n");

    /**
     * 描述
     *
     * @author Toddle Chen
     * @created Jul 27, 2017 11:34:33 AM
     */
    public Sm4Utils() {
    }

    /**
     * 描述
     *
     * @param secretkey
     * @param ivString
     * @param hexBool
     * @author Toddle Chen
     * @created Jul 27, 2017 11:34:36 AM
     */
    public Sm4Utils(String secretkey, String ivString, boolean hexBool) {
        this.secretKey = secretkey;
        this.iv = ivString;
        this.hexString = hexBool;
    }

    /**
     * 描述
     *
     * @param args
     * @throws IOException
     * @author Toddle Chen
     * @created Jul 27, 2017 11:34:52 AM
     */
    public static void main(String[] args) throws IOException {
        String plainText = "（1）本文件是福建省经济信息中心(以下简称买方)为拟建的";
        Sm4Utils sm4 = new Sm4Utils("4028b8815ccf2ffe", "F54A3s2D1g", false);
        // sm4.secretKey =
        // "4028b8815ccf2ffe";//"UISwD9fW6cFh9SNS";//JeF8U9wHFOMfs2Y8//平台分配的加密盐
        // sm4.hexString = false;
        // sm4.iv = "F54A3s2D1g";//初始化向量IV
        System.out.println("ECB模式");
        String cipherText = sm4.encryptDataECB(plainText);
        System.out.println("密文: " + cipherText);
        plainText = sm4.decryptDataECB(cipherText);
        System.out.println("明文: " + plainText);

        System.out.println("CBC模式");
        cipherText = sm4.encryptDataCBC(plainText);
        System.out.println("密文: " + cipherText);
        plainText = sm4.decryptDataCBC(cipherText);
        System.out.println("明文: " + plainText);
    }

    /**
     * 描述
     *
     * @param plainText
     * @return
     * @author Toddle Chen
     * @created Jul 27, 2017 11:34:40 AM
     */
    public String encryptDataECB(String plainText) {
        try {
            Sm4Context ctx = new Sm4Context();
            ctx.isPadding = true;
            ctx.mode = Sm4.SM4_ENCRYPT;

            byte[] keyBytes;
            if (hexString) {
                keyBytes = Sm4StringUtil.hexStringToBytes(secretKey);
            } else {
                keyBytes = secretKey.getBytes();
            }

            Sm4 sm4 = new Sm4();
            sm4.sm4SetkeyEnc(ctx, keyBytes);
            byte[] encrypted = sm4.sm4CryptEcb(ctx, plainText.getBytes("GBK"));
            String cipherText = new BASE64Encoder().encode(encrypted);
            if (cipherText != null && cipherText.trim().length() > 0) {
                //Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = STRING_PATTERN.matcher(cipherText);
                cipherText = m.replaceAll("");
            }
            return cipherText;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 描述
     *
     * @param cipherText
     * @return
     * @author Toddle Chen
     * @created Jul 27, 2017 11:34:44 AM
     */
    public String decryptDataECB(String cipherText) {
        try {
            Sm4Context ctx = new Sm4Context();
            ctx.isPadding = true;
            ctx.mode = Sm4.SM4_DECRYPT;

            byte[] keyBytes;
            if (hexString) {
                keyBytes = Sm4StringUtil.hexStringToBytes(secretKey);
            } else {
                keyBytes = secretKey.getBytes();
            }

            Sm4 sm4 = new Sm4();
            sm4.sm4SetkeyDec(ctx, keyBytes);
            byte[] decrypted = sm4.sm4CryptEcb(ctx, new BASE64Decoder().decodeBuffer(cipherText));
            return new String(decrypted, "GBK");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 描述
     *
     * @param plainText
     * @return
     * @author Toddle Chen
     * @created Jul 27, 2017 11:34:47 AM
     */
    public String encryptDataCBC(String plainText) {
        return encryptDataCBC(plainText, null);
    }

    /**
     * 描述
     *
     * @param plainText
     * @return
     * @author Toddle Chen
     * @created Jul 27, 2017 11:34:47 AM
     */
    public String encryptDataCBC(String plainText, String charCode) {
        if (StringUtils.isEmpty(charCode)) {
            charCode = "GBK";
        }
        try {
            Sm4Context ctx = new Sm4Context();
            ctx.isPadding = true;
            ctx.mode = Sm4.SM4_ENCRYPT;

            byte[] keyBytes;
            byte[] ivBytes;
            if (hexString) {
                keyBytes = Sm4StringUtil.hexStringToBytes(secretKey);
                ivBytes = Sm4StringUtil.hexStringToBytes(iv);
            } else {
                keyBytes = secretKey.getBytes();
                ivBytes = iv.getBytes();
            }

            Sm4 sm4 = new Sm4();
            sm4.sm4SetkeyEnc(ctx, keyBytes);
            byte[] encrypted = sm4.sm4CryptCbc(ctx, ivBytes, plainText.getBytes(charCode));
            String cipherText = new BASE64Encoder().encode(encrypted);
            if (cipherText != null && cipherText.trim().length() > 0) {
                //Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = STRING_PATTERN.matcher(cipherText);
                cipherText = m.replaceAll("");
            }
            return cipherText;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 描述
     *
     * @param cipherText
     * @return
     * @author Toddle Chen
     * @created Jul 27, 2017 11:34:49 AM
     */
    public String decryptDataCBC(String cipherText) {
        return decryptDataCBC(cipherText, null);
    }

    /**
     * 描述
     *
     * @param cipherText
     * @return
     * @author Toddle Chen
     * @created Jul 27, 2017 11:34:49 AM
     */
    public String decryptDataCBC(String cipherText, String charCode) {
        if (StringUtils.isEmpty(charCode)) {
            charCode = "GBK";
        }
        try {
            Sm4Context ctx = new Sm4Context();
            ctx.isPadding = true;
            ctx.mode = Sm4.SM4_DECRYPT;

            byte[] keyBytes;
            byte[] ivBytes;
            if (hexString) {
                keyBytes = Sm4StringUtil.hexStringToBytes(secretKey);
                ivBytes = Sm4StringUtil.hexStringToBytes(iv);
            } else {
                keyBytes = secretKey.getBytes();
                ivBytes = iv.getBytes();
            }

            Sm4 sm4 = new Sm4();
            sm4.sm4SetkeyDec(ctx, keyBytes);
            byte[] decrypted = sm4.sm4CryptCbc(ctx, ivBytes, new BASE64Decoder().decodeBuffer(cipherText));
            return new String(decrypted, charCode);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
