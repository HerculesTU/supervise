package com.housoo.platform.core.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import java.util.HashMap;

import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.xmlbeans.XmlOptions;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;

/**
 * 描述：操作Word文档工具类
 *
 * @author zxl
 * @version 1.0
 * @time 2018-11-15
 */
public class PlatWordUtil {
    /**
     * 获取指定表格
     *
     * @param xdoc
     * @param index
     * @return
     */

    public static XWPFTable getTableByIndex(XWPFDocument xdoc, int index) {
        List<XWPFTable> tablesList = getAllTable(xdoc);
        if (tablesList == null || index < 0 || index > tablesList.size()) {
            return null;
        }
        return tablesList.get(index);
    }

    /**
     * 获取所有表格
     *
     * @param xdoc
     * @return
     */
    public static List<XWPFTable> getAllTable(XWPFDocument xdoc) {
        return xdoc.getTables();
    }


    /**
     * 删除指定位置的表格,被删除表格后的索引位置
     *
     * @param xdoc
     * @param pos
     */
    public void deleteTableByIndex(XWPFDocument xdoc, int pos) {
        Iterator<IBodyElement> bodyElement = xdoc.getBodyElementsIterator();
        int eIndex = 0, tableIndex = -1;
        while (bodyElement.hasNext()) {
            IBodyElement element = bodyElement.next();
            BodyElementType elementType = element.getElementType();
            if (elementType == BodyElementType.TABLE) {
                tableIndex++;
                if (tableIndex == pos) {
                    break;
                }
            }
            eIndex++;
        }
        xdoc.removeBodyElement(eIndex);
    }


    /**
     * 得到表格内容(第一次跨行单元格视为一个，第二次跳过跨行合并的单元格)
     *
     * @param table
     * @return
     */
    public List<List<String>> getTableRContent(XWPFTable table) {
        List<List<String>> tableContentList = new ArrayList<List<String>>();
        for (int rowIndex = 0, rowLen = table.getNumberOfRows(); rowIndex < rowLen; rowIndex++) {
            XWPFTableRow row = table.getRow(rowIndex);
            List<String> cellContentList = new ArrayList<String>();
            for (int colIndex = 0, colLen = row.getTableCells().size(); colIndex < colLen; colIndex++) {
                XWPFTableCell cell = row.getCell(colIndex);
                CTTc ctTc = cell.getCTTc();
                if (ctTc.isSetTcPr()) {
                    CTTcPr tcPr = ctTc.getTcPr();
                    if (tcPr.isSetHMerge()) {
                        CTHMerge hMerge = tcPr.getHMerge();
                        if (STMerge.RESTART.equals(hMerge.getVal())) {
                            cellContentList.add(getTableCellContent(cell));
                        }
                    } else if (tcPr.isSetVMerge()) {
                        CTVMerge vMerge = tcPr.getVMerge();
                        if (STMerge.RESTART.equals(vMerge.getVal())) {
                            cellContentList.add(getTableCellContent(cell));
                        }
                    } else {
                        cellContentList.add(getTableCellContent(cell));
                    }
                }
            }
            tableContentList.add(cellContentList);
        }
        return tableContentList;
    }

    /**
     * 得到表格内容, 合并后的单元格视为一个单元格
     *
     * @param table
     * @return
     */
    public static List<List<String>> getTableContent(XWPFTable table) {
        List<List<String>> tableContentList = new ArrayList<List<String>>();
        for (int rowIndex = 0, rowLen = table.getNumberOfRows(); rowIndex < rowLen; rowIndex++) {
            XWPFTableRow row = table.getRow(rowIndex);
            List<String> cellContentList = new ArrayList<String>();
            for (int colIndex = 0, colLen = row.getTableCells().size(); colIndex < colLen; colIndex++) {
                XWPFTableCell cell = row.getCell(colIndex);
                cellContentList.add(getTableCellContent(cell));
            }
            tableContentList.add(cellContentList);
        }
        return tableContentList;
    }

