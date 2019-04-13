
package com.runonce.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyExcelUtil {

    static XSSFWorkbook wb = null;

    public static void main(String[] args) throws IOException {
        File file = new File("E://22222.xlsx");
        wb = new XSSFWorkbook(new FileInputStream(file));
        Sheet sheet = wb.getSheetAt(7);


        List<List<String>> dataList = new ArrayList<>();
        //读取数据
        List<String> data;
        for (int i = 2; i < 7; i++) {
            data = new ArrayList<>();
            Row row = sheet.getRow(i);
            for (int k = 0; k < 20; k++) {
                data.add(row.getCell(k).getStringCellValue());
            }
            dataList.add(data);
        }

        for (int i = 0; i < dataList.size(); i++) {
            for (int k = 0; k < dataList.get(i).size(); k++) {
                System.out.print("|" + dataList.get(i).get(k));
            }
            System.out.println();
        }


        //上一个单元格的值
        //当前单元格的值
        Stringexcel thisCellValue;
        Stringexcel lastCellValue = new Stringexcel();

        List<Stringexcel> Stringexcel = new ArrayList<>();
        int startX=0;
        int startY=2;
     for (int i = 0; i < dataList.size(); i++) {

            for (int k = 0; k < dataList.get(i).size(); k++) {

                if (k == 0) {

                    lastCellValue.setY_start(startY+i);
                    lastCellValue.setY_end(startY+i);
                    lastCellValue.setX_start(startX+k);
                    lastCellValue.setX_end(startX+k);
                    lastCellValue.setStrData(dataList.get(i).get(k));


                }

                thisCellValue = new Stringexcel();

                thisCellValue.setY_start(startY+i);
                thisCellValue.setY_end(startY+i);
                thisCellValue.setX_start(startX+k);
                thisCellValue.setX_end(startX+k);
                thisCellValue.setStrData(dataList.get(i).get(k));

                if (!thisCellValue.getStrData().equals(lastCellValue.getStrData()) || k == dataList.get(i).size() - 1) {

                    int b;
                   if (k == dataList.get(i).size() - 1 && dataList.get(i).get(k).equals(dataList.get(i).get(k-1))) {
                        b = thisCellValue.getX_end();
                    } else {
                       b = thisCellValue.getX_end() - 1;
                   }
                    lastCellValue.setX_end(b);

                    if (lastCellValue.getX_start() != lastCellValue.getX_end()) {
                        Stringexcel.add(lastCellValue);
                    }

                    lastCellValue = new Stringexcel();
                    lastCellValue.setY_start(startY+i);
                    lastCellValue.setY_end(startY+i);
                    lastCellValue.setX_start(startX+k);
                    lastCellValue.setX_end(startX+k);
                    lastCellValue.setStrData(dataList.get(i).get(k));

                }

            }
        }

        //纵向合并
        for (int k = 0; k < dataList.get(0).size(); k++) {
        for (int i = 0; i < dataList.size(); i++) {



                if (i == 0) {

                    lastCellValue.setY_start(startY+i);
                    lastCellValue.setY_end(startY+i);
                    lastCellValue.setX_start(startX+k);
                    lastCellValue.setX_end(startX+k);
                    lastCellValue.setStrData(dataList.get(i).get(k));


                }

                thisCellValue = new Stringexcel();

                        thisCellValue.setY_start(startY+i);
                    thisCellValue.setY_end(startY+i);
                    thisCellValue.setX_start(startX+k);
                    thisCellValue.setX_end(startX+k);

                thisCellValue.setStrData(dataList.get(i).get(k));

                if (!thisCellValue.getStrData().equals(lastCellValue.getStrData())|| i ==dataList.size()-1) {

                    int b;
                    if ( i== dataList.size()-1 && dataList.get(i).get(k).equals(dataList.get(i-1).get(k))) {
                       b = thisCellValue.getY_end();
                   } else {
                        b = thisCellValue.getY_end() - 1;
                   }
                    lastCellValue.setY_end(b);

                    if (lastCellValue.getY_start() != lastCellValue.getY_end()) {
                        Stringexcel.add(lastCellValue);
                    }

                    lastCellValue = new Stringexcel();
                   lastCellValue.setY_start(startY+i);
                    lastCellValue.setY_end(startY+i);
                    lastCellValue.setX_start(startX+k);
                    lastCellValue.setX_end(startX+k);
                    lastCellValue.setStrData(dataList.get(i).get(k));

                }

            }
        }

        for (int k = 0; k < Stringexcel.size(); k++) {

            sheet.addMergedRegion(new CellRangeAddress(Stringexcel.get(k).getY_start(), Stringexcel.get(k).getY_end(), Stringexcel.get(k).getX_start(), Stringexcel.get(k).getX_end()));

        }

        FileOutputStream outputStream = new FileOutputStream("E://22222.xlsx");
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();

    }
}

