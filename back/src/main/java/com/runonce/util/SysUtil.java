package com.runonce.util;

import com.runonce.model.common.WebToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.math.BigDecimal;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 *
 * @author hufx
 * @version 1.0
 * @date 2018年1月22日下午8:02:18
 */
@Component
public class SysUtil {
    @SuppressWarnings("unused")
    private static final int defaultLogTime = 7;// 设置默认日志缓存时间为7天
    public static SysUtil faultUtil;
    private static Logger log = LoggerFactory.getLogger(SysUtil.class);
    private static WebToken webToken = new WebToken();
    //
    // @Autowired
    // private TDictionaryItemsMapper dictionaryItemsMapper;//数据字典值
    //
    // @Autowired
    // private TServiceLogMapper tServiceLogMapper;

    // @Reference(version="1.0.0",timeout=100000)
    // public LogService logService;//日志服务

    static {
        try {
            Resource resource = new ClassPathResource("/webtoken.properties");
            Properties Props = PropertiesLoaderUtils.loadProperties(resource);
            webToken.setExpiresSecond(Props.getProperty("expiresSecond") == null
                    ? 6379
                    : Integer.parseInt(Props.getProperty("expiresSecond")));
            webToken.setName(Props.getProperty("name"));
            webToken.setSalt(Props.getProperty("salt"));
        } catch (IOException e) {
            log.error("获取 WebTokenProps : " + e);
        }
    }

    // @Autowired
    // private TLogMapper logMapper;//日志
    //


    /**
     * 功能描述: 版本号符合规则验证
     *
     * @param: str:版本号
     * @return:
     * @auther: tjw
     * @date: 2018/10/10 15:57
     */
    public static Boolean Version(String str) {
        String version = "\\d+(.\\d+)*";
        Pattern pattern = Pattern.compile(version);
        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        // 字符串是否与正则表达式相匹配
        return matcher.matches();
    }

    /**
     * 比较版本号的大小,前者大则返回一个正数,后者大返回一个负数,相等则返回0 规则是按日期订的例如：2.10.15 项目启动第2年的8月15号
     *
     * @param version1
     * @param version2
     * @return
     */
    public static Integer compareVersion(String version1, String version2) {
        int diff = 0;
        try {
            if (version1 == null || version2 == null) {
                throw new Exception("compareVersion error:illegal params.");
            }
            String[] versionArray1 = version1.split("\\.");// 注意此处为正则匹配，不能用"."；
            for (int i = 0; i < versionArray1.length; i++) { // 如果位数只有一位则自动补零（防止出现一个是04，一个是5 直接以长度比较）
                if (versionArray1[i].length() == 1) {
                    versionArray1[i] = "0" + versionArray1[i];
                }
            }
            String[] versionArray2 = version2.split("\\.");
            for (int i = 0; i < versionArray2.length; i++) {// 如果位数只有一位则自动补零
                if (versionArray2[i].length() == 1) {
                    versionArray2[i] = "0" + versionArray2[i];
                }
            }
            int idx = 0;
            int minLength = Math.min(versionArray1.length, versionArray2.length);// 取最小长度值

            while (idx < minLength && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0// 先比较长度
                    && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {// 再比较字符
                ++idx;
            }
            // 如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
            diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
        } catch (Exception e) {
            return null;
        }
        return diff;
    }

    /**
     * 获取6位随机数
     */
    public static String getRandom() throws NoSuchAlgorithmException {
        int max = 999999;
        int min = 100000;
        Random random = SecureRandom.getInstanceStrong();
        ;
        int verifyCode = random.nextInt(max) % (max - min + 1) + min;
        return verifyCode + "";
    }

    /**
     * 根据开始时间和结束时间返回时间段内的时间集合
     *
     * @param beginDate
     * @param endDate
     * @return
     * @Title: getDatesBetweenTwoDate
     * @author tjw
     * @date 2018年3月15日 下午5:17:12
     */
    public static List<Date> getDatesBetweenTwoDate(Date beginDate, Date endDate) {

        List<Date> lDate = new ArrayList<Date>();
        lDate.add(beginDate);// 把开始时间加入集合
        Calendar cal = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        cal.setTime(beginDate);
        boolean bContinue = true;
        while (bContinue) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.DAY_OF_MONTH, 1);

            if (isSameDate(cal.getTime(), endDate)) {
                break;
            }
            // 测试此日期是否在指定日期之后
            if (endDate.after(cal.getTime())) {
                lDate.add(cal.getTime());
            } else {
                break;
            }
        }
        if (!isSameDate(beginDate, endDate)) {
            lDate.add(endDate);// 把结束时间加入集合
        }

