package com.housoo.platform.core.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.viewerpreferences.PDViewerPreferences;

import org.apache.pdfbox.printing.PDFPageable;
import org.apache.pdfbox.printing.PDFPrintable;
import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.print.*;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.PageRanges;
import javax.print.attribute.standard.Sides;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 描述 调用打印机实现打印 PDF文件  和  JPG图片
 *
 * @author zxl
 * @version 1.0
 * @timer 2018-8-29
 */

public class PlatPrintUtil {
    public static final String FILE_TYPE_JPG = "jpg";
    private static int printNum = 0;

    /**
     * 将指定的pdf文件转换为指定路径的图片
     *
     * @param pathList 原文件路径
     */
    public static boolean convertPdfToImg(List<String> pathList) {
        boolean flag = true;
        //生成图片存放路径
        String templateFilePath = PlatPropUtil.getPropertyValue("config.properties", "templateFilePath");
        String imagepath = templateFilePath + "imgTemporary" + File.separator;
        //缩略图显示倍数，1表示不缩放，0.3则缩小到30%
        float zoom = 5f;
        Document document = null;
        float rotation = 0f;
        document = new Document();
        int num;
        int jpgNum = 0;
        int pdfNum = 0;
        //判断总计需要打印图片数量
        for (int m = 0; m < pathList.size(); m++) {
            String filePath = pathList.get(m).toString();
            String s = PlatFileUtil.getFileExt(filePath);
            if ("jpg".equals(s)) {
                jpgNum++;
            }
            if ("pdf".equals(s)) {
                try {
                    document.setFile(filePath);
                } catch (PDFException e) {
                    e.printStackTrace();
                } catch (PDFSecurityException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int pdfMaxPage = document.getPageTree().getNumberOfPages();
                pdfNum += pdfMaxPage;
            }
        }
        num = jpgNum + pdfNum;   //需要打印图片数量
        for (int z = 0; z < pathList.size(); z++) {
            String filePath = pathList.get(z).toString();
            String s = PlatFileUtil.getFileExt(filePath);
            if ("jpg".equals(s)) {
                File printFile = new File(filePath);
                flag = printImg(printFile, num);
            }
            if ("pdf".equals(s)) {
                try {
                    document.setFile(filePath);
                } catch (PDFException e) {
                    flag = false;
                    e.printStackTrace();
                } catch (PDFSecurityException e) {
                    flag = false;
                    e.printStackTrace();
                } catch (IOException e) {
                    flag = false;
                    e.printStackTrace();
                }
                int maxPages = document.getPageTree().getNumberOfPages();
                for (int i = 0; i < maxPages; i++) {
                    BufferedImage img = null;
                    try {
                        img = (BufferedImage) document.getPageImage(i, GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, rotation, zoom);
                    } catch (InterruptedException e) {
                        flag = false;
                        e.printStackTrace();
                    }
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
                    try {
                        outFile.createNewFile();
                        out = new FileOutputStream(outFile);
                        outImage = ImageIO.createImageOutputStream(out);
                        writer.setOutput(outImage);
                        writer.write(new IIOImage(img, null, null));
                    } catch (IOException e) {
                        flag = false;
                        e.printStackTrace();
                    }
                }
                //获取该文件夹下的所有图片进行打印
                //获取某个目录下扩展名满足fileExts要求的文件
                Set<String> set = new HashSet<String>();
                set.add("jpg");
                File srcDir = new File(templateFilePath + "imgTemporary" + File.separator);
                List<File> childFilesList = PlatFileUtil.findChildFiles(set, srcDir, null);
                for (int i = 0; i < childFilesList.size(); i++) {
                    File childFile = childFilesList.get(i);
                    flag = printImg(childFile, num);
                }
            }
        }
        return flag;
    }

    /**
     * 打印图片
     *
     * @param file
     */
    public static boolean printImg(File file, int num) {
        boolean flag = true;

        FileInputStream psStream = null;
        try {
            psStream = new FileInputStream(file);
        } catch (FileNotFoundException ffne) {
            ffne.printStackTrace();
        }
        if (psStream == null) {
            return false;
        }
        //设置打印数据的格式
        DocFlavor psInFormat = DocFlavor.INPUT_STREAM.JPEG;
        //创建打印数据
        DocAttributeSet docAttr = new HashDocAttributeSet();//设置文档属性
        docAttr.add(new MediaPrintableArea(0, 0, 210, 310, MediaPrintableArea.MM));
        Doc myDoc = new SimpleDoc(psStream, psInFormat, docAttr);
        //       Doc myDoc = new SimpleDoc(psStream, psInFormat, null);

        //设置打印属性
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        aset.add(new Copies(1));//打印份数，1份

        //获取默认打印机
        PrintService PS = PrintServiceLookup.lookupDefaultPrintService();
        // PS.getName();
        PrintService myPrinter = PS;

        if (myPrinter != null) {
            DocPrintJob job = myPrinter.createPrintJob();//创建文档打印作业
            try {
                printNum++;
                job.print(myDoc, aset);//打印文档

            } catch (Exception pe) {
                flag = false;
                pe.printStackTrace();
            }
        } else {
            System.out.println("未找到打印机");
        }
        //删除生成的临时图片
        if (num == printNum) {
            String templateFilePath = PlatPropUtil.getPropertyValue("config.properties", "templateFilePath");
            PlatFileUtil.deleteFileDir(templateFilePath + "imgTemporary");
        }
        return flag;
    }

    /**
     * 打印PDF
     *
     * @param filePathList
     * @return
     * @throws IOException
     * @throws PrintException
     * @throws PrinterException
     */
    public static boolean printPdf(List<String> filePathList) throws IOException, PrintException, PrinterException {
        boolean flag = true;
        for (int z = 0; z < filePathList.size(); z++) {
            String filePath = filePathList.get(z).toString();
            PDDocument document = PDDocument.load(new File(filePath));
            // choose your printing method:
            print(document);
            //printWithAttributes(document);
            //printWithDialog(document);
            //printWithDialogAndAttributes(document);
            //printWithPaper(document);
            document.close();
        }
        return flag;
    }

    /**
     * Prints the document at its actual size. This is the recommended way to print.
     */
    private static void print(PDDocument document) throws IOException, PrinterException {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(new PDFPageable(document));
        job.print();
    }

    /**
     * Prints using custom PrintRequestAttribute values.
     */
    private static void printWithAttributes(PDDocument document)
            throws IOException, PrinterException {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(new PDFPageable(document));

        PrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();
        attr.add(new PageRanges(1, 1)); // pages 1 to 1

        job.print(attr);
    }

    /**
     * Prints with a print preview dialog.
     */
    private static void printWithDialog(PDDocument document) throws IOException, PrinterException {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(new PDFPageable(document));

        if (job.printDialog()) {
            job.print();
        }
    }

    /**
     * Prints with a print preview dialog and custom PrintRequestAttribute values.
     */
    private static void printWithDialogAndAttributes(PDDocument document)
            throws IOException, PrinterException {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(new PDFPageable(document));

        PrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();
        attr.add(new PageRanges(1, 1)); // pages 1 to 1

        PDViewerPreferences vp = document.getDocumentCatalog().getViewerPreferences();
        if (vp != null && vp.getDuplex() != null) {
            String dp = vp.getDuplex();
            if (PDViewerPreferences.DUPLEX.DuplexFlipLongEdge.toString().equals(dp)) {
                attr.add(Sides.TWO_SIDED_LONG_EDGE);
            } else if (PDViewerPreferences.DUPLEX.DuplexFlipShortEdge.toString().equals(dp)) {
                attr.add(Sides.TWO_SIDED_SHORT_EDGE);
            } else if (PDViewerPreferences.DUPLEX.Simplex.toString().equals(dp)) {
                attr.add(Sides.ONE_SIDED);
            }
        }

        if (job.printDialog(attr)) {
            job.print(attr);
        }
    }

    /**
     * Prints using a custom page size and custom margins.
     */
    private static void printWithPaper(PDDocument document)
            throws IOException, PrinterException {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(new PDFPageable(document));

        // define custom paper
        Paper paper = new Paper();
        paper.setSize(306, 396); // 1/72 inch
        paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight()); // no margins

        // custom page format
        PageFormat pageFormat = new PageFormat();
        pageFormat.setPaper(paper);

        // override the page format
        Book book = new Book();
        // append all pages
        book.append(new PDFPrintable(document), pageFormat, document.getNumberOfPages());
        job.setPageable(book);

        job.print();
    }

    public static void main(String[] args) throws InterruptedException, PDFSecurityException, PDFException, IOException {
        List<String> paramList = new ArrayList<>();
        paramList.add("C:\\Users\\Administrator\\Desktop\\31320900008_山西省电信公司_通知书2.pdf");
        convertPdfToImg(paramList);
    }


}




