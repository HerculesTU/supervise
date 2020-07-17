package com.housoo.platform.core.util;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.FileURIResolver;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 描述 封装办公相关API
 *
 * @author
 * @created 2017年4月30日 上午11:47:29
 */
public class PlatOfficeUtil {

    /**
     * html源代码转换成word
     *
     * @param htmlCode
     * @param wordPath
     */
    public static void htmlCodeToWordByJacob(String htmlCode, String wordPath) {
        StringBuffer content = new StringBuffer("<html><head></head><body>").append(htmlCode).append("</body></html>");
        String folderPath = wordPath.substring(0, wordPath.lastIndexOf("/"));
        File file = new File(folderPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String htmlPath = folderPath + "/" + UUIDGenerator.getUUID() + ".html";
        PlatFileUtil.writeDataToDisk(content.toString(), htmlPath, "GBK");
        PlatOfficeUtil.htmlFileToWordByJacob(htmlPath, wordPath);
        File htmlFile = new File(htmlPath);
        if (htmlFile.exists()) {
            htmlFile.delete();
        }
    }

    /**
     * 将html源文件转换成word文件
     *
     * @param htmlPath
     * @param wordPath
     */
    public static void htmlFileToWordByJacob(String htmlPath, String wordPath) {
        String folderPath = wordPath.substring(0, wordPath.lastIndexOf("/"));
        File file = new File(folderPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        ActiveXComponent app = new ActiveXComponent("Word.Application"); // 启动word
        try {
            app.setProperty("Visible", new Variant(false));
            Dispatch wordDoc = app.getProperty("Documents").toDispatch();
            wordDoc = Dispatch.invoke(wordDoc, "Add", Dispatch.Method,
                    new Object[0], new int[1]).toDispatch();
            Dispatch.invoke(app.getProperty("Selection").toDispatch(),
                    "InsertFile", Dispatch.Method, new Object[]{htmlPath, "",
                            new Variant(false), new Variant(false),
                            new Variant(false)}, new int[3]);
            Dispatch.invoke(wordDoc, "SaveAs", Dispatch.Method, new Object[]{
                    wordPath, new Variant(1)}, new int[1]);
            Dispatch.call(wordDoc, "Close", new Variant(false));
        } catch (Exception e) {
            PlatLogUtil.printStackTrace(e);
        } finally {
            app.invoke("Quit", new Variant[]{});
        }
    }

    /**
     * 将word转换成html代码
     *
     * @param wordPath:Word路径
     * @param htmlPath:html路径
     */
    public static void wordToHtmlCode(String wordPath, String htmlPath) {
        // 启动word应用程序(Microsoft Office Word 2003)
        ActiveXComponent app = new ActiveXComponent("Word.Application");
        try {
            // 设置word应用程序不可见
            app.setProperty("Visible", new Variant(false));
            // documents表示word程序的所有文档窗口，（word是多文档应用程序）
            Dispatch docs = app.getProperty("Documents").toDispatch();
            // 打开要转换的word文件
            Dispatch doc = Dispatch.invoke(
                    docs,
                    "Open",
                    Dispatch.Method,
                    new Object[]{wordPath, new Variant(false),
                            new Variant(true)}, new int[1]).toDispatch();
            // 作为html格式保存到临时文件
            Dispatch.invoke(doc, "SaveAs", Dispatch.Method, new Object[]{
                    htmlPath, new Variant(8)}, new int[1]);
            // 关闭word文件
            Dispatch.call(doc, "Close", new Variant(false));
        } catch (Exception e) {
            PlatLogUtil.printStackTrace(e);
        } finally {
            // 关闭word应用程序
            app.invoke("Quit", new Variant[]{});
        }
    }

    /**
     * 获取单元格的值
     *
     * @param row       :当前行
     * @param cellIndex :单元格的索引
     * @return
     */
    public static Object getExcelCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell != null) {
            // 获取单元格类型
            int cellType = cell.getCellType();
            switch (cellType) {
                case Cell.CELL_TYPE_STRING:
                    return row.getCell(cellIndex).getStringCellValue().trim();
                case Cell.CELL_TYPE_BLANK:
                    return "";
                case Cell.CELL_TYPE_BOOLEAN:
                    return row.getCell(cellIndex).getBooleanCellValue();
                case Cell.CELL_TYPE_FORMULA:
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    DecimalFormat df = new DecimalFormat("0");
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        return PlatDateTimeUtil.formatDate(cell.getDateCellValue(), "YYYY-MM-dd");
                    } else {
                        return df.format(cell.getNumericCellValue());
                    }

                    /*
                     * Long longVal = Math.round(cell.getNumericCellValue());
                     * Double doubleVal = cell.getNumericCellValue(); if
                     * (Double.parseDouble(longVal + ".0") == doubleVal) {
                     * return longVal.intValue(); } else { return doubleVal; }
                     */
                default:
                    break;
            }
        }
        return null;
    }

    /**
     * 读取excel的行数据
     *
     * @param excelPath:文件路径
     * @param startRowNum:起始行号,从0开始计算
     * @param startColNum:起始列号,从0开始计算
     * @param columnCount:需要读取的列的数量
     * @return
     */
    public static List<List<Object>> readExcelRowValues(String excelPath,
                                                        int startRowNum, int startColNum, int columnCount) {
        return PlatOfficeUtil.readExcelRowValues(excelPath, startRowNum, startColNum, 0, columnCount);
    }

    /**
     * 读取excel的行数据
     *
     * @param excelPath:excel的路径
     * @param startRowNum:起始行号,从0开始计算
     * @param startColNum:起始列号,从0开始计算
     * @param sheetNum:sheet的编号
     * @return
     */
    public static List<List<Object>> readExcelRowValues(String excelPath,
                                                        int startRowNum, int startColNum, int sheetNum, int columnCount) {
        String fileType = PlatFileUtil.getFileExt(excelPath);
        Workbook workbook = null;
        Sheet sheet = null;
        List<List<Object>> rowValues = new ArrayList<List<Object>>();
        try {
            // 取得相应工作簿
            if ("xls".equals(fileType)) {
                workbook = new HSSFWorkbook(new FileInputStream(excelPath));
            } else if ("xlsx".equals(fileType)) {
                workbook = new XSSFWorkbook(new FileInputStream(excelPath));
            }
            if (sheetNum <= workbook.getNumberOfSheets() - 1) {
                sheet = workbook.getSheetAt(sheetNum);
                int rowCount = sheet.getPhysicalNumberOfRows();
                for (int k = startRowNum; k < rowCount; k++) { // 获取到第K行 HSSFRow
                    List<Object> rowValue = new ArrayList<Object>();
                    Row row = sheet.getRow(k);
                    if (row != null) {
                        // 获取列数量值
                        /*if(maxColCount==0){
                            maxColCount = row.getPhysicalNumberOfCells();
                        }*/
                        //int colCount = maxColCount;
                        //int colCount = row.getPhysicalNumberOfCells();
                        int colCount = 0;
                        if (columnCount != 0) {
                            colCount = columnCount;
                        } else {
                            colCount = row.getPhysicalNumberOfCells();
                        }
                        int checkCount = 0;
                        for (int j = startColNum; j < colCount; j++) {
                            Object cellValue = PlatOfficeUtil.getExcelCellValue(row, j);
                            if (cellValue != null) {
                                rowValue.add(cellValue);
                            } else {
                                rowValue.add("");
                                checkCount++;
                            }
                        }
                        if (rowValue.size() > 0 && checkCount < (colCount - startColNum - 1)) {
                            rowValues.add(rowValue);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            PlatLogUtil.printStackTrace(e);
        } catch (IOException e) {
            PlatLogUtil.printStackTrace(e);
        }
        return rowValues;
    }

    /**
     * 使用XSTL根据模版路径和模版路径生产文件
     *
     * @param templateFilePath
     * @param map
     * @param destFilePath
     */
    public static void createExcelByXSTL(String templateFilePath, Map map, String destFilePath) {
        try {
            //创建XLSTransformer对象
            XLSTransformer transformer = new XLSTransformer();
            //生成Excel文件
            transformer.transformXLS(templateFilePath, map, destFilePath);
        } catch (Exception e) {
            PlatLogUtil.printStackTrace(e);
        }
    }

    /**
     * 把数据写入XSTL模版的表格中
     *
     * @param fileName         文件名称
     * @param templateFilePath 模版文件路径
     * @param data             数据
     * @param response
     */
    public static void writeDataToExecl(String fileName, String templateFilePath, Map data, HttpServletResponse response) {
        InputStream in = null;
        OutputStream out = null;
        try {
            String destFileName = new String(fileName.getBytes("GBK"), "ISO8859-1");
            XLSTransformer transformer = new XLSTransformer();
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            //设置响应  
            response.setHeader("Content-Disposition", "attachment;filename=" + destFileName);
            response.setContentType("application/vnd.ms-excel");
            in = new BufferedInputStream(new FileInputStream(templateFilePath));
            Workbook workbook = transformer.transformXLS(in, data);
            out = response.getOutputStream();
            //将内容写入输出流并把缓存的内容全部发出去  
            workbook.write(out);
            out.flush();
        } catch (InvalidFormatException e) {
            PlatLogUtil.printStackTrace(e);
        } catch (IOException e) {
            PlatLogUtil.printStackTrace(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    PlatLogUtil.printStackTrace(e);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    PlatLogUtil.printStackTrace(e);
                }
            }
        }
    }

    /**
     * 将HTML代码转换成PDF文件
     *
     * @param htmlCode
     * @param pdfPath
     */
    public static void htmlCodeToPdfByGoogle(String htmlCode, String pdfPath) {
        String htmlPath = PlatBeanUtil.getClassFolderPath(PlatOfficeUtil.class)
                + "/" + UUIDGenerator.getUUID() + ".html";
        PlatFileUtil.writeDataToDisk(htmlCode, htmlPath, "UTF-8");
        htmlFileToPdfByGoogle(htmlPath, pdfPath);
        File file = new File(htmlPath);
        file.delete();
    }

    /**
     * 将html代码转换成pdf
     *
     * @param htmlUrlOrFilePath:html的文件路径或者URL地址
     * @param pdfPath:生成pdf的路径
     */
    public static void htmlFileToPdfByGoogle(String htmlUrlOrFilePath, String pdfPath) {
        String folderPath = pdfPath.substring(0, pdfPath.lastIndexOf("/"));
        File file = new File(folderPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String pdfGenerateSoftPath = PlatPropUtil.getPropertyValue("config.properties",
                "pdfGenerateSoftPath");
        executeCommand(pdfGenerateSoftPath + "/wkhtmltopdf " + htmlUrlOrFilePath + " "
                + pdfPath);
    }

    /**
     * @param command
     * @return
     */
    public static String executeCommand(String command) {
        Runtime rt = Runtime.getRuntime();
        BufferedReader reader = null;
        try {
            StringBuffer output = new StringBuffer();
            Process p = rt.exec(command);
            p.waitFor();
            reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
            return output.toString();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * word 转 html
     *
     * @param dbfilepath
     * @throws TransformerException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    public static String docToHtmlByPoi(String dbfilepath)
            throws TransformerException, IOException,
            ParserConfigurationException {
        String attachFilePath = PlatPropUtil.getPropertyValue("config.properties", "attachFilePath");
        final String attachFileUrl = PlatPropUtil.getPropertyValue("config.properties", "attachFileUrl");
        String realPath = attachFilePath + dbfilepath;
        final String parentFilePath = dbfilepath.substring(0, dbfilepath.lastIndexOf("."));


        HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(realPath));
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
                DocumentBuilderFactory.newInstance().newDocumentBuilder()
                        .newDocument());
        wordToHtmlConverter.setPicturesManager(new PicturesManager() {
            @Override
            public String savePicture(byte[] content,
                                      PictureType pictureType, String suggestedName,
                                      float widthInches, float heightInches) {
                return attachFileUrl + "/" + parentFilePath + "/" + suggestedName;
            }
        });
        wordToHtmlConverter.processDocument(wordDocument);

        PlatFileUtil.createDir(attachFilePath + parentFilePath + "/");

        //保存图片
        List pics = wordDocument.getPicturesTable().getAllPictures();
        if (pics != null) {
            for (int i = 0; i < pics.size(); i++) {
                Picture pic = (Picture) pics.get(i);
                try {
                    pic.writeImageContent(new FileOutputStream(attachFilePath + parentFilePath + "/"
                            + pic.suggestFullFileName()));
                } catch (FileNotFoundException e) {
                    PlatLogUtil.printStackTrace(e);
                }
            }
        }
        Document htmlDocument = wordToHtmlConverter.getDocument();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DOMSource domSource = new DOMSource(htmlDocument);

        StreamResult streamResult = new StreamResult(out);


        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();

        serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "HTML");
        serializer.transform(domSource, streamResult);
        out.close();
        String htmlContent = new String(out.toString("UTF-8"));
        org.jsoup.nodes.Document doc = Jsoup.parse(htmlContent);
        String styleOld = doc.getElementsByTag("style").html();
        //统一字体格式为宋体
        styleOld = styleOld.replaceAll("font-family:.+(?=;\\b)", "font-family:SimSun");

        doc.getElementsByTag("head").empty();
        doc.getElementsByTag("head").append("<meta http-equiv=\"Content-Type\" "
                + "content=\"text/html; charset=UTF-8\"></meta>");
        doc.getElementsByTag("head").append(" <style type=\"text/css\"></style>");
        doc.getElementsByTag("style").append(styleOld);
        /*正则表达式查询字体内容：font-family:.+(?=;\b)*/
        htmlContent = doc.html();
        htmlContent = htmlContent.replace("<meta http-equiv=\"Content-Type\" "
                        + "content=\"text/html; charset=UTF-8\">",
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></meta>");
        return htmlContent;
    }

    /**
     * @param dbfilepath
     * @return
     */
    public static String docxToHtmlByPoi(String dbfilepath) throws Exception {
        String attachFilePath = PlatPropUtil.getPropertyValue("config.properties", "attachFilePath");
        String attachFileUrl = PlatPropUtil.getPropertyValue("config.properties", "attachFileUrl");
        String realPath = attachFilePath + dbfilepath;
        String parentFilePath = dbfilepath.substring(0, dbfilepath.lastIndexOf("."));
        PlatFileUtil.createDir(attachFilePath + parentFilePath + "/");
        String fileOutName = attachFilePath + parentFilePath + ".html";
        XWPFDocument document = new XWPFDocument(new FileInputStream(realPath));
        XHTMLOptions options = XHTMLOptions.create().indent(4);
        // Extract image
        File imageFolder = new File(attachFilePath + parentFilePath);
        options.setExtractor(new FileImageExtractor(imageFolder));
        // URI resolver
        options.URIResolver(new FileURIResolver(imageFolder));

        File outFile = new File(fileOutName);
        outFile.getParentFile().mkdirs();
        OutputStream out = new FileOutputStream(outFile);
        XHTMLConverter.getInstance().convert(document, out, options);
        File file = new File(fileOutName);
        InputStream input = null;
        try {
            input = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            PlatLogUtil.printStackTrace(e);
        }
        StringBuffer buffer = new StringBuffer();
        byte[] bytes = new byte[1024];
        try {
            for (int n; (n = input.read(bytes)) != -1; ) {
                buffer.append(new String(bytes, 0, n, "UTF-8"));
            }
        } catch (IOException e) {
            PlatLogUtil.printStackTrace(e);
        }
        String htmlContent = buffer.toString().replace(attachFilePath, attachFileUrl);
        htmlContent = StringEscapeUtils.unescapeHtml3(htmlContent);
        return htmlContent;
    }
}
