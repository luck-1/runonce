package com.runonce.util;


import org.apache.poi.hpsf.ClassIDPredefined;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.ooxml.POIXMLException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.*;
import org.apache.poi.poifs.filesystem.Ole10Native;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.ImageUtils;
import org.apache.poi.util.Units;
import org.apache.poi.xssf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.drawingml.x2006.main.*;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTTwoCellAnchor;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObjects;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;

import javax.imageio.ImageIO;
import javax.xml.namespace.QName;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author swq
 * @date 2019/1/8 0008
 * @description
 */
public class ExcelExport {

    public static void main(String[] args) {
        FileOutputStream fileOut;
        try {

            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet1 = wb.createSheet("sheet1");
            java.util.List<String> data = new ArrayList<>();
            data.add("e:/20190107185622.png");
            data.add("e:/timg.jpg");
            createHorizontalImg(wb, sheet1, data, 0, 0, 50f, 50f, 0);
            fileOut = new FileOutputStream("e:/excel1.xlsx");

            // ????
            wb.write(fileOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    public static Dimension setPreferredSize(Picture picture, double scaleX, double scaleY) {
//        ClientAnchor anchor = picture.getClientAnchor();
//        PictureData data = picture.getPictureData();
//        Sheet sheet = picture.getSheet();
//        // in pixel
//        Dimension imgSize = getImageDimension(new ByteArrayInputStream(data.getData()), data.getPictureType());
//        // in emus
//        Dimension anchorSize = getDimensionFromAnchor(picture);
//        final double scaledWidth = (scaleX == Double.MAX_VALUE)
//                ? imgSize.getWidth() : anchorSize.getWidth() / EMU_PER_PIXEL * scaleX;
//        final double scaledHeight = (scaleY == Double.MAX_VALUE)
//                ? imgSize.getHeight() : anchorSize.getHeight() / EMU_PER_PIXEL * scaleY;
//
//        double w = 0;
//        int col2 = anchor.getCol1();
//        int dx2 = 0;
//
//        //space in the leftmost cell
//        w = sheet.getColumnWidthInPixels(col2++);
//        w -= anchor.getDx1() / (double) EMU_PER_PIXEL;
//
//
//        while (w < scaledWidth) {
//            w += sheet.getColumnWidthInPixels(col2++);
//        }
//
//        if (w > scaledWidth) {
//            //calculate dx2, offset in the rightmost cell
//            double cw = sheet.getColumnWidthInPixels(--col2);
//            double delta = w - scaledWidth;
//
//            dx2 = (int) ((cw - delta) * EMU_PER_PIXEL);
//            if (dx2 < 0) {
//                dx2 = 0;
//            }
//        }
//        anchor.setCol2(col2);
//        anchor.setDx2(dx2);
//
//        double h = 0;
//        int row2 = anchor.getRow1();
//        int dy2 = 0;
//
//        h = getRowHeightInPixels(sheet, row2++);
//
//
//        h -= anchor.getDy1() / (double) EMU_PER_PIXEL;
//
//
//        while (h < scaledHeight) {
//            h += getRowHeightInPixels(sheet, row2++);
//        }
//
//        if (h > scaledHeight) {
//            double ch = getRowHeightInPixels(sheet, --row2);
//            double delta = h - scaledHeight;
//
//            dy2 = (int) ((ch - delta) * EMU_PER_PIXEL);
//
//            if (dy2 < 0) {
//                dy2 = 0;
//            }
//        }
//
//        anchor.setRow2(row2);
//        anchor.setDy2(dy2);
//
//        return new Dimension(
//                (int) Math.round(scaledWidth * EMU_PER_PIXEL),
//                (int) Math.round(scaledHeight * EMU_PER_PIXEL)
//        );
//    }
//
//    public static double getRowHeightInPixels(Sheet sheet, int rowNum) {
//        Row r = sheet.getRow(rowNum);
//        double points = (r == null) ? sheet.getDefaultRowHeightInPoints() : r.getHeightInPoints();
//        return Units.toEMU(points) / (double) EMU_PER_PIXEL;
//    }
//
//    public static void resize(XSSFSheet sheet, XSSFPicture picture, double scaleX, double scaleY) {
////        CreationHelper helper = sheet.getWorkbook().getCreationHelper();
//        XSSFClientAnchor anchor = picture.getClientAnchor();
//        XSSFClientAnchor pref = getPreferredSize(sheet, picture, scaleX, scaleY);
//        if (anchor == null || pref == null) {
//
//            return;
//        }
//
//        int row2 = anchor.getRow1() + (pref.getRow2() - pref.getRow1());
//        int col2 = anchor.getCol1() + (pref.getCol2() - pref.getCol1());
//
//        anchor.setCol2(row2);
//        // anchor.setDx1(0);
//        int i = pref.getDx2();
//        anchor.setDx2(pref.getDx2());
//        int w = pref.getDx2();
//        anchor.setRow2(col2);
//        // anchor.setDy1(0);
//        anchor.setDy2(pref.getDy2());
//    }
//
//
//    public static XSSFClientAnchor getPreferredSize(XSSFSheet sheet, XSSFPicture picture, double scaleX, double scaleY) {
//        Dimension dim = setPreferredSize(picture, scaleX, scaleY);
//        CTPicture ctPicture = picture.getCTPicture();
//        CTPositiveSize2D size2d = ctPicture.getSpPr().getXfrm().getExt();
//        size2d.setCx((int) dim.getWidth());
//        size2d.setCy((int) dim.getHeight());
//
////        CreationHelper helper = sheet.getWorkbook().getCreationHelper();
//        XSSFClientAnchor anchor = picture.getClientAnchor();
//        return anchor;
//    }
//
//    public static Dimension getDimensionFromAnchor(Picture picture) {
//        ClientAnchor anchor = picture.getClientAnchor();
//        Dimension dimension = picture.getImageDimension();
//        double ww = dimension.getWidth();
//        double hh = dimension.getHeight();
//        boolean isHSSF = (anchor instanceof HSSFClientAnchor);
//        Sheet sheet = picture.getSheet();
//
//        double w = ww;
//        int col2 = anchor.getCol1();
//
//        //space in the leftmost cell
////        w = sheet.getColumnWidthInPixels(col2++);
//        if (isHSSF) {
//            w *= 1 - anchor.getDx1() / 1024d;
//        } else {
//            w -= anchor.getDx1() / (double) EMU_PER_PIXEL;
//        }
//
//        while (col2 < anchor.getCol2()) {
//            w += sheet.getColumnWidthInPixels(col2++);
//        }
//
//        if (isHSSF) {
//            w += sheet.getColumnWidthInPixels(col2) * anchor.getDx2() / 1024d;
//        } else {
//            w += anchor.getDx2() / (double) EMU_PER_PIXEL;
//        }
//
//        double h = hh;
//        int row2 = anchor.getRow1();
//
////        h = getRowHeightInPixels(sheet,row2++);
//        if (isHSSF) {
//            h *= 1 - anchor.getDy1() / 256d;
//        } else {
//            h -= anchor.getDy1() / (double) EMU_PER_PIXEL;
//        }
//
//        while (row2 < anchor.getRow2()) {
//            h += getRowHeightInPixels(sheet, row2++);
//        }
//
//        if (isHSSF) {
//            h += getRowHeightInPixels(sheet, row2) * anchor.getDy2() / 256;
//        } else {
//            h += anchor.getDy2() / (double) EMU_PER_PIXEL;
//        }
//
//        w *= EMU_PER_PIXEL;
//        h *= EMU_PER_PIXEL;
//
//        return new Dimension((int) Math.rint(w), (int) Math.rint(h));
//    }

//    /**
//     * ??
//     *
//     * @param finalSheet       sheet
//     * @param startRowNum      ???
//     * @param startCellNum     ???
//     * @param maxWidth         ???????? ??????????
//     * @param maxHeight        ???????? ??????????
//     * @param headHeight       ?????
//     * @param pictureIdxList   ??
//     * @param bottomValueCount ???????????? 0???????
//     */
//    public static void pictureToSheet(Sheet finalSheet, int startRowNum, int startCellNum, float maxWidth, float maxHeight, float headHeight, java.util.List<Integer> pictureIdxList, int bottomValueCount) throws NoSuchAlgorithmException {
//
//        // ???????
//        //????????
//        Row startRow = null;
//        int createRowNum = 0;
//        //??????
//        int valueRowNum = startRowNum;
//        for (Integer pictureIdx : pictureIdxList) {
//
//            if (finalSheet.getRow(startRowNum) == null) {
//                startRow = finalSheet.createRow(startRowNum);
//
//            } else {
//                startRow = finalSheet.getRow(startRowNum);
//            }
//            Drawing patriarch = finalSheet.createDrawingPatriarch();
//            CreationHelper helper = finalSheet.getWorkbook().getCreationHelper();
//            ClientAnchor anchor = helper.createClientAnchor();
//            // ??????
//            anchor.setCol1(startCellNum);
//            anchor.setRow1(startRowNum);
//
//            // ????????
//            float cellWidth = finalSheet.getColumnWidthInPixels(startCellNum);
//            // ??????
//            float cellHeight = (startRow.getHeightInPoints() / 72) * 96;
//
//            // ????
//            Picture pict = patriarch.createPicture(anchor, pictureIdx);
//            //????
//            Dimension dimension = pict.getImageDimension();
//            double scale = 0;
//            //????
//            int imgWidth = dimension.width;
//            //????
//            int imgHeight = dimension.height;
//
//            float standardWidth = 0;
//            float standardHeight = 0;
//
//            if (maxWidth > 0) {
//                scale = maxWidth / imgWidth;
//                standardWidth = (int) (scale * imgWidth);
//                standardHeight = (int) (scale * imgHeight);
//            }
//            if (standardHeight > maxHeight) {
//                if (maxHeight > 0) {
//                    scale = div(maxHeight + "", imgHeight + "");
//                    standardWidth = (int) (scale * imgWidth);
//                    standardHeight = (int) (scale * imgHeight);
//                }
//            }
//
//            //???????
//            double div = div(standardHeight + "", cellHeight + "");
//            createRowNum = (int) (div(standardHeight + "", cellHeight + ""));
//
//            startRowNum = startRowNum + createRowNum;
//
//            // ????????????
//            double a = standardWidth / cellWidth;
//            double b = standardHeight / cellHeight;
//
//            pict.resize(a, b);
//        }
//
//    }
//

    /**
     * ??????????????????????????scale???
     * BigDecimal.ROUND_UP??
     *
     * @param v1 ???
     * @param v2 ??
     * @return ??????
     */
    private static double div(String v1, String v2) {

        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, 5, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    /**
     * ????(??)
     *
     * @param workbook        ???
     * @param finalSheet      sheet?
     * @param objectsNameList ??
     * @param startRowNum     ???
     * @param startCellNum    ???
     * @param maxWidth        ????
     * @param maxHeight       ????
     * @throws Exception ??
     */
    private static void createHorizontalImg(XSSFWorkbook workbook, XSSFSheet finalSheet, List<String> objectsNameList, int startRowNum, int startCellNum, Float maxWidth, Float maxHeight, int rowValueCount) throws Exception {

        if (objectsNameList == null || objectsNameList.size() <= 0) {
            return;
        }

        int bottomStartRowCountNum = startRowNum;

        for (String name : objectsNameList) {
            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();

            //????
            BufferedImage bufferImg = ImageIO.read(new File(name));
            ImageIO.write(bufferImg, "png", byteArrayOut);

//            if (EXUtil.isPicture(name)) {
//
//                float imgWidth = bufferImg.getWidth();
//                //???
//                float imgHeight = bufferImg.getHeight();
//                double scale = div(maxWidth + "", imgWidth + "");
//                int width = (int) (scale * imgWidth);
//                int height = (int) (scale * imgHeight);
//
//                if (height > maxHeight) {
//                    scale = div(maxHeight + "", imgHeight + "");
//                    width = (int) (scale * imgWidth);
//                    height = (int) (scale * imgHeight);
//
//                }
//                anchor.setRow1(bottomStartRowCountNum);
//                anchor.setCol1(startCellNum);
//                // ????????
//                float cellWidth = finalSheet.getColumnWidthInPixels(startCellNum);
//
//                // ??????
//                float cellHeight = (float) ImageUtils.getRowHeightInPixels(finalSheet, startRowNum);
//                // ????????????
//                double a = width / cellWidth;
//                double b = height / cellHeight;
//                int pictureIdx = workbook.addPicture(byteArrayOut.toByteArray(), Workbook.PICTURE_TYPE_PNG);
//                System.out.println("pictureIdx :" + pictureIdx);
//                XSSFPicture pict = patriarch.createPicture(anchor, pictureIdx);
//
//                pict.resize(a, b);
////                int type = "1.png".substring("1.png".lastIndexOf(".")).toLowerCase().equals("png") ? Workbook.PICTURE_TYPE_PNG : Workbook.PICTURE_TYPE_JPEG;
////                int pictureIdx1 = workbook.addPicture(byteArrayOut.toByteArray(), type);
////                XSSFPicture picture = patriarch.createPicture(anchor, pictureIdx1);
////                pict.resize();
//            }

            XSSFDrawing pat = finalSheet.createDrawingPatriarch();
            int picIdx = workbook.addPicture(byteArrayOut.toByteArray(), Workbook.PICTURE_TYPE_PNG);
            XSSFClientAnchor anchor1 = pat.createAnchor(0, 0, 0, 0, 1, 1, 3, 6);

            int oleIdx = workbook.addOlePackage(byteArrayOut.toByteArray(), name, name, name);
            XSSFObjectData xssfObjectData = pat.createObjectData(anchor1, oleIdx, picIdx);
            xssfObjectData.getOleObject().setAutoLoad(true);
//            EXUtil.embedObjectToCell(finalSheet, byteArrayOut.toByteArray(), 1 + ".png", startCellNum + 2, bottomStartRowCountNum, 0, 0);
            bottomStartRowCountNum += 4;
            byteArrayOut.close();
        }
//
    }


}
