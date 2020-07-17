package com.housoo.platform.core.util;

import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * 图片加水印
 * pdf文件加水印
 * 工具类
 * Created by cjr on 2018/8/28.
 */
public class PDFWatermarkUtil {

    private static int interval = -5;

    public static void main(String[] args) {
    }

    /**
     * @param sourceImgPath    源图片路径
     * @param tarImgPath       保存的图片路径
     * @param waterMarkContent 水印内容
     * @param fileExt          图片格式
     * @return void
     * @description 图片加水印
     */
    public static void addWatermarkImg(String sourceImgPath, String tarImgPath, String waterMarkContent, String fileExt) {
        Font font = new Font("黑体", Font.BOLD, 16);//水印字体，大小
        Color markContentColor = Color.gray;//水印颜色
        Integer degree = 35;//设置水印文字的旋转角度
        float alpha = 0.5f;//设置水印透明度
        OutputStream outImgStream = null;
        try {
            File srcImgFile = new File(sourceImgPath);//得到文件
            Image srcImg = ImageIO.read(srcImgFile);//文件转化为图片
            int srcImgWidth = srcImg.getWidth(null);//获取图片的宽
            int srcImgHeight = srcImg.getHeight(null);//获取图片的高
            // 加水印
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufImg.createGraphics();//得到画笔
            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
            g.setColor(markContentColor); //设置水印颜色
            g.setFont(font);              //设置字体
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));//设置水印文字透明度
            if (null != degree) {
                g.rotate(Math.toRadians(degree));//设置水印旋转
            }
            JLabel label = new JLabel(waterMarkContent);
            FontMetrics metrics = label.getFontMetrics(font);
            int width = metrics.stringWidth(label.getText());//文字水印的宽
            int rowsNumber = srcImgHeight / width;// 图片的高  除以  文字水印的宽    ——> 打印的行数(以文字水印的宽为间隔)
            int columnsNumber = srcImgWidth / width;//图片的宽 除以 文字水印的宽   ——> 每行打印的列数(以文字水印的宽为间隔)
            //防止图片太小而文字水印太长，所以至少打印一次
            if (rowsNumber < 1) {
                rowsNumber = 1;
            }
            if (columnsNumber < 1) {
                columnsNumber = 1;
            }
            for (int j = 0; j < rowsNumber; j++) {
                for (int i = 0; i < columnsNumber; i++) {
                    g.drawString(waterMarkContent, i * width + j * width, -i * width + j * width);//画出水印,并设置水印位置
                }
            }
            g.dispose();// 释放资源
            // 输出图片
            outImgStream = new FileOutputStream(tarImgPath);
            ImageIO.write(bufImg, fileExt, outImgStream);
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        } finally {
            try {
                if (outImgStream != null) {
                    outImgStream.flush();
                    outImgStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                e.getMessage();
            }
        }
    }

    /**
     * pdf文件加水印
     *
     * @param inputFile
     * @param outputFile
     * @param waterMarkName
     */
    public static void addWaterMarkPdf(String inputFile,
                                       String outputFile, String waterMarkName, String fontPath) {
        try {
            PdfReader reader = new PdfReader(inputFile);
            File file = new File(outputFile);
            if (!file.exists()) {
                file.getParentFile().mkdir();
            }
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
                    outputFile));
            BaseFont base = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Rectangle pageRect = null;
            PdfGState gs = new PdfGState();
            gs.setFillOpacity(0.3f);
            gs.setStrokeOpacity(0.4f);
            int total = reader.getNumberOfPages() + 1;

            JLabel label = new JLabel();
            FontMetrics metrics;
            int textH = 0;
            int textW = 0;
            label.setText(waterMarkName);
            metrics = label.getFontMetrics(label.getFont());
            textH = metrics.getHeight();
            textW = metrics.stringWidth(label.getText());

            PdfContentByte under;
            for (int i = 1; i < total; i++) {
                pageRect = reader.getPageSizeWithRotation(i);
                under = stamper.getOverContent(i);
                under.saveState();
                under.setGState(gs);
                under.beginText();
                under.setFontAndSize(base, 5);

                // 水印文字成30度角倾斜
                //你可以随心所欲的改你自己想要的角度
                for (int height = interval + textH; height < pageRect.getHeight();
                     height = height + textH * 3) {
                    for (int width = interval + textW; width < pageRect.getWidth() + textW;
                         width = width + textW * 1) {
                        under.showTextAligned(Element.ALIGN_LEFT
                                , waterMarkName, width - textW,
                                height - textH, 20);
                    }
                }
                // 添加水印文字
                under.endText();
            }
            stamper.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
