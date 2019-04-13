package com.runonce.util;

import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.runonce.model.system.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.runonce.model.common.WebToken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


/**
 * @author yf
 * jwt
 * 依赖 /webtoken.properties 配置文件 信息生成token值
 * @version 1.0
 * @date 创建时间：2017年12月2日 上午10:45:57
 */
public class WebTokenUtil {
    private static Logger log = LoggerFactory.getLogger(WebTokenUtil.class);
    private static WebToken webToken = new WebToken();

    static {
        try {
            Resource resource = new ClassPathResource("/webtoken.properties");
            Properties Props = PropertiesLoaderUtils.loadProperties(resource);
            webToken.setExpiresSecond(Props.getProperty("expiresSecond") == null ? 6379
                    : Integer.parseInt(Props.getProperty("expiresSecond")));
            webToken.setName(Props.getProperty("name"));
            webToken.setSalt(Props.getProperty("salt"));
        } catch (IOException e) {
            log.error("获取 WebTokenProps : " + e.getMessage(), e);
        }
    }

    /**
     * @param key
     * @param obj
     * @return
     * @desc 生成一个token值
     */
    public static String getWebToken(String key, Object obj) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        // 生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(webToken.getSalt());
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
        // 添加构成JWT的参数

        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT").claim(key, obj).setIssuer(webToken.getName())
                .signWith(SignatureAlgorithm.HS256, signingKey);
        // 添加Token过期时间
        long TTLMillis = (long)webToken.getExpiresSecond() * 24 * 60 * 60 * 1000;
        if (TTLMillis >= 0) {
            long expMillis = nowMillis + TTLMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp).setNotBefore(now);
        }
        // 生成JWT
        return builder.compact();
    }


    public static String getUserByWebToken(HttpServletRequest request) {
        try {
            String jsonWebToken = request.getHeader("x-access-token");

            Object object = parseWebToken(jsonWebToken, "user");
            if (object == null) {
                return null;
            }
            String s = object.toString();
            return s;
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * @param jsonWebToken
     * @param key
     * @return
     * @desc 解析token值
     */
    public static Object parseWebToken(String jsonWebToken, String key) {
        try {
            Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(webToken.getSalt()))
                    .parseClaimsJws(jsonWebToken).getBody();

            Object map = claims.get(key);
            return claims.get(key);
        } catch (Exception ex) {
            log.error("解析token值：" + ex.getMessage());
            return null;
        }
    }

}
