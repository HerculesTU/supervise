package com.housoo.platform.core.util;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * ZIP压缩文件操作工具类 支持密码 依赖zip4j jar包 版本1.3.2
 *
 * @author cjr
 *         Created 2018/07/20
 */
public class PlatZipUtil {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(PlatZipUtil.class);
    private static final int BUFFER = 2048;

    /**
     * 使用给定密码解压指定的ZIP压缩文件到指定目录,如果无密码，则密码输入空: ""
     * <p>
     * 如果指定目录不存在,可以自动创建,不合法的路径将导致异常
     *
     * @param zip      指定的ZIP压缩文件
     * @param dest     解压目录
     * @param password ZIP文件的密码
     * @return 解压后文件数组
     * @throws ZipException 压缩文件有损坏或者解压缩失败抛出
     */
    public static File[] unzip(String zip, String dest, String password)
            throws ZipException {
        File zipFile = new File(zip);
        return unzip(zipFile, dest, password);
    }

    /**
     * 使用给定密码解压指定的ZIP压缩文件到当前目录，有密码时输入密码，无密码时输入空 ""
     *
     * @param zip      指定的ZIP压缩文件
     * @param password ZIP文件的密码
     * @return 解压后文件数组
     * @throws ZipException 压缩文件有损坏或者解压缩失败抛出
     */
    public static File[] unzip(String zip, String password) throws ZipException {
        File zipFile = new File(zip);
        File parentDir = zipFile.getParentFile();
        return unzip(zipFile, parentDir.getAbsolutePath(), password);
    }

    /**
     * 使用给定密码解压指定的ZIP压缩文件到指定目录
     * <p>
     * 如果指定目录不存在,可以自动创建,不合法的路径将导致异常被抛出
     *
     * @param zipFile  指定的ZIP压缩文件
     * @param dest     解压目录
     * @param password ZIP文件的密码
     * @return 解压后文件数组
     * @throws ZipException 压缩文件有损坏或者解压缩失败抛出
     */
    public static File[] unzip(File zipFile, String dest, String password)
            throws ZipException {
        ZipFile zFile = new ZipFile(zipFile);
        zFile.setFileNameCharset("UTF-8");
        if (!zFile.isValidZipFile()) {
            throw new ZipException("压缩文件不合法,可能被损坏！");
        }
        File destDir = new File(dest);
        if (destDir.isDirectory() && !destDir.exists()) {
            destDir.mkdir();
        }
        if (zFile.isEncrypted()) {
            zFile.setPassword(password.toCharArray());
        }
        zFile.extractAll(dest);

        List<FileHeader> headerList = zFile.getFileHeaders();
        List<File> extractedFileList = new ArrayList<File>();
        for (FileHeader fileHeader : headerList) {
            if (!fileHeader.isDirectory()) {
                extractedFileList.add(new File(destDir, fileHeader
                        .getFileName()));
            }
        }
        File[] extractedFiles = new File[extractedFileList.size()];
        extractedFileList.toArray(extractedFiles);
        return extractedFiles;
    }

    /**
     * 压缩指定文件到当前文件夹
     *
     * @param src 要压缩的指定文件
     * @return 最终的压缩文件存放的绝对路径, 如果为null则说明压缩失败.
     */
    public static String zip(String src) {
        return zip(src, null);
    }

    /**
     * 使用给定密码压缩指定文件或文件夹到当前目录
     *
     * @param src      要压缩的文件
     * @param password 压缩使用的密码
     * @return 最终的压缩文件存放的绝对路径, 如果为null则说明压缩失败.
     */
    public static String zip(String src, String password) {
        return zip(src, null, password);
    }

    /**
     * 使用给定密码压缩指定文件或文件夹到指定目录
     *
     * @param src      要压缩的文件
     * @param dest     压缩文件存放路径
     * @param password 压缩使用的密码
     * @return 最终的压缩文件存放的绝对路径, 如果为null则说明压缩失败.
     */
    public static String zip(String src, String dest, String password) {
        return zip(src, dest, true, password);
    }

