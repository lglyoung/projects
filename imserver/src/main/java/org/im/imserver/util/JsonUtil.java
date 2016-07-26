package org.im.imserver.util;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * json解析工具类
 * @author lglyoung 2016.07.24
 */
public class JsonUtil {
	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	/**
	 * 将java bean对象转成json对象
	 * @param obj
	 * @return
	 */
	public static <T> String toJson(T obj) {
		String json = null;
		try {
			json = OBJECT_MAPPER.writeValueAsString(obj);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
		return json;
	}
	
	/**
	 * 将json字符串转成pojo对象
	 * @return
	 */
	public static <T> T fromJson(String json, Class<T> type) {
		T pojo = null;
		try {
			pojo = OBJECT_MAPPER.readValue(json, type);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
		return pojo;
	}
}
