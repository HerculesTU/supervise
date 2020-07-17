package com.housoo.platform.core.util;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 描述
 *
 * @author
 * @created 2017年4月27日 上午11:31:55
 */
public class CommonExcelView extends AbstractExcelView {

    /**
     * 描述 文件名编码
     *
     * @param request
     * @param fileName
     * @return
     * @throws Exception
     * @author Keravon Feng
     * @created 2016年8月4日 下午5:53:21
     */
    public static String encodeFilename(HttpServletRequest request,
                                        String fileName) throws Exception {
        String agent = request.getHeader("USER-AGENT");
        // Firefox
        if (null != agent && -1 != agent.toLowerCase().indexOf("firefox")) {
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        } else {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        }
        return fileName;
    }

    /**
     * @Description 重写父类的buildExcelDocument方法
     * @author Keravon Feng
     */
    @Override
    protected void buildExcelDocument(Map<String, Object> model,
                                      HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // 设置字体
        HSSFFont fontT = createFontT(workbook);
        // 设置单元格类型
        HSSFCellStyle cellStyle1 = createCellStyle1(workbook, fontT);
        // 设置内容样式
        HSSFCellStyle cellStyle2 = createCellStyle2(workbook);
        // 工作簿的名称
        String excelFileName = String.valueOf(model.get("excelFileName"));
        String[] values = (String[]) model.get("values");
        String singleRowTrue = (String) model.get("singleHeader");
        //数据
        List<Map<String, Object>> list = (List<Map<String, Object>>) model.get("list");
        if (StringUtils.isBlank(excelFileName)) {
            excelFileName = "";
        }
        int iRows;
        HSSFSheet sheet = workbook.getSheet(excelFileName);
        if (sheet == null) {
            sheet = workbook.createSheet(excelFileName);
        }
        //创建标题行
        iRows = 0;
        HSSFRow row = sheet.createRow(iRows);
        row.setHeightInPoints(Short.valueOf("25"));
        HSSFCell cell = row.createCell(0, HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(excelFileName);
        for (int i = 0; i < values.length; i++) {
            cell = row.createCell(i, HSSFCell.CELL_TYPE_STRING);
            cell.setCellStyle(cellStyle1);
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, values.length - 1));

        iRows++;
        //创建表头
        //1.单行表头
        if ("true".equalsIgnoreCase(singleRowTrue.trim())) {
            createSingleExcelColumns(model, cellStyle1, cellStyle2, values, list,
                    iRows, sheet);
        } else {
            //多分组表头
            createMultiExcelColumns(model, cellStyle1, cellStyle2, values, list,
                    iRows, sheet);
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename="
                + CommonExcelView.encodeFilename(request, excelFileName +
                PlatDateTimeUtil.formatDate(new Date(), "yyyyMMdd") + ".xls"));
        OutputStream ouputStream = response.getOutputStream();
        workbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }

    /**
     * 描述 设置字体格式
     *
     * @param workbook
     * @return
     * @author Keravon Feng
     * @created 2016年8月4日 下午6:26:46
     */
    private HSSFFont createFontT(HSSFWorkbook workbook) {
        HSSFFont fontT = workbook.createFont();
        fontT.setFontHeightInPoints((short) 11); // 字体高度
        fontT.setColor(HSSFFont.BOLDWEIGHT_NORMAL); // 字体颜色
        fontT.setFontName("黑体"); // 字体
        fontT.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 宽度
        return fontT;
    }

    /**
     * 描述
     *
     * @param workbook
     * @return
     * @author Keravon Feng
     * @created 2016年8月4日 下午6:25:30
     */
    private HSSFCellStyle createCellStyle2(HSSFWorkbook workbook) {
        HSSFCellStyle cellStyle2 = workbook.createCellStyle();
        // cellStyle1.setFont(fontT);
        cellStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平布局：靠左
        cellStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        cellStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        cellStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        cellStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        cellStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        cellStyle2.setWrapText(true);
        return cellStyle2;
    }

    /**
     * 描述 单元格样式
     *
     * @param workbook
     * @param fontT
     * @return
     * @author Keravon Feng
     * @created 2016年8月4日 下午6:25:06
     */
    private HSSFCellStyle createCellStyle1(HSSFWorkbook workbook, HSSFFont fontT) {
        HSSFCellStyle cellStyle1 = workbook.createCellStyle();
        cellStyle1.setFont(fontT);
        cellStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平布局：居中
        cellStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        cellStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        cellStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        cellStyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        cellStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        cellStyle1.setWrapText(true);
        return cellStyle1;
    }

