package com.sky.oa.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.sky.oa.R;
import com.sky.sdk.utils.BitmapUtils;

import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by libin on 2020/03/25 12:49 PM Wednesday.
 */
public class ExcelPOiUtils {
    /**
     * 创建xslx格式的excel
     *
     * @param fileName  文件绝对路径
     * @param sheetName 页签名称
     * @param columName 列名
     */
    public static void initExcelAndXLSX(Context context, String fileName, String sheetName, String[] columName) {
        Workbook wb = new XSSFWorkbook();//创建xlsx格式的excel
        Sheet sheet = wb.createSheet(sheetName);//创建页签sheet
        Row row = sheet.createRow(0);
        Cell cell = null;
        for (int i = 0; i < columName.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(columName[i]);
        }

        //先把读进来的图片放到一个ByteArrayOutputStream中，以便产生ByteArray
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        BitmapUtils.getBitmapFromId(context, R.mipmap.ic_banner).compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOut);
        //读进一个excel模版
        //创建一个工作薄
        Drawing patriarch = sheet.createDrawingPatriarch();
        XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 1023, 255, 0, 20, 10, 30);
        patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), XSSFWorkbook.PICTURE_TYPE_JPEG));

//        sheet.setDefaultRowHeight((short) 40);
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(fileName);
            wb.write(fileOut);
            wb.close();
            fileOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加数据
     *
     * @param datas     数据
     * @param fileName  文件绝对路径
     * @param sheetName 页签名称
     */
    public static void writeToExcelXLSX(List<List<String>> datas, String fileName, String sheetName) {
        try {
            Workbook wb = new XSSFWorkbook(new FileInputStream(fileName));
            Sheet sheet = wb.getSheet(sheetName);

            List<String> data;
            String content;
            Row row = null;
            Cell cell = null;

            for (int i = 0; i < datas.size(); i++) {
                data = datas.get(i);
                row = sheet.createRow(i + 1);
                for (int j = 0; j < data.size(); j++) {
                    content = data.get(j);
                    cell = row.createCell(j);
                    cell.setCellValue(content);
                }
            }
            FileOutputStream fileOut = new FileOutputStream(fileName);
            wb.write(fileOut);
            wb.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取图片和位置 (xlsx)
     *
     * @param sheet
     * @return
     * @throws IOException
     */
    public static Map<String, PictureData> getPictures2(XSSFSheet sheet) throws IOException {
        Map<String, PictureData> map = new HashMap<String, PictureData>();
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