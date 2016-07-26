package org.im.util;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * 字符集（UTF-8）工具类
 * @author lglyoung 2016.07.24
 */
public class CharsetUtil {
	private final static Charset CHARSET = Charset.forName("UTF-8");
	
	/**
	 * 编码：用utf-8字符集将字符串编码成字节
	 * @author lglyoung 2016.07.24
	 * @param str
	 * @return
	 */
	public static ByteBuffer encode(String str) {
		return CHARSET.encode(str);
	}
	
	/**
	 * 解码：用utf-8字符集将字符串编码成字节
	 * @author lglyoung 2016.07.24
	 * @param str
	 * @return
	 */
	public static String decode(ByteBuffer buffer) {
		return CHARSET.decode(buffer).toString();
	}
}
