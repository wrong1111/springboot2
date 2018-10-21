package com.yucei.admin.common.utils;

import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

/**
 * 图片具体转换，添加水印入口操作类
 * @author wangyong
 */
public class Base64ImageUtil {
	// 图片转化成base64字符串
	public static String GetImageStr(String path, String filename) throws Exception {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		if(StringUtils.isBlank(filename)){
			throw new Exception("images will be changed to base64 string. filename must not null !");
		}
		if(StringUtils.isBlank(path)){
			path = File.separator+"tmp";
		}
		String imgFile = path+ File.separator+filename;// 待处理的图片
		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);// 返回Base64编码过的字节数组字符串
	}

	// base64字符串转化成图片
	public static boolean GenerateImage(String imgStr,
			String path,
			String filename) { // 对字节数组字符串进行Base64解码并生成图片
		if (imgStr == null) { // 图像数据为空
			return false;
		}
		if (StringUtils.isBlank(filename)) {
			return false;
		}
		if (StringUtils.isBlank(path)) {
			path = File.separator+ "tmp";
		}
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			// Base64解码
			byte[] b = decoder.decodeBuffer(imgStr);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {// 调整异常数据
					b[i] += 256;
				}
			}
			// 生成jpeg图片
			String imgFilePath = path + File.separator + filename;// 新生成的图片
			OutputStream out = new FileOutputStream(imgFilePath);
			out.write(b);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
