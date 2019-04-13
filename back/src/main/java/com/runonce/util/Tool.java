package com.runonce.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 
 * @version 创建：2017年5月9日 下午6:14:30 类说明
 */
public class Tool {
	/**
	 * MultipartFile 转 file
	 */
	public static File getFile(MultipartFile file) throws Exception {
		File f = null;
		f = File.createTempFile("tmp", null);
		System.out.println(f.getPath());
		file.transferTo(f);
		f.deleteOnExit();
		return f;
	}

	public static int getInt(int a, int b) {
		int c = 0;
		if (a % b != 0) {
			c = a / b + 1;
		} else {
			c = a / b;
		}
		return c;
	}
	
	/**
	 * 分页工具类
	 */
	public static List<Map<String, Object>> getPaging(List<Map<String, Object>> list, Integer start, Integer end) {
		List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
		for (int i = start; i < end; i++) {
			if (i + 1 > list.size()) {
				break;
			}
			list2.add(list.get(i));
		}
		return list2;
	}

	public static String md5Encode(String inStr) throws UnsupportedEncodingException {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}

		byte[] byteArray = inStr.getBytes("UTF-8");
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


//	public static void main(String[] args) {
//
//		List<Map<String,Integer>> mapList = new ArrayList<>();
//
//		Map map1 = new HashMap();
//		map1.put("sheetNum",1);
//		map1.put("state",1);
//
//		mapList.add(map1);
//		Map map2 = new HashMap();
//		map2.put("sheetNum",2);
//		map2.put("state",1);
//		mapList.add(map2);
//
//
//		List<Integer> stateList = mapList.stream().map(map -> {
//			Integer state = null;
//			if (map.containsKey("state")) {
//				state = map.get("state");
//			}
//			return state;
//		}).distinct().collect((Collectors.toList()));
//		System.out.println(stateList.toString());
//	}

}
