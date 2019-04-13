package com.runonce.util;

import com.runonce.model.system.User;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author swq
 * @date 2018/12/13 0013
 * @description 根据注解获取类的指定注解的属性值
 */
public class ColumnUtil {


//    public static void main(String[] args) {
//        String str = getColumnValue(User.class, "userName");
//        System.out.println(str);
//
//    }


    public static String getColumnValue(Class classZ, String columnName) {
        Field[] fields = classZ.getDeclaredFields();
        Field field;
        String value = null;
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
        }
        for (int i = 1; i < fields.length; i++) {
            try {
                System.out.println(fields[i].getName());
                if (fields[i].getName().equals(columnName)) {
                    field = classZ.getDeclaredField(fields[i].getName());
                    //获取指定类型注解
                    ApiModelProperty column = field.getAnnotation(ApiModelProperty.class);
                    value = column.value();
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

}
