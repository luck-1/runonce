package com.runonce.util;

/**
 * @author swq
 * @date 2018/12/24 0024
 * @description
 */

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * POI工具类 功能点： 1、实现excel的sheet复制，复制的内容包括单元的内容、样式、注释
 * setMForeColor修改HSSFColor.YELLOW的色值，setMBorderColor修改PINK的色值
 *
 * @author Administrator
 */
public final class PoiUtil {


    /**
     * 复制单元格
     *
     * @param srcCell
     * @param distCell
     * @param copyValueFlag true则连同cell的内容一起复制
     */
    public static void copyCell( Cell srcCell, Cell distCell,
                                boolean copyValueFlag,boolean clearValueFlag) {
//        CellStyle newStyle = wb.createCellStyle();

        CellStyle newStyle = srcCell.getCellStyle();

        newStyle.cloneStyleFrom(srcCell.getCellStyle());

        //样式
        distCell.setCellStyle(newStyle);
        //评论
        if (srcCell.getCellComment() != null) {
            distCell.setCellComment(srcCell.getCellComment());
        }
        // 不同数据类型处理
        CellType srcCellType = srcCell.getCellType();
        distCell.setCellType(srcCellType);
        // 处理单元格内容
        if (copyValueFlag) {
            switch (srcCellType) {
                case STRING:
                    distCell.setCellValue(srcCell.getRichStringCellValue());
                    break;
                case NUMERIC:
                    distCell.setCellValue(srcCell.getNumericCellValue());
                    break;
                case BLANK:
                    distCell.setCellType(CellType.BLANK);
                    break;
                case BOOLEAN:
                    distCell.setCellValue(srcCell.getBooleanCellValue());
                    break;
                case ERROR:
                    distCell.setCellErrorValue(srcCell.getErrorCellValue());
                    break;
                case FORMULA:
                    distCell.setCellFormula(srcCell.getCellFormula());
                    break;
                default:
                    break;


            }
            if (clearValueFlag){
                srcCell.setCellValue("");
            }
        }


    }


}
