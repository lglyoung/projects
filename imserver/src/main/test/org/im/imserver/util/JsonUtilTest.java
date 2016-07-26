package org.im.imserver.util;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.im.util.JsonUtil;
import org.junit.Test;


public class JsonUtilTest {
	
	@Test
	public void toJsonTest() {
		System.out.println(JsonUtil.toJson(new User("lglyoung", "yy0725")));
	}
	
	@Test
	public void fromJsonTest() {
		String json = "{\"username\":\"lglyoung\",\"pwd\":\"yy0725\"}";
		User user = JsonUtil.fromJson(json, User.class);
		System.out.println(user);
	}
	
	@Test
	public void uft8() {
		String str = "world4喝";
		Charset charset = Charset.forName("utf-8");
		ByteBuffer buff = charset.encode(str);
		System.out.println(buff.remaining());
	}
	
	@Test
	public void throwRuntimeTest() {
		throw new RuntimeException("test");
	}
}
