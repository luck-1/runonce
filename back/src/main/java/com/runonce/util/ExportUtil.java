package com.runonce.util;

import io.swagger.annotations.ApiModelProperty;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ExportUtil {

    //map -> k,v反转
    public static Map<String,String> recover(Map<String,Object> map){
        Iterator iterator = map.keySet().iterator();
        Map<String,String> map1 =new LinkedHashMap<>();
        while(iterator.hasNext()){
            String k = (String) iterator.next();
            String v = (String) map.get(k);
            map1.put(v,k);
        }
        return map1;
    }

    //获取对象所有属性和值
    public static Map<String, Object> getKeyAndValue(Object obj) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        Class userCla = (Class) obj.getClass();
        Field[] fs = userCla.getDeclaredFields();
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            f.setAccessible(true);
            Object val = new Object();
            try {
                val = f.get(obj);
                map.put(f.getName(), val);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
    //获取属性和api注解
    public static Map<String,Object> getColumnValue(Class classZ) {
        Field[] fields = classZ.getDeclaredFields();
        Field field;
        Object val;
        Map<String,Object> map = new LinkedHashMap<>();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
        }
        for (int i = 1; i < fields.length; i++) {
            try {
                System.out.println(fields[i].getName());
                field = classZ.getDeclaredField(fields[i].getName());
                ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);

                if(apiModelProperty==null) continue;
                val = apiModelProperty.value();

                map.put(field.getName(),val);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return map;
    }




    //获取对象子类及父类所有api属性和值
    public static Map<String, Object> getApiKeyAndValue(Object obj) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        Class userCla = obj.getClass();

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
                Object val = f.get(obj);
                map.put(apiModelProperty.value(), val);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
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
                Object val = f.get(obj);
                map.put(apiModelProperty.value(), val);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }


        return map;
    }


    public static Map<String,Object> getApiAllKV(Object object1,Object object2){

        Map<String,Object> map1 = getApiKeyAndValue(object1);
        Map<String,Object> map2 = getApiKeyAndValue(object2);

        Iterator iterator = map2.keySet().iterator();
        for(int i=0;i<map2.size();i++){
            String key = iterator.next().toString();
            Object value = map2.get(key);
            if(map1.containsKey(key)){
                map1.remove(key);
            }
            map1.put(key,value);
        }
        return map1;
    }


    /**
     * 按比例对图片进行缩放.
     *
     * @param _width
     * @param _height
     * @param bufferedImage
     * @throws IOException
     */
    public static ByteArrayOutputStream zoomByScale(int _width, int _height, BufferedImage bufferedImage) throws IOException {


        //获取缩放后的Image对象
        Image _img = bufferedImage.getScaledInstance(_width, _height, Image.SCALE_SMOOTH);
        //新建一个和Image对象相同大小的画布
        BufferedImage image = new BufferedImage(_width, _height, BufferedImage.TYPE_INT_RGB);
        //获取画笔
        Graphics2D graphics = image.createGraphics();

        //bufferImage.getGraphics().drawImage(image,0,0,width,height,null);//设置底色
      //  graphics.setBackground(new Color( 255, 255, 255) );
        //将Image对象画在画布上,最后一个参数,ImageObserver:接收有关 Image 信息通知的异步更新接口,没用到直接传空
        graphics.drawImage(_img, 0, 0, image.getGraphics().getColor(),null);
        //释放资源
        graphics.dispose();
        //使用ImageIO的方法进行输出

        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        ImageIO.write(image, "JPEG", byteArrayOut);

        return byteArrayOut;
    }

}