    /**
     * 描述 创建多行表头的表格
     *
     * @param model
     * @param cellStyle1
     * @param cellStyle2
     * @param values
     * @param list
     * @param iRows
     * @param sheet
     * @author Keravon Feng
     * @created 2016年8月4日 下午6:23:41
     */
    private void createMultiExcelColumns(Map<String, Object> model,
                                         HSSFCellStyle cellStyle1, HSSFCellStyle cellStyle2,
                                         String[] values, List<Map<String, Object>> list, int iRows,
                                         HSSFSheet sheet) {
        HSSFRow row;
        HSSFCell cell;
        int headerNum = Integer.parseInt(String.valueOf(model.get("headgroupNum")));
        String[][] titles = (String[][]) model.get("titles");
        /*
         * String[][] titles = {
         * {"业务单位#0:1:3","监察数据#1:5:1","预警情况#6:5:1","反馈情况#11:5:1"},
         * {"业务数#1:1:2","人工监察#2:3:1"
         * ,"自动监察#5:1:2","黄灯#6:2:1","红灯#8:2:1","小计#10:1:2"
         * ,"黄灯#11:2:1","红灯#13:2:1","小计#15:1:2"},
         * {"已监察#2:1:1","未监察#3:1:1","小计#4:1:1"
         * ,"人工#6:1:1","自动#7:1:1","人工#8:1:1","自动#9:1:1"
         * ,"人工#11:1:1","自动#12:1:1","人工#13:1:1","自动#14:1:1"} };
         */
        int startRow = iRows;
        //创建表头所占区域
        for (int i = 0; i < headerNum; i++) {
            row = sheet.createRow(iRows++);
            for (int k = 0; k < values.length; k++) {
                cell = row.createCell(k, HSSFCell.CELL_TYPE_STRING);
                cell.setCellStyle(cellStyle1);
                cell.setCellValue("");
            }
        }
        for (int i = 0; i < headerNum; i++) {
            row = sheet.getRow(startRow++);
            for (int j = 0; j < titles[i].length; j++) {
                String[] title = titles[i][j].split("#");
                String[] regions = title[1].split(":");
                int startcol = Integer.parseInt(regions[0]);
                int colspan = Integer.parseInt(regions[1]);
                int rowspan = Integer.parseInt(regions[2]);
                cell = row.getCell(startcol);
                cell.setCellValue(title[0]);
                cell.setCellStyle(cellStyle1);
                if (colspan > 1) {
                    sheet.addMergedRegion(
                            new CellRangeAddress(row.getRowNum(), row.getRowNum() + rowspan - 1,
                                    startcol, startcol + colspan - 1));
                }
                if (rowspan > 1) {
                    sheet.addMergedRegion(
                            new CellRangeAddress(row.getRowNum(), row.getRowNum() + rowspan - 1,
                                    startcol, startcol + colspan - 1));
                }
            }
        }
        //数据组装
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            row = sheet.createRow(iRows);
            for (int j = 0; j < values.length; j++) {
                cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(String.valueOf(map.get(values[j])));
                cell.setCellStyle(cellStyle2);
            }
            iRows++;
        }
    }

    /**
     * 描述  创建单行表头的表格
     *
     * @param model
     * @param cellStyle1
     * @param cellStyle2
     * @param values
     * @param list
     * @param iRows
     * @param sheet
     * @author Keravon Feng
     * @created 2016年8月4日 下午6:22:47
     */
    private void createSingleExcelColumns(Map<String, Object> model,
                                          HSSFCellStyle cellStyle1, HSSFCellStyle cellStyle2,
                                          String[] values, List<Map<String, Object>> list, int iRows,
                                          HSSFSheet sheet) {
        HSSFRow row;
        HSSFCell cell;
        String[] titles = (String[]) model.get("titles");
        row = sheet.createRow(iRows);
        for (int i = 0; i < titles.length; i++) {
            cell = row.createCell(i, HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(titles[i]);
            cell.setCellStyle(cellStyle1);
            sheet.setColumnWidth(i, titles[i].getBytes().length * 512);
        }
        iRows++;
        //数据组装
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            row = sheet.createRow(iRows);
            for (int j = 0; j < values.length; j++) {
                cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(new HSSFRichTextString(
                        clearHtml(String.valueOf(map.get(values[j])))));
                cell.setCellStyle(cellStyle2);
                sheet.setColumnWidth(j, (values[j].getBytes().length > titles[j].getBytes().length) ?
                        values[j].getBytes().length * 512 :
                        titles[j].getBytes().length * 512);
            }
            iRows++;
        }
    }

    /**
     * 去除
     *
     * @param str
     * @return
     */
    private String clearHtml(String str) {
        if (str != null && !str.trim().isEmpty()) {
            str = str.replaceAll("<font color='red'>", "");
            str = str.replaceAll("</font>", "");
            str = str.replace("<br/>", "\r\n");
            str = str.replace("null", "");
        }
        return str;
    }

}
