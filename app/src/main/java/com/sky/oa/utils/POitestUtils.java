//package com.sky.oa.utils;
//
//import com.sky.sdk.utils.StringUtils;
//
//import org.apache.poi.hssf.usermodel.HSSFCell;
//import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
//import org.apache.poi.hssf.usermodel.HSSFPicture;
//import org.apache.poi.hssf.usermodel.HSSFPictureData;
//import org.apache.poi.hssf.usermodel.HSSFShape;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ooxml.POIXMLDocumentPart;
//import org.apache.poi.sl.usermodel.PictureData;
//import org.apache.poi.sl.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
//import org.apache.poi.xssf.usermodel.XSSFDrawing;
//import org.apache.poi.xssf.usermodel.XSSFPicture;
//import org.apache.poi.xssf.usermodel.XSSFShape;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//
///**
// * Created by libin on 2020/03/25 12:49 PM Wednesday.
// */
//public class POitestUtils {
//    /**
//     * excel导入
//     *
//     * @param file
//     * @return
//     */
//    @Transactional
//    @Override
//    public String importInstallManageExcel(File file) {
//
//        try {
//
//            //服务器地址
//            String uploadRoot = "D:/";
//
//            //上传的目录
//            String uploadSubDir = "pic";
//
//            //图片对应的行和图片数据
//            Map<Integer, PictureData> sheetIndexPicMap = new HashMap<Integer, PictureData>();
//
//            //图片对应的行和存放路径
//            Map<Integer, String> path = new HashMap<Integer, String>();
//
//            Sheet sheet;
//            if (file.getName().substring(file.getName().length() - 4).equals("xlsx")) {
//                XSSFWorkbook workBook = new XSSFWorkbook(new FileInputStream(file));
//                sheet = (Sheet) workBook.getSheetAt(0);
//
//                //读取sheet的图片放入Map
//                for (POIXMLDocumentPart dr : ((XSSFSheet) sheet).getRelations()) {
//                    if (dr instanceof XSSFDrawing) {
//                        XSSFDrawing drawing = (XSSFDrawing) dr;
//                        List<XSSFShape> shapes = drawing.getShapes();
//                        for (XSSFShape shape : shapes) {
//                            XSSFPicture pic = (XSSFPicture) shape;
//                            XSSFClientAnchor anchor = pic.getPreferredSize();
//                            CTMarker ctMarker = anchor.getFrom();
//                            sheetIndexPicMap.put(ctMarker.getRow(), (PictureData) pic.getPictureData());
//
//                            //获取图片格式
//                            String ext = pic.getPictureData().suggestFileExtension();
//
//                            //保存的文件名
//                            String fileName = UUID.randomUUID().toString() + "." + ext;
//
//                            path.put(ctMarker.getRow(), uploadSubDir + "/" + fileName);
//                        }
//                    }
//                }
//            } else {
//                HSSFWorkbook workBook = new HSSFWorkbook(file.getInputStream());
//                sheet = workBook.getSheetAt(0);
//                //读取sheet的图片放入Map
//                List<HSSFPictureData> pictures = workBook.getAllPictures();
//                if (pictures.size() != 0) {
//                    for (HSSFShape shape : ((HSSFSheet) sheet).getDrawingPatriarch().getChildren()) {
//                        HSSFClientAnchor anchor = (HSSFClientAnchor) shape.getAnchor();
//                        if (shape instanceof HSSFPicture) {
//                            HSSFPicture pic = (HSSFPicture) shape;
//                            int pictureIndex = pic.getPictureIndex() - 1;
//                            HSSFPictureData picData = pictures.get(pictureIndex);
//                            sheetIndexPicMap.put(anchor.getRow1(), picData);
//
//                            //获取图片格式
//                            String ext = picData.suggestFileExtension();
//
//                            //保存的文件名
//                            String fileName = UUID.randomUUID().toString() + "." + ext;
//
//                            path.put(anchor.getRow1(), uploadSubDir + "/" + fileName);
//                        }
//                    }
//                }
//            }
//
//            if (sheet.getLastRowNum() == 0) {
//                logger.error("表中无数据！");
//                return "表中无数据！";
//            }
//
//            //定义数据集合存放excel所有数据
//            List<LockWorker> installManageList = new ArrayList<LockWorker>();
//
//            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
//
//                //错误提示消息
//                StringBuffer msg = new StringBuffer();
//
//                Row row = sheet.getRow(i);
//
//                if (null == row)
//                    continue;
//
//                LockWorker im = new LockWorker();
//
//                im.setInstallerPicture(path.get(i));
//
//                for (int j = 1; j < 12; j++) {
//                    Cell cell = row.getCell(j);
//
//                    String value = null;
//
//                    if (null != cell) {
//                        //全部作为文本处理
//                        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//
//                        value = cell.getStringCellValue();
//                    }
//
//                    if (1 == j) {//姓名
//                        if (StringUtils.isNotBlank(value)) {
//                            im.setInstallerName(value);
//                        } else {
//                            msg.append("请填写姓名！\n");
//                        }
//                    } else if (2 == j) {//备注
//                        im.setRemark(value);
//                    }
//                }
//
//                if (!StringUtils.isBlank(msg.toString())) {
//                    logger.error("第\t" + i + "行： " + msg.toString());
//                    return "第\t" + i + "行： " + msg.toString();
//                }
//
//                installManageList.add(im);
//
//            }
//
//            if (!installManageList.isEmpty()) {
//
//                if (!sheetIndexPicMap.isEmpty()) {
//
//                    for (Map.Entry<Integer, PictureData> map : sheetIndexPicMap.entrySet()) {
//
//                        // 获取图片流
//                        PictureData pic = map.getValue();
//
//                        byte[] data = pic.getData();
//
//                        //输出全路径
//                        String outPath = uploadRoot + path.get(map.getKey());
//
//                        FileOutputStream out = new FileOutputStream(outPath);
//                        out.write(data);
//                        out.close();
//                    }
//                }
//
//                //批量保存
//                installerManageMapper.batchInsert(installManageList);
//
//            }
//
//        } catch (Exception e) {
//            logger.error("导入异常", e);
//            return "系统异常";
//        }
//
//        return "success";
//    }
//}