        return lDate;
    }

    /**
     * 判断日期是否是同一天
     *
     * @param date1
     * @param date2
     * @return boolean
     * @throws @Title: isSameDate
     * @author tjw
     * @date 2018年3月29日 上午9:32:16
     */
    public static boolean isSameDate(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
        boolean isSameMonth = isSameYear && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        boolean isSameDate = isSameMonth && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);

        return isSameDate;
    }

    /***
     * @desc MD5加密
     * @param inStr
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String getMD5(String inStr) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    /**
     * 生成UUID
     *
     * @return
     * @author tjw
     * @date 2018年3月24日 下午6:17:49
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 日期格式化
     *
     * @return
     * @author tjw
     * @date 2018年6月2日 下午4:57:12
     */
    public static String DateFormat(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String tim = sdf.format(date);
        return tim;
    }

    /**
     * 日期比较 1:date1 > date2 -1:date1<date2 0:date1 = date2
     *
     * @param DATE1
     * @param DATE2
     * @return
     * @throws ParseException
     * @author tjw
     * @date 2018年6月12日 下午4:47:32
     */
    public static int compare_date(String DATE1, String DATE2) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt1 = df.parse(DATE1);
        Date dt2 = df.parse(DATE2);
        if (dt1.getTime() > dt2.getTime()) {
            return 1;
        } else if (dt1.getTime() < dt2.getTime()) {
            return -1;
        }
        return 0;
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
        long TTLMillis = (long) webToken.getExpiresSecond() * 24 * 60 * 60 * 1000;
        if (TTLMillis >= 0) {
            long expMillis = nowMillis + TTLMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp).setNotBefore(now);
        }
        // 生成JWT
        return builder.compact();
    }

    /**
     * @param jsonWebToken
     * @param key
     * @return
     * @desc 解析token值
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> parseWebToken(String jsonWebToken, String key) {
        try {
            Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(webToken.getSalt()))
                    .parseClaimsJws(jsonWebToken).getBody();

            Map<String, Object> res = (Map<String, Object>) claims.get(key);
            return res;
        } catch (Exception ex) {
            log.error("解析token值：" + ex);
        }
        return null;
    }

    /**
     * 获取webtoken的失效时间
     *
     * @param jsonWebToken
     * @param key
     * @return
     * @author tjw
     * @date 2018年7月10日 上午11:52:52
     */
    public static Long getWebTokenTime(String jsonWebToken, String key) {
        try {
            Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(webToken.getSalt()))
                    .parseClaimsJws(jsonWebToken).getBody();
            return claims.getExpiration().getTime();
        } catch (Exception ex) {
            log.error("解析token值：" + ex);
        }
        return null;
    }

    /**
     * 上传文件
     *
     * @param url
     *            文件存放的路径: 如:/measurfile/tcertificatetype/ </br>
     *            即表示上传路径为：<br/>
     *            request.getSession().getServletContext().getRealPath("/")的父路径（getParent()）
     *            /measurfile/tcertificatetype/文件名
     * @param type
     *            1 ：word类文件</br>
     *            2:word excel pdf</br>
     *            3:图片jpg、png</br>
     *            4: </br>
     *            5:zip</br>
     *            6.xls、xlsx</br>
     * @param request
     * @return
     * @author tjw
     * @date 2018年7月5日 下午2:10:57
     */
