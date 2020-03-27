package com.sky.oa.utils;

import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFPictureData;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtils {
    // 默认单元格格式化日期字符串
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 
    // 默认单元格内容为数字时格式
    private static DecimalFormat df = new DecimalFormat("0");
    // 格式化数字
    private static DecimalFormat nf = new DecimalFormat("0.00");
 
    /**
     * 读取Excel中sheet1的内容
     * @param file
     * @return ArrayList<ArrayList<Object>>
     */
    public static ArrayList<ArrayList<Object>> readExcel(File file) {
        if (file == null) {
            return null;
        }
        if (file.getName().endsWith("xlsx")) {
            // 处理ecxel2007
            return readExcel2007(file);
        } else if (file.getName().endsWith("xls")) {
            // 处理ecxel2003
            return readExcel2003(file);
        } else {
            return null;
        }
    }
 
    /**
     * @param file
     * @return
     */
    private static ArrayList<ArrayList<Object>> readExcel2003(File file) {
        try {
            ArrayList<ArrayList<Object>> rowList = new ArrayList<ArrayList<Object>>();
            ArrayList<Object> oneRow = null;
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));
            HSSFSheet sheet = workbook.getSheetAt(0);
            HSSFRow row;
            HSSFCell cell;
            Object value;
            for (int i = sheet.getFirstRowNum(); i < sheet.getPhysicalNumberOfRows(); i++) {
                row = sheet.getRow(i);
                oneRow = new ArrayList<Object>();
                if (row == null || checkRowNull2003(row)) {
                    continue;
                }
                for (int j = row.getFirstCellNum(); j < row.getPhysicalNumberOfCells(); j++) {
                    cell = row.getCell(j);
                    if (cell == null || cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
                        if (j != row.getLastCellNum()) {
                            oneRow.add("");
                        }
                        continue;
                    }
                    switch (cell.getCellType()) {
                    case HSSFCell.CELL_TYPE_STRING:
                        value = cell.getStringCellValue();
                        break;
                    case HSSFCell.CELL_TYPE_BOOLEAN:
                        value = cell.getBooleanCellValue();
                        break;
                    case HSSFCell.CELL_TYPE_BLANK:
                        value = "";
                        break;
                    case HSSFCell.CELL_TYPE_NUMERIC:
                        if ("General".equals(cell.getCellStyle().getDataFormatString())) {
                            double doubleVal = cell.getNumericCellValue();
                            int intVal = (int) Math.round(doubleVal); 
                            if (Double.parseDouble(intVal + ".0") == doubleVal) {
                                value = df.format(intVal);
                            } else {
                                value = nf.format(doubleVal);
                            }
                        } else {
                            value = ((Double)cell.getNumericCellValue()).toString();
                        }
                        break;
                    default:
                        value = cell.toString();
                        break;
                    }
                    oneRow.add(value);
                }
                rowList.add(oneRow);
            }
            return rowList;
        } catch (IOException e) {
            return null;
        }
    }
 
    /**
     * @param file
     * @return
     */
    private static ArrayList<ArrayList<Object>> readExcel2007(File file) {
        try {
            ArrayList<ArrayList<Object>> rowList = new ArrayList<ArrayList<Object>>();
            ArrayList<Object> oneRow = null;
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file));
            XSSFSheet sheet = workbook.getSheetAt(0);
            XSSFRow row;
            XSSFCell cell;
            Object value;
            for (int i = sheet.getFirstRowNum(); i < sheet.getPhysicalNumberOfRows(); i++) {
                row = sheet.getRow(i);
                oneRow = new ArrayList<Object>();
                if (row == null || checkRowNull2007(row)) {
                    continue;
                }
                for (int j = row.getFirstCellNum(); j < row.getPhysicalNumberOfCells(); j++) {
                    cell = row.getCell(j);
                    if (cell == null || cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
                        if (j != row.getLastCellNum()) {
                            oneRow.add("");
                        }
                        continue;
                    }
                    switch (cell.getCellType()) {
                    case XSSFCell.CELL_TYPE_STRING:
                        value = cell.getStringCellValue();
                        break;
                    case XSSFCell.CELL_TYPE_BOOLEAN:
                        value = cell.getBooleanCellValue();
                        break;
                    case XSSFCell.CELL_TYPE_BLANK:
                        value = "";
                        break;
                    case XSSFCell.CELL_TYPE_NUMERIC:
                        if ("General".equals(cell.getCellStyle().getDataFormatString())) {
                            double doubleVal = cell.getNumericCellValue();
                            int intVal = (int) Math.round(doubleVal); 
                            if (Double.parseDouble(intVal + ".0") == doubleVal) {
                                value = df.format(intVal);
                            } else {
                                value = nf.format(doubleVal);
                            }
                        } else {
                            value = ((Double)cell.getNumericCellValue()).toString();
                        }
                        break;
                    default:
                        value = cell.toString();
                        break;
                    }
                    oneRow.add(value);
                }
                rowList.add(oneRow);
            }
            return rowList;
        } catch (IOException e) {
            return null;
        }
    }
 
    /**
     * 判断行为空(xls)
     * @param row
     * @return
     */
    private static boolean checkRowNull2003(HSSFRow row) {
        for (int i = row.getFirstCellNum(); i < row.getPhysicalNumberOfCells(); i++) {
            HSSFCell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != HSSFCell.CELL_TYPE_BLANK) {
                return false;
            }
        }
        return true;
    }
 
    /**
     * 判断行为空(xlsx)
     * @param row
     * @return
     */
    private static boolean checkRowNull2007(XSSFRow row) {
        for (int i = row.getFirstCellNum(); i < row.getPhysicalNumberOfCells(); i++) {
            XSSFCell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != XSSFCell.CELL_TYPE_BLANK) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取图片和位置 (xls)
     * @param sheet
     * @return
     * @throws IOException
     */
    public static Map<String, HSSFPictureData> getPictures (HSSFSheet sheet) throws IOException {
        Map<String, HSSFPictureData> map = new HashMap<String, HSSFPictureData>();
        List<HSSFShape> list = sheet.getDrawingPatriarch().getChildren();
        for (HSSFShape shape : list) {
            if (shape instanceof HSSFPicture) {
                HSSFPicture picture = (HSSFPicture) shape;
                HSSFClientAnchor cAnchor = picture.getClientAnchor();
                HSSFPictureData pdata = picture.getPictureData();
                String key = cAnchor.getRow1() + "-" + cAnchor.getCol1(); // 行号-列号
                map.put(key, pdata);
            }
        }
        return map;
    }

    /**
     * 获取图片和位置 (xlsx)
     * @param sheet
     * @return
     * @throws IOException
     */
    public static Map<String, XSSFPictureData> getPictures (XSSFSheet sheet) throws IOException {
        Map<String, XSSFPictureData> map = new HashMap<String, XSSFPictureData>();
        List<POIXMLDocumentPart> list = sheet.getRelations();
        for (POIXMLDocumentPart part : list) {
            if (part instanceof XSSFDrawing) {
                XSSFDrawing drawing = (XSSFDrawing) part;
                List<XSSFShape> shapes = drawing.getShapes();
                for (XSSFShape shape : shapes) {
                    XSSFPicture picture = (XSSFPicture) shape;
                    XSSFClientAnchor anchor = picture.getPreferredSize();
                    CTMarker marker = anchor.getFrom();
                    String key = marker.getRow() + "-" + marker.getCol();
                    map.put(key, picture.getPictureData());
                }
            }
        }
        return map;
    }


}