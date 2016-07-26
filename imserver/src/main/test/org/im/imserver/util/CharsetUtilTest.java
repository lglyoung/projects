package org.im.imserver.util;

import java.nio.ByteBuffer;

import org.junit.Test;

public class CharsetUtilTest {
	@Test
	public void encodeTest() {
		String str = "和我来咯";
		ByteBuffer buff = CharsetUtil.encode(str);
		System.out.println(CharsetUtil.decode(buff));
	}
	
	@Test
	public void nullTest() {
		String str = null;
		ByteBuffer buff = CharsetUtil.encode(str);
		System.out.println(CharsetUtil.decode(buff));
	}
}