//	public static Msg uploadFile(MultipartFile file, String url, HttpServletRequest request, Integer type) {
//		Msg msg = new Msg();
//		try {
//
//			String fileNameOld = file.getOriginalFilename();
//			String suffixName = fileNameOld.substring(fileNameOld.lastIndexOf("."));
//
//			fileNameOld = fileNameOld.substring(0, fileNameOld.lastIndexOf(".")) + SysUtil.getRandom() + suffixName;
//
//			// 获取文件的后缀名
//			switch (type) {
//				case 1 : {
//					if (!".doc".equalsIgnoreCase(suffixName) && !".docx".equalsIgnoreCase(suffixName)) {
//						msg.setSuccess(false);
//						msg.setMsg("上传文件类型错误");
//						return msg;
//					}
//					break;
//				}
//				case 2 : {
//					if (!".doc".equalsIgnoreCase(suffixName) && !".docx".equalsIgnoreCase(suffixName)
//							&& !".xls".equalsIgnoreCase(suffixName) && !".xlsx".equalsIgnoreCase(suffixName)
//							&& !".pdf".equalsIgnoreCase(suffixName)) {
//						msg.setSuccess(false);
//						msg.setMsg("上传文件类型错误");
//						return msg;
//					}
//					break;
//				}
//				case 3 : {
//					if (!".png".equalsIgnoreCase(suffixName) && !".jpg".equalsIgnoreCase(suffixName)
//							&& !".jpeg".equalsIgnoreCase(suffixName)) {
//						msg.setSuccess(false);
//						msg.setMsg("上传文件类型错误");
//						return msg;
//					}
//					break;
//				}
//				case 4 : {
//					if (!".doc".equalsIgnoreCase(suffixName) && !".docx".equalsIgnoreCase(suffixName)
//							&& !".xls".equalsIgnoreCase(suffixName) && !".xlsx".equalsIgnoreCase(suffixName)
//							&& !".pdf".equalsIgnoreCase(suffixName) && !".png".equalsIgnoreCase(suffixName)
//							&& !".jpg".equalsIgnoreCase(suffixName) && !".jpeg".equalsIgnoreCase(suffixName)) {
//						msg.setSuccess(false);
//						msg.setMsg("上传文件类型错误,只允许上传.doc .docx .xls .xlsx .pdf文档和.png .jpg .jpeg图片!");
//						return msg;
//					}
//					break;
//				}
//				case 5 : {
//					if (!".zip".equalsIgnoreCase(suffixName)) {
//						msg.setSuccess(false);
//						msg.setMsg("上传文件类型错误");
//						return msg;
//					}
//					break;
//				}
//				case 6 : {
//					if (!".xls".equalsIgnoreCase(suffixName) && !".xlsx".equalsIgnoreCase(suffixName)) {
//						msg.setSuccess(false);
//						msg.setMsg("上传文件类型错误,只能上传xls、xlsx!");
//						return msg;
//					}
//					break;
//				}
//				case 7 : {
//
//					break;
//				}
//                case 8 : { // 资源默认背景图 文件名固定bg.后缀名
//                    if (!".png".equalsIgnoreCase(suffixName) && !".jpg".equalsIgnoreCase(suffixName)
//                            && !".jpeg".equalsIgnoreCase(suffixName)) {
//                        msg.setSuccess(false);
//                        msg.setMsg("上传文件类型错误");
//                        return msg;
//                    }
//                    fileNameOld = "bg"+suffixName;
//                    break;
//                }
//				default : {
//
//				}
//			}
//			url = url + "/";
//			File cat = new File(request.getSession().getServletContext().getRealPath("/"));
//			String Path = cat.getParent() + url;
//			File dir = new File(Path);
//			if (!dir.exists()) {
//				dir.mkdirs();
//			}
//			// 上传文件
//			byte[] bytes = file.getBytes();
//			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(Path + fileNameOld)));
//			stream.write(bytes);
//			stream.close();
//			msg.setObj(url + fileNameOld);
//		} catch (Exception e) {
//			msg.setSuccess(false);
//			msg.setMsg("上传文件异常!");
//			return msg;
//		}
//		return msg;
//	}

    /**
     * base64编码
     *
     * @param bstr
     * @return String
     * @author hufx
     * @date 2018年5月19日下午5:44:58
     */
    @SuppressWarnings("restriction")
    public static String getBase64(byte[] bstr) {
        return new sun.misc.BASE64Encoder().encode(bstr);
    }

    /**
     * base64解码
     *
     * @param str
     * @return
     * @throws IOException byte[]
     * @author hufx
     * @date 2018年5月19日下午5:45:08
     */
    @SuppressWarnings("restriction")
    public static byte[] parseBase64(String str) throws IOException {
        byte[] bt = null;
        sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
        bt = decoder.decodeBuffer(str);
        return bt;
    }

    /**
     * java对象转json对象 <br/>
     * 此方法返回数据中的日期将全部以日期字符串表示 再次使用时使用Date即可接收 <br/>
     * 此类将处理以下问题：<br/>
     * 将java对象使用JSONObject.fromObject(Object)进行转换并且使用JSONObject.toString()返回字符串时,<br/>
     * 会默认将java.util.Date日期转换成下列结果： <br/>
     * "birthday": { "date": 3, "day": 4, "hours": 9, "minutes": 5, "month": 11,
     * "seconds": 1, "time": 1449104701018, "timezoneOffset": -480, "year": 115 },
     * <br/>
     * 为了方便JSON数据的直接使用，因此需要进行配置转换。返回结果如下： <br/>
     * Sat Feb 10 15:42:49 CST 2018
     *
     * @param obj
     *            要转换的对象
     * @return String
     * @author hufx
     * @date 2018年2月10日下午3:31:01
     */
