//package com.sky.oa.utils;
//
//import android.content.Context;
//import android.widget.Toast;
//
//import com.sky.sdk.utils.ToastUtils;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import jxl.Cell;
//import jxl.Sheet;
//import jxl.Workbook;
//import jxl.WorkbookSettings;
//import jxl.format.Colour;
//import jxl.write.Label;
//import jxl.write.WritableCell;
//import jxl.write.WritableCellFormat;
//import jxl.write.WritableFont;
//import jxl.write.WritableSheet;
//import jxl.write.WritableWorkbook;
//import jxl.write.WriteException;
//
///**
// * Created by libin on 2020/03/25 12:49 PM Wednesday.
// */
//public class ExcelJXLUtils {
//    private static WritableFont arial14font = null;
//    private static WritableCellFormat arial14format = null;
//    private static WritableFont arial10font = null;
//    private static WritableCellFormat arial10format = null;
//    private static WritableFont arial12font = null;
//    private static WritableCellFormat arial12format = null;
//    private final static String UTF8_ENCODING = "UTF-8";
//
//    /**
//     * 单元格的格式设置 字体大小 颜色 对齐方式、背景颜色等...
//     */
//    private static void format() {
//        try {
//            arial14font = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD);
//            arial14font.setColour(jxl.format.Colour.LIGHT_BLUE);
//            arial14format = new WritableCellFormat(arial14font);
//            arial14format.setAlignment(jxl.format.Alignment.CENTRE);
//            arial14format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
//            arial14format.setBackground(jxl.format.Colour.VERY_LIGHT_YELLOW);
//
//            arial10font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
//            arial10format = new WritableCellFormat(arial10font);
//            arial10format.setAlignment(jxl.format.Alignment.CENTRE);
//            arial10format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
//            arial10format.setBackground(Colour.GRAY_25);
//
//            arial12font = new WritableFont(WritableFont.ARIAL, 10);
//            arial12format = new WritableCellFormat(arial12font);
//            //对齐格式
//            arial10format.setAlignment(jxl.format.Alignment.CENTRE);
//            //设置边框
//            arial12format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
//
//        } catch (WriteException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 初始化Excel
//     *
//     * @param fileName  导出excel存放的地址（目录）
//     * @param sheetName 页签名称
//     * @param colName   excel中包含的列名（可以有多个）
//     */
//    public static void initExcel(String fileName, String sheetName, String[] colName) {
//        format();
//        WritableWorkbook workbook = null;
//        try {
//            File file = new File(fileName);
//            if (!file.exists()) {
//                file.createNewFile();
//            }
//            workbook = Workbook.createWorkbook(file);
//            //设置表格的名字
//            WritableSheet sheet = workbook.createSheet(sheetName, 0);
//            //创建标题栏
//            sheet.addCell((WritableCell) new Label(0, 0, fileName, arial14format));
//            for (int col = 0; col < colName.length; col++) {
//                sheet.addCell(new Label(col, 0, colName[col], arial10format));
//            }
//            //设置行高
//            sheet.setRowView(0, 340);
//            workbook.write();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (workbook != null) {
//                try {
//                    workbook.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    @SuppressWarnings("unchecked")
//    public static void writeContentToExcel(List<List<String>> datas, String fileName, String sheetName, Context c) {
//        if (datas == null && datas.isEmpty()) {
//            ToastUtils.showShort(c, "数据错误");
//            return;
//        }
//        WritableWorkbook writebook = null;
//        InputStream in = null;
//        List<String> data;
//        try {
//            WorkbookSettings setEncode = new WorkbookSettings();
//            setEncode.setEncoding(UTF8_ENCODING);
//            in = new FileInputStream(new File(fileName));
//            Workbook workbook = Workbook.getWorkbook(in);
//            writebook = Workbook.createWorkbook(new File(fileName), workbook);
//            //获取页签
//            WritableSheet sheet = writebook.getSheet(sheetName);
//
//            for (int i = 0; i < datas.size(); i++) {
//                data = datas.get(i);
//
//                for (int j = 0; j < data.size(); j++) {
//                    String content = data.get(j);
//                    sheet.addCell(new Label(j, i + 1, content, arial12format));
////                    if (data.get(j).length() <= 4) {
////                        //设置列宽
////                        sheet.setColumnView(j, data.get(j).length() + 8);
////                    } else {
////                        //设置列宽
////                        sheet.setColumnView(j, data.get(j).length() + 5);
////                    }
//                }
//                //设置行高
//                sheet.setRowView(i + 1, 350);
//            }
//            writebook.write();
//            Toast.makeText(c, "导出Excel成功", Toast.LENGTH_SHORT).show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (writebook != null) {
//                try {
//                    writebook.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            if (in != null) {
//                try {
//                    in.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    /**
//     * 读取EXCEL内容
//     *
//     * @param filePath
//     * @return
//     * @throws Exception
//     */
//    public static List getExcelContent(String filePath) throws Exception {
//        File file = new File(filePath);
//        if (!file.exists()) {
//            return null;
//        }
//        List<Map<String, Object>> list = new ArrayList<>();
//        Map<String, Object> mapResult = null;//根据具体的生成对应的对象文件
//
//        // 创建输入流
//        InputStream stream = new FileInputStream(filePath);
//        // 获取Excel文件对象
//        Workbook rwb = Workbook.getWorkbook(stream);
//        // 获取文件的指定工作表 默认的第一个
//        Sheet sheet = rwb.getSheet(0);
//        // 行数(表头的目录不需要，从1开始)
//        for (int i = 1; i < sheet.getRows(); i++) {
//            mapResult = new HashMap<>();
//            // 列数
//            for (int j = 0; j < sheet.getColumns(); j++) {
//                // 获取第i行，第j列的key
//                Cell keyCell = sheet.getCell(j, 0);
//                // 获取第i行，第j列的value
//                Cell valueCell = sheet.getCell(j, i);
//                mapResult.put(keyCell.getContents(), valueCell.getContents());
//            }
//            // 把刚获取的列存入list，也可以存入到数据库
//            list.add(mapResult);
//        }
//        return list;
//    }
//
//    /**
//     * 读取EXCEL内容
//     *
//     * @param filePath
//     * @return
//     * @throws Exception
//     */
//    public static List getExcelImage(String filePath) throws Exception {
//        File file = new File(filePath);
//        if (!file.exists()) {
//            return null;
//        }
//        List<Map<String, Object>> list = new ArrayList<>();
//        Map<String, Object> mapResult = null;//根据具体的生成对应的对象文件
//
//        // 创建输入流
//        InputStream stream = new FileInputStream(filePath);
//        // 获取Excel文件对象
//        Workbook rwb = Workbook.getWorkbook(stream);
//        // 获取文件的指定工作表 默认的第一个
//        Sheet sheet = rwb.getSheet(0);
//        // 行数(表头的目录不需要，从1开始)
//        for (int i = 1; i < sheet.getRows(); i++) {
//            mapResult = new HashMap<>();
//            // 列数
//            for (int j = 0; j < sheet.getColumns(); j++) {
//                // 获取第i行，第j列的key
//                Cell keyCell = sheet.getCell(j, 0);
//                // 获取第i行，第j列的value
//                Cell valueCell = sheet.getCell(j, i);
//                mapResult.put(keyCell.getContents(), valueCell.getContents());
//            }
//            // 把刚获取的列存入list，也可以存入到数据库
//            list.add(mapResult);
//        }
//        return list;
//    }
//}