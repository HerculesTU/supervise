package com.housoo.platform.core.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by cjr on 2018/7/2
 */
public class MD5AndSHA1Helper {

    /**
     * 将字节数组转换为16进制字符串
     *
     * @param byteArray
     * @return 16进制字符串
     */
    private static String byteArrayToHex(byte[] byteArray) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] resultCharArr = new char[byteArray.length * 2];
        int index = 0;
        for (byte b : byteArray) {
            resultCharArr[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArr[index++] = hexDigits[b & 0xf];
        }
        return new String(resultCharArr);
    }

    /**
     * 获取字符串的MD5
     *
     * @param input
     * @return
     */
    public static String getStringMD5(String input) {
        try {
            MessageDigest mDigest = MessageDigest.getInstance("MD5");
            byte[] inputArr = input.getBytes();
            mDigest.update(inputArr);
            byte[] resultArr = mDigest.digest();
            return byteArrayToHex(resultArr);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取文件的MD5，可以替换为SHA1
     *
     * @param fileUrl
     * @return
     * @throws IOException
     */
    @SuppressWarnings("resource")
    public static String getFileMD5(String fileUrl) throws IOException {
        int bufferSize = 1024 * 1024;
        FileInputStream fileInputStream = null;
        DigestInputStream digestInputStream = null;
        try {
            // 可以替换为"SHA1"
            MessageDigest mDigest = MessageDigest.getInstance("MD5");
            fileInputStream = new FileInputStream(fileUrl);
            digestInputStream = new DigestInputStream(fileInputStream, mDigest);
            byte[] buffer = new byte[bufferSize];
            while (digestInputStream.read(buffer) > 0) {
            }
            mDigest = digestInputStream.getMessageDigest();
            byte[] resultArr = mDigest.digest();
            return byteArrayToHex(resultArr);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            fileInputStream.close();
            digestInputStream.close();
        }
        return null;
    }

    /**
     * 根据文件流获取文件md5值
     *
     * @param file
     * @return String
     * @throws FileNotFoundException
     */
    public static String getMd5ByFile(File file) throws FileNotFoundException {
        String value = null;
        FileInputStream in = new FileInputStream(file);
        try {
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

    /**
     * 根据文件流获取文件md5值
     *
     * @param multipartFile
     * @return String
     * @throws FileNotFoundException
     */
    public static String getMd5ByFile(MultipartFile multipartFile) throws FileNotFoundException {
        /*
        //将springMVC的MultipartFile转换成java的File
        CommonsMultipartFile cf= (CommonsMultipartFile)multipartFile;
        DiskFileItem dfi = (DiskFileItem)cf.getFileItem();
        File file = dfi.getStoreLocation();

        String value = null;
        FileInputStream in = new FileInputStream(file);
        try {
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/
        String value = "";
        try {
            File file = null;
            if ("".equals(multipartFile) || multipartFile.getSize() <= 0) {
                file = null;
            } else {
                InputStream in = multipartFile.getInputStream();
                file = new File(multipartFile.getOriginalFilename());
                PlatFileUtil.inputStreamToFile(in, file);
            }
            value = DigestUtils.md5Hex(new FileInputStream(file));
            File deleteFile = new File(file.toURI());
            deleteFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 获取String的SHA1
     *
     * @param input
     * @return
     */
    public static String getStringSHA1(String input) {
        try {
            MessageDigest mDigest = MessageDigest.getInstance("SHA1");
            byte[] inputArr = input.getBytes();
            mDigest.update(inputArr);
            byte[] resultArr = mDigest.digest();
            return byteArrayToHex(resultArr);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取文件的SHA1，可以替换为MD5
     *
     * @param fileUrl
     * @return
     * @throws IOException
     */
    @SuppressWarnings("resource")
    public static String getFileSHA1(String fileUrl) throws IOException {
        int bufferSize = 1024 * 1024;
        FileInputStream fileInputStream = null;
        DigestInputStream digestInputStream = null;
        try {
            // 可以替换为"MD5"
            MessageDigest mDigest = MessageDigest.getInstance("SHA1");
            fileInputStream = new FileInputStream(fileUrl);
            digestInputStream = new DigestInputStream(fileInputStream, mDigest);
            byte[] buffer = new byte[bufferSize];
            while (digestInputStream.read(buffer) > 0) {
            }
            mDigest = digestInputStream.getMessageDigest();
            byte[] resultArr = mDigest.digest();
            return byteArrayToHex(resultArr);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            fileInputStream.close();
            digestInputStream.close();
        }
        return null;
    }

    /**
     * 根据文件流获取文件SHA1值
     *
     * @param multipartFile
     * @return String
     * @throws FileNotFoundException
     */
    public static String getSHA1ByFile(MultipartFile multipartFile) throws FileNotFoundException {
        /*
        //将springMVC的MultipartFile转换成java的File
        CommonsMultipartFile cf= (CommonsMultipartFile)multipartFile;
        DiskFileItem dfi = (DiskFileItem)cf.getFileItem();
        File file = dfi.getStoreLocation();

        String value = null;
        FileInputStream in = new FileInputStream(file);
        try {
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("SHA1");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/

        String value = "";
        try {
            File file = null;
            if ("".equals(multipartFile) || multipartFile.getSize() <= 0) {
                file = null;
            } else {
                InputStream in = multipartFile.getInputStream();
                file = new File(multipartFile.getOriginalFilename());
                PlatFileUtil.inputStreamToFile(in, file);
            }
            value = DigestUtils.sha1Hex(new FileInputStream(file));
            File deleteFile = new File(file.toURI());
            deleteFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static void main(String[] args) {
        String path = "E:\\IntelliJIDEAWorkSpace\\金融财产邮件查询系统\\Direct.Bank.Service\\target\\direct.service-0.0.1-alpha\\upload\\181202019\\feedback\\1530839952125.zip";
        String str = "12345678";
        String result;
        try {
            result = MD5AndSHA1Helper.getFileMD5(path);
            System.out.println(result);
            System.out.println(MD5AndSHA1Helper.getFileMD5(str));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