//	public static String getJSONAndFormatJSONDate(Object obj) {
//		if (obj == null)
//			return null;
//		JsonConfig jsonConfig = new JsonConfig();
//		JsonDateValueProcessor jsonValueProcessor = new JsonDateValueProcessor();
//		jsonConfig.registerJsonValueProcessor(Date.class, jsonValueProcessor);
//		return JSONObject.fromObject(obj, jsonConfig).toString();
//	}

    /**
     * 发送http GET请求
     *
     * @param url
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     *             String
     * @author hufx
     * @date 2018年5月16日下午1:35:57
     */
    // @SuppressWarnings({ "resource" })
    // public static String doGET(String url) throws ClientProtocolException,
    // IOException {
    // HttpClient httpClient = new DefaultHttpClient();
    // HttpGet HttpGet = new HttpGet(url);
    // HttpResponse httpResponse = httpClient.execute(HttpGet);
    // if (httpResponse != null) {
    // HttpEntity httpEntity = httpResponse.getEntity();
    // String response = EntityUtils.toString(httpEntity);
    // return response;
    // }
    // return null;
    // }

    /**
     * 发送http POST请求
     *
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     *             String
     * @author hufx
     * @date 2018年5月16日上午10:46:55
     */
    // @SuppressWarnings({ "resource" })
    // public static String doPOST(String url, Map<String, String> params)
    // throws ClientProtocolException, IOException {
    // if (StringUtils.isBlank(url)) {
    // return null;
    // }
    // if (params == null || params.size() < 1) {
    // return null;
    // }
    // HttpClient httpClient = new DefaultHttpClient();
    // HttpPost httpPost = new HttpPost(url);
    // List<NameValuePair> _thisparams = new ArrayList<NameValuePair>();
    // Iterator<Entry<String, String>> it = params.entrySet().iterator();
    // while (it.hasNext()) {
    // Entry<String, String> ent = it.next();
    // _thisparams.add(new BasicNameValuePair(ent.getKey(), ent.getValue()));
    // }
    // log.info("_thisparams = "+_thisparams);
    // UrlEncodedFormEntity entity = null;
    // entity = new UrlEncodedFormEntity(_thisparams, "utf-8");
    // httpPost.setEntity(entity);
    // HttpResponse httpResponse = httpClient.execute(httpPost);
    // if (httpResponse != null) {
    // HttpEntity httpEntity = httpResponse.getEntity();
    // String response = EntityUtils.toString(httpEntity);
    // return response;
    // }
    // return null;
    // }

    /**
     * 发送http POST请求
     *
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     *             String
     * @author hufx
     * @date 2018年5月16日上午10:46:55
     */
    // @SuppressWarnings({ "resource" })
    // public static String doPOSTByJSONObject(String url, JSONObject params)
    // throws ClientProtocolException, IOException {
    // if (StringUtils.isBlank(url)) {
    // return null;
    // }
    // if (params == null || params.size() < 1) {
    // return null;
    // }
    // HttpClient httpClient = new DefaultHttpClient();
    // HttpPost httpPost = new HttpPost(url);
    // log.info("_thisparams = " + params);
    // StringEntity entity = new
    // StringEntity(params.toString(),"utf-8");//解决中文乱码问题
    // entity.setContentEncoding("UTF-8");
    // entity.setContentType("application/json");
    // httpPost.setEntity(entity);
    // HttpResponse httpResponse = httpClient.execute(httpPost);
    // if (httpResponse != null) {
    // HttpEntity httpEntity = httpResponse.getEntity();
    // String response = EntityUtils.toString(httpEntity);
    // return response;
    // }
    // return null;
    // }

    /**
     * java对象转json对象 <br/>
     * 此方法返回数据中的日期将全部以格式化后的字符串表示 再次使用时需使用String接收 <br/>
     * 格式化规则为pattern确定 <br/>
     * 此类将处理以下问题：<br/>
     * 将java对象使用JSONObject.fromObject(Object)进行转换并且使用JSONObject.toString()返回字符串时,<br/>
     * 会默认将java.util.Date日期转换成下列结果： <br/>
     * "birthday": { "date": 3, "day": 4, "hours": 9, "minutes": 5, "month": 11,
     * "seconds": 1, "time": 1449104701018, "timezoneOffset": -480, "year": 115 },
     * <br/>
     * 为了方便JSON数据的直接使用，因此需要进行配置转换。返回结果如下： <br/>
     * 例如 格式化规则为：yyyy-MM-dd 则结果为 ：2018-02-10
     *
     * @param obj
     *            要转换的对象
     * @param pattern
     *            日期格式化规则
     * @return String
     * @author hufx
     * @date 2018年2月10日下午3:44:17
     */
