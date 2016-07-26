package org.im.imclient;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.im.imserver.config.PackageType;
import org.im.imserver.model.Header;
import org.im.imserver.model.Body;
import org.im.imserver.model.User;
import org.im.imserver.util.JsonUtil;

/**
 * 程序入口类
 * @author lglyoung 2016.07.24
 * @version 1.0.0
 */
public class ImClientMain {
	public static void main(String[] args) throws Exception {
		ImClient imClient = ImClient.getInstance();
		imClient.start("127.0.0.1", 9000);
		SocketChannel sc = imClient.getSc();
		
		//要发送给服务器的报文
		User user = new User();
		user.set("10001", "lglyoung", "yy0725");
		Body text = new Body();
		text.setUser(user);
		text.setContent("nothing...");
		String textJson = JsonUtil.toJson(text);

		//要发送的头
		Header header = new Header(textJson.getBytes().length, PackageType.TEXT);
		String headerJson = JsonUtil.toJson(header);
		
		//发送
		ByteBuffer buffer = ByteBuffer.allocate(4+headerJson.getBytes().length+textJson.getBytes().length);
		buffer.putInt(headerJson.getBytes().length);
		buffer.put(headerJson.getBytes());
		buffer.put(textJson.getBytes());
		
		//为读数据做好准备
		buffer.flip();
		while(buffer.hasRemaining()) {
			sc.write(buffer);
		}
		
		buffer.flip();
		while(buffer.hasRemaining()) {
			sc.write(buffer);
		}
		
		//释放资源
		ImClient.getInstance().stop();
	}
}
