package com.runonce.util;

import io.swagger.annotations.ApiModelProperty;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author swq
 * @date 2019/2/14 0014
 * @description
 */
public class ImportUtil {

    //获取对象子类及父类所有api属性和值
    public static Map<String, Object> getNameEverseColumn(Class userCla,Map<String, Object>  map1) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        Field[] fs = userCla.getDeclaredFields();
        Class claSuperclass = userCla.getSuperclass();
        Field[] fields = claSuperclass.getDeclaredFields();

        for (int i = 0; i < fs.length; i++) {

            Field f = fs[i];
            fs[i].setAccessible(true);
            try {
                ApiModelProperty apiModelProperty = f.getAnnotation(ApiModelProperty.class);

                if (apiModelProperty == null){
                    continue;
                }
                Object value = map1.get(apiModelProperty.value());
                map.put(f.getName(), value);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        //父
        for (int i = 0; i < fields.length; i++) {

            Field f = fields[i];
            fields[i].setAccessible(true);
            try {
                ApiModelProperty apiModelProperty = f.getAnnotation(ApiModelProperty.class);

                if (apiModelProperty == null){
                    continue;
                }
                Object value = map1.get(apiModelProperty.value());
                map.put(f.getName(), value);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }


        return map;
    }

    public static String getValue(Cell cell) {
        Object obj = "";
        if(cell != null){
            switch (cell.getCellType()) {
                case BOOLEAN:
                    obj = cell.getBooleanCellValue();
                    break;
                case ERROR:
                    obj = cell.getErrorCellValue();
                    break;
                case NUMERIC:
                    obj = cell.getNumericCellValue();
                    break;
                case STRING:
                    obj = cell.getStringCellValue();
                    break;
                default:
                    break;
            }
        }
        return obj==null?"":obj.toString();
    }
}
