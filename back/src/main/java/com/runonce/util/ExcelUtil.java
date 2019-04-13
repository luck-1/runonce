package com.runonce.util;


import com.runonce.ReturnCode;
import com.runonce.model.eventversionone.MaterialGrouping;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Description: Excel操作
 * <p>
 * CreateTime: 2017年12月11日  下午3:08:09
 * <p>
 * Change History:
 * <p>
 * Date             CR Number              Name              Description of change
 */

public class ExcelUtil {
    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";


    /**
     * 判断Excel的版本,获取Workbook
     *
     * @param in
     * @param
     * @return
     * @throws IOException
     */

    public static Workbook getWorkbook(InputStream in, MultipartFile file) throws IOException {
        Workbook wb = null;
        ZipSecureFile.setMinInflateRatio(-1.0d);
        if (file.getOriginalFilename().matches("^.+\\.(?i)(xls)$")) {
            //Excel 2003
            wb = new HSSFWorkbook(in);

        } else if (file.getOriginalFilename().matches("^.+\\.(?i)(xlsx)$")) {
            // Excel 2007/2010
            wb = new XSSFWorkbook(in);
        }
        return wb;
    }


    /**
     * 读取Excel测试，兼容 Excel 2003/2007/2010
     *
     * @throws Exception
     */

    public static void main(String[] args) throws Exception {


        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            // 同时支持Excel 2003、2007  
            File excelFile = new File("e:/untangleMould_2.0.xlsx"); // 创建文件对象
            FileInputStream in = new FileInputStream(excelFile); // 文件流
            FileOutputStream out = new FileOutputStream(new File("e:/untangleMould_2.0.zip"));
            out.write(in.read());

            BufferedInputStream bis = new BufferedInputStream(new FileInputStream("e:/untangleMould_2.0.xlsx"));
            //读取文件内容
            byte[] b = new byte[bis.available()];
            out.write(bis.read(b));

			/*char[] c = new char[b.length];
			for (int i = 0; i < c.length; i++) {
				c[i]=(char) b[i];
			}
			System.out.println(Arrays.toString(c));//乱码
			 */

            System.out.println(Arrays.toString(b));//得到的是字节
            //String(byte[])把字节数组转成字符串
            System.out.println(new String(b));//可以得到中文
            bis.close();//关闭流(关闭bis就可以了)
            out.close();


        } catch (Exception e) {

        }
    }

/**
 * 设置当前excel中sheet的下标：0开始
 */
