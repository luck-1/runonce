package com.runonce.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import com.baidu.aip.ocr.AipOcr;
import com.runonce.model.test.ImageJsonBean;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhaolei
 * @Descriptions: oss客户端util
 * @Date: create at 2018/12/20 0020 下午 12:29
 */
public class  OssClientUtil {

    @Resource
    private static Environment environment;

    private static String endpoint;

    private static String accessKeyId;

    private static String accessKeySecret;

    private static String bucketName;

    private static OSSClient ossClient;

    static {
        endpoint = "https://oss-cn-beijing.aliyuncs.com";
        accessKeyId = "LTAI8A4BIWZOWIqv";
        accessKeySecret = "adKFZKEOG1ZKrKVcqd6EV3hnS3he1J";
        bucketName = "ikingrun";
    }


    /* *
     * 获取oss对象
     *
     * @return
     */
    public static OSSClient getOssClient() {
        return new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }


    /**
     * @return
     */
    public static String getBucketName() {
        return bucketName;
    }


    /**
     * 获取图片
     *
     * @param objectName
     * @return
     * @throws Exception
     */
    public static ByteArrayOutputStream getObjectData(String objectName) throws Exception {

        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();

        OSSClient ossClient = getOssClient();
        //oss对象存储加载图片
        boolean bool = ossClient.doesObjectExist(getBucketName(), objectName);
        if (!bool) {
            return null;
        }

        OSSObject ossObject = ossClient.getObject(getBucketName(), objectName);
        InputStream inputStream = ossObject.getObjectContent();
        inputStreamToByteArray(inputStream, byteArrayOut);
        //关闭
        ossClient.shutdown();
        return byteArrayOut;
    }


    /**
     * 获取图片
     *
     * @param objectName
     * @return
     * @throws Exception
     */
    public static ByteArrayOutputStream getObjectData(OSSClient ossClient, String objectName) throws Exception {

        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();

        //oss对象存储加载图片
        boolean bool = ossClient.doesObjectExist(getBucketName(), objectName);
        if (!bool) {
            return null;
        }

        OSSObject ossObject = ossClient.getObject(getBucketName(), objectName);
        InputStream inputStream = ossObject.getObjectContent();
        inputStreamToByteArray(inputStream, byteArrayOut);
        return byteArrayOut;
    }

    /**
     * inputStream 转 ByteArray
     *
     * @param ins
     * @param baoS
     * @throws Exception
     */
    public static void inputStreamToByteArray(InputStream ins, ByteArrayOutputStream baoS) throws Exception {
        byte[] buffer = new byte[1024];
        int len;
        byte[] dataBytes;
        while ((len = ins.read(buffer)) != -1) {
            baoS.write(buffer, 0, len);
        }

        ins.close();
        baoS.close();
    }
//
//    /**
//     * 获取对象简要信息
//     *
//     * @param objectName 对象名称
//     * @return
//     *//*
//    public static SimplifiedObjectMeta getObjectSimpleData(String objectName) {
//        return ossClient.getSimplifiedObjectMeta(bucketName, objectName);
//    }
//
//    *//**
//     * 获取对象全部信息
//     *
//     * @param objectName 对象名称
//     * @return
//     *//*
//    public static ObjectMetadata getObjectData(String objectName) {
//        return ossClient.getObjectMetadata(bucketName, objectName);
//    }


//    public static void main(String[] args) throws Exception {
//        System.out.println("===============================");
//        BufferedImage bufferImg = null;
//
//        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
//        //加载图片
//
//        String objectName = "2018-12-17/机上验证截图.jpg";
//        boolean bool = ossClient.doesObjectExist(bucketName, objectName);
//
//        if (!bool) {
//
//            System.out.println("文件不存在");
//        }
//        OSSObject ossObject = ossClient.getObject(bucketName, objectName);
//
//
//        File file = new File();
//        inputStreamToFile(ossObject.getObjectContent(), file);
//        bufferImg = ImageIO.read(file);
//        ImageIO.write(bufferImg, "jpg", byteArrayOut);
//        ossClient.shutdown();
//
//    }
//
//
//    public static void inputStreamToFile(InputStream ins, File file) throws Exception {
//        OutputStream os = new FileOutputStream(file);
//        int bytesRead = 0;
//        byte[] buffer = new byte[8192];
//        while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
//            os.write(buffer, 0, bytesRead);
//        }
//        os.close();
//        ins.close();
//    }


    public static void main(String[] args) throws Exception {

        BufferedImage bufferImg = null;

        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        //加载图片

        String objectName = "2018-12-17/机上验证截图.jpg";
        boolean bool = getOssClient().doesObjectExist(getBucketName(), objectName);

        if (!bool) {
            System.out.println("文件不存在");
        }
//        SimplifiedObjectMeta simplifiedObjectMeta =  ossClient.getSimplifiedObjectMeta(getBucketName(), objectName);
        String style = "image/resize,m_fixed,w_100,h_100";
        GetObjectRequest request = new GetObjectRequest(getBucketName(), objectName);
        request.setProcess(style);
        ObjectMetadata objectMetadata = getOssClient().getObject(request, new File("example-resize.jpg"));
        System.out.println(objectMetadata);
    }


    public static void inputStreamToByteArrayOutputStream(InputStream ins, ByteArrayOutputStream baoS) throws Exception {
        byte[] buffer = new byte[1024];
        int len;
        while ((len = ins.read(buffer)) != -1) {
            baoS.write(buffer, 0, len);
        }
        baoS.close();
        ins.close();
        baoS.toByteArray();
    }

