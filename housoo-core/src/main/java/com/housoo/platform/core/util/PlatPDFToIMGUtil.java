package com.housoo.platform.core.util;

import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * PDF 转 图片 工具类
 *
 * @author zxl
 * @time 2018-11-29
 */
public class PlatPDFToIMGUtil {
    public static final String FILE_TYPE_JPG = "jpg";

    /**
     * 将指定的pdf文件转换为指定路径的图片
     *
     * @param pathList 原文件路径
     */
    public static List<String> convertPdfToImg(List<String> pathList) throws IOException, PDFException, PDFSecurityException, InterruptedException {
        List<String> imgPathList = new ArrayList<>();
        //生成图片存放路径
        String templateFilePath = PlatPropUtil.getPropertyValue("config.properties", "attachFilePath");
        String imagepath = templateFilePath + "imgTemporary" + File.separator;
        //缩略图显示倍数，1表示不缩放，0.3则缩小到30%
        float zoom = 5f;
        Document document = new Document();
        float rotation = 0f;
        for (int z = 0; z < pathList.size(); z++) {
            String filePath = pathList.get(z).toString();
            String s = PlatFileUtil.getFileExt(filePath);
            if ("pdf".equals(s)) {
                document.setFile(filePath);
                int maxPages = document.getPageTree().getNumberOfPages();
                for (int i = 0; i < maxPages; i++) {
                    BufferedImage img = null;
                    img = (BufferedImage) document.getPageImage(i, GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, rotation, zoom);
                    Iterator iter = ImageIO.getImageWritersBySuffix(FILE_TYPE_JPG);
                    ImageWriter writer = (ImageWriter) iter.next();
                    File outFile = new File(imagepath + new File(filePath).getName() + "_" + new DecimalFormat("000").format(i) + "." + FILE_TYPE_JPG);
                    FileOutputStream out = null;
                    ImageOutputStream outImage = null;
                    //若没有上级目录则  创建
                    File fileParent = outFile.getParentFile();
                    if (!fileParent.exists()) {
                        fileParent.mkdirs();
                    }
                    outFile.createNewFile();
                    imgPathList.add(outFile.getAbsolutePath());
                    out = new FileOutputStream(outFile);
                    outImage = ImageIO.createImageOutputStream(out);
                    writer.setOutput(outImage);
                    writer.write(new IIOImage(img, null, null));
                }
            }
        }
        return imgPathList;
    }

    /**
     * 将指定的pdf文件转换为指定路径的图片
     *
     * @param pathList 原文件路径
     */
    public static List<String> convertPdfToImg(List<String> pathList, String lsh) throws IOException, PDFException, PDFSecurityException {
        List<String> imgPathList = new ArrayList<>();
        //生成图片存放路径
        String templateFilePath = PlatPropUtil.getPropertyValue("config.properties", "attachFilePath");
        String imagePath = "imgTemporary" + File.separator + lsh + File.separator;
        //缩略图显示倍数，1表示不缩放，0.3则缩小到30%
        float zoom = 5f;
        Document document = new Document();
        float rotation = 0f;
        ImageOutputStream outImage = null;
        FileOutputStream out = null;
        for (int z = 0; z < pathList.size(); z++) {
            String filePath = pathList.get(z).toString();
            String s = PlatFileUtil.getFileExt(filePath);
            if ("pdf".equals(s)) {
                document.setFile(filePath);
                int maxPages = document.getPageTree().getNumberOfPages();
                for (int i = 0; i < maxPages; i++) {
                    try {
                        BufferedImage img = null;
                        img = (BufferedImage) document.getPageImage(i, GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, rotation, zoom);
                        Iterator iter = ImageIO.getImageWritersBySuffix(FILE_TYPE_JPG);
                        ImageWriter writer = (ImageWriter) iter.next();
                        String name = imagePath + new File(filePath).getName().substring(0, new File(filePath).getName().lastIndexOf(".")) + "_" + new DecimalFormat("0").format(i) + "." + FILE_TYPE_JPG;
                        File outFile = new File(templateFilePath + imagePath + new File(filePath).getName().substring(0, new File(filePath).getName().lastIndexOf(".")) + "_" + new DecimalFormat("0").format(i) + "." + FILE_TYPE_JPG);

                        //若没有上级目录则  创建
                        File fileParent = outFile.getParentFile();
                        if (!fileParent.exists()) {
                            fileParent.mkdirs();
                        }
                        outFile.createNewFile();
                        imgPathList.add(name);
                        out = new FileOutputStream(outFile);

                        while (outImage == null || outImage.length() < 0) {
                            outImage = ImageIO.createImageOutputStream(out);
                        }

                        writer.setOutput(outImage);
                        writer.write(new IIOImage(img, null, null));

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            out.close();
                            outImage.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }
        return imgPathList;
    }


}