/*
//            Sheet sheet = workbook.getSheetAt(0);   // 遍历第一个Sheet
            Sheet sheet = workbook.getSheetAt(6);   // 遍历第三个Sheet

            //获取总行数
//          System.out.println(sheet.getLastRowNum());

            // 为跳过第一行目录设置count
            int isRead = 100;
            int count = 0;
            int isReadEnd = 0;
            int startNum = 0;
            int startNum2 = 0;
            String currType = "";
            List<MaterialGrouping> materialGroupingsList = new ArrayList<>();

            for (Row row : sheet) {
                MaterialGrouping materialGrouping = new MaterialGrouping();
                try {
                    //isRead=2;
                    // 跳过第一和第二行的目录
                    if (row.getCell(0) != null && row.getCell(0).toString().equals("1.0")) {
                        isRead = row.getRowNum();
                    }

                    if (isRead != 100) {
                        if (row.getCell(0) == null) {
                            isReadEnd = row.getRowNum();
                        }
                    }
                    if (count < isRead) {

                        for (int i = 0; i < row.getLastCellNum(); i++) {


                            if ("窗口提交材料要求".equals(getValue(row, i) + "".replace(" ", ""))) {
                                startNum = i;
                                // System.out.println("----------------------" + i);

                            }

                            if ("审批输出物".equals(getValue(row, i) + "".replace(" ", ""))) {
                                startNum2 = i;
                                // System.out.println("----------------------" + i);

                            }


                        }

                        count++;
                        continue;
                    }

                    if (isReadEnd != 0) {

                        if (row.getRowNum() == isReadEnd) {

                            return;

                        }
                    }


                    materialGrouping.setId(SysUtil.getUUID());
                    materialGrouping.setOrderNum(getValue(row, 0).toString());

                    if (getValue(row, 1) != null && !"".equals(getValue(row, 1).toString())) {

                        currType = getType(getValue(row, 1).toString());

                    }

                    materialGrouping.setType(currType);
                    materialGrouping.setNumber(getValue(row, 2).toString());
                    materialGrouping.setIo(getValue(row, 3).toString());
                    materialGrouping.setName(getValue(row, 4).toString());

                    if (getValue(row, startNum) != null && getValue(row, startNum).toString().equals("√")) {
                        materialGrouping.setOriginalScriptProvide(true);
                    } else {
                        materialGrouping.setOriginalScriptProvide(false);
                    }

                    if (getValue(row, startNum + 1) != null && getValue(row, startNum + 1).toString().equals("√")) {
                        materialGrouping.setCopyProvide(true);
                    } else {
                        materialGrouping.setCopyProvide(false);
                    }

                    if (getValue(row, startNum + 2) != null && getValue(row, startNum + 2).toString().equals("√")) {
                        materialGrouping.setElectronicProvide(true);
                    } else {
                        materialGrouping.setElectronicProvide(false);
                    }

                    if (getValue(row, startNum2) != null && getValue(row, startNum2).toString().equals("√")) {
                        materialGrouping.setOriginalScriptOutput(true);
                    } else {
                        materialGrouping.setOriginalScriptOutput(false);
                    }

                    if (getValue(row, startNum2 + 1) != null && getValue(row, startNum2 + 1).toString().equals("√")) {
                        materialGrouping.setElectronicOutput(true);
                    } else {

                        materialGrouping.setElectronicOutput(false);
                    }
                    if (getValue(row, startNum2 + 2) != null && !getValue(row, startNum2 + 2).toString().equals("")) {


                        Double f = Double.valueOf(getValue(row, startNum2 + 2).toString());

                        Integer countInter = (int) Math.ceil(f);
                        materialGrouping.setCount(countInter);
                    }


                    materialGrouping.setProvideWay(getValue(row, startNum2 + 3) == null ? "" : getValue(row, startNum2 + 3).toString());
                    materialGrouping.setSource(getValue(row, startNum2 + 4) == null ? "" : getValue(row, startNum2 + 4).toString());
                    materialGrouping.setDepartmrntName(getValue(row, startNum2 + 5) == null ? "" : getValue(row, startNum2 + 5).toString());
                    materialGrouping.setDepartmrntSystemName(getValue(row, startNum2 + 6) == null ? "" : getValue(row, startNum2 + 6).toString());
                    materialGrouping.setDepartmrntSystemUrl(getValue(row, startNum2 + 7) == null ? "" : getValue(row, startNum2 + 7).toString());


                } catch (Exception e) {
                    e.printStackTrace();
                }
                materialGroupingsList.add(materialGrouping);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

}*/

//    private static String getType(String Type) {
//
//        if (Type != null && !"".equals(Type)) {
//            if (Type.contains("证件")) {
//                return "证件";
//            }
//            if (Type.contains("证明")) {
//                return "证明";
//            }
//            if (Type.contains("申请表")) {
//                return "申请表";
//            }
//        }
//        return "";
//    }
//
//
//    private static Object getValue(Row row, int cellNumber) {
//
//        Cell cell = row.getCell(cellNumber);
//        if (cell == null) {
//            // System.out.print("null" + "\t");
//            return "";
//        }
//
//        Object obj = getValue(cell);
//
//
//        return obj;
//    }
//
//    private static Object getValue(Cell cell) {
//        Object obj = null;
//        switch (cell.getCellTypeEnum()) {
//            case BOOLEAN:
//                obj = cell.getBooleanCellValue();
//                break;
//            case ERROR:
//                obj = cell.getErrorCellValue();
//                break;
//            case NUMERIC:
//                obj = cell.getNumericCellValue();
//                break;
//            case STRING:
//                obj = cell.getStringCellValue();
//                break;
//            default:
//                break;
//        }
//        return obj;
//    }


}