    public static byte[] inputStreamToByte(InputStream is) {
        byte[] buffer = new byte[1024 * 5];
        int len;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        buffer = baos.toByteArray();
        while (true) {
            try {
                if (is != null) {
                    is.close();
                }
                if (baos != null) {
                    baos.close();
                }
                break;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return buffer;
    }

    /**
     * 通过inputStream方式向oss上传文件
     *
     * @param inputStream
     * @param type        图形种类（1.流程图 2.量化呈现图）
     */
    public static String uploadFileByInputStream(InputStream inputStream, String fileName, int type) {
        OSSClient ossClient = getOssClient();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(new Date());
        String filePath = "";
        String timeSteamp = String.valueOf(System.currentTimeMillis());
        filePath = dateString + "/" + timeSteamp + ".jpg" ;
//        if(type == 1){
////            filePath = "流程图/"+dateString+"/"+fileName+".png";
//            filePath = "process/"+dateString+"/"+timeSteamp+".png";
//
//        }else{
////            filePath = "量化呈现图/"+dateString+"/"+fileName+".png";
//            filePath = "quantitativeRendering/"+dateString+"/"+timeSteamp+".png";
//        }
        PutObjectResult result = ossClient.putObject(bucketName, filePath, inputStream);
        System.out.println(result.getETag());
        ossClient.shutdown();
        return filePath;
    }


    /**
     * Oss上传
     * @param inputStream
     * @param filePath
     * @return
     */
    public static String uploadFileByInputStream( OSSClient ossClient,InputStream inputStream, String filePath) {
        PutObjectResult result = ossClient.putObject(bucketName, filePath, inputStream);
        return filePath;
    }



    public static String copyImage(String oldKey) {

        List<String> imagesReturn = new ArrayList<>();
        ImageJsonBean imageJsonBeanReturn = new ImageJsonBean();
        if (oldKey != null && !"".equals(oldKey)) {
            ImageJsonBean imageJsonBean = null;
            try {
                imageJsonBean = JSON.parseObject(oldKey, new TypeReference<ImageJsonBean>() {
                });
            } catch (Exception e) {
                // imagesReturn.add(); ;
                // imageJsonBeanReturn.setImgs(imagesReturn);
                //e.printStackTrace();
                return copy(oldKey);
            }
            if (imageJsonBean != null && imageJsonBean.getImgs() != null && imageJsonBean.getImgs().size() > 0) {
                List<String> images = imageJsonBean.getImgs();

                for (String str :
                        images) {


                    imagesReturn.add(copy(str));

                }

                imageJsonBeanReturn.setImgs(imagesReturn);
            }


        }

        String str = JSONObject.toJSONString(imageJsonBeanReturn);//fastjson默认转换是不序列化null值对应的key的
        return str;

    }

    private static String copy(String str) {

        if(str!=null&&!"".equals(str)) {
            if(str.contains(".")) {
                String abc = str.substring(str.lastIndexOf("."), str.length());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = dateFormat.format(new Date());
                String filePath = "";
                String timeSteamp = String.valueOf(new Date().getTime());
                filePath = dateString + "/" + timeSteamp + abc;
                CopyObjectRequest copyObjectRequest = new CopyObjectRequest(bucketName, str, bucketName, filePath);
                OSSClient ossClient = getOssClient();
                ossClient.copyObject(copyObjectRequest);
                ossClient.shutdown();
                return filePath;
            }else{

                return "";
            }
        }
        else{

            return "";
        }
    }

    /**
     * OSS对象存储下载文件
     * @param ossClient
     * @param objectName
     * @return
     */
    public static byte[] getObjectForByte(OSSClient ossClient, String objectName) throws IOException {
        //文件夹名
        String bucketName = OssClientUtil.getBucketName();
        if (!ossClient.doesObjectExist(bucketName, objectName)) {
            return null;
        }
        //得到目标文件
        OSSObject ossObject = ossClient.getObject(bucketName, objectName);
        //得到输入流
        InputStream inputStream = ossObject.getObjectContent();
        //输入流转字节数组
        byte[] bytes = OssClientUtil.inputStreamToByte(inputStream);
        inputStream.close();
        return bytes;
    }

    public static InputStream getObjectForInputStream(OSSClient ossClient, String objectName) {
        //文件夹名
        String bucketName = OssClientUtil.getBucketName();
        if (!ossClient.doesObjectExist(bucketName, objectName)) {
            return null;
        }
        //得到目标文件
        OSSObject ossObject = ossClient.getObject(bucketName, objectName);
        //得到输入流
        return ossObject.getObjectContent();
    }
    //解析OSS图片对象得到文件名
    public static List<String> getFileName(String source) {
        Map<String, List<String>> map = JSON.parseObject(source, Map.class);
        if(!(map == null || map.size() == 0)){
            List<String> list = map.get("imgs");
            return list;
        }
        return null;
    }

    //百度文字识别客户端
    public static AipOcr getAipOcr() {
        String APP_ID = "15194026";
        String API_KEY = "cPrIGjfOxiErrXNBZpjh92f1";
        String SECRET_KEY = "kmtzs1oGwVD7rC90qPPYyxXSpc3rB7Kd";
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);//登录
        //可选参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        return client;
    }
}