//	public static String getJSONAndFormatJSONDate(Object obj, String pattern) {
//		if (obj == null)
//			return null;
//		JsonConfig jsonConfig = new JsonConfig();
//		JsonDateValueProcessor jsonValueProcessor = new JsonDateValueProcessor(pattern);
//		jsonConfig.registerJsonValueProcessor(Date.class, jsonValueProcessor);
//		return JSONObject.fromObject(obj, jsonConfig).toString();
//	}

    /**
     * JOSN对象转java对象忽略大小写 通过JSONObject.toBean() 或 JSONArray.to...()
     *
     * @return JsonConfig
     * @author hufx
     * @date 2018年5月19日下午12:40:44
     */
//	public static <T> JsonConfig getJSONCastUpperAndLowerCase(final Class<T> clazz) {
//		JsonConfig config = new JsonConfig();
//		config.setJavaIdentifierTransformer(new JavaIdentifierTransformer() {
//			@Override
//			public String transformToJavaIdentifier(String attributeName) {
//				Field fields[] = clazz.getDeclaredFields(); // 拿到所有的属性
//				if (fields.length < 1) {
//					return attributeName;
//				}
//				for (int i = 0; i < fields.length; i++) { // 遍历对象的所有属性
//					String typename = fields[i].getGenericType().toString(); // 获取属性类型
//					if (typename != null && typename.length() > 0 && typename.startsWith("class")) {
//						typename = typename.substring(6); // 非基本类型的名称都会以class开头
//						// eg. class
//						// java.lang.String
//					}
//					if (SysUtil.isJavaInnerType(typename)) { // 当属性类型为自定义对象或数组时
//						String _attributeName = fields[i].getName(); // 拿到对象中的属性名称
//						String _attributeNameLower = _attributeName.toLowerCase().trim();// 拿到对象中属性名称的小写
//						String attributeNameLower = attributeName.toLowerCase().trim();// 拿到JSON中的属性名称小写
//						if (_attributeNameLower.equals(attributeNameLower)) { // 当对象中的属性名称与JSON中的属性名称相同时
//							attributeName = _attributeName; // 将JSON中的属性名称换成对象中的属性名称
//						}
//					} else { // 当属性类型不是自定义对象或数组时 替换首字母为小写
//						char[] chars = attributeName.toCharArray();
//						chars[0] = Character.toLowerCase(chars[0]);
//						attributeName = new String(chars);
//					}
//				}
//				return attributeName;
//			}
//		});
//		config.setRootClass(clazz);
//		return config;
//	}

    /**
     * 判断对象的属性类型是否是自定义对象或数组/集合
     *
     * @param type Field.getGenericType().getTypeName()获取的反射类型字符串
     * @return boolean 是 返回true 不是则返回false
     * @author hufx
     * @date 2018年5月19日下午4:48:44
     */
    public static boolean isJavaInnerType(String type) {
        List<String> typeList = new ArrayList<String>();
        typeList.add("byte");
        typeList.add("short");
        typeList.add("int");
        typeList.add("long");
        typeList.add("float");
        typeList.add("double");
        typeList.add("boolean");
        typeList.add("char");
        typeList.add("java.lang.Integer");
        typeList.add("java.lang.String");
        typeList.add("java.lang.Short");
        typeList.add("java.lang.Byte");
        typeList.add("java.lang.Character");
        typeList.add("java.lang.Double");
        typeList.add("java.lang.Float");
        typeList.add("java.lang.Boolean");
        typeList.add("java.math.BigInteger");
        typeList.add("java.math.BigDecmail");
        typeList.add("java.util.Date");
        typeList.add("java.sql.Date");
        typeList.add("java.lang.Object");
        typeList.add("java.lang.Number");
        for (String string : typeList) {
            if (!string.equals(type.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取当前登录的用户对象 HttpUserInfoRes
     *
     * @author hufx
     * @date 2018年8月15日上午11:53:35
     */
//	public static HttpUserInfoRes getUserByWebToken(HttpServletRequest request) {
//		try {
//			String jsonWebToken = request.getHeader("token");
//			if (jsonWebToken == null) {
//				return null;
//			}
//			Map<String, Object> tUserMap = (Map<String, Object>) parseWebToken(jsonWebToken, "tUser");
//			if (tUserMap == null) {
//				return null;
//			}
//			HttpUserInfoRes user = new HttpUserInfoRes();
//			user.setfId((String) tUserMap.get("fId"));
//            user.setfDeptId((String) tUserMap.get("fDeptId"));
//			user.setfAccount((String) tUserMap.get("fAccount"));
//			user.setfCreateTime(new Date((long) tUserMap.get("fCreateTime")));
//			user.setfLastLoginIp((String) tUserMap.get("fLastLoginIp"));
//			user.setfLastLoginTime(new Date((long) tUserMap.get("fLastLoginTime")));
//			user.setfUserName((String) tUserMap.get("fUserName"));
//			user.setfState((Integer) tUserMap.get("fState"));
//			return user;
//		} catch (Exception e) {
//			return null;
//		}
//	}

    /**
     * 数字转汉字
     *
     * @param param
     * @return
     * @author zhaolei
     * @time 2018年11月23日下午5:59:36
     */
    public static String numberToChinese(int param) {
        String[] s1 = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        String[] s2 = {"十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千"};

        String result = "";
        String str = String.valueOf(param);
        int n = str.length();
        for (int i = 0; i < n; i++) {

            int num = str.charAt(i) - '0';

            if (i != n - 1 && num != 0) {
                result += s1[num] + s2[n - 2 - i];
            } else {
                result += s1[num];
            }
        }
        return result;

    }

    /**
     * 添加日志 void
     *
     * @author hufx
     * @date 2018年8月11日下午2:26:49
     */
    // public static void setLog(TLog log){
    // faultUtil.logMapper.insert(log);
    // }

    /**
     * 清除指定时间前的日志
     *
     * void
     *
     * @author hufx
     * @date 2018年8月18日下午12:23:47
     */
    // public static void clearLog(){
    // int logtime = defaultLogTime;
    // TDictionaryItems logTimeObj =
    // faultUtil.dictionaryItemsMapper.selectByTypeIdAndPym("logtime",
    // "logtime");
    // if(logTimeObj != null){
    // logtime = Integer.parseInt(logTimeObj.getfItemValue());
    // }
    // List<String> clearList = faultUtil.logMapper.selectClearLogList(logtime);
    // if(clearList == null || clearList.size() < 1){
    // return;
    // }
    // faultUtil.logMapper.deleteLogList(clearList);
    // }

    /**
     * 添加流转日志
     *
     * @param req
     * @param request
     * @return int
     * @author hufx
     * @date 2018年12月14日下午2:45:38
     */
    // public static int setServiceLog(HttpInsertServiceLogReq
    // req,HttpServletRequest request){
    // if(req == null) return -1;
    // HttpUserInfoRes user = FaultUtil.getUserByWebToken(request);
    // if(user == null) return -1;
    // if(StringUtils.isBlank(req.getfFaultId()) ||
    // StringUtils.isBlank(req.getfCurrentOperate()) ||
    // StringUtils.isBlank(req.getfNextOperate()) ||
    // req.getfPreviousState() == null ||
    // req.getfAfterState() == null ||
    // req.getfSysExecState() == null ||
    // req.getfServiceState() == null ||
    // req.getfIsApprove() == null ||
    // req.getfApproveType() == null||
    // req.getfApproveServiceId() == null){
    // return -1;
    // }
    // TServiceLog serviceLog = new TServiceLog();
    // serviceLog.setfId(FaultUtil.getUUID());
    // serviceLog.setfFaultId(req.getfFaultId());
    // serviceLog.setfCurrentOperate(req.getfCurrentOperate());
    // serviceLog.setfNextOperate(req.getfNextOperate());
    // serviceLog.setfPreviousState(req.getfPreviousState());
    // serviceLog.setfAfterState(req.getfAfterState());
    // serviceLog.setfSysExecState(req.getfSysExecState());
    // serviceLog.setfServiceState(req.getfServiceState());
    // serviceLog.setfOperateUserId(user.getfEmployeeId());
    // serviceLog.setfOperateTime(new Date());
    // serviceLog.setfIsApprove(req.getfIsApprove());
    // serviceLog.setfApproveType(req.getfApproveType());
    // serviceLog.setfApproveServiceId(req.getfApproveServiceId());
    // return faultUtil.tServiceLogMapper.insert(serviceLog);
    // }

    /**
     * 计算给定两个日期相差小时数
     *
     * @return
     * @author zhaolei
     * @time 2018年1月22日下午2:07:39
     */
    public static Double getHours(Date beginTime, Date endTime) {
        Double hours = null;
        if (beginTime == null || endTime == null) {
            return null;
        }
        try {
            Long time = (endTime.getTime() - beginTime.getTime()) / (1000 * 60 * 60);
            BigDecimal bg = BigDecimal.valueOf(time.doubleValue());
            hours = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        } catch (Exception e) {
            return null;
        }
        return hours;
    }

    /**
     * 获取当前登录用户的角色列表 List<String>
     *
     * @author hufx
     * @date 2018年8月15日下午3:23:00
     */
    // @SuppressWarnings({ "static-access", "unchecked" })
    // public static List<String> getCurrentRoleList(HttpServletRequest request)
    // {
    // HttpUserInfoRes user = getUserByWebToken(request);
    // if (user == null) {
    // return null;
    // }
    // List<String> list = null;
    // try {
    // String roleIdListStr = RedisUtil.redisGet(user.getfId());
    // JSONArray jsonArray = new JSONArray();
    // System.out.println(roleIdListStr);
    // jsonArray = jsonArray.fromObject(roleIdListStr);
    // list = jsonArray.toList(jsonArray);
    // } catch (Exception e) {
    // log.error("获取当前登录用户的角色列表异常 : " + e);
    // }
    // if (list == null || list.size() < 1) {
    // list =
    // faultUtil.userService.selectRoleIdByUserId(user.getfId());
    // }
    // return list;
    // }

    /**
     * 复制单个文件
     * <p>
     * <br/>
     * 参数为文件全路径包含文件全名
     *
     * @param from String 原文件路径 如：c:/fqf.txt
     * @param to   String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    @SuppressWarnings("unused")
    public static boolean fileCopy(String from, String to) throws IOException {

        InputStream inStream = null;
        FileOutputStream fs = null;
        try {
            if (from == null || to == null) {
                return false;
            }
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(from);
            if (oldfile.exists()) { // 文件存在时
                inStream = new FileInputStream(oldfile); // 读入原文件
                File file = new File(to);
                File fileParent = file.getParentFile();
                if (!fileParent.exists()) {
                    fileParent.mkdirs();
                }
                file.createNewFile();
                fs = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.close();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        } finally {

            if (inStream != null) {
                inStream.close();
            }
            if (fs != null) {
                fs.close();
            }

        }
    }

    /**
     * 复制单个文件 <br/>
     * 参数为文件全路径包含文件全名
     *
     * @param from String 原文件路径 如：c:/fqf.txt
     * @param to   String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    @SuppressWarnings("unused")
    public static boolean fileCopy(File from, File to) throws IOException {
        InputStream inStream = null;
        FileOutputStream fs = null;
        try {
            if (from == null || to == null) {
                return false;
            }
            int bytesum = 0;
            int byteread = 0;
            File oldfile = from;
            if (oldfile.exists()) { // 文件存在时
                inStream = new FileInputStream(oldfile); // 读入原文件
                File file = to;
                File fileParent = file.getParentFile();
                if (!fileParent.exists()) {
                    fileParent.mkdirs();
                }
                file.createNewFile();
                fs = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        } finally {
            if (inStream != null) {
                inStream.close();
            }
            if (fs != null) {
                fs.close();
            }
        }
    }

    /**
     * 向输出流中写文件， 即 读取指定文件并写入到流向的路径
     *
     * @param file 文件
     * @param out  输出流
     * @return boolean
     * @author hufx
     * @date 2018年2月9日下午2:48:08
     */
    @SuppressWarnings("unused")
    public static boolean fileOut(File file, OutputStream out) throws IOException {
        InputStream inStream = null;
        FileOutputStream fs = null;
        try {
            if (file == null || out == null) {
                return false;
            }
            int bytesum = 0;
            int byteread = 0;
            File oldfile = file;
            if (oldfile.exists()) { // 文件存在时
                inStream = new FileInputStream(oldfile); // 读入原文件
                byte[] buffer = new byte[1024];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小
                    out.write(buffer, 0, byteread);
                }
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        } finally {
            if (inStream != null) {
                inStream.close();
            }
            if (out !=null){

                out.close();
            }
        }
    }

    /**
     * 递归删除文件目录
     *
     * @param dir
     * @return boolean
     * @author hufx
     * @date 2018年2月9日下午4:33:19
     */
    public static boolean fileDel(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children == null || children.length < 1) {
                return dir.delete();
            }
            // 递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = fileDel(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * MultipartFile 转换成File
     *
     * @param multfile 原文件类型
     * @return File
     * @throws IOException
     */
    public static File multipartToFile(String path, MultipartFile multfile) throws IOException {
        String suffix = multfile.getOriginalFilename().substring(multfile.getOriginalFilename().lastIndexOf("."));
        File dest = new File(path + "/" + UUID.randomUUID().toString().replaceAll("-", "") + suffix);
        if (!dest.exists()) {
            dest.getParentFile().mkdirs();
            dest.createNewFile();
        }

        multfile.transferTo(dest);
        log.info("生成文件 --> " + dest.getAbsolutePath());
        // CommonsMultipartFile cf = (CommonsMultipartFile) multfile;
        // //这个myfile是MultipartFile的
        // DiskFileItem fi = (DiskFileItem) cf.getFileItem();
        // File file = fi.getStoreLocation();
        // // //手动创建临时文件
        // if (file.length() < /*CommonConstants.MIN_FILE_SIZE*/ 2048) {
        // File tmpFile = new File(System.getProperty("java.io.tmpdir") +
        // System.getProperty("file.separator") +
        // file.getName());
        // multfile.transferTo(tmpFile);
        // return tmpFile;
        // }
        return dest;
    }

    /**
     * 递归删除
     */
    public static boolean recursion(File r) {
        try {
            log.info("文件源路径 --> " + r.getAbsolutePath());
            if (r.isDirectory()) {
                for (File rc : r.listFiles()) {
                    recursion(rc);
                }
            } else {
                log.info("删除文件 --> " + r.getAbsolutePath());
                return r.delete();
            }
        } catch (Exception e) {
            System.out.println("异常信息 --> " + e);
        }
        return r.delete();
    }

    /**
     * 使用dos命令强力删除目录 强力删除文件夹,里面就算有子文件夹,隐藏的,只读的,都能够全部删除掉. directory 需要删除的目录 return
     * 如果目录不存在,则返回"目录不存在";删除成功,返回ok;删除失败 ,返回失败原因
     */
    public static String forceDeleteDirectory(String directory) {
        File tagFile = new File(directory);
        if (tagFile.exists()) {
            try {
                String cmd = "cmd /c rd " + directory + " /s/q";
                Runtime rt = Runtime.getRuntime(); // 获取运行时系统
                rt.exec(cmd); // 执行命令
            } catch (Throwable t) {
                return t.getMessage();
            }
            return "ok";
        } else {
            return "目录不存在";
        }
    }

    /**
     * 功能描述:是否是英文
     *
     * @param:
     * @return:
     * @auther: tjw
     * @date: 2018/10/10 15:46
     */
    public static boolean isEnglish(String charaString) {

        return charaString.matches("^[a-zA-Z]*");

    }

    /**
     * 功能描述: 是否是中文
     *
     * @param:
     * @return:
     * @auther: tjw
     * @date: 2018/10/10 15:46
     */
    public static boolean isChinese(String str) {

        String regEx = "[\\u4e00-\\u9fa5]+";

        Pattern p = Pattern.compile(regEx);

        Matcher m = p.matcher(str);

        if (m.find())

            return true;

        else

            return false;

    }

//	@PostConstruct
//	public void init() {
//		faultUtil = this;
//		faultUtil.userService = this.userService;
//		// faultUtil.logService = this.logService;
//		// faultUtil.logMapper = this.logMapper;
//		// faultUtil.userRoleMapper = this.userRoleMapper;
//		// faultUtil.dictionaryItemsMapper = this.dictionaryItemsMapper;
//		// faultUtil.tServiceLogMapper = this.tServiceLogMapper;
//	}

    /**
     * java对象转json时Date日期的处理配置类 <br/>
     * 此类用于处理以下问题：<br/>
     * 将java对象使用JSONObject.fromObject(Object)进行转换并且使用JSONObject.toString()返回字符串时,<br/>
     * 会默认将java.util.Date日期转换成下列结果： <br/>
     * "birthday": { "date": 3, "day": 4, "hours": 9, "minutes": 5, "month": 11,
     * "seconds": 1, "time": 1449104701018, "timezoneOffset": -480, "year": 115 },
     * <br/>
     * 为了方便JSON数据的直接使用，因此需要进行配置转换
     *
     * @author hufx
     * @version 1.0
     * @date 2018年2月10日下午3:01:55
     */
//	private static class JsonDateValueProcessor implements JsonValueProcessor {
//		// 日期格式化规则
//		private String pattern = null;
//
//		/**
//		 * 使用默认构造方法处理时，将返回原来java.util.Date 的toString()方法结果
//		 */
//		public JsonDateValueProcessor() {
//
//		}
//
//		/**
//		 * 使用此构造方法处理时，将根据指定的格式返回java.util.Date format后的字符串结果
//		 *
//		 * @param pattern
//		 */
//		public JsonDateValueProcessor(String pattern) {
//			this.pattern = pattern;
//		}
//
////		public Object processArrayValue(Object value, JsonConfig config) {
////			return process(value);
////		}
////
////		public Object processObjectValue(String key, Object value, JsonConfig config) {
////			return process(value);
////		}
//
//		private Object process(Object value) {
//			if (value instanceof Date) {
//				// 如果日期格式化规则不可用,则返回java.util.Date().toString()
//				if (StringUtils.isBlank(pattern)) {
//					return value == null ? "" : value.toString();
//				} else {// 如果日期格式化规则可用，则返回格式化后的日期结果
//					SimpleDateFormat sdf = new SimpleDateFormat(pattern);
//					return sdf.format(value);
//				}
//			}
//			return value == null ? "" : value.toString();
//		}
//
//	}

}
