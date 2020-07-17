package com.housoo.platform.core.util;

import org.apache.commons.io.FileUtils;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;


public class LibreOfficeConvertPDF {
    private static Logger logger = LoggerFactory.getLogger(LibreOfficeConvertPDF.class);

    /**
     * 打开libreOffice服务的方法
     *
     * @return
     */
    public static String getLibreOfficeHome() {
        String osName = System.getProperty("os.name");

        if (Pattern.matches("Linux.*", osName)) {
            //获取linux系统下libreoffice主程序的位置
            logger.info("获取Linux系统LibreOffice路径");
            return "/opt/libreoffice6.0/";
        } else if (Pattern.matches("Windows.*", osName)) {
            //获取windows系统下libreoffice主程序的位置
            logger.info("获取windows系统LibreOffice路径");
            return "C:\\Program Files (x86)\\LibreOffice";
        }
        return null;
    }

    /**
     * 转换libreoffice支持的文件为pdf
     *
     * @param inputfile
     * @param outputfile
     */
    public static void libreOffice2PDF(File inputfile, File outputfile) {
        String LibreOffice_HOME = getLibreOfficeHome();
        logger.info("libreoffice路径为" + LibreOffice_HOME);
        String fileName = inputfile.getName();
        logger.info(new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()) + "文件" + inputfile.getName());
        logger.info(fileName.substring(fileName.lastIndexOf(".")));
        if (".txt".equalsIgnoreCase(fileName.substring(fileName.lastIndexOf(".")))) {

            new LibreOfficeConvertPDF().TXTHandler(inputfile);
        }
        DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();
        // libreOffice的安装目录
        configuration.setOfficeHome(new File(LibreOffice_HOME));
        // 端口号
        configuration.setPortNumber(8100);
        configuration.setTaskExecutionTimeout(1000 * 60 * 25L);
//         设置任务执行超时为10分钟
        configuration.setTaskQueueTimeout(1000 * 60 * 60 * 24L);
//         设置任务队列超时为24小时
        OfficeManager officeManager = configuration.buildOfficeManager();
        officeManager.start();
        logger.info(new Date().toString() + "开始转换......");
        OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
        converter.getFormatRegistry();
        try {
            converter.convert(inputfile, outputfile);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("转换失败");
        } finally {
            officeManager.stop();
        }


        logger.info(new Date().toString() + "转换结束....");
    }


    /**
     * 转换txt文件编码的方法
     *
     * @param file
     * @return
     */
    public File TXTHandler(File file) {
        //或GBK
        String code = "gb2312";
        byte[] head = new byte[3];
        try {
            InputStream inputStream = new FileInputStream(file);
            inputStream.read(head);
            if (head[0] == -1 && head[1] == -2) {
                code = "UTF-16";
            } else if (head[0] == -2 && head[1] == -1) {
                code = "Unicode";
            } else if (head[0] == -17 && head[1] == -69 && head[2] == -65) {
                code = "UTF-8";
            }
            inputStream.close();

            System.out.println(code);
            if ("UTF-8".equals(code)) {
                return file;
            }
            String str = FileUtils.readFileToString(file, code);
            FileUtils.writeStringToFile(file, str, "UTF-8");
            System.out.println("转码结束");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }


    /**
     * 测试的方法
     *
     * @param args
     */
    public static void main(String[] args) {
        String input = "D:" + File.separator + "test.docx";
        File inFile = new File(input);
        String out = "D:" + File.separator + "test.pdf";
        File outFile = new File(out);
        LibreOfficeConvertPDF.libreOffice2PDF(inFile, outFile);
        logger.info("第二次转换");
        String input2 = "D:" + File.separator + "test1.docx";
        File inFile2 = new File(input2);
        String out2 = "D:" + File.separator + "test1.pdf";
        File outFile2 = new File(out2);
        LibreOfficeConvertPDF.libreOffice2PDF(inFile2, outFile2);
    }

}
