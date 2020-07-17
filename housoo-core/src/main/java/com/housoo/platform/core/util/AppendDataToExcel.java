package com.housoo.platform.core.util;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Excel追加数据工具类
 * <p>
 * Created by cjr on 2018/11/15.
 */
public class AppendDataToExcel {

    /**
     * 向已有的工作表的追加数据
     *
     * @param exportFilePath 模版excel文件
     * @param list           需要追加的数据文件
     */
    public static <T> void addDataToExcel(String exportFilePath, List<T> list, String title) {
        FileInputStream fs;
        try {
            fs = new FileInputStream(exportFilePath);
            POIFSFileSystem ps;
            try {
                ps = new POIFSFileSystem(fs);
                HSSFWorkbook wb = new HSSFWorkbook(ps);
                // 获取到工作表，因为一个excel可能有多个工作表
                HSSFSheet sheet = wb.getSheetAt(0);
                // 向xls文件中写数据
                FileOutputStream out = new FileOutputStream(exportFilePath);
                //设置标头
                HSSFRow row_1 = sheet.getRow(0);
                int startColumn = row_1.getLastCellNum();

                row_1.createCell(startColumn).setCellValue(title);

                for (int i = 1; i < 26; i++) {
                    // 获取第i行（excel中的行默认从0开始，所以这就是为什么，一个excel必须有字段列头），即，字段列头，便于赋值
                    HSSFRow row = sheet.getRow(i);
                    // 在现有行号后追加数据
                    // row = sheet.createRow(i);
                    // 设置第二个（从0开始）单元格的数据
                    row.createCell(startColumn).setCellValue(Integer.parseInt(list.get(i).toString()));
                }
                out.flush();
                wb.write(out);
                out.close();
            } catch (IOException e) {
                PlatLogUtil.println("向excel中写入数据时发生IO异常");
            }
        } catch (FileNotFoundException e) {
            PlatLogUtil.println("向excel中写入数据时发生找不到文件的异常");
        }
    }

    public void appendDataToExcel(String excelPath) {
        try {
            FileInputStream in = new FileInputStream(excelPath);
            Workbook wb = WorkbookFactory.create(in);
            // 获得第一个工作薄
            Sheet sheet = wb.getSheetAt(0);
            // 获得总列数
            int coloumNum = sheet.getRow(0).getPhysicalNumberOfCells();
            // 获得总行数
            int rowNum = sheet.getLastRowNum();
            Row newRow = null;
            Cell newCell = null;
            for (int j = 0; j < 4; j++) {
                newRow = sheet.createRow(rowNum + 1 + j);
                //新建第一行，可合并，写入新的标头
                if (j == 0) {
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum + 1 + j, rowNum + 1 + j, 0, coloumNum - 1);
                    sheet.addMergedRegion(cellRangeAddress);
                    newCell = newRow.createCell(0);
                    newCell.setCellValue(1818);
                } else {
                    //
                    for (int i = 0; i < coloumNum; i++) {
                        newCell = newRow.createCell(i);
                        newCell.setCellValue(17);
                    }
                }
            }
            FileOutputStream os = new FileOutputStream(
                    excelPath);
            wb.write(os);
            in.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void appendDataToExcel(String excelPath, List<Map<String, Object>> dataList) {

        try {
            FileInputStream in = new FileInputStream(excelPath);
            Workbook wb = WorkbookFactory.create(in);
            // 获得第一个工作薄
            Sheet sheet = wb.getSheetAt(0);
            // 获得总列数
            int coloumNum = sheet.getRow(0).getPhysicalNumberOfCells();
            // 获得总行数
            int rowNum = sheet.getLastRowNum();
            Row newRow = null;
            Cell newCell = null;
            for (int j = 0; j < 4; j++) {
                newRow = sheet.createRow(rowNum + 1 + j);
                //新建第一行，可合并，写入新的标头
                if (j == 0) {
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum + 1 + j, rowNum + 1 + j, 0, coloumNum - 1);
                    sheet.addMergedRegion(cellRangeAddress);
                    newCell = newRow.createCell(0);
                    newCell.setCellValue(1818);
                } else {
                    //
                    for (int i = 0; i < coloumNum; i++) {
                        newCell = newRow.createCell(i);
                        newCell.setCellValue(17);
                    }
                }
            }
            FileOutputStream os = new FileOutputStream(
                    excelPath);
            wb.write(os);
            in.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        String excelPath = "C:\\Users\\Administrator\\Desktop\\05.xlsx";
        new AppendDataToExcel().appendDataToExcel(excelPath);
    }

}
