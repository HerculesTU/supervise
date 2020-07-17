package com.housoo.platform.core.util;

import jodd.io.FileUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 描述 封装主要的文件操作工具类
 *
 * @author gf
 * @created 2017年1月5日 下午3:28:54
 */
public class PlatFileUtil {
    /**
     * 加密秘钥值
     */
    public static String ENCRYPT_KEY = "HOUSOO";

    /**
     * 获取格式化文件大小值
     *
     * @param fileLength
     * @return
     */
    public static String getFormatFileSize(long fileLength) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileLength < 1024) {
            fileSizeString = df.format((double) fileLength) + "B";
        } else if (fileLength < 1048576) {
            fileSizeString = df.format((double) fileLength / 1024) + "K";
        } else if (fileLength < 1073741824) {
            fileSizeString = df.format((double) fileLength / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileLength / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 删除文件目录
     *
     * @param fileDirPath
     */
    public static void deleteFileDir(String fileDirPath) {
        if (FileUtil.isExistingFolder(new File(fileDirPath))) {
            try {
                FileUtil.deleteDir(fileDirPath);
            } catch (IOException e) {
                PlatLogUtil.printStackTrace(e);
            }
        }
    }

    /**
     * 获取网络文件大小
     * cjr 20190227
     *
     * @param urlString
     * @return
     */
    public static long getNetWorkFile(String urlString) {
        long length = 0L;
        URL url;
        try {
            url = new URL(urlString);
            HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();//打开连接
            //根据响应获取文件大小
            length = urlcon.getContentLength();
            urlcon.disconnect();//关闭连接
        } catch (Exception e) {
            e.printStackTrace();
        }
        return length;
    }

    /**
     * 删除指定文件夹下的所有文件
     *
     * @param fileDirPath
     */
    public static void deleteFilesOfDir(String fileDirPath) {
        File dir = new File(fileDirPath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File[] fileList = dir.listFiles();
        for (File file : fileList) {
            file.delete();
        }
    }

    /**
     * 读取文件的内容
     *
     * @param filePath:文件的路径
     * @return
     */
    public static String readFileString(String filePath) {
        String fileContent = "";
        try {
            fileContent = FileUtil.readString(new File(filePath));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            PlatLogUtil.printStackTrace(e);
        }
        return fileContent;
    }

    /**
     * 读取文件内容
     *
     * @param filePath
     * @param encoding
     * @return
     */
    public static String readFileString(String filePath, String encoding) {
        String fileContent = "";
        try {
            fileContent = FileUtil.readString(new File(filePath), encoding);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            PlatLogUtil.printStackTrace(e);
        }
        return fileContent;
    }

    /**
     * 从某行开始读取文件
     *
     * @param filePath
     * @param startRowNum
     * @return
     */
    public static String readFileString(String filePath, int startRowNum) {
        StringBuffer sf = new StringBuffer();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
            String temp = null;
            int count = 0;
            while ((temp = br.readLine()) != null) {
                count++;
                if (count >= startRowNum) {
                    sf.append(temp + "\n");
                }
            }
        } catch (FileNotFoundException e) {
            PlatLogUtil.printStackTrace(e);
        } catch (IOException e) {
            PlatLogUtil.printStackTrace(e);
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                PlatLogUtil.printStackTrace(e);
            }
        }
        return sf.toString();
    }

    /**
     * 获取待复制文件夹的文件夹名
     *
     * @param dir
     * @return String
     */
    private static String getDirName(String dir) {
        if (dir.endsWith(File.separator)) { // 如果文件夹路径以"//"结尾，则先去除末尾的"//"  
            dir = dir.substring(0, dir.lastIndexOf(File.separator));
        }
        return dir.substring(dir.lastIndexOf(File.separator) + 1);
    }

    /**
     * 拷贝文件夹
     *
     * @param srcPath:源文件夹
     * @param destDir:目标文件夹
     */
    public static void copyFileDir(String srcPath, String destDir) {
        try {
            //获取最后一个文件夹
            File srcFolder = new File(srcPath);
            String folderName = srcFolder.getName();
            String destPath = destDir + File.separator + folderName;
            FileUtil.copyDir(srcPath, destPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 剪切文件夹
     *
     * @param srcPath
     * @param destDir
     */
    public static void cutFileDir(String srcPath, String destDir) {
        copyFileDir(srcPath, destDir);
        deleteFileDir(srcPath);
    }


    /**
     * 拷贝文件夹
     *
     * @param srcDir:源文件夹对象
     * @param destDir:目标文件夹对象
     */
    public static void copyFileDir(File srcDir, File destDir) {
        try {
            FileUtil.copyDir(srcDir, destDir);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            PlatLogUtil.printStackTrace(e);
        }
    }

    /**
     * 读取文本文件的行数
     *
     * @param file
     * @return
     */
    public static int getTextFileTotalLine(File file) {
        try {
            return FileUtil.readLines(file).length;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            PlatLogUtil.printStackTrace(e);
        }
        return 0;
    }

    /**
     * 读取文本文件的行数
     *
     * @param filePath:文件路径
     * @return
     */
    public static int getTextFileTotalLine(String filePath) {
        try {
            return FileUtil.readLines(filePath).length;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            PlatLogUtil.printStackTrace(e);
        }
        return 0;
    }

    /**
     * 读取文件的扩展名
     *
     * @param filePath
     * @return
     */
    public static String getFileExt(String filePath) {
        int i = filePath.lastIndexOf(".");
        if (i > 0 && i < filePath.length() - 1) {
            return filePath.substring(i + 1).toLowerCase();
        }
        return null;
    }

    /**
     * 将文本内容写入到磁盘
     *
     * @param data
     * @param destFilePath
     */
    public static void writeDataToDisk(String data, String destFilePath, String encoding) {
        String folderPath = destFilePath.substring(0, destFilePath.lastIndexOf("/"));
        File fileFolder = new File(folderPath);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
        try {
            if (StringUtils.isNotEmpty(encoding)) {
                FileUtil.writeString(destFilePath, data, encoding);
            } else {
                FileUtil.writeString(destFilePath, data);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            PlatLogUtil.printStackTrace(e);
        }
    }

    /**
     * 根据传入的文件路径创建文件夹
     *
     * @param filePath:文件路径
     */
    public static void createDir(String filePath) {
        String folderPath = filePath.substring(0, filePath.lastIndexOf("/"));
        File fileFolder = new File(folderPath);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
    }

    /**
     * 获取某个目录下的所有子文件和子文件夹
     *
     * @param srcDir:目标目录
     * @param childFiles:递归用,初次调用传入null
     * @return
     */
    public static List<File> findChildFiles(File srcDir, List<File> childFiles) {
        if (childFiles == null) {
            childFiles = new ArrayList<File>();
        }
        if (srcDir.isFile()) {
            childFiles.add(srcDir);
        } else {
            File[] f = srcDir.listFiles();
            for (File file : f) {
                findChildFiles(file, childFiles);
            }
        }
        return childFiles;
    }

    /**
     * 获取一个文件夹下的所有文件全路径
     *
     * @param path
     * @param listFileName     
     */
    public static void getAllFileName(String path, ArrayList<String> listFileName) {
        File file = new File(path);
        File[] files = file.listFiles();
        String[] names = file.list();
        if (names != null) {
            String[] completNames = new String[names.length];
            for (int i = 0; i < names.length; i++) {
                completNames[i] = path + names[i];
            }
            listFileName.addAll(Arrays.asList(completNames));
        }
        for (File a : files) {
            if (a.isDirectory()) {//如果文件夹下有子文件夹，获取子文件夹下的所有文件全路径。
                getAllFileName(a.getAbsolutePath() + File.separator, listFileName);
            }
        }
    }


    /**
     * 获取某个目录下扩展名满足fileExts要求的文件
     *
     * @param fileExts:文件扩展名
     * @param srcDir:目标目录
     * @param childFiles:递归用,初次调用传入null
     * @return
     */
    public static List<File> findChildFiles(Set<String> fileExts, File srcDir, List<File> childFiles) {
        if (childFiles == null) {
            childFiles = new ArrayList<File>();
        }
        if (srcDir.isFile()) {
            String fileName = srcDir.getName();
            String format = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (fileExts.contains(format)) {
                childFiles.add(srcDir);
            }
        } else {
            File[] f = srcDir.listFiles();
            for (File file : f) {
                findChildFiles(fileExts, file, childFiles);
            }
        }
        return childFiles;
    }

    /**
     * 获取某个目录下扩展名满足fileExts要求的文件
     *
     * @param fileExts:文件扩展名
     * @param srcDir:目标目录
     * @param childFiles:递归用,初次调用传入null
     * @return
     */
    public static List<File> findChildFiles(String fileExts, File srcDir, List<File> childFiles) {
        if (childFiles == null) {
            childFiles = new ArrayList<File>();
        }
        if (srcDir.isFile()) {
            String fileName = srcDir.getName();
            String format = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (fileExts.equals(format)) {
                childFiles.add(srcDir);
            }
        } else {
            File[] f = srcDir.listFiles();
            for (File file : f) {
                findChildFiles(fileExts, file, childFiles);
            }
        }
        return childFiles;
    }

    /**
     * 获取某个目录下的所有子文件全路径信息(不递归)
     * <p>
     * cjr 20181213 新增
     *
     * @param srcDir:目标目录
     * @return
     */
    public static List<String> findChildFiles(File srcDir) {
        List<String> childFiles = new ArrayList<String>();
        if (srcDir.isFile()) {
            childFiles.add(srcDir.getAbsolutePath());
        } else {
            File[] f = srcDir.listFiles();
            for (File file : f) {
                if (file.isFile()) {
                    childFiles.add(file.getAbsolutePath());
                }
            }
        }
        return childFiles;
    }

    /**
     * 删除临时文件
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
                file.delete();
            }
        } else {

        }
    }

    /**
     * 删除文件集合
     *
     * @param files
     */
    public static void deleteFile(List<File> files) {
        if (files != null && files.size() > 0) {
            for (File file : files) {
                if (file.isFile()) {
                    file.delete();
                } else {
                    deleteFile(file);
                }
            }
        }
    }

    /**
     * 合并分割文件
     *
     * @param dirFile
     * @param tempFile
     * @param fileName
     * @throws IOException
     */
    public static void unionFile(File dirFile, File tempFile, String fileName) {
        //判断目标地址是否存在，不存在则创建  
        if (!dirFile.isFile()) {
            dirFile.mkdirs();
        }
        List<FileInputStream> list = new ArrayList<FileInputStream>();
        //获取暂存地址中的文件  
        File[] files = tempFile.listFiles();
        SequenceInputStream sis = null;
        FileOutputStream fos = null;
        try {
            for (int i = 0; i < files.length; i++) {
                //用FileInputStream读取放入list集合  
                list.add(new FileInputStream(new File(tempFile, i + ".part")));
            }
            //使用 Enumeration（列举） 将文件全部列举出来  
            Enumeration<FileInputStream> eum = Collections.enumeration(list);
            //SequenceInputStream合并流 合并文件  
            sis = new SequenceInputStream(eum);
            fos = new FileOutputStream(new File(dirFile, fileName));
            byte[] by = new byte[100];
            int len;
            while ((len = sis.read(by)) != -1) {
                fos.write(by, 0, len);
            }
            fos.flush();
            fos.close();
            sis.close();
        } catch (FileNotFoundException e) {
            PlatLogUtil.printStackTrace(e);
        } catch (IOException e) {
            PlatLogUtil.printStackTrace(e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    PlatLogUtil.printStackTrace(e);
                }
            }
            if (sis != null) {
                try {
                    sis.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    PlatLogUtil.printStackTrace(e);
                }
            }
        }
        deleteFile(tempFile);
    }

    /**
     * 描述 对base64文件进行解码
     *
     * @param base64Code
     * @param targetPath
     * @author gf
     * @created 2015年6月25日 上午11:27:31
     */
    public static void decodeBase64File(String base64Code, String targetPath) {
        String folderPath = targetPath.substring(0, targetPath.lastIndexOf("/"));
        File file = new File(folderPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        byte[] buffer;
        FileOutputStream out = null;
        try {
            byte[] sbyte = base64Code.getBytes();
            buffer = Base64.decodeBase64(sbyte);
            out = new FileOutputStream(targetPath);
            out.write(buffer);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * 描述 对文件进行base64编码
     *
     * @param filePath
     * @return
     * @author gf
     * @created 2015年6月25日 上午11:25:18
     */
    public static String encodeBase64File(String filePath) {
        File file = new File(filePath);
        FileInputStream inputFile = null;
        try {
            inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
            String code = new String(Base64.encodeBase64(buffer));
            return code;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (inputFile != null) {
                try {
                    inputFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    /**
     * 解压jar 文件
     *
     * @param fileName
     * @param outputPath   输出的文件夹
     * @param isDelSrcFile 是否删除源文件
     */
    public static void deCompressJar(String fileName, String outputPath, boolean isDelSrcFile) {
        if (!outputPath.endsWith(File.separator)) {
            outputPath += File.separator;
        }
        File dir = new File(outputPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        JarFile jf = null;
        try {
            jf = new JarFile(fileName);
            for (Enumeration<JarEntry> e = jf.entries(); e.hasMoreElements(); ) {
                JarEntry je = (JarEntry) e.nextElement();
                String outFileName = outputPath + je.getName();
                File f = new File(outFileName);
                if (je.isDirectory()) {
                    if (!f.exists()) {
                        f.mkdirs();
                    }
                } else {
                    File pf = f.getParentFile();
                    if (!pf.exists()) {
                        pf.mkdirs();
                    }
                    InputStream in = jf.getInputStream(je);
                    OutputStream out = new BufferedOutputStream(
                            new FileOutputStream(f));
                    byte[] buffer = new byte[2048];
                    int nBytes = 0;
                    while ((nBytes = in.read(buffer)) > 0) {
                        out.write(buffer, 0, nBytes);
                    }
                    out.flush();
                    out.close();
                    in.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jf != null) {
                try {
                    jf.close();
                    if (isDelSrcFile) {
                        File jar = new File(jf.getName());
                        if (jar.exists()) {
                            jar.delete();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 按照行读取数据
     *
     * @param filePath
     * @param encode
     * @return
     */
    public static List<String> readTxtByLine(String filePath, String encode) {
        List<String> lines = new ArrayList<String>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(
                    filePath), encode));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (StringUtils.isNotEmpty(line)) {
                    lines.add(line);
                }
            }
            br.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return lines;
    }

    /**
     * 文件加密
     *
     * @param fileUrl 加密原文件
     * @param tarUrl  加密后文件
     * @param key     加密秘钥值
     * @throws Exception
     */
    public static void encrypt(String fileUrl, String tarUrl, String key) throws Exception {
        File file = new File(fileUrl);
        String path = file.getPath();
        if (!file.exists()) {
            return;
        }
        int index = path.lastIndexOf("\\");
        String destFile = path.substring(0, index) + "\\" + "housoofile";
        File dest = new File(destFile);
        InputStream in = new FileInputStream(fileUrl);
        OutputStream out = new FileOutputStream(destFile);
        byte[] buffer = new byte[1024];
        int r;
        byte[] buffer2 = new byte[1024];
        while ((r = in.read(buffer)) > 0) {
            for (int i = 0; i < r; i++) {
                byte b = buffer[i];
                buffer2[i] = b == 255 ? 0 : ++b;
            }
            out.write(buffer2, 0, r);
            out.flush();
        }
        in.close();
        out.close();
        dest.renameTo(new File(tarUrl));
        appendMethodA(tarUrl, key);
    }

    /**
     * 加入秘钥
     *
     * @param fileName
     * @param content
     */
    public static void appendMethodA(String fileName, String content) throws Exception {
        // 打开一个随机访问文件流，按读写方式
        RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
        // 文件长度，字节数
        long fileLength = randomFile.length();
        // 将写文件指针移到文件尾。
        randomFile.seek(fileLength);
        randomFile.writeBytes(content);
        randomFile.close();

    }

    /**
     * 解密
     *
     * @param fileUrl   加密文件
     * @param tempUrl   解密后文件
     * @param keyLength 秘钥长度
     * @return
     * @throws Exception
     */
    public static String decrypt(String fileUrl, String tempUrl, int keyLength)
            throws Exception {
        File file = new File(fileUrl);
        if (!file.exists()) {
            return null;
        }
        File dest = new File(tempUrl);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        InputStream is = new FileInputStream(fileUrl);
        OutputStream out = new FileOutputStream(tempUrl);
        byte[] buffer = new byte[1024];
        byte[] buffer2 = new byte[1024];
        byte bMax = (byte) 255;
        long size = file.length() - keyLength;
        int mod = (int) (size % 1024);
        int div = (int) (size >> 10);
        int count = mod == 0 ? div : (div + 1);
        int k = 1, r;
        while ((k <= count && (r = is.read(buffer)) > 0)) {
            if (mod != 0 && k == count) {
                r = mod;
            }
            for (int i = 0; i < r; i++) {
                byte b = buffer[i];
                buffer2[i] = b == 0 ? bMax : --b;
            }
            out.write(buffer2, 0, r);
            k++;
        }
        out.close();
        is.close();
        return tempUrl;
    }

    /**
     * * 判断文件是否加密
     *
     * @param fileName
     * @return
     */
    public static String readFileLastByte(String fileName, int keyLength) {
        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }
        StringBuffer str = new StringBuffer();
        try {
            // 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile(fileName, "r");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            // 将写文件指针移到文件尾。
            for (int i = keyLength; i >= 1; i--) {
                randomFile.seek(fileLength - i);
                str.append((char) randomFile.read());
            }
            randomFile.close();
            return str.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建json文件
     *
     * @param jsonString json字符串
     * @param filePath   文件路径
     * @param fileName   文件名称
     * @return
     */
    public static String createJsonFile(String jsonString, String filePath, String fileName) {
        // 标记文件生成是否成功
        boolean flag = true;

        // 拼接文件完整路径
        String fullPath = filePath + File.separator + fileName + ".json";

        // 生成json格式文件
        try {
            // 保证创建一个新文件
            File file = new File(fullPath);
            if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
                file.getParentFile().mkdirs();
            }
            if (file.exists()) { // 如果已存在,删除旧文件
                file.delete();
            }
            file.createNewFile();

            // 将格式化后的字符串写入文件
            Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            write.write(jsonString);
            write.flush();
            write.close();
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }

        // 文件全路径
        return fullPath;
    }


    /**
     * 得到文件的名字部分。
     * 实际上就是路径中的最后一个路径分隔符后的部分。
     *
     * @param fileName 文件名
     * @return 文件名中的名字部分
     * @since 0.5
     */
    public static String getNamePart(String fileName) {
        int point = getPathLsatIndex(fileName);
        int length = fileName.length();
        if (point == -1) {
            return fileName;
        } else if (point == length - 1) {
            int secondPoint = getPathLsatIndex(fileName);
            if (secondPoint == -1) {
                if (length == 1) {
                    return fileName;
                } else {
                    return fileName.substring(0, point);
                }
            } else {
                return fileName.substring(secondPoint + 1, point);
            }
        } else {
            return fileName.substring(point + 1);
        }
    }

    /**
     * 得到文件名中的父路径部分。
     * 对两种路径分隔符都有效。
     * 不存在时返回""。
     * 如果文件名是以路径分隔符结尾的则不考虑该分隔符，例如"/path/"返回""。
     *
     * @param fileName 文件名
     * @return 父路径，不存在或者已经是父目录时返回""
     * @since 0.5
     */
    public static String getPathPart(String fileName) {
        int point = getPathLsatIndex(fileName);
        int length = fileName.length();
        if (point == -1) {
            return "";
        } else if (point == length - 1) {
            int secondPoint = getPathLsatIndex(fileName);
            if (secondPoint == -1) {
                return "";
            } else {
                return fileName.substring(0, secondPoint);
            }
        } else {
            return fileName.substring(0, point);
        }
    }


    /**
     * 得到路径分隔符在文件路径中最后出现的位置。
     * 对于DOS或者UNIX风格的分隔符都可以。
     *
     * @param fileName 文件路径
     * @return 路径分隔符在路径中最后出现的位置，没有出现时返回-1。
     * @since 0.5
     */
    public static int getPathLsatIndex(String fileName) {
        int point = fileName.lastIndexOf('/');
        if (point == -1) {
            point = fileName.lastIndexOf('\\');
        }
        return point;
    }

    /**
     * 复制文件到指定目录
     *
     * @param oldPath
     * @param newPath
     * @throws IOException
     */
    public static void copyFile(String oldPath, String newPath) throws IOException {

        int bytesum = 0;
        int byteread = 0;
        File oldfile = new File(oldPath);
        if (oldfile.exists()) { //文件存在时
            InputStream inStream = new FileInputStream(oldPath); //读入原文件
            FileOutputStream fs = new FileOutputStream(newPath);
            byte[] buffer = new byte[1444];
            int length;
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread; //字节数 文件大小
                fs.write(buffer, 0, byteread);
            }
            inStream.close();
            fs.close();
        }
    }

    /**
     * 流转文件
     *
     * @param in
     * @param file
     */
    public static void inputStreamToFile(InputStream in, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = in.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 复制文件
     *
     * @param source
     * @param dest
     * @throws IOException
     */
    public static void copyFile(File source, File dest) throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            inputChannel.close();
            outputChannel.close();
        }
    }

    /**
     * 重命名文件
     *
     * @param oldFilePath
     * @param newFilePath
     * @return
     */
    public static boolean renameFile(String oldFilePath, String newFilePath) {
        File oldFile = new File(oldFilePath);
        File newFile = new File(newFilePath);
        boolean flag = oldFile.renameTo(newFile);
        return flag;
    }

    /**
     * 获取指定目录下所有文件
     *
     * @param dir
     */
    public static List<String> getFiles(String dir) {
        List<String> childs = new ArrayList<>();
        File file = new File(dir);
        // 如果这个路径是文件夹
        if (file.isDirectory()) {
            // 获取路径下的所有文件
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                // 如果还是文件夹 递归获取里面的文件 文件夹
                if (files[i].isDirectory()) {
                    getFiles(files[i].getPath());
                } else {
                    childs.add(files[i].getPath());
                }
            }
        } else {
            PlatLogUtil.println("必须是目录");
            return null;
        }
        return childs;
    }

    /**
     * 删除指定目录下的所有文件夹
     *
     * @param dir
     */
    public static void clearDir(File dir) {
        File[] dirs = dir.listFiles();
        for (int i = 0; i < dirs.length; i++) {
            if (dirs[i].isDirectory()) {
                deleteFile(dirs[i]);
            }
        }
    }

}
