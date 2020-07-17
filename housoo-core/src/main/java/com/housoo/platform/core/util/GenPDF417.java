package com.housoo.platform.core.util;

import com.itextpdf.text.pdf.BarcodePDF417;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * PDF417条码生成工具类
 */
public class GenPDF417 {

    /**
     * 生成pdf417条形码
     *
     * @param codeString
     * @throws IOException
     */
    public static String createPdf417(String codeString, String num) throws IOException {
        String templateFilePath = PlatPropUtil.getPropertyValue("config.properties", "templateFilePath");
        String realPath = templateFilePath + "temporary" + File.separator + num + ".png";
        BarcodePDF417 pdf = new BarcodePDF417();
        pdf.setText(codeString.getBytes("GBK"));
        Image pdfImg = pdf.createAwtImage(Color.black, Color.white);
        BufferedImage img = new BufferedImage((int) pdfImg.getWidth(null), (int) pdfImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = img.getGraphics();
        graphics.drawImage(pdfImg, 0, 0, Color.white, null);
        // 新建一个空白pdf文档
        File file = new File(realPath);
        File fileParent = file.getParentFile();
        if (!fileParent.exists()) {
            fileParent.mkdirs();
        }
        file.createNewFile();
        File nFile = new File(realPath);
        ImageIO.write(img, "PNG", nFile);
        return realPath;
    }

    public static String createPdf417(String codeString) throws IOException {
        String templateFilePath = PlatPropUtil.getPropertyValue("config.properties", "templateFilePath");
        String realPath = templateFilePath + "temporary" + File.separator + "PDF417.jpg";
        BarcodePDF417 pdf = new BarcodePDF417();
        pdf.setText(codeString.getBytes("GBK"));
        Image pdfImg = pdf.createAwtImage(Color.black, Color.white);
        BufferedImage img = new BufferedImage((int) pdfImg.getWidth(null), (int) pdfImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = img.getGraphics();
        graphics.drawImage(pdfImg, 0, 0, Color.white, null);
        // 新建一个空白pdf文档
        File file = new File(realPath);
        File fileParent = file.getParentFile();
        if (!fileParent.exists()) {
            fileParent.mkdirs();
        }
        file.createNewFile();
        File nFile = new File(realPath);
        ImageIO.write(img, "PNG", nFile);
        return realPath;
    }

    public static void main(String[] args) throws IOException {
        createPdf417("456", "456");
    }


}