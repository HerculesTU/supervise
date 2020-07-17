package com.housoo.platform.core.util;

import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;

import java.io.*;
import java.util.regex.Pattern;

/**
 * 功能描述
 *
 * @author zxl
 * @create 创建时间:2018-11-23 9:14
 */
public class WordToPdf {

    public static String doc2pdf(String inPath, String outPath) {
        InputStream is = null;
        FileOutputStream os = null;
        try {
            long old = System.currentTimeMillis();
            //注册
            String path = "";
            String osName = System.getProperty("os.name");
            if (Pattern.matches("Linux.*", osName)) {
                path = new WordToPdf().getClass().getResource("/").getPath();
                path = path.substring(1);
                path = path.replace("/WEB-INF/classes/", "/WEB-INF/license.xml");
                path = File.separator + path;
            } else if (Pattern.matches("Windows.*", osName)) {
                path = new WordToPdf().getClass().getResource("/").getPath();
                path = path.substring(1);
                path = path.replace("/WEB-INF/classes/", "/WEB-INF/license.xml");
            }
            is = new FileInputStream(path);
            License aposeLic = new License();
            aposeLic.setLicense(is);
            File file = new File(outPath); // 新建一个空白pdf文档
            File fileParent = file.getParentFile();
            if (!fileParent.exists()) {
                fileParent.mkdirs();
            }
            file.createNewFile();
            os = new FileOutputStream(file);
            Document doc = new Document(inPath); // Address是将要被转化的word文档
            doc.save(os, SaveFormat.PDF);// 全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF,
            // EPUB, XPS, SWF 相互转换
            long now = System.currentTimeMillis();
            System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒"); // 转化用时
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return outPath;
    }
}