    public static String getTableCellContent(XWPFTableCell cell) {
        StringBuffer sb = new StringBuffer();
        List<XWPFParagraph> cellPList = cell.getParagraphs();
        if (cellPList != null && cellPList.size() > 0) {
            for (XWPFParagraph xwpfPr : cellPList) {
                List<XWPFRun> runs = xwpfPr.getRuns();
                if (runs != null && runs.size() > 0) {
                    for (XWPFRun xwpfRun : runs) {
                        sb.append(xwpfRun.getText(0));
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
     * 创建表格, 创建后表格至少有1行1列, 设置列宽
     *
     * @param xdoc
     * @param rowSize
     * @param cellSize
     * @param isSetColWidth
     * @param colWidths
     * @return
     */
    public XWPFTable createTable(XWPFDocument xdoc, int rowSize, int cellSize,
                                 boolean isSetColWidth, int[] colWidths) {
        XWPFTable table = xdoc.createTable(rowSize, cellSize);
        if (isSetColWidth) {
            CTTbl ttbl = table.getCTTbl();
            CTTblGrid tblGrid = ttbl.addNewTblGrid();
            for (int j = 0, len = Math.min(cellSize, colWidths.length); j < len; j++) {
                CTTblGridCol gridCol = tblGrid.addNewGridCol();
                gridCol.setW(new BigInteger(String.valueOf(colWidths[j])));
            }
        }
        return table;
    }

    /**
     * 设置表格总宽度与水平对齐方式
     *
     * @param table
     * @param width
     * @param enumValue
     */
    public void setTableWidthAndHAlign(XWPFTable table, String width,
                                       STJc.Enum enumValue) {
        CTTblPr tblPr = getTableCTTblPr(table);
        CTTblWidth tblWidth = tblPr.isSetTblW() ? tblPr.getTblW() : tblPr
                .addNewTblW();
        if (enumValue != null) {
            CTJc cTJc = tblPr.addNewJc();
            cTJc.setVal(enumValue);
        }
        tblWidth.setW(new BigInteger(width));
        tblWidth.setType(STTblWidth.DXA);
    }

    /**
     * 得到Table的CTTblPr, 不存在则新建
     *
     * @param table
     * @return
     */
    public CTTblPr getTableCTTblPr(XWPFTable table) {
        CTTbl ttbl = table.getCTTbl();
        CTTblPr tblPr = ttbl.getTblPr() == null ? ttbl.addNewTblPr() : ttbl
                .getTblPr();
        return tblPr;
    }

    /**
     * 得到Table的边框, 不存在则新建
     *
     * @param table
     * @return
     */
    public CTTblBorders getTableBorders(XWPFTable table) {
        CTTblPr tblPr = getTableCTTblPr(table);
        CTTblBorders tblBorders = tblPr.isSetTblBorders() ? tblPr
                .getTblBorders() : tblPr.addNewTblBorders();
        return tblBorders;
    }

    /**
     * 设置表格边框样式
     *
     * @param table
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setTableBorders(XWPFTable table, CTBorder left, CTBorder top,
                                CTBorder right, CTBorder bottom) {
        CTTblBorders tblBorders = getTableBorders(table);
        if (left != null) {
            tblBorders.setLeft(left);
        }
        if (top != null) {
            tblBorders.setTop(top);
        }
        if (right != null) {
            tblBorders.setRight(right);
        }
        if (bottom != null) {
            tblBorders.setBottom(bottom);
        }
    }

    /**
     * 表格指定位置插入一行, index为新增行所在的行位置(不能大于表行数)
     *
     * @param table
     * @param index
     */
    public static void insertTableRowAtIndex(XWPFTable table, int index) {
        XWPFTableRow firstRow = table.getRow(0);
        XWPFTableRow row = table.insertNewTableRow(index);
        if (row == null) {
            return;
        }
        CTTbl ctTbl = table.getCTTbl();
        CTTblGrid tblGrid = ctTbl.getTblGrid();
        int cellSize = 0;
        boolean isAdd = false;
        if (tblGrid != null) {
            List<CTTblGridCol> gridColList = tblGrid.getGridColList();
            if (gridColList != null && gridColList.size() > 0) {
                isAdd = true;
                for (CTTblGridCol ctlCol : gridColList) {
                    XWPFTableCell cell = row.addNewTableCell();
                    setCellWidthAndVAlign(cell, ctlCol.getW().toString(),
                            STTblWidth.DXA, null);
                }
            }
        }
        // 大部分都不会走到这一步
        if (!isAdd) {
            cellSize = getCellSizeWithMergeNum(firstRow);
            for (int i = 0; i < cellSize; i++) {
                row.addNewTableCell();
            }
        }
    }

    /**
     * 删除表一行
     *
     * @param table
     * @param index
     */
    public static void deleteTableRow(XWPFTable table, int index) {
        table.removeRow(index);
    }

    /**
     * 统计列数(包括合并的列数)
     *
     * @param row
     * @return
     */
    public static int getCellSizeWithMergeNum(XWPFTableRow row) {
        List<XWPFTableCell> firstRowCellList = row.getTableCells();
        int cellSize = firstRowCellList.size();
        for (XWPFTableCell xwpfTableCell : firstRowCellList) {
            CTTc ctTc = xwpfTableCell.getCTTc();
            if (ctTc.isSetTcPr()) {
                CTTcPr tcPr = ctTc.getTcPr();
                if (tcPr.isSetGridSpan()) {
                    CTDecimalNumber gridSpan = tcPr.getGridSpan();
                    cellSize += gridSpan.getVal().intValue() - 1;
                }
            }
        }
        return cellSize;
    }

    /**
     * 得到CTTrPr, 不存在则新建
     *
     * @param row
     * @return
     */
    public static CTTrPr getRowCTTrPr(XWPFTableRow row) {
        CTRow ctRow = row.getCtRow();
        CTTrPr trPr = ctRow.isSetTrPr() ? ctRow.getTrPr() : ctRow.addNewTrPr();
        return trPr;
    }

    /**
     * 设置行高
     *
     * @param row
     * @param hight
     * @param heigthEnum
     */
    public static void setRowHeight(XWPFTableRow row, String hight,
                                    STHeightRule.Enum heigthEnum) {
        CTTrPr trPr = getRowCTTrPr(row);
        CTHeight trHeight;
        if (trPr.getTrHeightList() != null && trPr.getTrHeightList().size() > 0) {
            trHeight = trPr.getTrHeightList().get(0);
        } else {
            trHeight = trPr.addNewTrHeight();
        }
        trHeight.setVal(new BigInteger(hight));
        if (heigthEnum != null) {
            trHeight.setHRule(heigthEnum);
        }
    }


    /**
     * 设置单元格内容
     *
     * @param table
     * @param rowIndex
     * @param col
     * @param content
     */
    public static void setCellNewContent(XWPFTable table, int rowIndex, int col, String content) {
        XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
        XWPFParagraph p = getCellFirstParagraph(cell);
        List<XWPFRun> cellRunList = p.getRuns();
        if (cellRunList == null || cellRunList.size() == 0) {
            return;
        }
        for (int i = cellRunList.size() - 1; i >= 1; i--) {
            p.removeRun(i);
        }
        XWPFRun run = cellRunList.get(0);
        run.setText(content);
    }

    /**
     * 删除单元格内容
     *
     * @param table
     * @param rowIndex
     * @param col
     */
    public void deleteCellContent(XWPFTable table, int rowIndex, int col) {
        XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
        XWPFParagraph p = getCellFirstParagraph(cell);
        List<XWPFRun> cellRunList = p.getRuns();
        if (cellRunList == null || cellRunList.size() == 0) {
            return;
        }
        for (int i = cellRunList.size() - 1; i >= 0; i--) {
            p.removeRun(i);
        }
    }


    /**
     * 得到Cell的CTTcPr, 不存在则新建
     *
     * @param cell
     * @return
     */
    public static CTTcPr getCellCTTcPr(XWPFTableCell cell) {
        CTTc cttc = cell.getCTTc();
        CTTcPr tcPr = cttc.isSetTcPr() ? cttc.getTcPr() : cttc.addNewTcPr();
        return tcPr;
    }

    /**
     * 设置垂直对齐方式
     *
     * @param cell
     * @param vAlign
     */
    public void setCellVAlign(XWPFTableCell cell, STVerticalJc.Enum vAlign) {
        setCellWidthAndVAlign(cell, null, null, vAlign);
    }

    /**
     * 设置列宽和垂直对齐方式
     *
     * @param cell
     * @param width
     * @param typeEnum
     * @param vAlign
     */
    public static void setCellWidthAndVAlign(XWPFTableCell cell, String width,
                                             STTblWidth.Enum typeEnum, STVerticalJc.Enum vAlign) {
        CTTcPr tcPr = getCellCTTcPr(cell);
        CTTblWidth tcw = tcPr.isSetTcW() ? tcPr.getTcW() : tcPr.addNewTcW();
        if (width != null) {
            tcw.setW(new BigInteger(width));
        }
        if (typeEnum != null) {
            tcw.setType(typeEnum);
        }
        if (vAlign != null) {
            CTVerticalJc vJc = tcPr.isSetVAlign() ? tcPr.getVAlign() : tcPr
                    .addNewVAlign();
            vJc.setVal(vAlign);
        }
    }

    /**
     * 得到单元格第一个Paragraph
     *
     * @param cell
     * @return
     */
    public static XWPFParagraph getCellFirstParagraph(XWPFTableCell cell) {
        XWPFParagraph p;
        if (cell.getParagraphs() != null && cell.getParagraphs().size() > 0) {
            p = cell.getParagraphs().get(0);
        } else {
            p = cell.addParagraph();
        }
        return p;
    }

    /**
     * 跨列合并
     *
     * @param table
     * @param row
     * @param fromCell
     * @param toCell
     */
    public static void mergeCellsHorizontal(XWPFTable table, int row, int fromCell,
                                            int toCell) {
        for (int cellIndex = fromCell; cellIndex <= toCell; cellIndex++) {
            XWPFTableCell cell = table.getRow(row).getCell(cellIndex);
            if (cellIndex == fromCell) {
                // The first merged cell is set with RESTART merge value
                getCellCTTcPr(cell).addNewHMerge().setVal(STMerge.RESTART);
            } else {
                // Cells which join (merge) the first one,are set with CONTINUE
                getCellCTTcPr(cell).addNewHMerge().setVal(STMerge.CONTINUE);
            }
        }
    }

    /**
     * 跨行合并单元格
     *
     * @param table
     * @param col
     * @param fromRow
     * @param toRow
     */
    public static void mergeCellsVertically(XWPFTable table, int col, int fromRow, int toRow) {
        for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {
            XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
            if (rowIndex == fromRow) {
                // The first merged cell is set with RESTART merge value
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
            } else {
                // Cells which join (merge) the first one, are set with CONTINUE
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
            }
        }
    }

    /**
     * 在单元格中赋值
     *
     * @param cell
     * @param text
     */

    public static void setCellText(XWPFTableCell cell, String text) {
        CTTc cttc = cell.getCTTc();
        CTTcPr ctPr = cttc.addNewTcPr();
        cell.setText(text);
    }


    /**
     * 根据指定的参数值、模板，生成 word 文档
     *
     * @param param    需要替换的变量
     * @param template 模板
     */
    public static CustomXWPFDocument generateWord(Map<String, Object> param, String template) {
        CustomXWPFDocument doc = null;
        try {
            OPCPackage pack = POIXMLDocument.openPackage(template);
            doc = new CustomXWPFDocument(pack);
            if (param != null && param.size() > 0) {

                //处理段落
                List<XWPFParagraph> paragraphList = doc.getParagraphs();
                processParagraphs(paragraphList, param, doc);

                //处理表格
                Iterator<XWPFTable> it = doc.getTablesIterator();
                while (it.hasNext()) {
                    XWPFTable table = it.next();
                    List<XWPFTableRow> rows = table.getRows();
                    for (XWPFTableRow row : rows) {
                        List<XWPFTableCell> cells = row.getTableCells();
                        for (XWPFTableCell cell : cells) {
                            List<XWPFParagraph> paragraphListTable = cell.getParagraphs();
                            processParagraphs(paragraphListTable, param, doc);
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }

    /**
     * 处理段落
     *
     * @param paragraphList
     */
    public static void processParagraphs(List<XWPFParagraph> paragraphList, Map<String, Object> param, CustomXWPFDocument doc) {
        if (paragraphList != null && paragraphList.size() > 0) {
            for (XWPFParagraph paragraph : paragraphList) {

                List<XWPFRun> runs = paragraph.getRuns();
                String text = "";
                for (XWPFRun run : runs) {
                    if (StringUtils.isNotEmpty(run.toString())) {
                        text += run.getText(0);
                    }
                }
                if (text != null) {
                    boolean isSetText = false;
                    for (Map.Entry<String, Object> entry : param.entrySet()) {
                        String key = entry.getKey();
                        if (text.indexOf(key) != -1) {
                            isSetText = true;
                            Object value = entry.getValue();
                            if (value instanceof String) {//文本替换
                                text = text.replace(key, value.toString());
                            } else if (value instanceof Map) {  //图片替换
                                text = text.replace(key, "");
                                byte[] byteArray = (byte[]) ((Map<?, ?>) value).get("content");
                                ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteArray);
                                String type = (String) ((Map<?, ?>) value).get("type");
                                int width = (Integer) ((Map<?, ?>) value).get("width");
                                int height = (Integer) ((Map<?, ?>) value).get("height");
                                String blipId = null;
                                try {
                                    blipId = doc.addPictureData(byteInputStream, getPictureType(type));
                                } catch (InvalidFormatException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    doc.createPicture(blipId, doc.getNextPicNameNumber(getPictureType(type)), width, height, paragraph);
                                } catch (InvalidFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    if (isSetText) {
                        int flag = 1;
                        for (XWPFRun run : runs) {
                            if (flag-- == 1) {
                                run.setText(text, 0);
                            } else {
                                run.setText("", 0);
                            }

                        }
                    }
                }
            }
        }
    }

    /**
     * 根据图片类型，取得对应的图片类型代码
     *
     * @param picType
     * @return int
     */
    private static int getPictureType(String picType) {
        int res = CustomXWPFDocument.PICTURE_TYPE_PICT;
        if (picType != null) {
            if ("png".equalsIgnoreCase(picType)) {
                res = CustomXWPFDocument.PICTURE_TYPE_PNG;
            } else if ("dib".equalsIgnoreCase(picType)) {
                res = CustomXWPFDocument.PICTURE_TYPE_DIB;
            } else if ("emf".equalsIgnoreCase(picType)) {
                res = CustomXWPFDocument.PICTURE_TYPE_EMF;
            } else if ("jpg".equalsIgnoreCase(picType) || "jpeg".equalsIgnoreCase(picType)) {
                res = CustomXWPFDocument.PICTURE_TYPE_JPEG;
            } else if ("wmf".equalsIgnoreCase(picType)) {
                res = CustomXWPFDocument.PICTURE_TYPE_WMF;
            }
        }
        return res;
    }

    /**
     * 将输入流中的数据写入字节数组
     *
     * @param in
     * @return
     */
    public static byte[] inputStream2ByteArray(InputStream in, boolean isClose) {
        byte[] byteArray = null;
        try {
            int total = in.available();
            byteArray = new byte[total];
            in.read(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (isClose) {
                try {
                    in.close();

                } catch (Exception e2) {
                    System.out.println("关闭流失败");
                }
            }
        }
        return byteArray;
    }

    /**
     * 设置字体信息  字号信息  加粗
     *
     * @param p
     * @param fontFamily
     * @param fontSize
     * @param isBlod
     * @param position
     */

    public static void setTextFontInfo(XWPFParagraph p, String fontFamily,
                                       String fontSize, boolean isBlod,
                                       int position, boolean isUnderLine, int underLineStyle,
                                       String underLineColor) {

        List<XWPFRun> runs = p.getRuns();
        if (runs.size() > 1) {

            for (int m = 0; m < runs.size(); m++) {
                XWPFRun pRun = runs.get(m);

                // 设置字体样式
                pRun.setBold(isBlod);
                pRun.setTextPosition(position);

                CTRPr pRpr = null;
                if (pRun.getCTR() != null) {
                    pRpr = pRun.getCTR().getRPr();
                    if (pRpr == null) {
                        pRpr = pRun.getCTR().addNewRPr();
                    }
                } else {
                    // pRpr = p.getCTP().addNewR().addNewRPr();
                }
                CTFonts fonts = pRpr.isSetRFonts() ? pRpr.getRFonts() : pRpr
                        .addNewRFonts();
                fonts.setAscii(fontFamily);
                fonts.setEastAsia(fontFamily);
                fonts.setHAnsi(fontFamily);

                // 设置字体大小
                CTHpsMeasure sz = pRpr.isSetSz() ? pRpr.getSz() : pRpr.addNewSz();
                sz.setVal(new BigInteger(fontSize));

                CTHpsMeasure szCs = pRpr.isSetSzCs() ? pRpr.getSzCs() : pRpr
                        .addNewSzCs();
                szCs.setVal(new BigInteger(fontSize));

                //设置下划线
                if (isUnderLine) {
                    CTUnderline u = pRpr.isSetU() ? pRpr.getU() : pRpr.addNewU();
                    u.setVal(STUnderline.Enum.forInt(Math.abs(underLineStyle % 19)));
                    if (underLineColor != null) {
                        u.setColor(underLineColor);
                    }
                }
            }
        } else {
            XWPFRun pRun = null;

            if (p.getRuns() != null && p.getRuns().size() > 0) {
                pRun = p.getRuns().get(0);
            } else {
                pRun = p.createRun();
            }

            // 设置字体样式
            pRun.setBold(isBlod);

            pRun.setTextPosition(position);

            CTRPr pRpr = null;
            if (pRun.getCTR() != null) {
                pRpr = pRun.getCTR().getRPr();
                if (pRpr == null) {
                    pRpr = pRun.getCTR().addNewRPr();
                }
            } else {
                // pRpr = p.getCTP().addNewR().addNewRPr();
            }
            // 设置字体
            CTFonts fonts = pRpr.isSetRFonts() ? pRpr.getRFonts() : pRpr
                    .addNewRFonts();
            fonts.setAscii(fontFamily);
            fonts.setEastAsia(fontFamily);
            fonts.setHAnsi(fontFamily);

            // 设置字体大小
            CTHpsMeasure sz = pRpr.isSetSz() ? pRpr.getSz() : pRpr.addNewSz();
            sz.setVal(new BigInteger(fontSize));

            CTHpsMeasure szCs = pRpr.isSetSzCs() ? pRpr.getSzCs() : pRpr
                    .addNewSzCs();
            szCs.setVal(new BigInteger(fontSize));

            //设置下划线
            if (isUnderLine) {
                CTUnderline u = pRpr.isSetU() ? pRpr.getU() : pRpr.addNewU();
                u.setVal(STUnderline.Enum.forInt(Math.abs(underLineStyle % 19)));
                if (underLineColor != null) {
                    u.setColor(underLineColor);
                }
            }
        }

    }

    /**
     * 设置段落间距信息
     *
     * @param p
     * @param isSpace
     * @param before
     * @param after
     * @param beforeLines
     * @param afterLines
     * @param isLine
     * @param line
     * @param lineValue
     */

    public static void setParagraphSpacingInfo(XWPFParagraph p, boolean isSpace,
                                               String before, String after, String beforeLines, String afterLines,
                                               boolean isLine, String line, STLineSpacingRule.Enum lineValue) {
        CTPPr pPPr = null;
        if (p.getCTP() != null) {
            if (p.getCTP().getPPr() != null) {
                pPPr = p.getCTP().getPPr();
            } else {
                pPPr = p.getCTP().addNewPPr();
            }
        }
        CTSpacing pSpacing = pPPr.getSpacing() != null ? pPPr.getSpacing()
                : pPPr.addNewSpacing();
        if (isSpace) {
            // 段前磅数
            if (before != null) {
                pSpacing.setBefore(new BigInteger(before));
            }
            // 段后磅数
            if (after != null) {
                pSpacing.setAfter(new BigInteger(after));
            }
            // 段前行数
            if (beforeLines != null) {
                pSpacing.setBeforeLines(new BigInteger(beforeLines));
            }
            // 段后行数
            if (afterLines != null) {
                pSpacing.setAfterLines(new BigInteger(afterLines));
            }
        }
        if (isLine) {
            if (line != null) {
                pSpacing.setLine(new BigInteger(line));
            }
            if (lineValue != null) {
                pSpacing.setLineRule(lineValue);
            }
        }
    }

    /**
     * 多个word文件合并，采用poi实现,兼容图片的迁移
     *
     * @param src
     * @param append
     * @throws Exception
     */

    public static void appendBody(XWPFDocument src, XWPFDocument append) throws Exception {
        CTBody src1Body = src.getDocument().getBody();
        CTBody src2Body = append.getDocument().getBody();

        List<XWPFPictureData> allPictures = append.getAllPictures();
        // 记录图片合并前及合并后的ID
        Map<String, String> map = new HashMap();
        for (XWPFPictureData picture : allPictures) {
            String before = append.getRelationId(picture);
            //将原文档中的图片加入到目标文档中
            String after = src.addPictureData(picture.getData(), Document.PICTURE_TYPE_PNG);
            map.put(before, after);
        }
        appendBody(src1Body, src2Body, map);

    }

    private static void appendBody(CTBody src, CTBody append, Map<String, String> map) throws Exception {
        XmlOptions optionsOuter = new XmlOptions();
        optionsOuter.setSaveOuter();
        String appendString = append.xmlText(optionsOuter);

        String srcString = src.xmlText();
        String prefix = srcString.substring(0, srcString.indexOf(">") + 1);
        String mainPart = srcString.substring(srcString.indexOf(">") + 1, srcString.lastIndexOf("<"));
        String sufix = srcString.substring(srcString.lastIndexOf("<"));
        String addPart = appendString.substring(appendString.indexOf(">") + 1, appendString.lastIndexOf("<"));

        if (map != null && !map.isEmpty()) {
            //对xml字符串中图片ID进行替换
            for (Map.Entry<String, String> set : map.entrySet()) {
                addPart = addPart.replace(set.getKey(), set.getValue());
            }
        }
        //将两个文档的xml内容进行拼接
        CTBody makeBody = CTBody.Factory.parse(prefix + mainPart + addPart + sufix);

        src.set(makeBody);
    }

}
