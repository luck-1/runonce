package com.runonce.util.constant;

import com.runonce.util.EXUtil;
import com.runonce.util.OssClientUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author LiXingcheng
 */
public class TestEmbed {

    public static void main(String[] args) throws Exception {

        List list = Arrays.asList(new String[]{"1", "2", "3", "4", "5"});
        list.forEach(System.out::println);
//        System.out.println(String.join(" ", list));

//        String str = list.stream().filter((a) -> System.out.println(a));
//        for (int i = 0; i < list.size(); i++) {
//
//        }


//        XSSFWorkbook workbook = new XSSFWorkbook("C:\\Users\\000000\\Desktop\\temp\\1.xlsx");
//        byte[] bytes = OssClientUtil.inputStreamToByte(new FileInputStream("C:\\Users\\000000\\Desktop\\temp\\填写说明\\1.png"));
//        XSSFSheet sheet = workbook.getSheetAt(0);
//        EXUtil.sheetAddPic(sheet,bytes,"title",7,7,0,0);
//        EXUtil.output(workbook);
    }

}