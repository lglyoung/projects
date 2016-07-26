package org.im.imclient;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.im.config.PackageInfo;
import org.im.model.Package;
import org.im.model.User;
import org.im.util.JsonUtil;
import org.im.util.RWUtil;

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
		
		//要发的报文
		Package pack = new Package();
		pack.setType(PackageInfo.SEND_MSG_CMD);
		pack.setFrom(new User("10001", "lglyoung", "yy0725"));
		pack.setTo(new User("10002", "young", "yy0725"));
		
		RWUtil.writeToSc(sc, pack);
		
		//释放资源
		ImClient.getInstance().stop();
	}
}
