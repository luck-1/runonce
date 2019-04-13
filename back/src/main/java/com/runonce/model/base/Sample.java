package com.runonce.model.base;

import com.baidu.aip.ocr.AipOcr;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class Sample {
    //设置APPID/AK/SK
    public static final String APP_ID = "15194026";
    public static final String API_KEY = "cPrIGjfOxiErrXNBZpjh92f1";
    public static final String SECRET_KEY = "kmtzs1oGwVD7rC90qPPYyxXSpc3rB7Kd";
    public static void main(String[] args) throws IOException {
        // 初始化一个AipOcr
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        // 调用接口
        String path = "D://123.jpg";
        JSONObject res = client.basicGeneral(path, new HashMap<String, String>());
        System.out.println(res.toString(2));
    }
}