    /**
     * 使用给定密码压缩指定文件或文件夹到指定位置.
     * <p>
     * dest可传最终压缩文件存放的绝对路径,也可以传存放目录,也可以传null或者""
     * 如果传null或者""则将压缩文件存放在当前目录,即跟源文件同目录,压缩文件名取源文件名,以.zip为后缀
     * 如果以路径分隔符(File.separator)结尾,则视为目录,压缩文件名取源文件名,以.zip为后缀,否则视为文件名
     *
     * @param src         要压缩的文件或文件夹路径
     * @param dest        压缩文件存放路径
     * @param isCreateDir 是否在压缩文件里创建目录,仅在压缩文件为目录时有效
     *                    如果为false,将直接压缩目录下文件到压缩文件
     * @param password    压缩使用的密码
     * @return 最终的压缩文件存放的绝对路径, 如果为null则说明压缩失败
     */
    public static String zip(String src, String dest, boolean isCreateDir,
                             String password) {
        File srcFile = new File(src);
        dest = buildDestinationZipFilePath(srcFile, dest);
        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE); // 压缩方式
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL); // 压缩级别
        if (!StringUtils.isEmpty(password)) {
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD); // 加密方式
            parameters.setPassword(password.toCharArray());
        }
        try {
            ZipFile zipFile = new ZipFile(dest);
            if (srcFile.isDirectory()) {
                // 如果不创建目录的话,将直接把给定目录下的文件压缩到压缩文件,即没有目录结构
                if (!isCreateDir) {
                    File[] subFiles = srcFile.listFiles();
                    ArrayList<File> temp = new ArrayList<File>();
                    Collections.addAll(temp, subFiles);
                    zipFile.addFiles(temp, parameters);
                    return dest;
                }
                zipFile.addFolder(srcFile, parameters);
            } else {
                zipFile.addFile(srcFile, parameters);
            }
            return dest;
        } catch (ZipException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 使用给定密码压缩指定文件或文件夹到指定位置.
     * <p>
     * dest可传最终压缩文件存放的绝对路径,也可以传存放目录,也可以传null或者""
     * 如果传null或者""则将压缩文件存放在当前目录,即跟源文件同目录,压缩文件名取源文件名,以.zip为后缀
     * 如果以路径分隔符(File.separator)结尾,则视为目录,压缩文件名取源文件名,以.zip为后缀,否则视为文件名
     *
     * @param src         要压缩的文件或文件夹路径
     * @param dest        压缩文件存放路径
     * @param isCreateDir 是否在压缩文件里创建目录,仅在压缩文件为目录时有效
     *                    如果为false,将直接压缩目录下文件到压缩文件
     * @param password    压缩使用的密码
     * @return 最终的压缩文件存放的绝对路径, 如果为null则说明压缩失败
     */
    public static String zipWithLsh(String src, String dest, boolean isCreateDir, String lsh,
                                    String password) {
        File srcFile = new File(src);
        dest = buildDestinationZipFilePath(srcFile, dest);
        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE); // 压缩方式
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL); // 压缩级别
        if (!StringUtils.isEmpty(password)) {
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD); // 加密方式
            parameters.setPassword(password.toCharArray());
        }
        try {
            ZipFile zipFile = new ZipFile(dest);
            if (srcFile.isDirectory()) {
                // 如果不创建目录的话,将直接把给定目录下的文件压缩到压缩文件,即没有目录结构
                if (!isCreateDir) {
                    File[] subFiles = srcFile.listFiles();
                    ArrayList<File> temp = new ArrayList<File>();
                    for (File file : subFiles) {
                        if (file.isDirectory()) {
                            String parent = file.getAbsolutePath();
                            String subLsh = parent.substring(parent.lastIndexOf(File.separator) + 1);
                            if (!lsh.equals(subLsh)) {
                                zipFile.addFolder(parent, parameters);
                            }
                        } else {
                            //temp.add(file);
                            zipFile.addFile(file, parameters);
                        }
                    }
                    //zipFile.addFiles(temp, parameters);
                    return dest;
                }
                //zipFile.addFolder(srcFile, parameters);
            } else {
                zipFile.addFile(srcFile, parameters);
            }
            return dest;
        } catch (ZipException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用给定密码压缩指定文件或文件夹到指定位置.
     * <p>
     * dest可传最终压缩文件存放的绝对路径,也可以传存放目录,也可以传null或者""
     * 如果传null或者""则将压缩文件存放在当前目录,即跟源文件同目录,压缩文件名取源文件名,以.zip为后缀
     * 如果以路径分隔符(File.separator)结尾,则视为目录,压缩文件名取源文件名,以.zip为后缀,否则视为文件名
     *
     * @param src         文件夹路径
     * @param dir         要压缩的文件夹
     * @param dest        压缩文件存放路径
     * @param isCreateDir 是否在压缩文件里创建目录,仅在压缩文件为目录时有效
     *                    如果为false,将直接压缩目录下文件到压缩文件
     * @param password    压缩使用的密码
     * @return 最终的压缩文件存放的绝对路径, 如果为null则说明压缩失败
     */
    public static String zip(String src, String[] dir, String dest, boolean isCreateDir,
                             String password) throws IOException {
        File srcFile = new File(src);
        dest = buildDestinationZipFilePath(srcFile, dest);
        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE); // 压缩方式
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL); // 压缩级别
        if (!StringUtils.isEmpty(password)) {
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD); // 加密方式
            parameters.setPassword(password.toCharArray());
        }
        try {
            ZipFile zipFile = new ZipFile(dest);
            if (srcFile.isDirectory()) {
                // 如果不创建目录的话,将直接把给定目录下的文件压缩到压缩文件,即没有目录结构
                if (!isCreateDir) {
                    File[] subFiles = srcFile.listFiles();
                    File[] files = new File[dir.length];
                    for (int i = 0; i < subFiles.length; i++) {
                        for (int j = 0; j < files.length; j++) {
                            File newFile = new File(src + File.separator + dir[j]);
                            // 判断目标目录与已存在的目录是否相等
                            if (newFile.equals(subFiles[i])) {
                                //for (int k = 0; k < subFiles[i].listFiles().length; k++) {
                                files[j] = subFiles[i];
                                //zipFile.addFile(subFiles[i].listFiles()[k], parameters);
                                //}
                                zipFile.addFolder(files[j], parameters);
                            }
                        }
                    }

                    return dest;
                }
            } else {
                zipFile.addFile(srcFile, parameters);
            }
            return dest;
        } catch (ZipException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用给定密码压缩指定文件或文件夹到指定位置.
     * <p>
     * dest可传最终压缩文件存放的绝对路径,也可以传存放目录,也可以传null或者""
     * 如果传null或者""则将压缩文件存放在当前目录,即跟源文件同目录,压缩文件名取源文件名,以.zip为后缀
     * 如果以路径分隔符(File.separator)结尾,则视为目录,压缩文件名取源文件名,以.zip为后缀,否则视为文件名
     *
     * @param src         文件夹路径
     * @param bankOrg     要压缩的文件夹
     * @param dest        压缩文件存放路径
     * @param isCreateDir 是否在压缩文件里创建目录,仅在压缩文件为目录时有效
     *                    如果为false,将直接压缩目录下文件到压缩文件
     * @param password    压缩使用的密码
     * @return 最终的压缩文件存放的绝对路径, 如果为null则说明压缩失败
     */
    public static String zip(String src, Map<String, String> bankOrg, String dest, boolean isCreateDir,
                             String password) throws IOException {
        //1.源文件
        File srcFile = new File(src);
        //2.创建压缩文件存储路径
        dest = buildDestinationZipFilePath(srcFile, dest);
        //3.压缩参数
        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE); // 压缩方式
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL); // 压缩级别
        //4.压缩文件密码
        if (!StringUtils.isEmpty(password)) {
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD); // 加密方式
            parameters.setPassword(password.toCharArray());
        }
        try {
            //5.创建压缩文件
            ZipFile zipFile = new ZipFile(dest);
            //5.1 源文件为目录
            if (srcFile.isDirectory()) {
                // 如果不创建目录的话,将直接把给定目录下的文件压缩到压缩文件,即没有目录结构
                if (!isCreateDir) {
                    //5.2 获取源文件目录下所有子文件
                    File[] subFiles = srcFile.listFiles();
                    //5.3 遍历源文件目录下所有子文件（与参数加源文件路径一致则加入压缩文件中）
                    for (int i = 0; i < subFiles.length; i++) {
                        //遍历银行map（传递的参数）【银行code一一对应用户名】
                        for (Map.Entry<String, String> entry : bankOrg.entrySet()) {
                            //File newFile = new File(src + File.separator + entry.getKey());
                            File downLoadFile = new File(src + File.separator + entry.getValue());
                            // 判断目标目录与已存在的目录是否相等
                            if (downLoadFile.equals(subFiles[i])) {
                                //subFiles[i].renameTo(downLoadFile);
                                //subFiles[i] = downLoadFile;
                                zipFile.addFolder(subFiles[i], parameters);
                            }
                        }
                    }
                    return dest;
                }
            } else {
                zipFile.addFile(srcFile, parameters);
            }
            return dest;
        } catch (ZipException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 构建压缩文件存放路径,如果不存在将会创建 传入的可能是文件名或者目录,也可能不传,此方法用以转换最终压缩文件的存放路径
     *
     * @param srcFile   源文件
     * @param destParam 压缩目标路径
     * @return 正确的压缩文件存放路径
     */
    private static String buildDestinationZipFilePath(File srcFile,
                                                      String destParam) {
        if (StringUtils.isEmpty(destParam)) {
            if (srcFile.isDirectory()) {
                destParam = srcFile.getParent() + File.separator
                        + srcFile.getName() + ".zip";
            } else {
                String fileName = srcFile.getName().substring(0,
                        srcFile.getName().lastIndexOf("."));
                destParam = srcFile.getParent() + File.separator + fileName
                        + ".zip";
            }
        } else {
            // 在指定路径不存在的情况下将其创建出来
            createDestDirectoryIfNecessary(destParam);
            if (destParam.endsWith(File.separator)) {
                String fileName = "";
                if (srcFile.isDirectory()) {
                    fileName = srcFile.getName();
                } else {
                    fileName = srcFile.getName().substring(0,
                            srcFile.getName().lastIndexOf("."));
                }
                destParam += fileName + ".zip";
            }
        }
        return destParam;
    }

    /**
     * 在必要的情况下创建压缩文件存放目录,比如指定的存放路径并没有被创建
     *
     * @param destParam 指定的存放路径,有可能该路径并没有被创建
     */
    private static void createDestDirectoryIfNecessary(String destParam) {
        File destDir = null;
        if (destParam.endsWith(File.separator)) {
            destDir = new File(destParam);
        } else {
            destDir = new File(destParam.substring(0,
                    destParam.lastIndexOf(File.separator)));
        }
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
    }

    /**
     * 解压zip文件到指定目录
     *
     * @param filePath
     * @param upZipPath
     * @return 返回解压的文件集合
     */
    public static List<File> unZip(String filePath, String upZipPath) {
        List<File> list = new ArrayList<File>();
        int count = -1;
        File file = null;
        InputStream is = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;

        // 生成指定的保存目录
        String savePath = upZipPath;
        if (!new File(savePath).exists()) {
            new File(savePath).mkdirs();
        }

        try {
            org.apache.tools.zip.ZipFile zipFile = new org.apache.tools.zip.ZipFile(filePath, "GBK");
            Enumeration enu = zipFile.getEntries();
            while (enu.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry) enu.nextElement();
                if (zipEntry.isDirectory()) {
                    new File(savePath + "/" + zipEntry.getName()).mkdirs();
                    continue;
                }
                if (zipEntry.getName().indexOf("/") != -1) {
                    new File(savePath
                            + "/"
                            + zipEntry.getName().substring(0,
                            zipEntry.getName().lastIndexOf("/")))
                            .mkdirs();
                }
                is = zipFile.getInputStream(zipEntry);
                file = new File(savePath + "/" + zipEntry.getName());
                fos = new FileOutputStream(file);
                bos = new BufferedOutputStream(fos, BUFFER);

                byte[] buf = new byte[BUFFER];
                while ((count = is.read(buf)) > -1) {
                    bos.write(buf, 0, count);
                }

                bos.flush();
                fos.close();
                is.close();
                list.add(file);
            }

            zipFile.close();
            return list;
        } catch (IOException ioe) {
            LOG.error(ioe.getMessage());
            return list;
        }
    }

    /**
     * 解压rar文件到指定目录
     *
     * @param filePath
     * @param unRarPath 路径要唯一，否则获取文件列表会出错
     * @return
     */
    public static int unRar(String filePath, String unRarPath) {
        int result = -99;
        if (!(new File(unRarPath).exists())) {
            new File(unRarPath).mkdirs();
        }
        try {
            String cmd = "test";
            String unrarCmd = cmd + " e -r -o+ " + filePath + " " + unRarPath;
            Runtime rt = Runtime.getRuntime();

            Process pre = rt.exec(unrarCmd);
            while (result == -99) {
                try {
                    Thread.sleep(1000L);
                    result = pre.exitValue();
                } catch (Exception e) {
                    result = -99;
                }
            }
            InputStreamReader isr = new InputStreamReader(pre.getInputStream());
            BufferedReader bf = new BufferedReader(isr);
            String line = null;
            while ((line = bf.readLine()) != null) {
                line = line.trim();
                if ("".equals(line)) {
                    continue;
                }
                LOG.info(line);
            }
            bf.close();
            if (result != 0) {
                LOG.error("unRar " + pre.exitValue());
            }
            // 杀死进程 退出
            // pre.destroy();
            return result;
        } catch (Exception e) {
            LOG.error(e.getMessage() + ": " + e.getStackTrace());
            return -2;
        }
    }

    /**
     * 将多个文件打成压缩包
     * cjr 20190228 修改
     *
     * @param list        需打包的文件路径集合
     * @param zipFilename 压缩包名称
     */
    public static String listToZip(List<String> list, String zipFilename, String zipPath) throws IOException {
        ZipOutputStream zos = null;
        FileInputStream is = null;
        String path = "";
        File file = null;
        if (list != null && list.size() > 0) {
            File f = new File(zipPath);
            if (!f.exists()) {
                f.mkdirs();
            }
            zipFilename = zipPath + File.separator + zipFilename + ".zip";
            //创建zip文件输出流
            zos = new ZipOutputStream(new FileOutputStream(new File(zipFilename)));
            zos.setEncoding("GBK");
            for (int i = 0; i < list.size(); i++) {
                path = list.get(i);
                file = new File(path);
                if (file.exists()) {
                    //创建源文件输入流
                    is = new FileInputStream(file);
                    zos.putNextEntry(new ZipEntry(file.getName()));
                    byte[] buf = new byte[BUFFER];
                    int length = -1;
                    while ((length = is.read(buf)) != -1) {
                        zos.write(buf, 0, length);
                        zos.flush();
                    }
                    zos.closeEntry();
                    is.close();

                } else {
                    LOG.info("源文件不存在！");
                }
            }
            zos.close();
            return zipFilename;
        }

        return null;
    }


    /**
     * 将多个文件打成压缩包
     * cjr 20190228 修改
     *
     * @param list        需打包的文件路径集合
     * @param zipFilename 压缩包名称
     */
    public static String listToZipWithTzs(List<String> list, String zipFilename, String zipPath) {
        FileInputStream is = null;
        String path = "";
        File file = null;
        ZipOutputStream zos = null;
        try {
            if (list != null && list.size() > 0) {
                File f = new File(zipPath);
                if (!f.exists()) {
                    f.mkdirs();
                }
                zipFilename = zipPath + File.separator + zipFilename + ".zip";
                //创建zip文件输出流
                zos = new ZipOutputStream(new FileOutputStream(new File(zipFilename)));
                zos.setEncoding("GBK");
                for (int i = 0; i < list.size(); i++) {
                    path = list.get(i);
                    file = new File(path);
                    if (file.exists()) {
                        if (file.isFile()) {
                            //创建源文件输入流
                            is = new FileInputStream(file);
                            zos.putNextEntry(new ZipEntry(file.getName()));
                            byte[] buf = new byte[BUFFER];
                            int length = -1;
                            while ((length = is.read(buf)) != -1) {
                                zos.write(buf, 0, length);
                                zos.flush();
                            }
                        } else {
                            File[] subFiles = file.listFiles();
                            for (File file1 : subFiles) {
                                //创建源文件输入流
                                is = new FileInputStream(file1);
                                zos.putNextEntry(new ZipEntry("TZS/" + file1.getName()));
                                byte[] buf = new byte[BUFFER];
                                int length = -1;
                                while ((length = is.read(buf)) != -1) {
                                    zos.write(buf, 0, length);
                                    zos.flush();
                                }
                            }
                        }
                        zos.closeEntry();
                        is.close();
                    } else {
                        LOG.info("源文件不存在！");
                    }
                }
                return zipFilename;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (zos != null) {
                    zos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 多个文件压缩，（创建目录结构）
     * cjr 20181213 新增
     *
     * @param list
     * @param lshArr
     * @param zipFilename
     * @param zipPath
     * @return
     */
    public static String listToZip(List<String> list, String[] lshArr, String zipFilename, String zipPath) {
        boolean flag = true;
        FileInputStream is = null;
        String path = "";
        File file = null;
        ZipOutputStream zos = null;
        try {
            if (list != null && list.size() > 0) {
                //List<String> newList=removeDuplicate(list);
                File f = new File(zipPath);
                if (!f.exists()) {
                    f.mkdirs();
                }
                zipFilename = zipPath + File.separator + zipFilename + ".zip";
                //创建zip文件输出流
                zos = new ZipOutputStream(new FileOutputStream(new File(zipFilename)));
                zos.setEncoding("GBK");
                for (int i = 0; i < list.size(); i++) {
                    path = list.get(i);
                    file = new File(path);
                    if (file.exists()) {
                        //创建源文件输入流
                        is = new FileInputStream(file);
                        zos.putNextEntry(new ZipEntry(file.getName()));
                        byte[] buf = new byte[BUFFER];
                        int length = -1;
                        while ((length = is.read(buf)) != -1) {
                            zos.write(buf, 0, length);
                            zos.flush();
                        }
                        zos.closeEntry();
                        is.close();
                    } else {
                        flag = false;
                    }


                }
                if (flag == true) {
                    return zipFilename;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (zos != null) {
                    zos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 将多个文件压缩
     *
     * @param list        要压缩的文件路径集合
     * @param zipFilename 压缩包名称
     * @param zipPath     压缩路径
     * @param password    密码
     * @return
     */
    public static String listToZipWithPwd(List<String> list, String lsh, String zipFilename, String zipPath, String password) {

        String path = "";
        File file = null;
        String attachFilePath = PlatPropUtil.getPropertyValue("config.properties", "attachFilePath");
        try {
            if (list != null && list.size() > 0) {
                File f = new File(zipPath);
                if (!f.exists()) {
                    f.mkdirs();
                }
                //遍历文件路径集合
                boolean flag = true;
                //复制待下载文件到临时文件夹
                for (int i = 0; i < list.size(); i++) {
                    String tempPath = attachFilePath + "feedbackInfo" + File.separator + "temp" + File.separator;
                    File folder = new File(tempPath);
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }
                    path = list.get(i);
                    file = new File(path);
                    if (file.exists()) {
                        String parent = file.getParent();
                        String nrdmOrLsh = parent.substring(file.getParent().lastIndexOf(File.separator) + 1);
                        if (!lsh.equals(nrdmOrLsh)) {
                            if (flag == true) {
                                //创建第一级文件夹
                                tempPath = tempPath + lsh + File.separator;
                                folder = new File(tempPath);
                                if (!folder.exists()) {
                                    folder.mkdirs();
                                }
                                //创建第二级文件夹
                                tempPath = tempPath + nrdmOrLsh + File.separator;
                                folder = new File(tempPath);
                                if (!folder.exists()) {
                                    folder.mkdirs();
                                }
                                //复制文件
                                File file1 = new File(path);
                                String fileName = file1.getName();
                                PlatFileUtil.copyFile(path, tempPath + fileName);
                            }
                        } else {
                            //创建第一级文件夹
                            tempPath = tempPath + lsh + File.separator;
                            folder = new File(tempPath);
                            if (!folder.exists()) {
                                folder.mkdirs();
                            }
                            //复制文件
                            PlatFileUtil.copyFile(path, tempPath + PlatFileUtil.getNamePart(path));
                        }

                    }
                }
                //创建压缩文件
                String filePath = attachFilePath + "feedbackInfo" + File.separator + "temp" + File.separator;
                String finalPath = zip(filePath + lsh + File.separator, zipPath + File.separator + zipFilename + ".zip", true, password);
                PlatLogUtil.println("压缩文件路径为" + finalPath);
                return finalPath;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将多个文件压缩
     *
     * @param list        要压缩的文件路径集合
     * @param zipFilename 压缩包名称
     * @param zipPath     压缩路径
     * @param password    密码
     * @return
     */
    public static String listToZip(List<String> list, String lsh, String zipFilename, String zipPath, String password) {

        String path = "";
        File file = null;
        String attachFilePath = PlatPropUtil.getPropertyValue("config.properties", "attachFilePath");
        try {
            if (list != null && list.size() > 0) {
                File f = new File(zipPath);
                if (!f.exists()) {
                    f.mkdirs();
                }
                //遍历文件路径集合
                boolean flag = true;
                //复制待下载文件到临时文件夹
                for (int i = 0; i < list.size(); i++) {
                    String tempPath = attachFilePath + "requestInfo" + File.separator + "temporary" + File.separator + "temp" + File.separator;
                    File folder = new File(tempPath);
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }
                    path = list.get(i);
                    file = new File(path);
                    if (file.exists()) {
                        String parent = file.getParent();
                        String nrdmOrLsh = parent.substring(file.getParent().lastIndexOf(File.separator) + 1);
                        if (!lsh.equals(nrdmOrLsh)) {
                            if (flag == true) {
                                //创建第一级文件夹
                                tempPath = tempPath + lsh + File.separator;
                                folder = new File(tempPath);
                                if (!folder.exists()) {
                                    folder.mkdirs();
                                }
                                //创建第二级文件夹
                                tempPath = tempPath + nrdmOrLsh + File.separator;
                                folder = new File(tempPath);
                                if (!folder.exists()) {
                                    folder.mkdirs();
                                }
                                //复制文件
                                File file1 = new File(path);
                                String fileName = file1.getName();
                                PlatFileUtil.copyFile(path, tempPath + fileName);
                            }
                        } else {
                            //创建第一级文件夹
                            tempPath = tempPath + lsh + File.separator;
                            folder = new File(tempPath);
                            if (!folder.exists()) {
                                folder.mkdirs();
                            }
                            //复制文件
                            PlatFileUtil.copyFile(path, tempPath + PlatFileUtil.getNamePart(path));
                        }

                    }
                }
                //创建压缩文件
                String filePath = attachFilePath + "requestInfo" + File.separator + "temporary" + File.separator + "temp" + File.separator;
                String finalPath = zipWithLsh(filePath + lsh + File.separator, zipPath + File.separator + zipFilename + ".zip", false, lsh, password);
                PlatLogUtil.println("压缩文件路径为" + finalPath);
                return finalPath;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将多个文件压缩
     *
     * @param list        要压缩的文件路径集合
     * @param zipFilename 压缩包名称
     * @param zipPath     压缩路径
     * @param password    密码
     * @return
     */
    public static Map<String, Object> listToZipWithPwd2(List<String> list, String lsh, String zipFilename, String zipPath, String password) {
        Map<String, Object> result = new HashMap<>();
        String path = "";
        File file = null;
        String attachFilePath = PlatPropUtil.getPropertyValue("config.properties", "attachFilePath");
        try {
            if (list != null && list.size() > 0) {
                File f = new File(zipPath);
                if (!f.exists()) {
                    f.mkdirs();
                }
                //遍历文件路径集合
                boolean flag = true;
                //复制待下载文件到临时文件夹
                for (int i = 0; i < list.size(); i++) {
                    String tempPath = attachFilePath + "feedbackInfo" + File.separator + "temp" + File.separator;
                    File folder = new File(tempPath);
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }
                    path = list.get(i);
                    file = new File(path);
                    if (file.exists()) {
                        String parent = file.getParent();
                        String nrdmOrLsh = parent.substring(file.getParent().lastIndexOf(File.separator) + 1);
                        if (!lsh.equals(nrdmOrLsh)) {
                            if (flag == true) {
                                //创建第一级文件夹
                                tempPath = tempPath + lsh + File.separator;
                                folder = new File(tempPath);
                                if (!folder.exists()) {
                                    folder.mkdirs();
                                }
                                //创建第二级文件夹
                                tempPath = tempPath + nrdmOrLsh + File.separator;
                                folder = new File(tempPath);
                                if (!folder.exists()) {
                                    folder.mkdirs();
                                }
                                //复制文件
                                File file1 = new File(path);
                                String fileName = file1.getName();
                                PlatFileUtil.copyFile(path, tempPath + fileName);
                            }
                        } else {
                            //创建第一级文件夹
                            tempPath = tempPath + lsh + File.separator;
                            folder = new File(tempPath);
                            if (!folder.exists()) {
                                folder.mkdirs();
                            }
                            //复制文件
                            PlatFileUtil.copyFile(path, tempPath + PlatFileUtil.getNamePart(path));
                        }

                    }
                }
                //创建压缩文件
                String filePath = attachFilePath + "feedbackInfo" + File.separator + "temp" + File.separator;
                String finalPath = zip(filePath + lsh + File.separator, zipPath + File.separator + zipFilename + ".zip", true, password);
                result.put("message", "success");
                result.put("finalPath", finalPath);
                return result;
            }
        } catch (Exception e) {
            result.put("message", "fail");
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 将多个文件打成压缩包
     *
     * @param list        需打包的文件路径集合
     * @param zipFilename 压缩包名称
     */
    public static String listToZip(List<String> list, String zipFilename, String zipPath, boolean isCreator) {
        String lsh = zipFilename;
        // 压缩参数
        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        // 压缩方式
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        // 压缩级别
        String path = "";
        File file = null;
        try {
            if (list != null && list.size() > 0) {
                File f = new File(zipPath);
                if (!f.exists()) {
                    f.mkdirs();
                }
                zipFilename = zipPath + "/" + zipFilename + ".zip";
                //创建压缩文件
                ZipFile zipFile = new ZipFile(zipFilename);
                //遍历文件路径集合
                for (int i = 0; i < list.size(); i++) {
                    path = list.get(i);
                    file = new File(path);
                    if (file.exists()) {
                        String parent = file.getParent().substring(file.getParent().lastIndexOf(File.separator) + 1);
                        if (!lsh.equals(parent)) {
                            zipFile.addFolder(file.getParent(), parameters);
                            zipFile.addFile(file, parameters);
                        } else {
                            zipFile.addFile(file, parameters);
                        }
                        //zipFile.addFolder(file.getParent(),parameters);
                        //加入压缩文件中

                    } else {
                        LOG.info("源文件不存在！");
                    }
                }
                return zipFilename;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将多个文件压缩
     *
     * @param list        要压缩的文件路径集合
     * @param zipFilename 压缩包名称
     * @param zipPath     压缩路径
     * @param password    密码
     * @return
     */
    public static String listToZipWithPwd(List<String> list, String zipFilename, String zipPath, String password) {

        // 压缩参数
        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        // 压缩方式
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        // 压缩级别
        // 压缩文件密码
        if (!StringUtils.isEmpty(password)) {
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
            // 加密方式
            parameters.setPassword(password.toCharArray());
        }
        String path = "";
        File file = null;
        try {
            if (list != null && list.size() > 0) {
                File f = new File(zipPath);
                if (!f.exists()) {
                    f.mkdirs();
                }
                zipFilename = zipPath + File.separator + zipFilename + ".zip";
                //创建压缩文件
                ZipFile zipFile = new ZipFile(zipFilename);
                //遍历文件路径集合
                for (int i = 0; i < list.size(); i++) {
                    path = list.get(i);
                    file = new File(path);
                    if (file.exists()) {
                        zipFile.addFolder(file.getParent(), parameters);
                        //加入压缩文件中
                        zipFile.addFile(file, parameters);
                    } else {
                        LOG.info("源文件不存在！");
                    }
                }
                return zipFilename;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将多个文件压缩
     *
     * @param list        要压缩的文件路径集合
     * @param zipFilename 压缩包名称
     * @param zipPath     压缩路径
     * @param password    密码
     * @return
     */
    public static String listToZipWithPwd(List<String> list, String zipFilename, String zipPath, String password, boolean isCreateDir) {

        // 压缩参数
        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        // 压缩方式
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        // 压缩级别
        // 压缩文件密码
        if (!StringUtils.isEmpty(password)) {
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
            // 加密方式
            parameters.setPassword(password.toCharArray());
        }
        String path = "";
        File file = null;
        try {
            if (list != null && list.size() > 0) {
                File f = new File(zipPath);
                if (!f.exists()) {
                    f.mkdirs();
                }
                zipFilename = zipPath + File.separator + zipFilename + ".zip";
                //创建压缩文件
                ZipFile zipFile = new ZipFile(zipFilename);
                //遍历文件路径集合
                for (int i = 0; i < list.size(); i++) {
                    path = list.get(i);
                    file = new File(path);
                    if (file.exists()) {
                        if (isCreateDir) {
                            zipFile.addFolder(file.getParent(), parameters);
                        }
                        //加入压缩文件中
                        zipFile.addFile(file, parameters);
                    } else {
                        LOG.info("源文件不存在！");
                    }
                }
                return zipFilename;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @param list
     * @param zipFilename
     * @param zipPath
     * @param password
     * @return
     */
    public static String listToZipWithPwdAndLSH(String src, List<String> list, String zipFilename, String zipPath, String password) {
        //1.源文件
        //File srcFile = new File(src);
        //2.创建压缩文件存储路径
        //zipPath = buildDestinationZipFilePath(srcFile, zipPath);
        // 压缩参数
        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        // 压缩方式
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        // 压缩级别
        // 压缩文件密码
        if (!StringUtils.isEmpty(password)) {
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
            // 加密方式
            parameters.setPassword(password.toCharArray());
        }
        String path = "";
        File file = null;
        try {
            if (list != null && list.size() > 0) {
                File f = new File(zipPath);
                if (!f.exists()) {
                    f.mkdirs();
                }
                zipFilename = zipPath + File.separator + zipFilename + ".zip";
                //创建压缩文件
                ZipFile zipFile = new ZipFile(zipFilename);
                //遍历文件路径集合
                for (int i = 0; i < list.size(); i++) {
                    path = src + list.get(i);
                    file = new File(path);
                    if (file.exists()) {
                        //加入压缩文件中
                        zipFile.addFile(file, parameters);
                    } else {
                        LOG.info("源文件不存在！");
                    }
                }
                return zipFilename;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 将多个文件打成压缩包(压缩包内文件名称由参数中传入)
     *
     * @param list        需打包的文件信息集合
     * @param zipFilename 压缩包名称
     */
    public static void listMapToZip(List<Map<String, Object>> list, String zipFilename, String zipPath) {
        FileInputStream is = null;
        String path = "";
        File file = null;
        ZipOutputStream zos = null;
        try {
            if (list != null && list.size() > 0) {
                File f = new File(zipPath);
                if (!f.exists()) {
                    f.mkdirs();
                }
                zipFilename = zipPath + "/" + zipFilename;
                //创建zip文件输出流
                zos = new ZipOutputStream(new FileOutputStream(new File(
                        zipFilename)));
                zos.setEncoding("GBK");
                for (Map map : list) {
                    path = map.get("filePath") + "";
                    file = new File(path);
                    if (file.exists()) {
                        //创建源文件输入流
                        is = new FileInputStream(file);
                        zos.putNextEntry(new ZipEntry(map.get("fileName") + ""));
                        byte[] buf = new byte[BUFFER];
                        int length = -1;
                        while ((length = is.read(buf)) != -1) {
                            zos.write(buf, 0, length);
                            zos.flush();
                        }
                        zos.closeEntry();
                        is.close();
                    } else {
                        LOG.info("源文件不存在");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (zos != null) {
                    zos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}