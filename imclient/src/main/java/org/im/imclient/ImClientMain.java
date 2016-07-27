package org.im.imclient;

import java.nio.channels.SocketChannel;
import java.util.Scanner;

import org.im.commons.config.PackageInfo;
import org.im.commons.model.Pack;
import org.im.commons.model.User;
import org.im.commons.util.RWUtil;

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
		User fromUser = new User("10001", "lglyoung", "yy0725");
		User toUser = new User("10002", "young", "yy0725");
		
		//要发的报文
		Pack pack = new Pack();
		pack.setType(PackageInfo.SEND_MSG_CMD);
		pack.setFrom(fromUser);
		pack.setTo(toUser);
		
		Scanner scan = new Scanner(System.in);
		System.out.print(fromUser.getUserId()+" > ");
		while(scan.hasNext()) {
			System.out.print(fromUser.getUserId()+" > ");
			String readIn = scan.nextLine();
			if(readIn.trim().equals("")) continue;
			String[] cmdAndOpiton = readIn.split(" ");
			if(cmdAndOpiton[0].equals("exit")) {
				break;
			} else if(cmdAndOpiton[0].equals("st")) {		//send text
				
			}
			
		}
		scan.close();
		
		RWUtil.writeToSc(sc, pack);
		
		//释放资源
		ImClient.getInstance().stop();
	}
}
